package ar.edu.itba.pod.api.collators;

import ar.edu.itba.pod.api.models.CountyPlateByInfractionCountDto;
import com.hazelcast.mapreduce.Collator;

import java.util.Collection;
import java.util.TreeSet;


@SuppressWarnings("deprecation")
public class MostInfractionsCountyPlateCollator implements Collator<CountyPlateByInfractionCountDto, TreeSet<CountyPlateByInfractionCountDto>> {

    @Override
    public TreeSet<CountyPlateByInfractionCountDto> collate(Iterable<CountyPlateByInfractionCountDto> resultData) {
        TreeSet<CountyPlateByInfractionCountDto> result = new TreeSet<>();
        resultData.forEach(result::add);
        return result;
    }
}
