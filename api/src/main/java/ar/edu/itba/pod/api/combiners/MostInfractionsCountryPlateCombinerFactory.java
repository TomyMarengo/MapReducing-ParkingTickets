package ar.edu.itba.pod.api.combiners;

import ar.edu.itba.pod.api.models.dtos.InfractionPlateDto;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

import java.util.HashMap;
import java.util.Map;


/* No se usa, solo para query 4 alternativa */
@SuppressWarnings("deprecation")
public class MostInfractionsCountryPlateCombinerFactory implements CombinerFactory<String, String, Map<String,Integer>> {

    @Override
    public Combiner<String, Map<String,Integer>> newCombiner(String key) {
        return new MostInfractionsCountryPlateCombinerFactory.MostInfractionsCountryPlateCombiner();
    }

    private static class MostInfractionsCountryPlateCombiner extends Combiner<String, Map<String,Integer>> {
        private  Map<String,Integer> plateCounts = new HashMap<>();

        @Override
        public void combine(String plate) {
            plateCounts.putIfAbsent(plate,0);
            plateCounts.merge(plate,1, Integer::sum);
        }

        @Override
        public Map<String,Integer> finalizeChunk() {
            return plateCounts;
        }

        @Override
        public void reset(){
            plateCounts = new HashMap<>();
        }
    }

}