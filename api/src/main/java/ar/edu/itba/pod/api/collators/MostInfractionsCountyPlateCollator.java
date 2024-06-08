package ar.edu.itba.pod.api.collators;

import ar.edu.itba.pod.api.models.CountyPlateByInfractionCountDto;
import ar.edu.itba.pod.api.models.PlateAndQuantity;
import com.hazelcast.mapreduce.Collator;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


@SuppressWarnings("deprecation")
public class MostInfractionsCountyPlateCollator implements Collator<Map.Entry<String, PlateAndQuantity>, TreeSet<CountyPlateByInfractionCountDto>> {



    @Override
    public TreeSet<CountyPlateByInfractionCountDto> collate(Iterable<Map.Entry<String, PlateAndQuantity>> iterable) {
        TreeSet<CountyPlateByInfractionCountDto> result = new TreeSet<>();
        iterable.forEach((mapEntry) -> result.add(new CountyPlateByInfractionCountDto(mapEntry.getKey(),mapEntry.getValue().plate(),mapEntry.getValue().quantity())));
        return result;
    }
}
