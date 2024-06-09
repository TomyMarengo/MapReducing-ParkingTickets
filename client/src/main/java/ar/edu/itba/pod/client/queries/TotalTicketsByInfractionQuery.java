package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.api.HazelcastCollections;
import ar.edu.itba.pod.api.combiners.TotalTicketsByInfractionCombinerFactory;
import ar.edu.itba.pod.api.interfaces.TriConsumer;
import ar.edu.itba.pod.api.mappers.TotalTicketsByInfractionMapper;
import ar.edu.itba.pod.api.models.dtos.InfractionDto;
import ar.edu.itba.pod.api.models.TicketByInfraction;
import ar.edu.itba.pod.api.models.dtos.InfractionDefinitionDto;
import ar.edu.itba.pod.api.reducers.TotalTicketsByInfractionReducerFactory;
import ar.edu.itba.pod.api.collators.TotalTicketsByInfractionCollator;
import ar.edu.itba.pod.client.utils.*;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import com.hazelcast.core.ICompletableFuture;

import java.util.*;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class TotalTicketsByInfractionQuery extends Query {
    private static final String HEADER = "Infraction;Tickets";

    @Override
    protected TriConsumer<String[], CsvMappingConfig, Integer> ticketsConsumer() {
        IMap<Integer, InfractionDefinitionDto> tickets = hazelcastInstance.getMap(HazelcastCollections.TICKETS_BY_INFRACTION_MAP.getName());
        IMap<String, InfractionDto> infractions = hazelcastInstance.getMap(HazelcastCollections.INFRACTIONS_MAP.getName());

        return (fields, config, id) -> {
            if (fields.length >= Constants.FIELD_COUNT) {
                try {
                    String infractionCode = fields[config.getColumnIndex("infractionCode")];
                    InfractionDto infractionDto = infractions.get(infractionCode);

                    //TODO: remove this block and get the infraction definition from the dto (because exists)
                    //TODO: its just to test the complete dataset of CHI
                    String infractionDefinition;
                    if (infractionDto != null) {
                        infractionDefinition = infractionDto.getDefinition();
                    } else {
                        infractionDefinition = fields[config.getColumnIndex("infractionDefinition")];
                    }
                    tickets.putIfAbsent(id, new InfractionDefinitionDto(infractionDefinition));
                } catch (Exception e) {
                    logger.error("Error processing ticket data", e);
                }
            } else {
                logger.error(String.format("Invalid line format, expected %d fields, found %d", Constants.FIELD_COUNT, fields.length));
            }
        };
    }

    @Override
    protected void executeJob() throws ExecutionException, InterruptedException {
        IMap<Integer, InfractionDefinitionDto> tickets = hazelcastInstance.getMap(HazelcastCollections.TICKETS_BY_INFRACTION_MAP.getName());
        IMap<String, InfractionDto> infractions = hazelcastInstance.getMap(HazelcastCollections.INFRACTIONS_MAP.getName());

        System.out.println(tickets.size()); //TODO: remove, only for debugging parallel reading

        JobTracker jobTracker = hazelcastInstance.getJobTracker(Constants.QUERY_1_JOB_TRACKER_NAME);
        KeyValueSource<Integer, InfractionDefinitionDto> source = KeyValueSource.fromMap(tickets);
        Job<Integer, InfractionDefinitionDto> job = jobTracker.newJob(source);

        final ICompletableFuture<TreeSet<TicketByInfraction>> future = job
                .mapper(new TotalTicketsByInfractionMapper())
                .combiner(new TotalTicketsByInfractionCombinerFactory())
                .reducer(new TotalTicketsByInfractionReducerFactory())
                .submit(new TotalTicketsByInfractionCollator());

        TreeSet<TicketByInfraction> result = future.get();
        writeData(HEADER, result);
        tickets.clear();
        infractions.clear();
    }
}