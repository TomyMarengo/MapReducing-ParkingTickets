package ar.edu.itba.pod.api.reducers;

import ar.edu.itba.pod.api.models.CountyPlateByInfractionCountDto;
import ar.edu.itba.pod.api.models.PlateAndQuantity;
import ar.edu.itba.pod.api.models.dtos.InfractionPlateDto;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("deprecation")
public class MostInfractionsCountyPlateReducerFactory implements ReducerFactory<String, Map<String,Integer>, PlateAndQuantity> {

    @Override
    public Reducer<Map<String,Integer>, PlateAndQuantity> newReducer(String county) {
        return new MostInfractionsCountryPlateReducer();
    }

    private static class MostInfractionsCountryPlateReducer extends Reducer<Map<String,Integer>, PlateAndQuantity> {
        private final Map<String,Integer> combinedCounts = new HashMap<>();



        @Override
        public void reduce(Map<String,Integer> counts) {
            counts.forEach((plate,amount) -> {
                combinedCounts.putIfAbsent(plate,0);
                combinedCounts.merge(plate,amount,Integer::sum);
            });
        }

        @Override
        public PlateAndQuantity finalizeReduce() {
            return  combinedCounts.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map((entry) -> new PlateAndQuantity(entry.getKey(),entry.getValue()))
                    .orElse(null);//TODO ver que lanzamos aca
        }

    }

}