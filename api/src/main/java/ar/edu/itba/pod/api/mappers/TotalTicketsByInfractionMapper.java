package ar.edu.itba.pod.api.mappers;

import ar.edu.itba.pod.api.models.Ticket;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class TotalTicketsByInfractionMapper implements Mapper<String, Integer, String, Integer> {

    @Override
    public void map(String s, Integer integer, Context<String, Integer> context) {
        context.emit(s, integer);
    }
}