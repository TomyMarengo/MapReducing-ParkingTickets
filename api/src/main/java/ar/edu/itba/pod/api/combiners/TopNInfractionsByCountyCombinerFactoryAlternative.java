package ar.edu.itba.pod.api.combiners;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class TopNInfractionsByCountyCombinerFactoryAlternative implements CombinerFactory<String, String, Map<String, Integer> > {
    @Override
    public Combiner<String, Map<String, Integer>> newCombiner(String s) {
        return new TopNInfractionsByCountyCombinerAlternative();
    }

    private static class TopNInfractionsByCountyCombinerAlternative extends Combiner<String, Map<String, Integer>> {
        private Map<String, Integer> map = new HashMap<>();

        @Override
        public void combine(String s) {
            map.put(s, map.getOrDefault(s, 0) + 1);
        }

        @Override
        public Map<String, Integer> finalizeChunk() {
            return map;
        }

        @Override
        public void reset() {
            map = new HashMap<>();
        }
    }
}
