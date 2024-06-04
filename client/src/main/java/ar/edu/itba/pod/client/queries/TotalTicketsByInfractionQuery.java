package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.api.mappers.TotalTicketsByInfractionMapper;
import ar.edu.itba.pod.api.models.Ticket;
import ar.edu.itba.pod.api.models.Infraction;
import ar.edu.itba.pod.api.models.TicketByInfractionDto;
import ar.edu.itba.pod.api.reducers.TotalTicketsByInfractionReducerFactory;
import ar.edu.itba.pod.api.submitters.TotalTicketsByInfractionSubmitter;
import ar.edu.itba.pod.client.utils.*;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import com.hazelcast.core.ICompletableFuture;

import java.text.ParseException;
import java.util.*;

@SuppressWarnings("deprecation")
public class TotalTicketsByInfractionQuery extends Query {
    private static final String HEADER = "Infraction,Total Tickets";

    @Override
    protected void loadData() {
        IMap<String, Infraction> infractions = hazelcastInstance.getMap(Constants.INFRACTIONS_MAP);
        IMap<String, Integer> ticketsCount = hazelcastInstance.getMap(Constants.TICKETS_BY_INFRACTION_MAP);

        // Parse infractions CSV
        CsvFileIterator.readCsv(arguments, CsvFileType.INFRACTIONS, (fields, config) -> {
            if (fields.length == Infraction.FIELD_COUNT) {
                String code = fields[config.getColumnIndex("code")];
                String definition = fields[config.getColumnIndex("definition")];
                infractions.put(code, new Infraction(code, definition));
            } else {
                logger.error(String.format("Invalid line format, expected %d fields, found %d", Infraction.FIELD_COUNT, fields.length));
            }
        });

        // Parse tickets CSV and count infractions
        CsvFileIterator.readCsv(arguments, CsvFileType.TICKETS, (fields, config) -> {
            if (fields.length >= Ticket.FIELD_COUNT) {
                try {
                    String infractionCode = fields[config.getColumnIndex("infractionCode")];
                    String definition = infractions.get(infractionCode).getDefinition();
                    ticketsCount.merge(definition, 1, Integer::sum);
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
        IMap<String, Integer> ticketsCount = hazelcastInstance.getMap(Constants.TICKETS_BY_INFRACTION_MAP);

        JobTracker jobTracker = hazelcastInstance.getJobTracker(Constants.QUERY_1_JOB_TRACKER_NAME);
        KeyValueSource<String, Integer> source = KeyValueSource.fromMap(ticketsCount);
        Job<String, Integer> job = jobTracker.newJob(source);

        final ICompletableFuture<TreeSet<TicketByInfractionDto>> future = job
                .mapper(new TotalTicketsByInfractionMapper())
                .reducer(new TotalTicketsByInfractionReducerFactory())
                .submit(new TotalTicketsByInfractionSubmitter());

        try {
            TreeSet<TicketByInfractionDto> result = future.get();
            writeData(HEADER, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}