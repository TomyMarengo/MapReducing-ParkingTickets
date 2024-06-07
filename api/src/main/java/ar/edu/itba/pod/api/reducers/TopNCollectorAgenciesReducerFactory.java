package ar.edu.itba.pod.api.reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.math.BigDecimal;

@SuppressWarnings("deprecation")
public class TopNCollectorAgenciesReducerFactory implements ReducerFactory<String, BigDecimal, BigDecimal> {
    @Override
    public Reducer<BigDecimal, BigDecimal> newReducer(String key) {
        return new TopNCollectorAgenciesReducer();
    }

    private static class TopNCollectorAgenciesReducer extends Reducer<BigDecimal, BigDecimal> {
        private BigDecimal sum = BigDecimal.ZERO;

        @Override
        public void reduce(BigDecimal value) {
            sum = sum.add(value);
        }

        @Override
        public BigDecimal finalizeReduce() {
            return sum;
        }
    }
}