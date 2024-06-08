package ar.edu.itba.pod.api.combiners;

import ar.edu.itba.pod.api.models.dtos.CountyInfractionDto;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;
@SuppressWarnings("deprecation")
public class TopNInfractionsByCountyCombinerFactory implements CombinerFactory<CountyInfractionDto, Integer, Integer> {

    @Override
    public Combiner<Integer, Integer> newCombiner(CountyInfractionDto countyInfractionDto) {
        return new Top3InfractionsByCountyCombiner();
    }

    private static class Top3InfractionsByCountyCombiner extends Combiner<Integer, Integer>{
        private int sum = 0;

        @Override
        public void combine(Integer value) {
            sum += value;
        }

        @Override
        public Integer finalizeChunk() {
            return sum;
        }

        @Override
        public void reset() {
            sum = 0;
        }
    }
}
