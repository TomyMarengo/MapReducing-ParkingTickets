package ar.edu.itba.pod.api.collators;

import ar.edu.itba.pod.api.models.AgencyPercentage;
import com.hazelcast.mapreduce.Collator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.TreeSet;

@SuppressWarnings("deprecation")
public class TopNCollectorAgenciesCollator implements Collator<Map.Entry<String, BigDecimal>, TreeSet<AgencyPercentage>> {
    private final int n;

    public TopNCollectorAgenciesCollator(int n) {
        this.n=n;
    }

    @Override
    public TreeSet<AgencyPercentage> collate(Iterable<Map.Entry<String, BigDecimal>> values) {
        TreeSet<AgencyPercentage> topN = new TreeSet<>();
        TreeSet<AgencyFine> topNAgencies = new TreeSet<>();
        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<String, BigDecimal> entry : values) {
            total = total.add(entry.getValue());
            topNAgencies.add(new AgencyFine(entry.getKey(), entry.getValue()));
        }

        final BigDecimal finalTotal = total;
        topNAgencies.stream().limit(n).forEach(
                agencyFine -> topN.add(
                        new AgencyPercentage(
                                agencyFine.agency(),
                                agencyFine.fine()
                                        .divide(finalTotal, 4, RoundingMode.DOWN)
                                        .multiply(BigDecimal.valueOf(100))
                                        .setScale(2, RoundingMode.DOWN)
                                        .doubleValue()
                        )
                )
        );

        return topN;
    }

    private record AgencyFine(String agency, BigDecimal fine) implements Comparable<AgencyFine> {

        @Override
            public int compareTo(AgencyFine o) {
                int fineComparison = o.fine.compareTo(fine);
                if (fineComparison != 0) {
                    return fineComparison;
                }
                return agency.compareTo(o.agency);
            }
        }
}