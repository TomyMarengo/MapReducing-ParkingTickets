package ar.edu.itba.pod.api.collators;

import ar.edu.itba.pod.api.models.CountyAndInfractionDto;
import ar.edu.itba.pod.api.models.InfractionsCount;
import ar.edu.itba.pod.api.models.TopNSet;
import com.hazelcast.mapreduce.Collator;

import java.util.Map;
@SuppressWarnings("deprecation")
public class TopNInfractionsByCountyCollator implements Collator<Map.Entry<CountyAndInfractionDto, Integer>, TopNSet<String, InfractionsCount>> {
    private final int n;

    public TopNInfractionsByCountyCollator(int n) {
        this.n=n;
    }

    @Override
    public TopNSet<String, InfractionsCount> collate(Iterable<Map.Entry<CountyAndInfractionDto, Integer>> values) {
        TopNSet<String, InfractionsCount> topNSet = new TopNSet<>(n);
        for (Map.Entry<CountyAndInfractionDto, Integer> entry : values) {
            topNSet.add(entry.getKey().getCounty(), new InfractionsCount(entry.getKey().getInfractionDescription(), entry.getValue()));
        }
        return topNSet;
    }
}
