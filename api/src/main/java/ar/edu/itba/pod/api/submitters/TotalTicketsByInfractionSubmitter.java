package ar.edu.itba.pod.api.submitters;

import ar.edu.itba.pod.api.models.TicketByInfractionDto;
import com.hazelcast.mapreduce.Collator;

import java.util.*;

@SuppressWarnings("deprecation")
public class TotalTicketsByInfractionSubmitter implements Collator<Map.Entry<String, Integer>, TreeSet<TicketByInfractionDto>> {
    @Override
    public TreeSet<TicketByInfractionDto> collate(Iterable<Map.Entry<String, Integer>> values) {
        TreeSet<TicketByInfractionDto> resultSet = new TreeSet<>();
        for (Map.Entry<String, Integer> entry : values) {
            resultSet.add(new TicketByInfractionDto(entry.getKey(), entry.getValue()));
        }
        return resultSet;
    }

    //TODO: Check infractions with 0 tickets, are they showing up?
}