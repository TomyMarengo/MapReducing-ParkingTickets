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

public class CsvFileIterator implements Iterator<String[]>, Closeable {
    private final BufferedReader reader;
    private String currentLine;
    private static final SimpleDateFormat infractionsDateFormat = new SimpleDateFormat(Constants.INFRACTIONS_DATE_FORMAT);
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

    public static void parseInfractionsCsv(String inPath, String city, IMap<String, Infraction> infractionMap) {
        CsvFileIterator fileIterator = new CsvFileIterator(inPath + "/infractions" + city + ".csv");
        CsvMappingConfig config;
        try {
            config = CsvMappingConfigFactory.getInfractionConfig(inPath, city);
        } catch (IOException e) {
            logger.error("Failed to load CSV mapping configuration for city: " + city, e);
            return;
        }

        while (fileIterator.hasNext()) {
            String[] fields = fileIterator.next();
            if (fields.length == Infraction.FIELD_COUNT) {
                String code = fields[config.getColumnIndex("code")];
                String definition = fields[config.getColumnIndex("definition")];
                infractionMap.put(code, new Infraction(code, definition));
            } else {
                logger.error(String.format("Invalid line format, expected %d fields, found %d", Infraction.FIELD_COUNT, fields.length));
            }
        }
        fileIterator.close();
    }


    public static void parseTicketsCsv(String inPath, String city, IList<Ticket> ticketsList){
        CsvFileIterator fileIterator = new CsvFileIterator(inPath + "/tickets" + city + ".csv");
        CsvMappingConfig config;
        try {
            config = CsvMappingConfigFactory.getTicketConfig(inPath, city);
        } catch (IOException e) {
            logger.error("Failed to load CSV mapping configuration for city: " + city, e);
            return;
        }

        while (fileIterator.hasNext()) {
            String[] fields = fileIterator.next();
            if (fields.length >= Ticket.FIELD_COUNT) {
                try {
                    String plate = fields[config.getColumnIndex("plate")];
                    Date issueDate = infractionsDateFormat.parse(fields[config.getColumnIndex("issueDate")]);
                    String infractionCode = fields[config.getColumnIndex("infractionCode")];
                    Double fineAmount = Double.parseDouble(fields[config.getColumnIndex("fineAmount")]);
                    String countyName = fields[config.getColumnIndex("countyName")];
                    String issuingAgency = fields[config.getColumnIndex("issuingAgency")];

                    ticketsList.add(new Ticket(plate, issueDate, infractionCode, fineAmount, countyName, issuingAgency));
                } catch (ParseException e) {
                    logger.error("Error parsing date", e);
                }
            } else {
                logger.error(String.format("Invalid line format, expected %d fields, found %d", Ticket.FIELD_COUNT, fields.length));
            }
        }
        fileIterator.close();
    }
}