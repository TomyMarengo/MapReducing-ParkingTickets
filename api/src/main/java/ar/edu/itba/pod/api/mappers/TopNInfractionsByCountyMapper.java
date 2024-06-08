package ar.edu.itba.pod.api.mappers;

import ar.edu.itba.pod.api.models.dtos.CountyInfractionDto;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class TopNInfractionsByCountyMapper implements Mapper<Integer, CountyInfractionDto, CountyInfractionDto, Integer> {

    @Override
    public void map(Integer integer, CountyInfractionDto dto, Context<CountyInfractionDto, Integer> context) {
        context.emit(dto, 1);
    }
}
