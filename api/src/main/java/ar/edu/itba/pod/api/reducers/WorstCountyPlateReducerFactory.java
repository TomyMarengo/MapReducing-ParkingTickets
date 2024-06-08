package ar.edu.itba.pod.api.reducers;

import ar.edu.itba.pod.api.models.dtos.PlateTicketsDto;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class WorstCountyPlateReducerFactory implements ReducerFactory<String, PlateTicketsDto, PlateTicketsDto> {

    @Override
    public Reducer<PlateTicketsDto, PlateTicketsDto> newReducer(String key) {
        return new WorstCountyPlateReducer();
    }

    private static class WorstCountyPlateReducer extends Reducer<PlateTicketsDto, PlateTicketsDto> {
        private PlateTicketsDto worst = null;

        @Override
        public void reduce(PlateTicketsDto value) {
            if (worst == null || value.getTickets() > worst.getTickets()) {
                worst = value;
            }
        }

        @Override
        public PlateTicketsDto finalizeReduce() {
            return worst;
        }
    }
}