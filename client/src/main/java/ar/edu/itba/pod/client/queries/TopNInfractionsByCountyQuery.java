package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.api.HazelcastCollections;
import ar.edu.itba.pod.api.collators.TopNInfractionsByCountyCollator;
import ar.edu.itba.pod.api.collections.TopNSet;
import ar.edu.itba.pod.api.combiners.TopNInfractionsByCountyCombinerFactory;
import ar.edu.itba.pod.api.interfaces.TriConsumer;
import ar.edu.itba.pod.api.mappers.TopNInfractionsByCountyMapper;
import ar.edu.itba.pod.api.models.*;
import ar.edu.itba.pod.api.models.dtos.CountyAndInfractionDto;
import ar.edu.itba.pod.api.models.dtos.InfractionDto;
import ar.edu.itba.pod.api.reducers.TopNInfractionsByCountyReducerFactory;
import ar.edu.itba.pod.client.utils.*;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.util.*;

@SuppressWarnings("deprecation")
public class TopNInfractionsByCountyQuery extends Query {

    @Override
    protected TriConsumer<String[], CsvMappingConfig, Integer> ticketsConsumer() {
        IMap<Integer, CountyAndInfractionDto> tickets = hazelcastInstance.getMap(HazelcastCollections.TICKETS_BY_COUNTY_MAP.getName());
        IMap<String, InfractionDto> infractions = hazelcastInstance.getMap(HazelcastCollections.INFRACTIONS_MAP.getName());

        return (fields, config, id) -> {
            if (fields.length >= Ticket.FIELD_COUNT) {
                try {
                    String countyName = fields[config.getColumnIndex("countyName")];
                    String infractionCode = fields[config.getColumnIndex("infractionCode")];
                    String infractionDefinition = infractions.get(infractionCode).getDefinition();

                    tickets.putIfAbsent(id, new CountyAndInfractionDto(countyName, infractionDefinition));
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
        IMap<Integer, CountyAndInfractionDto> tickets = hazelcastInstance.getMap(HazelcastCollections.TICKETS_BY_COUNTY_MAP.getName());
        IMap<String, InfractionDto> infractions = hazelcastInstance.getMap(HazelcastCollections.INFRACTIONS_MAP.getName());

        System.out.println(tickets.size()); //TODO: remove, only for debugging parallel reading

        JobTracker jobTracker = hazelcastInstance.getJobTracker(Constants.QUERY_2_JOB_TRACKER_NAME);
        KeyValueSource<Integer, CountyAndInfractionDto> source = KeyValueSource.fromMap(tickets);
        Job<Integer, CountyAndInfractionDto> job = jobTracker.newJob(source);

        final ICompletableFuture<TopNSet<String, InfractionsCount>> future = job
                .mapper(new TopNInfractionsByCountyMapper())
                .combiner(new TopNInfractionsByCountyCombinerFactory())
                .reducer(new TopNInfractionsByCountyReducerFactory())
                .submit(new TopNInfractionsByCountyCollator(arguments.getN()));

        try {
            TopNSet<String, InfractionsCount> result = future.get();
            //todo: refactor?
            Set<TopNInfractionsByCounty> topNInfractionsByCounty = new TreeSet<>();
            for(Map.Entry<String, NavigableSet<InfractionsCount>> entry : result){
                String county = entry.getKey();
                NavigableSet<InfractionsCount> infractionsCount = entry.getValue();
                List<String> topInfractions = new ArrayList<>();
                for(InfractionsCount infractionCount : infractionsCount){
                    topInfractions.add(infractionCount.getInfractionDefinition());
                }
                topNInfractionsByCounty.add(new TopNInfractionsByCounty(county, topInfractions, arguments.getN()));
            }
            StringBuilder buildHeader = new StringBuilder("County");
            for (int i = 1; i <= arguments.getN(); i++) {
                buildHeader.append(";InfractionTop").append(i);
            }
            String HEADER = buildHeader.toString();

            writeData(HEADER, topNInfractionsByCounty);
            tickets.clear();
            infractions.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
