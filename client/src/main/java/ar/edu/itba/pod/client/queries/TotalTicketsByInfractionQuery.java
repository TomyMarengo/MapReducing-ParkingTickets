package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.api.HazelcastCollections;
import ar.edu.itba.pod.api.combiners.TotalTicketsByInfractionCombinerFactory;
import ar.edu.itba.pod.api.mappers.TotalTicketsByInfractionMapper;
import ar.edu.itba.pod.api.models.Ticket;
import ar.edu.itba.pod.api.models.Infraction;
import ar.edu.itba.pod.api.models.TicketByInfraction;
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
    private static final String HEADER = "Infraction,Total Tickets";

    //TODO: in queries we should only define the consumers
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

        // Parse tickets CSV and count infractions
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

        System.out.println(tickets.size()); //TODO: remove, only for debugging parallel reading

        JobTracker jobTracker = hazelcastInstance.getJobTracker(Constants.QUERY_1_JOB_TRACKER_NAME);
        KeyValueSource<Integer, Ticket> source = KeyValueSource.fromMap(tickets);
        Job<Integer, Ticket> job = jobTracker.newJob(source);

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