package ar.edu.itba.pod.api.combiners;

import ar.edu.itba.pod.api.models.dtos.PlateCountyDto;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;



@SuppressWarnings("deprecation")
public class PairCountyPlateCombinerFactory implements CombinerFactory<PlateCountyDto, Integer, Integer> {

    @Override
    public Combiner<Integer, Integer> newCombiner(PlateCountyDto key) {
        return new MostInfractionsCountryPlateCombiner();
    }

    private static class MostInfractionsCountryPlateCombiner extends Combiner<Integer, Integer> {
        private Integer sum = 0;

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