package ar.edu.itba.pod.api.mappers;

import ar.edu.itba.pod.api.HazelcastCollections;
import ar.edu.itba.pod.api.models.Infraction;
import ar.edu.itba.pod.api.models.Ticket;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class TotalTicketsByInfractionMapper implements Mapper<Integer, Ticket, String, Integer>, HazelcastInstanceAware {
    private transient HazelcastInstance hazelcastInstance;
    @Override
    public void map(Integer integer, Ticket ticket, Context<String, Integer> context) {
        IMap<String, Infraction> infractions = hazelcastInstance.getMap(HazelcastCollections.INFRACTIONS_MAP.getName());
        String definition = infractions.get(ticket.getInfractionCode()).getDefinition();
        if (definition != null) {
            context.emit(definition, 1);
        }
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}