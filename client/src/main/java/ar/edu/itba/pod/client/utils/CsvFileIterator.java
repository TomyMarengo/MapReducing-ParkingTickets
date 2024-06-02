package ar.edu.itba.pod.client.utils;

import ar.edu.itba.pod.api.models.Ticket;
import ar.edu.itba.pod.client.exceptions.ClientFileException;
import ar.edu.itba.pod.client.exceptions.ClientIllegalArgumentException;
import com.hazelcast.core.IList;
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
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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

    public static void parseTicketsCsv(String inPath, String city, IList<Ticket> ticketsList){
        CsvFileIterator fileIterator = new CsvFileIterator(inPath + "/tickets" + city + ".csv");
        while (fileIterator.hasNext()) {
            String[] fields = fileIterator.next();
            if (fields.length == Ticket.FIELD_COUNT) {
                String plate = fields[0];
                Date issueDate;
                try {
                    issueDate = dateFormat.parse(fields[1]);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                String infractionCode = fields[2];
                Double fineAmount = Double.parseDouble(fields[3]);
                String countyName = fields[4];
                String issuingAgency = fields[5];
                ticketsList.add(new Ticket(plate, issueDate, infractionCode, fineAmount, countyName, issuingAgency));
                System.out.println("Added ticket: " + new Ticket(plate, issueDate, infractionCode, fineAmount, countyName, issuingAgency));
            }
            else {
                logger.error(String.format("Invalid line format, expected %d fields, found %d", Ticket.FIELD_COUNT, fields.length));
            }
        }
        fileIterator.close();
    }
}