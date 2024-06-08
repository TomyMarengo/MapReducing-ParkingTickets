package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.api.HazelcastCollections;
import ar.edu.itba.pod.api.collators.TopNInfractionsByCountyCollator;
import ar.edu.itba.pod.api.collators.TopNInfractionsByCountyCollatorAlternative;
import ar.edu.itba.pod.api.collators.TotalTicketsByInfractionCollator;
import ar.edu.itba.pod.api.collections.TopNSet;
import ar.edu.itba.pod.api.combiners.TopNInfractionsByCountyCombinerFactory;
import ar.edu.itba.pod.api.combiners.TopNInfractionsByCountyCombinerFactoryAlternative;
import ar.edu.itba.pod.api.combiners.TotalTicketsByInfractionCombinerFactory;
import ar.edu.itba.pod.api.interfaces.TriConsumer;
import ar.edu.itba.pod.api.mappers.TopNInfractionsByCountyMapper;
import ar.edu.itba.pod.api.mappers.TopNInfractionsByCountyMapperAlternative;
import ar.edu.itba.pod.api.mappers.TotalTicketsByInfractionMapper;
import ar.edu.itba.pod.api.models.InfractionsCount;
import ar.edu.itba.pod.api.models.TicketByInfraction;
import ar.edu.itba.pod.api.models.TopNInfractionsByCounty;
import ar.edu.itba.pod.api.models.dtos.CountyInfractionDto;
import ar.edu.itba.pod.api.models.dtos.InfractionDefinitionDto;
import ar.edu.itba.pod.api.models.dtos.InfractionDto;
import ar.edu.itba.pod.api.reducers.TopNInfractionsByCountyReducerFactory;
import ar.edu.itba.pod.api.reducers.TopNInfractionsByCountyReducerFactoryAlternative;
import ar.edu.itba.pod.api.reducers.TotalTicketsByInfractionReducerFactory;
import ar.edu.itba.pod.client.utils.Constants;
import ar.edu.itba.pod.client.utils.CsvMappingConfig;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MultiMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class TopNInfractionsByCountyQueryAlternative extends Query{
    @Override
    protected TriConsumer<String[], CsvMappingConfig, Integer> ticketsConsumer() {
        MultiMap<String,String> tickets = hazelcastInstance.getMultiMap(HazelcastCollections.TICKETS_BY_COUNTY_MAP.getName());
        IMap<String, InfractionDto> infractions = hazelcastInstance.getMap(HazelcastCollections.INFRACTIONS_MAP.getName());

        return (fields, config, id) -> {
            if (fields.length >= Constants.FIELD_COUNT) {
                try {
                    String countyName = fields[config.getColumnIndex("countyName")];
                    String infractionCode = fields[config.getColumnIndex("infractionCode")];
                    String infractionDefinition = infractions.get(infractionCode).getDefinition();

                    tickets.put(countyName, infractionDefinition);
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
        MultiMap<String, String> tickets = hazelcastInstance.getMultiMap(HazelcastCollections.TICKETS_BY_COUNTY_MAP.getName());

        System.out.println(tickets.size()); //TODO: remove, only for debugging parallel reading

        JobTracker jobTracker = hazelcastInstance.getJobTracker(Constants.QUERY_2_JOB_TRACKER_NAME);
        KeyValueSource<String, String> source = KeyValueSource.fromMultiMap(tickets);
        Job<String, String> job = jobTracker.newJob(source);

        final ICompletableFuture<TreeSet<TopNInfractionsByCounty>> future = job
                .mapper(new TopNInfractionsByCountyMapperAlternative())
                .combiner(new TopNInfractionsByCountyCombinerFactoryAlternative())
                .reducer(new TopNInfractionsByCountyReducerFactoryAlternative())
                .submit(new TopNInfractionsByCountyCollatorAlternative(arguments.getN()));


        TreeSet<TopNInfractionsByCounty> result = future.get();
        StringBuilder buildHeader = new StringBuilder("County");
        for (int i = 1; i <= arguments.getN(); i++) {
            buildHeader.append(";InfractionTop").append(i);
        }
        String HEADER = buildHeader.toString();

        writeData(HEADER, result);
        tickets.clear();


    }
}
