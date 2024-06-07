package ar.edu.itba.pod.api.models;

import ar.edu.itba.pod.api.interfaces.CsvWritable;

import java.util.List;

public record TopNInfractionsByCounty(String county, List<String> infractions, int n) implements CsvWritable, Comparable<TopNInfractionsByCounty> {
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
