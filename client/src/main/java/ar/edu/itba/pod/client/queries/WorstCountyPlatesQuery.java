package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.api.HazelcastCollections;
import ar.edu.itba.pod.api.collators.WorstCountyPlateCollator;
import ar.edu.itba.pod.api.combiners.PairCountyPlateCombinerFactory;
import ar.edu.itba.pod.api.combiners.WorstCountyPlateCombinerFactory;
import ar.edu.itba.pod.api.interfaces.TriConsumer;
import ar.edu.itba.pod.api.mappers.PairCountyPlateMapper;
import ar.edu.itba.pod.api.mappers.WorstCountyPlateMapper;
import ar.edu.itba.pod.api.models.dtos.PlateCountyDateDto;
import ar.edu.itba.pod.api.models.dtos.PlateCountyDto;
import ar.edu.itba.pod.api.models.WorstPlates;
import ar.edu.itba.pod.api.reducers.PairCountyPlateReducerFactory;
import ar.edu.itba.pod.api.reducers.WorstCountyPlateReducerFactory;
import ar.edu.itba.pod.client.utils.*;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.util.Date;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class WorstCountyPlatesQuery extends Query {
    private static final String HEADER = "County;Plate;Tickets";

    @Override
    protected TriConsumer<String[], CsvMappingConfig, Integer> infractionsConsumer() {
        return null;
    }

    @Override
    protected TriConsumer<String[], CsvMappingConfig, Integer> ticketsConsumer() {
        IMap<Integer, PlateCountyDateDto> map = hazelcastInstance.getMap(HazelcastCollections.TICKETS_BY_COUNTY_PLATE_MAP.getName());
        return (fields, config, id) -> {
            if (fields.length >= Constants.FIELD_COUNT) {
                try {
                    String plate = fields[config.getColumnIndex("plate")];
                    Date issueDate = DateFormats.parseDate(fields[config.getColumnIndex("issueDate")]);
                    String countyName = fields[config.getColumnIndex("countyName")];

                    map.putIfAbsent(id, new PlateCountyDateDto(plate, issueDate, countyName));
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
        IMap<Integer, PlateCountyDateDto> map = hazelcastInstance.getMap(HazelcastCollections.TICKETS_BY_COUNTY_PLATE_MAP.getName());

        JobTracker jobTracker = hazelcastInstance.getJobTracker(Constants.QUERY_4_JOB_TRACKER_NAME);

        KeyValueSource<Integer, PlateCountyDateDto> source = KeyValueSource.fromMap(map);
        Job<Integer, PlateCountyDateDto> job = jobTracker.newJob(source);

        final ICompletableFuture<Map<PlateCountyDto, Integer>> future = job
                .mapper(new PairCountyPlateMapper(arguments.getFrom(),arguments.getTo()))
                .reducer(new PairCountyPlateReducerFactory())
                .submit();

        Map<PlateCountyDto, Integer> result = future.get();
        IMap<PlateCountyDto, Integer> pairMap = hazelcastInstance.getMap(HazelcastCollections.PAIR_COUNTY_PLATE_MAP.getName());
        pairMap.putAll(result);

        JobTracker jobTracker2 = hazelcastInstance.getJobTracker(Constants.QUERY_4_JOB_TRACKER_NAME_2);
        KeyValueSource<PlateCountyDto, Integer> source2 = KeyValueSource.fromMap(pairMap);
        Job<PlateCountyDto, Integer> job2 = jobTracker2.newJob(source2);

        final ICompletableFuture<TreeSet<WorstPlates>> future2 = job2
                .mapper(new WorstCountyPlateMapper())
                .reducer(new WorstCountyPlateReducerFactory())
                .submit(new WorstCountyPlateCollator());

        TreeSet<WorstPlates> result2 = future2.get();

        try {
            writeData(HEADER, result2);
            map.clear();
            pairMap.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
