package ar.edu.itba.pod.api.mappers;

import ar.edu.itba.pod.api.models.dtos.InfractionFineDto;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class InfractionPairMapper implements Mapper<Integer, InfractionFineDto, String, Double> {
    @Override
    public void map(Integer integer, InfractionFineDto infractionFineDto, Context<String, Double> context) {
        context.emit(infractionFineDto.getInfractionDefinition(), infractionFineDto.getFineAmount());
    }
}