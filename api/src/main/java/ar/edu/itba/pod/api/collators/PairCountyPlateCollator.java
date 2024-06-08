package ar.edu.itba.pod.api.collators;

import ar.edu.itba.pod.api.models.dtos.PlateCountyDto;
import ar.edu.itba.pod.api.models.WorstPlates;
import com.hazelcast.mapreduce.Collator;

import java.util.Map;
import java.util.TreeSet;

@SuppressWarnings("deprecation")
public class PairCountyPlateCollator implements Collator<Map.Entry<PlateCountyDto, Integer>, TreeSet<WorstPlates>>{

    @Override
    public TreeSet<WorstPlates> collate(Iterable<Map.Entry<PlateCountyDto, Integer>> iterable) {
        return null;
    }
}
