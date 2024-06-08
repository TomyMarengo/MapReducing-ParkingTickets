package ar.edu.itba.pod.api.reducers;

import ar.edu.itba.pod.api.models.dtos.CountyInfractionDto;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class TopNInfractionsByCountyReducerFactory implements ReducerFactory<CountyInfractionDto, Integer, Integer> {

    @Override
    public Reducer<Integer, Integer> newReducer(CountyInfractionDto key) {
        return new Top3InfractionsByCountyReducer();
    }

    private static class Top3InfractionsByCountyReducer extends  Reducer<Integer, Integer> {
        private int sum = 0;

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

