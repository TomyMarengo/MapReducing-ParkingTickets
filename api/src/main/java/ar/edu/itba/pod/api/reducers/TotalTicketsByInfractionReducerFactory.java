package ar.edu.itba.pod.api.reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class TotalTicketsByInfractionReducerFactory implements ReducerFactory<String, Integer, Integer> {
    @Override
    public Reducer<Integer, Integer> newReducer(String key) {
        return new TotalTicketsByInfractionReducer();
    }

    private static class TotalTicketsByInfractionReducer extends Reducer<Integer, Integer> {
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