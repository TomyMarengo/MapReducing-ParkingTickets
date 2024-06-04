package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.api.mappers.TotalTicketsByInfractionMapper;
import ar.edu.itba.pod.api.models.Ticket;
import ar.edu.itba.pod.api.models.TicketByInfractionDto;
import ar.edu.itba.pod.api.reducers.TotalTicketsByInfractionReducerFactory;
import ar.edu.itba.pod.api.submitters.TotalTicketsByInfractionSubmitter;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import com.hazelcast.core.ICompletableFuture;
import java.util.*;

public class TotalTicketsByInfractionQuery implements Query {
    @SuppressWarnings("deprecation")
    @Override
    public void execute(HazelcastInstance hazelcastInstance, String outputPath) {
        IList<Ticket> tickets = hazelcastInstance.getList("tickets");

        JobTracker jobTracker = hazelcastInstance.getJobTracker("totalTicketsByInfraction");
        KeyValueSource<String, Ticket> source = KeyValueSource.fromList(tickets);
        Job<String, Ticket> job = jobTracker.newJob(source);
        TotalTicketsByInfractionSubmitter submitter = new TotalTicketsByInfractionSubmitter();

        final ICompletableFuture<TreeSet<TicketByInfractionDto>> future = job
                .mapper(new TotalTicketsByInfractionMapper())
                .reducer(new TotalTicketsByInfractionReducerFactory())
                .submit(submitter);

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