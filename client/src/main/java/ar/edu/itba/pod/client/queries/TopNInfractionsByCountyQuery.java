package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.api.HazelcastCollections;
import ar.edu.itba.pod.api.collators.TopNInfractionsByCountyCollator;
import ar.edu.itba.pod.api.combiners.TopNInfractionsByCountyCombinerFactory;
import ar.edu.itba.pod.api.mappers.TopNInfractionsByCountyMapper;
import ar.edu.itba.pod.api.models.*;
import ar.edu.itba.pod.api.reducers.TopNInfractionsByCountyReducerFactory;
import ar.edu.itba.pod.client.utils.Constants;
import ar.edu.itba.pod.client.utils.CsvFileIterator;
import ar.edu.itba.pod.client.utils.CsvFileType;
import ar.edu.itba.pod.client.utils.DateFormats;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.util.*;

@SuppressWarnings("deprecation")
public class TopNInfractionsByCountyQuery extends Query {
    private static final int top = 3;
    private static final String HEADER = "County,InfractionTop1,InfractionTop2,InfractionTop3";

    @Override
    protected void loadData() {
        IMap<String, Infraction> infractions = hazelcastInstance.getMap(HazelcastCollections.INFRACTIONS_MAP.getName());
        IMap<Integer, Ticket> tickets = hazelcastInstance.getMap(HazelcastCollections.TICKETS_BY_INFRACTION_MAP.getName());

        // Parse infractions CSV
        CsvFileIterator.readCsv(arguments, CsvFileType.INFRACTIONS, (fields, config, id) -> {
            if (fields.length == Infraction.FIELD_COUNT) {
                String code = fields[config.getColumnIndex("code")];
                String definition = fields[config.getColumnIndex("definition")];
                infractions.put(code, new Infraction(code, definition));
            } else {
                logger.error(String.format("Invalid line format, expected %d fields, found %d", Infraction.FIELD_COUNT, fields.length));
            }
        });

        CsvFileIterator.readCsvParallel(arguments, CsvFileType.TICKETS, (fields, config, id) -> {
            if (fields.length >= Ticket.FIELD_COUNT) {
                try {
                    //TODO: really we don't need to parse all the fields, we only want the infractionCode in this query. Need another DTO. Ticket is just too much
                    String plate = fields[config.getColumnIndex("plate")];
                    Date issueDate = DateFormats.parseDate(fields[config.getColumnIndex("issueDate")]);
                    String infractionCode = fields[config.getColumnIndex("infractionCode")];
                    Double fineAmount = Double.parseDouble(fields[config.getColumnIndex("fineAmount")]);
                    String countyName = fields[config.getColumnIndex("countyName")];
                    String issuingAgency = fields[config.getColumnIndex("issuingAgency")];

                    Ticket ticket = new Ticket(plate, issueDate, infractionCode, fineAmount, countyName, issuingAgency);
                    tickets.putIfAbsent(id, ticket);
                } catch (Exception e) {
                    logger.error("Error processing ticket data", e);
                }
            } else {
                logger.error(String.format("Invalid line format, expected %d fields, found %d", Ticket.FIELD_COUNT, fields.length));
            }
        });
    }

    @Override
    protected void executeJob() {
        IMap<Integer, Ticket> tickets = hazelcastInstance.getMap(HazelcastCollections.TICKETS_BY_INFRACTION_MAP.getName());
        IMap<String, Infraction> infractions = hazelcastInstance.getMap(HazelcastCollections.INFRACTIONS_MAP.getName());

        JobTracker jobTracker = hazelcastInstance.getJobTracker(Constants.QUERY_2_JOB_TRACKER_NAME);
        KeyValueSource<Integer, Ticket> source = KeyValueSource.fromMap(tickets);
        Job<Integer, Ticket> job = jobTracker.newJob(source);

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
                    topInfractions.add(infractionCount.getInfractionDescription());
                }
                topNInfractionsByCounty.add(new TopNInfractionsByCounty(county, topInfractions, arguments.getN()));
            }
            writeData(HEADER, topNInfractionsByCounty);
            tickets.clear();
            infractions.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
