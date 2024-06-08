package ar.edu.itba.pod.api.collators;

import ar.edu.itba.pod.api.models.dtos.CountyInfractionDto;
import ar.edu.itba.pod.api.models.InfractionsCount;
import ar.edu.itba.pod.api.collections.TopNSet;
import com.hazelcast.mapreduce.Collator;

import java.util.Map;
@SuppressWarnings("deprecation")
public class TopNInfractionsByCountyCollator implements Collator<Map.Entry<CountyInfractionDto, Integer>, TopNSet<String, InfractionsCount>> {
    private final int n;

    public TopNInfractionsByCountyCollator(int n) {
        this.n=n;
    }

    @Override
    public TopNSet<String, InfractionsCount> collate(Iterable<Map.Entry<CountyInfractionDto, Integer>> values) {
        TopNSet<String, InfractionsCount> topNSet = new TopNSet<>(n);
        for (Map.Entry<CountyInfractionDto, Integer> entry : values) {
            topNSet.add(entry.getKey().getCounty(), new InfractionsCount(entry.getKey().getInfractionDefinition(), entry.getValue()));
        }
        return topNSet;
    }
}
