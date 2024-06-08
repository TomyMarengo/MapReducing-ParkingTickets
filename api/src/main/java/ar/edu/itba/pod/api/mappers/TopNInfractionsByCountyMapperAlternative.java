package ar.edu.itba.pod.api.mappers;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class TopNInfractionsByCountyMapperAlternative implements Mapper<String, String, String, String> {
    @Override
    public void map(String countyName, String infractionDescription, Context<String, String> context) {
        context.emit(countyName, infractionDescription);
    }
}
