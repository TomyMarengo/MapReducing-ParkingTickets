package ar.edu.itba.pod.api.mappers;

import ar.edu.itba.pod.api.HazelcastCollections;
import ar.edu.itba.pod.api.models.dtos.CountyAndInfractionDto;
import ar.edu.itba.pod.api.models.dtos.InfractionDto;
import ar.edu.itba.pod.api.models.Ticket;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class TopNInfractionsByCountyMapper implements Mapper<Integer, CountyAndInfractionDto, CountyAndInfractionDto, Integer> {

    @Override
    public void map(Integer integer, CountyAndInfractionDto dto, Context<CountyAndInfractionDto, Integer> context) {
        context.emit(dto, 1);
    }
}
