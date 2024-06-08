package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.api.HazelcastCollections;
import ar.edu.itba.pod.api.combiners.TotalTicketsByInfractionCombinerFactory;
import ar.edu.itba.pod.api.interfaces.TriConsumer;
import ar.edu.itba.pod.api.mappers.TotalTicketsByInfractionMapper;
import ar.edu.itba.pod.api.models.Ticket;
import ar.edu.itba.pod.api.models.dtos.InfractionDto;
import ar.edu.itba.pod.api.models.TicketByInfraction;
import ar.edu.itba.pod.api.models.dtos.SimpleInfractionDto;
import ar.edu.itba.pod.api.reducers.TotalTicketsByInfractionReducerFactory;
import ar.edu.itba.pod.api.collators.TotalTicketsByInfractionCollator;
import ar.edu.itba.pod.client.utils.*;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import com.hazelcast.core.ICompletableFuture;

import java.util.*;

@SuppressWarnings("deprecation")
public class TotalTicketsByInfractionQuery extends Query {
    private static final String HEADER = "Infraction;Tickets";

    @Override
    protected TriConsumer<String[], CsvMappingConfig, Integer> ticketsConsumer() {
        IMap<Integer, SimpleInfractionDto> tickets = hazelcastInstance.getMap(HazelcastCollections.TICKETS_BY_INFRACTION_MAP.getName());
        IMap<String, InfractionDto> infractions = hazelcastInstance.getMap(HazelcastCollections.INFRACTIONS_MAP.getName());

        return (fields, config, id) -> {
            if (fields.length >= Ticket.FIELD_COUNT) {
                try {
                    String infractionCode = fields[config.getColumnIndex("infractionCode")];
                    String infractionDefinition = infractions.get(infractionCode).getDefinition();

                    tickets.putIfAbsent(id, new SimpleInfractionDto(infractionDefinition));
                } catch (Exception e) {
                    logger.error("Error processing ticket data", e);
                }
            } else {
                logger.error(String.format("Invalid line format, expected %d fields, found %d", Ticket.FIELD_COUNT, fields.length));
            }
        };
    }

    @Override
    protected void executeJob() {
        IMap<Integer, SimpleInfractionDto> tickets = hazelcastInstance.getMap(HazelcastCollections.TICKETS_BY_INFRACTION_MAP.getName());
        IMap<String, InfractionDto> infractions = hazelcastInstance.getMap(HazelcastCollections.INFRACTIONS_MAP.getName());

        System.out.println(tickets.size()); //TODO: remove, only for debugging parallel reading

        JobTracker jobTracker = hazelcastInstance.getJobTracker(Constants.QUERY_1_JOB_TRACKER_NAME);
        KeyValueSource<Integer, SimpleInfractionDto> source = KeyValueSource.fromMap(tickets);
        Job<Integer, SimpleInfractionDto> job = jobTracker.newJob(source);

        final ICompletableFuture<TreeSet<TicketByInfraction>> future = job
                .mapper(new TotalTicketsByInfractionMapper())
                .combiner(new TotalTicketsByInfractionCombinerFactory())
                .reducer(new TotalTicketsByInfractionReducerFactory())
                .submit(new TotalTicketsByInfractionCollator());

        try {
            TreeSet<TicketByInfraction> result = future.get();
            writeData(HEADER, result);
            tickets.clear();
            infractions.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}