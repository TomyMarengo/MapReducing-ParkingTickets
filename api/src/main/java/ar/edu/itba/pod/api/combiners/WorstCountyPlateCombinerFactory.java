package ar.edu.itba.pod.api.combiners;

import ar.edu.itba.pod.api.models.dtos.PlateCountyDto;
import ar.edu.itba.pod.api.models.dtos.PlateTicketsDto;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

@SuppressWarnings("deprecation")
public class WorstCountyPlateCombinerFactory implements CombinerFactory<String, PlateTicketsDto, PlateTicketsDto> {

        @Override
        public Combiner<PlateTicketsDto, PlateTicketsDto> newCombiner(String key) {
            return new WorstCountyPlateCombiner();
        }

        private static class WorstCountyPlateCombiner extends Combiner<PlateTicketsDto, PlateTicketsDto> {
            private PlateTicketsDto worst = null;

            @Override
            public void combine(PlateTicketsDto value) {
                if (worst == null || value.getTickets() > worst.getTickets()) {
                    worst = value;
                }
            }

            @Override
            public PlateTicketsDto finalizeChunk() {
                return worst;
            }

            @Override
            public void reset() {
                worst = null;
            }
        }
}
