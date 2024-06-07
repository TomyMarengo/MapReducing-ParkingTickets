package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.api.HazelcastCollections;
import ar.edu.itba.pod.api.collators.TopNCollectorAgenciesCollator;
import ar.edu.itba.pod.api.combiners.TopNCollectorAgenciesCombinerFactory;
import ar.edu.itba.pod.api.interfaces.TriConsumer;
import ar.edu.itba.pod.api.mappers.TopNCollectorAgenciesMapper;
import ar.edu.itba.pod.api.models.*;
import ar.edu.itba.pod.api.reducers.TopNCollectorAgenciesReducerFactory;
import ar.edu.itba.pod.client.utils.Constants;
import ar.edu.itba.pod.client.utils.CsvMappingConfig;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.util.*;

@SuppressWarnings("deprecation")
public class TopNCollectorAgenciesQuery extends Query{
    private static final String HEADER = "Issuing Agency;Percentage";
    @Override
    protected TriConsumer<String[], CsvMappingConfig, Integer> infractionsConsumer() {
        return null;
    }

    @Override
    protected TriConsumer<String[], CsvMappingConfig, Integer> ticketsConsumer() {
        IMap<Integer, AgencyFineDto> agencies = hazelcastInstance.getMap(HazelcastCollections.AGENCY_FINE_MAP.getName());

        return (fields, config, id) -> {
            if (fields.length >= Ticket.FIELD_COUNT) {
                try {
                    Double fineAmount = Double.parseDouble(fields[config.getColumnIndex("fineAmount")]);
                    String issuingAgency = fields[config.getColumnIndex("issuingAgency")];
                    AgencyFineDto agencyFine = new AgencyFineDto(issuingAgency, fineAmount);

                    agencies.putIfAbsent(id, agencyFine);
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
        IMap<Integer, AgencyFineDto> agencies = hazelcastInstance.getMap(HazelcastCollections.AGENCY_FINE_MAP.getName());

        JobTracker jobTracker = hazelcastInstance.getJobTracker(Constants.QUERY_3_JOB_TRACKER_NAME);
        KeyValueSource<Integer, AgencyFineDto> source = KeyValueSource.fromMap(agencies);
        Job<Integer, AgencyFineDto> job = jobTracker.newJob(source);

        final ICompletableFuture<TreeSet<AgencyPercentage>> future = job
                .mapper(new TopNCollectorAgenciesMapper())
                .combiner(new TopNCollectorAgenciesCombinerFactory())
                .reducer(new TopNCollectorAgenciesReducerFactory())
                .submit(new TopNCollectorAgenciesCollator(arguments.getN()));

        try {
            TreeSet<AgencyPercentage> result = future.get();
            writeData(HEADER, result);
            agencies.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
