package ar.edu.itba.pod.api.reducers;

import ar.edu.itba.pod.api.models.dtos.InfractionPlateDto;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;


@SuppressWarnings("deprecation")
public class MostInfractionsCountyPlateReducerFactory implements ReducerFactory<InfractionPlateDto, Integer, Integer> {

    @Override
    public Reducer<Integer, Integer> newReducer(InfractionPlateDto key) {
        return new MostInfractionsCountryPlateReducer();
    }

    private static class MostInfractionsCountryPlateReducer extends Reducer<Integer, Integer> {
        private Integer sum = 0;

        @Override
        public void reduce(Integer value) {
            sum += value;
        }

        @Override
        public Integer finalizeReduce() {
            return sum;
        }

    }

}