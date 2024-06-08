package ar.edu.itba.pod.api.mappers;

import ar.edu.itba.pod.api.models.dtos.InfractionDefinitionDto;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class TotalTicketsByInfractionMapper implements Mapper<Integer, InfractionDefinitionDto, String, Integer> {
    @Override
    public void map(Integer integer, InfractionDefinitionDto dto, Context<String, Integer> context) {
        context.emit(dto.getDefinition(), 1);
    }
}