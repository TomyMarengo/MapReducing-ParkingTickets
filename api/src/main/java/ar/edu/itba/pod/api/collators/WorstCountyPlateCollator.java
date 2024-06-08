package ar.edu.itba.pod.api.collators;

import ar.edu.itba.pod.api.models.WorstPlates;
import ar.edu.itba.pod.api.models.dtos.PlateTicketsDto;
import com.hazelcast.mapreduce.Collator;

import java.util.Map;
import java.util.TreeSet;

@SuppressWarnings("deprecation")
public class WorstCountyPlateCollator implements Collator<Map.Entry<String, PlateTicketsDto>, TreeSet<WorstPlates>> {
    @Override
    public TreeSet<WorstPlates> collate(Iterable<Map.Entry<String, PlateTicketsDto>> values) {
        TreeSet<WorstPlates> resultSet = new TreeSet<>();
        for (Map.Entry<String, PlateTicketsDto> entry : values) {
            resultSet.add(new WorstPlates(entry.getValue().getPlate(), entry.getKey(), entry.getValue().getTickets()));
        }
        return resultSet;
    }
}