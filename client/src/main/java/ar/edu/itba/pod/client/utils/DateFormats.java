package ar.edu.itba.pod.client.utils;

import ar.edu.itba.pod.client.exceptions.ClientFileException;

import java.text.SimpleDateFormat;
import java.util.Date;

public enum DateFormats {
    CHI("yyyy-MM-dd HH:mm:ss"),
    NYC("yyyy-MM-dd");

    private final String format;

    DateFormats(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public static Date parseDate(String date) {
        for (DateFormats format : DateFormats.values()) {
            try {
                return new SimpleDateFormat(format.getFormat()).parse(date);
            } catch (Exception e) {
                // Ignore
            }
        }
        throw new ClientFileException("Invalid date format");
    }
}
