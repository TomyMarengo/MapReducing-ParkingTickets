package ar.edu.itba.pod.api.models;

import ar.edu.itba.pod.api.interfaces.CsvWritable;

public class InfractionPair implements CsvWritable, Comparable<InfractionPair> {
    private final String infractionA;
    private final String infractionB;
    private final int groupedAmount;

    public InfractionPair(String infractionA, String infractionB, int groupedAmount) {
        /* Saving the infraction names in lexicographical order */
        if (infractionA.compareTo(infractionB) < 0) {
            this.infractionA = infractionA;
            this.infractionB = infractionB;
        } else {
            this.infractionA = infractionB;
            this.infractionB = infractionA;
        }
        this.groupedAmount = groupedAmount;
    }

    public String getInfractionA() {
        return infractionA;
    }

    public String getInfractionB() {
        return infractionB;
    }

    public int getGroupedAmount() {
        return groupedAmount;
    }

    @Override
    public String toCsv() {
        return groupedAmount + ";" + infractionA + ";" + infractionB;
    }

    @Override
    public int compareTo(InfractionPair other) {
        /* Compare by groupedAmount, then by infractionA, then by infractionB */
        if (groupedAmount != other.groupedAmount) {
            return Integer.compare(groupedAmount, other.groupedAmount);
        }
        if (!infractionA.equals(other.infractionA)) {
            return infractionA.compareTo(other.infractionA);
        }
        return infractionB.compareTo(other.infractionB);
    }
}
