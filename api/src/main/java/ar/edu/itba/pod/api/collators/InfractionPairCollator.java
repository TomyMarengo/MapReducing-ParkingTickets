package ar.edu.itba.pod.api.collators;

import ar.edu.itba.pod.api.models.InfractionPair;
import com.hazelcast.mapreduce.Collator;

import java.util.*;

@SuppressWarnings("deprecation")
public class InfractionPairCollator implements Collator<Map.Entry<String, Integer>, TreeSet<InfractionPair>> {
    @Override
    public TreeSet<InfractionPair> collate(Iterable<Map.Entry<String, Integer>> values) {
        TreeSet<InfractionPair> result = new TreeSet<>();
        List<Map.Entry<String, Integer>> entries = new ArrayList<>();

        // Copy the iterable to a list to avoid multiple iterations using index access
        for (Map.Entry<String, Integer> entry : values) {
            entries.add(entry);
        }

        int size = entries.size();
        for (int i = 0; i < size; i++) {
            Map.Entry<String, Integer> entry1 = entries.get(i);
            if (entry1.getValue() != 0) {
                for (int j = i + 1; j < size; j++) { // Avoid comparing the same entry
                    Map.Entry<String, Integer> entry2 = entries.get(j);
                    if (entry1.getValue().equals(entry2.getValue())) {
                        result.add(new InfractionPair(entry1.getKey(), entry2.getKey(), entry1.getValue()));
                    }
                }
            }
        }

        return result;
    }
}
