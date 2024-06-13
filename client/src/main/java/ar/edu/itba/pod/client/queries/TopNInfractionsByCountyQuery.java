package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.api.HazelcastCollections;
import ar.edu.itba.pod.api.collators.TopNInfractionsByCountyCollator;
import ar.edu.itba.pod.api.collections.TopNSet;
import ar.edu.itba.pod.api.combiners.TopNInfractionsByCountyCombinerFactory;
import ar.edu.itba.pod.api.interfaces.TriConsumer;
import ar.edu.itba.pod.api.mappers.TopNInfractionsByCountyMapper;
import ar.edu.itba.pod.api.models.*;
import ar.edu.itba.pod.api.models.dtos.CountyInfractionDto;
import ar.edu.itba.pod.api.models.dtos.InfractionDto;
import ar.edu.itba.pod.api.reducers.TopNInfractionsByCountyReducerFactory;
import ar.edu.itba.pod.client.utils.*;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.util.*;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class TopNInfractionsByCountyQuery extends Query {

    @Override
    protected TriConsumer<String[], CsvMappingConfig, Integer> ticketsConsumer() {
        IMap<Integer, CountyInfractionDto> tickets = hazelcastInstance.getMap(HazelcastCollections.TICKETS_BY_COUNTY_MAP.getName());
        IMap<String, InfractionDto> infractions = hazelcastInstance.getMap(HazelcastCollections.INFRACTIONS_MAP.getName());

        return (fields, config, id) -> {
            if (fields.length >= Constants.FIELD_COUNT) {
                try {
                    String countyName = fields[config.getColumnIndex("countyName")];
                    String infractionCode = fields[config.getColumnIndex("infractionCode")];
                    InfractionDto infractionDto = infractions.get(infractionCode);
                    String infractionDefinition = infractionDto.getDefinition();
                    tickets.putIfAbsent(id, new CountyInfractionDto(countyName, infractionDefinition));
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
        IMap<Integer, CountyInfractionDto> tickets = hazelcastInstance.getMap(HazelcastCollections.TICKETS_BY_COUNTY_MAP.getName());
        IMap<String, InfractionDto> infractions = hazelcastInstance.getMap(HazelcastCollections.INFRACTIONS_MAP.getName());

        JobTracker jobTracker = hazelcastInstance.getJobTracker(Constants.QUERY_2_JOB_TRACKER_NAME);
        KeyValueSource<Integer, CountyInfractionDto> source = KeyValueSource.fromMap(tickets);
        Job<Integer, CountyInfractionDto> job = jobTracker.newJob(source);

        final ICompletableFuture<TopNSet<String, InfractionsCount>> future = job
                .mapper(new TopNInfractionsByCountyMapper())
                .combiner(new TopNInfractionsByCountyCombinerFactory())
                .reducer(new TopNInfractionsByCountyReducerFactory())
                .submit(new TopNInfractionsByCountyCollator(arguments.getN()));


        TopNSet<String, InfractionsCount> result = future.get();

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
    }
}
