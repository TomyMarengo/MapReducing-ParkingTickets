package ar.edu.itba.pod.api.models;

import ar.edu.itba.pod.api.interfaces.CsvWritable;

public class TicketByInfraction implements Comparable<TicketByInfraction>, CsvWritable {
    private String infractionDescription;
    private int count;

    // No-arg constructor for deserialization
    public TicketByInfraction() {}

    public TicketByInfraction(String infractionDescription, int count) {
        this.infractionDescription = infractionDescription;
        this.count = count;
    }

    public String getInfractionDescription() {
        return infractionDescription;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int compareTo(TicketByInfraction other) {
        int countComparison = Integer.compare(other.count, this.count);
        return countComparison != 0 ? countComparison : this.infractionDescription.compareTo(other.infractionDescription);
    }

    @Override
    public String toString() {
        return infractionDescription + "; " + count;
    }

    @Override
    public String toCsv() {
        return infractionDescription + ";" + count;
    }
}