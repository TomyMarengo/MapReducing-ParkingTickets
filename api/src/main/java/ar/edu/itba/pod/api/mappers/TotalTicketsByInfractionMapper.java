package ar.edu.itba.pod.api.mappers;

import ar.edu.itba.pod.api.models.Ticket;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class TotalTicketsByInfractionMapper implements Mapper<String, Ticket, String, Integer> {
    @Override
    public void map(String key, Ticket ticket, Context<String, Integer> context) {
        context.emit(ticket.getInfractionCode(), 1);
    }
}