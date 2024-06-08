package ar.edu.itba.pod.api.models;

import ar.edu.itba.pod.api.interfaces.CsvWritable;

public class TicketByInfraction implements Comparable<TicketByInfraction>, CsvWritable {
    private String infractionDefinition;
    private int count;

    // No-arg constructor for deserialization
    public TicketByInfraction() {}

    public TicketByInfraction(String infractionDefinition, int count) {
        this.infractionDefinition = infractionDefinition;
        this.count = count;
    }

    public String getInfractionDefinition() {
        return infractionDefinition;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int compareTo(TicketByInfraction other) {
        int countComparison = Integer.compare(other.count, this.count);
        return countComparison != 0 ? countComparison : this.infractionDefinition.compareTo(other.infractionDefinition);
    }

    @Override
    public String toString() {
        return infractionDefinition + "; " + count;
    }

    @Override
    public String toCsv() {
        return infractionDefinition + ";" + count;
    }
}