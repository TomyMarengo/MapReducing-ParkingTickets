package ar.edu.itba.pod.api.combiners;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

import java.math.BigDecimal;

@SuppressWarnings("deprecation")
public class TopNCollectorAgenciesCombinerFactory implements CombinerFactory<String, BigDecimal, BigDecimal> {
    @Override
    public Combiner<BigDecimal, BigDecimal> newCombiner(String key) {
        return new TopNCollectorAgenciesCombiner();
    }

    private static class TopNCollectorAgenciesCombiner extends Combiner<BigDecimal, BigDecimal> {
        private BigDecimal sum = BigDecimal.ZERO;

        @Override
        public void combine(BigDecimal value) {
            sum = sum.add(value);
        }

        @Override
        public BigDecimal finalizeChunk() {
            return sum;
        }

        @Override
        public void reset() {
            sum = BigDecimal.ZERO;
        }
    }
}