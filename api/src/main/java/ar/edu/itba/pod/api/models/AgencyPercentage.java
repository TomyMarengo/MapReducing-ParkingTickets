package ar.edu.itba.pod.api.models;

import ar.edu.itba.pod.api.interfaces.CsvWritable;

public record AgencyPercentage(String issuingAgency, Double percentage) implements Comparable<AgencyPercentage>, CsvWritable {

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
