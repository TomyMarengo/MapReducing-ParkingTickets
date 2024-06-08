package ar.edu.itba.pod.api.collators;

import ar.edu.itba.pod.api.models.TopNInfractionsByCounty;
import com.hazelcast.mapreduce.Collator;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;
@SuppressWarnings("deprecation")
public class TopNInfractionsByCountyCollatorAlternative implements Collator<Map.Entry<String, List<String>> , TreeSet<TopNInfractionsByCounty>> {
    private final int n;

    public TopNInfractionsByCountyCollatorAlternative(int n) {
        this.n = n;
    }

    @Override
    public TreeSet<TopNInfractionsByCounty> collate(Iterable<Map.Entry<String, List<String>>> values) {
        TreeSet<TopNInfractionsByCounty> topN = new TreeSet<>();
        for (Map.Entry<String, List<String>> entry : values) {
            topN.add(new TopNInfractionsByCounty(entry.getKey(), entry.getValue(), n));
        }
        return topN;
    }

}
