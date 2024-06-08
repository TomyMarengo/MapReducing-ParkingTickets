package ar.edu.itba.pod.api.mappers;

import ar.edu.itba.pod.api.models.dtos.CountyAndInfractionDto;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class TopNInfractionsByCountyMapper implements Mapper<Integer, CountyAndInfractionDto, CountyAndInfractionDto, Integer> {

    @Override
    public void map(Integer integer, CountyAndInfractionDto dto, Context<CountyAndInfractionDto, Integer> context) {
        context.emit(dto, 1);
    }
}
