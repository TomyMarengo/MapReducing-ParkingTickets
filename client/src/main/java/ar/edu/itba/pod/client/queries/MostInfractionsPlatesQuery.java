package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.api.HazelcastCollections;
import ar.edu.itba.pod.api.collators.MostInfractionsCountyPlateCollator;
import ar.edu.itba.pod.api.collators.TotalTicketsByInfractionCollator;
import ar.edu.itba.pod.api.combiners.MostInfractionsCountyPlateCombiner;
import ar.edu.itba.pod.api.combiners.TotalTicketsByInfractionCombinerFactory;
import ar.edu.itba.pod.api.interfaces.TriConsumer;
import ar.edu.itba.pod.api.mappers.MostInfractionsCountyPlateMapper;
import ar.edu.itba.pod.api.mappers.TotalTicketsByInfractionMapper;
import ar.edu.itba.pod.api.models.Ticket;
import ar.edu.itba.pod.api.models.TicketByInfraction;
import ar.edu.itba.pod.api.models.dtos.InfractionPlateDto;
import ar.edu.itba.pod.api.reducers.MostInfractionsCountyPlateReducerFactory;
import ar.edu.itba.pod.api.reducers.TotalTicketsByInfractionReducerFactory;
import ar.edu.itba.pod.client.utils.*;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.util.Date;
import java.util.TreeSet;

@SuppressWarnings("deprecation")
public class MostInfractionsPlatesQuery extends Query {
    public MostInfractionsPlatesQuery(Date from, Date to){
        this.from = from;
        this.to = to;
    }

    private final Date from;
    private final Date to;

    @Override
    protected TriConsumer<String[], CsvMappingConfig, Integer> infractionsConsumer() {
        return null;
    }

    @Override
    protected TriConsumer<String[], CsvMappingConfig, Integer> ticketsConsumer() {
        IMap<Integer, InfractionPlateDto> map = hazelcastInstance.getMap(HazelcastCollections.TICKETS_BY_COUNTY_PLATE_MAP.getName());
        return (fields, config, id) -> {
            if (fields.length >= Ticket.FIELD_COUNT) {
                try {
                    String plate = fields[config.getColumnIndex("plate")];
                    Date issueDate = DateFormats.parseDate(fields[config.getColumnIndex("issueDate")]);
                    String countyName = fields[config.getColumnIndex("countyName")];

                    map.putIfAbsent(id, new InfractionPlateDto(plate, issueDate, countyName));
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
        IMap<Integer, InfractionPlateDto> map = hazelcastInstance.getMap(HazelcastCollections.TICKETS_BY_COUNTY_PLATE_MAP.getName());

        JobTracker jobTracker = hazelcastInstance.getJobTracker(Constants.QUERY_4_JOB_TRACKER_NAME);

        KeyValueSource<Integer, InfractionPlateDto> source = KeyValueSource.fromMap(map);
        Job<Integer, InfractionPlateDto> job = jobTracker.newJob(source);

        final ICompletableFuture<TreeSet<TicketByInfraction>> future = job
                .mapper(new MostInfractionsCountyPlateMapper())
                .combiner(new MostInfractionsCountyPlateCombiner())
                .reducer(new MostInfractionsCountyPlateReducerFactory())
                .submit(new MostInfractionsCountyPlateCollator());

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
