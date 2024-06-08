package ar.edu.itba.pod.api.reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class TopNInfractionsByCountyReducerFactoryAlternative implements ReducerFactory<String, Map<String, Integer>, List<String>>{
@Override
    public Reducer<Map<String, Integer>, List<String>> newReducer(String key) {
        return new TopNInfractionsByCountyReducerAlternative();
    }

    private static class TopNInfractionsByCountyReducerAlternative extends Reducer<Map<String, Integer>, List<String>> {
        private Map<String, Integer> map = new HashMap<>();

        @Override
        public void reduce(Map<String, Integer> value) {
            value.forEach((k, v) -> map.put(k, map.getOrDefault(k, 0) + v));
        }

        @Override
        public List<String> finalizeReduce() {
            List<String> infractionsDescription = new ArrayList<>();
            map.entrySet().stream().sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                    .limit(3).forEach(e -> infractionsDescription.add(e.getKey()));
            return infractionsDescription;
        }
    }
}
