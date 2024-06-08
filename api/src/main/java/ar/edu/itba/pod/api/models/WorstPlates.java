package ar.edu.itba.pod.api.models;

import ar.edu.itba.pod.api.interfaces.CsvWritable;

public class WorstPlates implements CsvWritable, Comparable<WorstPlates> {
    private String plate;
    private String county;
    private Integer tickets;

    public WorstPlates() {}

    public WorstPlates(String plate, String county, Integer tickets) {
        this.plate = plate;
        this.county = county;
        this.tickets = tickets;
    }

    public String getPlate() {
        return plate;
    }

    public String getCounty(){
        return county;
    }

    public Integer getTickets() {
        return tickets;
    }

    @Override
    public String toCsv() {
        return county + ";" + plate + ";" + tickets;
    }

    @Override
    public int compareTo(WorstPlates o) {
        return county.compareTo(o.getCounty());
    }
}

