package ar.edu.itba.pod.api.collators;

import ar.edu.itba.pod.api.models.dtos.CountyPlateByInfractionCountDto;
import ar.edu.itba.pod.api.models.PlateAndQuantity;
import com.hazelcast.mapreduce.Collator;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


/* No se usa, solo para query 4 alternativa */
@SuppressWarnings("deprecation")
public class MostInfractionsCountyPlateCollator implements Collator<Map.Entry<String, PlateAndQuantity>, TreeSet<CountyPlateByInfractionCountDto>> {
    @Override
    public TreeSet<CountyPlateByInfractionCountDto> collate(Iterable<Map.Entry<String, PlateAndQuantity>> iterable) {
        TreeSet<CountyPlateByInfractionCountDto> result = new TreeSet<>();
        iterable.forEach((mapEntry) -> result.add(new CountyPlateByInfractionCountDto(mapEntry.getKey(),mapEntry.getValue().getPlate(),mapEntry.getValue().getQuantity())));
        return result;
    }
}