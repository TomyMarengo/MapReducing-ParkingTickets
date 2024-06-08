package ar.edu.itba.pod.api.combiners;

import ar.edu.itba.pod.api.models.dtos.FineAmountSumDto;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

import java.math.BigDecimal;

@SuppressWarnings("deprecation")
public class InfractionPairCombinerFactory implements CombinerFactory<String, Double, FineAmountSumDto> {
    @Override
    public Combiner<Double, FineAmountSumDto> newCombiner(String key) {
        return new InfractionPairCombiner();
    }

    private static class InfractionPairCombiner extends Combiner<Double, FineAmountSumDto> {
        private BigDecimal sum = BigDecimal.ZERO;
        private Integer count = 0;

        @Override
        public void combine(Double value) {
            sum = sum.add(BigDecimal.valueOf(value));
            count++;
        }

        @Override
        public FineAmountSumDto finalizeChunk() {
            return new FineAmountSumDto(sum, count);
        }

        @Override
        public void reset() {
            sum = BigDecimal.ZERO;
            count = 0;
        }
    }
}
