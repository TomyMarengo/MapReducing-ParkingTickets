package ar.edu.itba.pod.api.collators;

import ar.edu.itba.pod.api.models.TicketByInfraction;
import com.hazelcast.mapreduce.Collator;

import java.util.*;

@SuppressWarnings("deprecation")
public class TotalTicketsByInfractionCollator implements Collator<Map.Entry<String, Integer>, TreeSet<TicketByInfraction>> {
    @Override
    public TreeSet<TicketByInfraction> collate(Iterable<Map.Entry<String, Integer>> values) {
        TreeSet<TicketByInfraction> resultSet = new TreeSet<>();
        for (Map.Entry<String, Integer> entry : values) {
            resultSet.add(new TicketByInfraction(entry.getKey(), entry.getValue()));
        }
        return resultSet;
    }
}