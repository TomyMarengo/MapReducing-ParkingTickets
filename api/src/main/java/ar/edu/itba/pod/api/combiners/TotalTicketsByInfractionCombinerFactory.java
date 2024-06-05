package ar.edu.itba.pod.api.combiners;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

@SuppressWarnings("deprecation")
public class TotalTicketsByInfractionCombinerFactory implements CombinerFactory<String, Integer, Integer> {
    @Override
    public Combiner<Integer, Integer> newCombiner(String key) {
        return new TotalTicketsByInfractionCombiner();
    }

    private static class TotalTicketsByInfractionCombiner extends Combiner<Integer, Integer> {
        private int sum = 0;

        @Override
        public void combine(Integer value) {
            sum += value; // sum += 1 because the mapper emits 1 for each ticket
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