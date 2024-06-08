package ar.edu.itba.pod.api.models;

import ar.edu.itba.pod.api.interfaces.CsvWritable;

public class AgencyPercentage implements CsvWritable, Comparable<AgencyPercentage> {

    private final String issuingAgency;
    private final Double percentage;

    public AgencyPercentage(String issuingAgency, Double percentage) {
        this.issuingAgency = issuingAgency;
        this.percentage = percentage;
    }

    @Override
    public String toCsv() {
        return issuingAgency + ";" + String.format("%.2f", percentage) + "%";
    }

    @Override
    public int compareTo(AgencyPercentage o) {
        int percentageComparison = o.percentage.compareTo(percentage);
        if (percentageComparison != 0) {
            return percentageComparison;
        }
        return issuingAgency.compareTo(o.issuingAgency);
    }
}
