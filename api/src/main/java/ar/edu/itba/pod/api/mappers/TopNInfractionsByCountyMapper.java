package ar.edu.itba.pod.api.mappers;

import ar.edu.itba.pod.api.HazelcastCollections;
import ar.edu.itba.pod.api.models.CountyAndInfractionDto;
import ar.edu.itba.pod.api.models.Infraction;
import ar.edu.itba.pod.api.models.Ticket;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class TopNInfractionsByCountyMapper implements Mapper<Integer, Ticket, CountyAndInfractionDto, Integer>, HazelcastInstanceAware {
    private transient HazelcastInstance hazelcastInstance;

    @Override
    public void map(Integer integer, Ticket ticket, Context<CountyAndInfractionDto, Integer> context) {
        IMap<String, Infraction> infractions = hazelcastInstance.getMap(HazelcastCollections.INFRACTIONS_MAP.getName());
        String infractionDescription = infractions.get(ticket.getInfractionCode()).getDefinition(); //todo: check not null
        CountyAndInfractionDto countyAndInfractionDto = new CountyAndInfractionDto(ticket.getCountyName(), infractionDescription );
        //todo: check condition on countyAndInfractionDto?
        context.emit(countyAndInfractionDto, 1);
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
