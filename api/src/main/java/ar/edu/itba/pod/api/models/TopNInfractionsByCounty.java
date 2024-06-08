package ar.edu.itba.pod.api.models;

import ar.edu.itba.pod.api.interfaces.CsvWritable;

import java.util.List;

public class TopNInfractionsByCounty implements CsvWritable, Comparable<TopNInfractionsByCounty> {
    private final String county;
    private final List<String> infractions;
    private final int n;

    public TopNInfractionsByCounty(String county, List<String> infractions, int n) {
        this.county = county;
        this.infractions = infractions;
        this.n = n;
    }

    @Override
    public String toCsv() {
        StringBuilder sb = new StringBuilder();
        sb.append(county);
        for (int i = 0; i < n; i++) {
            if (i < infractions.size()) {
                sb.append(";").append(infractions.get(i));
            } else {
                sb.append(";-");
            }
        }
        return sb.toString();
    }

    @Override
    public int compareTo(TopNInfractionsByCounty o) {
        return county.compareTo(o.county);
    }
}
