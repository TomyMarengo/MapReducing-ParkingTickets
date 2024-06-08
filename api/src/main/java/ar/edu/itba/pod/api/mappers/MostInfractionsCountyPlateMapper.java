package ar.edu.itba.pod.api.mappers;

import ar.edu.itba.pod.api.models.Ticket;
import ar.edu.itba.pod.api.models.dtos.InfractionPlateDto;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class MostInfractionsCountyPlateMapper implements Mapper<Integer, InfractionPlateDto, String, Integer> {
    @Override
    public void map(Integer integer, InfractionPlateDto infractionPlateDto, Context<String, Integer> context) {
        // todo
    }
}
