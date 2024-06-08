package ar.edu.itba.pod.api.reducers;

import ar.edu.itba.pod.api.models.CountyPlateByInfractionCountDto;
import ar.edu.itba.pod.api.models.dtos.InfractionPlateDto;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("deprecation")
public class MostInfractionsCountyPlateReducerFactory implements ReducerFactory<String, Map<String,Integer>, CountyPlateByInfractionCountDto> {

    @Override
    public Reducer<Map<String,Integer>, CountyPlateByInfractionCountDto> newReducer(String county) {
        return new MostInfractionsCountryPlateReducer(county);
    }

    private static class MostInfractionsCountryPlateReducer extends Reducer<Map<String,Integer>, CountyPlateByInfractionCountDto> {
        private final Map<String,Integer> combinedCounts = new HashMap<>();
        private final String county;
        public MostInfractionsCountryPlateReducer(String county)
        {
            this.county = county;
        }


        @Override
        public void reduce(Map<String,Integer> counts) {
            counts.forEach((plate,amount) -> {
                combinedCounts.putIfAbsent(plate,amount);
                combinedCounts.merge(plate,amount,Integer::sum);
            });
        }

        @Override
        public CountyPlateByInfractionCountDto finalizeReduce() {
            return  combinedCounts.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map((entry) -> new CountyPlateByInfractionCountDto(county,entry.getKey(),entry.getValue()))
                    .orElse(null);//TODO ver que lanzamos aca
        }

    }

}