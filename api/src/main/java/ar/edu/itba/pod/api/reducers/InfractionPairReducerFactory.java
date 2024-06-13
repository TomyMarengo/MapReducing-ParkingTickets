package ar.edu.itba.pod.api.reducers;

import ar.edu.itba.pod.api.models.dtos.FineAmountSumDto;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

@SuppressWarnings("deprecation")
public class InfractionPairReducerFactory implements ReducerFactory<String, Double, Integer> {
    @Override
    public Reducer<Double, Integer> newReducer(String key) {
        return new InfractionPairReducer();
    }

    private static class InfractionPairReducer extends Reducer<Double, Integer> {
        private BigDecimal sum = BigDecimal.ZERO;
        private Integer count = 0;

        @Override
        public void reduce(Double value) {
            sum = sum.add(BigDecimal.valueOf(value));
            count ++;
        }

        @Override
        public Integer finalizeReduce() {
            BigDecimal average = sum.divide(BigDecimal.valueOf(count), 0, RoundingMode.DOWN);
            // Round the average to the nearest multiple of 100
            int roundedAverage = average.setScale(0, RoundingMode.DOWN).intValue();
            int remainder = roundedAverage / 100;
            return remainder * 100;
        }
    }
}