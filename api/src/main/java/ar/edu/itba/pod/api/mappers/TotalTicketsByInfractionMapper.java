package ar.edu.itba.pod.api.mappers;

import ar.edu.itba.pod.api.HazelcastCollections;
import ar.edu.itba.pod.api.models.dtos.InfractionDto;
import ar.edu.itba.pod.api.models.Ticket;
import ar.edu.itba.pod.api.models.dtos.SimpleInfractionDto;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class TotalTicketsByInfractionMapper implements Mapper<Integer, SimpleInfractionDto, String, Integer> {
    @Override
    public void map(Integer integer, SimpleInfractionDto dto, Context<String, Integer> context) {
        context.emit(dto.getDefinition(), 1);
    }
}