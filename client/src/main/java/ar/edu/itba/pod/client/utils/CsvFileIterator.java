package ar.edu.itba.pod.client.utils;

import ar.edu.itba.pod.api.models.Infraction;
import ar.edu.itba.pod.api.models.Ticket;
import ar.edu.itba.pod.client.exceptions.ClientFileException;
import ar.edu.itba.pod.client.exceptions.ClientIllegalArgumentException;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.function.BiConsumer;

public class CsvFileIterator implements Iterator<String[]>, Closeable {
    private final BufferedReader reader;
    private String currentLine;
    private static final Logger logger = LoggerFactory.getLogger(CsvFileIterator.class);

    public CsvFileIterator(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("The filename cannot be null");
        }

        try {
            reader = new BufferedReader(new FileReader(filename));
            reader.readLine(); // Skip header
            currentLine = reader.readLine();
        } catch (FileNotFoundException e) {
            throw new ClientIllegalArgumentException("The file " + filename + " was not found", e.getCause());
        } catch (IOException e) {
            throw new ClientFileException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean hasNext() {
        return currentLine != null;
    }

    @Override
    public String[] next() {
        if (!hasNext()) {
            throw new IllegalStateException("No more lines to read");
        }

        String[] fields = currentLine.split(";");

        try {
            currentLine = reader.readLine();
        } catch (IOException e) {
            throw new ClientFileException(e.getMessage(), e.getCause());
        }

        return fields;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove operation is not supported");
    }

    @Override
    public void close() {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                throw new ClientFileException(e.getMessage(), e.getCause());
            }
        }
    }

    //TODO: read with threads
    public static void readCsv(Arguments arguments, CsvFileType fileType, BiConsumer<String[], CsvMappingConfig> consumer) {
        CsvMappingConfig config;
        String filename;

        try {
            filename = switch (fileType) {
                case TICKETS -> {
                    config = CsvMappingConfigFactory.getTicketConfig(arguments.getInPath(), arguments.getCity());
                    yield arguments.getInPath() + "/tickets" + arguments.getCity() + ".csv";
                }
                case INFRACTIONS -> {
                    config = CsvMappingConfigFactory.getInfractionConfig(arguments.getInPath(), arguments.getCity());
                    yield arguments.getInPath() + "/infractions" + arguments.getCity() + ".csv";
                }
                default -> throw new IllegalArgumentException("Unsupported CSV file type");
            };
        } catch (IOException e) {
            throw new RuntimeException("Failed to load CSV mapping configuration for city: " + arguments.getCity(), e);
        }

        try (CsvFileIterator fileIterator = new CsvFileIterator(filename)) {
            while (fileIterator.hasNext()) {
                consumer.accept(fileIterator.next(), config);
            }
        }
    }
}