package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.api.mappers.TotalTicketsByInfractionMapper;
import ar.edu.itba.pod.api.models.Ticket;
import ar.edu.itba.pod.api.models.Infraction;
import ar.edu.itba.pod.api.models.TicketByInfractionDto;
import ar.edu.itba.pod.api.reducers.TotalTicketsByInfractionReducerFactory;
import ar.edu.itba.pod.api.submitters.TotalTicketsByInfractionSubmitter;
import ar.edu.itba.pod.client.utils.Constants;
import ar.edu.itba.pod.client.utils.CsvFileIterator;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import com.hazelcast.core.ICompletableFuture;
import java.util.*;

@SuppressWarnings("deprecation")
public class TotalTicketsByInfractionQuery extends Query {

    @Override
    protected void loadData() {
        IList<Ticket> tickets = hazelcastInstance.getList(Constants.TICKETS_LIST);
        IMap<String, Infraction> infractions = hazelcastInstance.getMap(Constants.INFRACTIONS_MAP);

        System.out.println("Loading infractions from " + arguments.getInPath() + " for city " + arguments.getCity());
        CsvFileIterator.parseInfractionsCsv(arguments.getInPath(), arguments.getCity(), infractions);
        System.out.println("Loading tickets from " + arguments.getInPath() + " for city " + arguments.getCity());
        CsvFileIterator.parseTicketsCsv(arguments.getInPath(), arguments.getCity(), tickets);
    }

    @Override
    protected void executeJob() {
        IList<Ticket> tickets = hazelcastInstance.getList(Constants.TICKETS_LIST);

        JobTracker jobTracker = hazelcastInstance.getJobTracker(Constants.QUERY_1_JOB_TRACKER_NAME);
        KeyValueSource<String, Ticket> source = KeyValueSource.fromList(tickets);
        Job<String, Ticket> job = jobTracker.newJob(source);

        final ICompletableFuture<TreeSet<TicketByInfractionDto>> future = job
                .mapper(new TotalTicketsByInfractionMapper())
                .reducer(new TotalTicketsByInfractionReducerFactory())
                .submit(new TotalTicketsByInfractionSubmitter());

        try {
            TreeSet<TicketByInfractionDto> result = future.get();
            System.out.println("Infraction Code\tTotal Tickets");
            for (TicketByInfractionDto dto : result) {
                System.out.println(dto.getInfractionCode() + "\t" + dto.getCount());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}