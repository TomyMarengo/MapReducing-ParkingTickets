package ar.edu.itba.pod.client.utils;

import ar.edu.itba.pod.api.models.CsvWritable;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class CsvWriter {
    public static <T extends CsvWritable> void writeCsv(String filePath, String header, Collection<T> data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(header);
            writer.newLine();
            for (T record : data) {
                writer.write(record.toCsv());
                writer.newLine();
            }
        }
    }
}