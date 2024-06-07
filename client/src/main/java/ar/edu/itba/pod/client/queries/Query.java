package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.api.interfaces.CsvWritable;
import ar.edu.itba.pod.api.interfaces.TriConsumer;
import ar.edu.itba.pod.client.Client;
import ar.edu.itba.pod.client.utils.*;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public abstract class Query {
    protected HazelcastInstance hazelcastInstance;
    protected Arguments arguments;
    protected static final Logger logger = LoggerFactory.getLogger(Client.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIMESTAMP_LOGS_DATE_FORMAT);
    String timestampLogFilePath;
    String queryOutputFilePath;

    public void execute(HazelcastInstance hazelcastInstance, Arguments arguments) {
        this.hazelcastInstance = hazelcastInstance;
        this.arguments = arguments;

        timestampLogFilePath = String.format(arguments.getOutPath() + Constants.TIMESTAMP_LOGS_FILE_TEMPLATE, arguments.getQuery());
        queryOutputFilePath = String.format(arguments.getOutPath() + Constants.QUERY_OUTPUT_FILE_TEMPLATE, arguments.getQuery());

        // Log and write the start time of data loading
        long startLoadingTime = System.currentTimeMillis();
        String startLoadingLog = sdf.format(new Date(startLoadingTime)) + Constants.FILE_READ_START_MESSAGE;
        logAndWrite(startLoadingLog, timestampLogFilePath);

        loadData();

        // Log and write the end time of data loading
        long endLoadingTime = System.currentTimeMillis();
        String endLoadingLog = sdf.format(new Date(endLoadingTime)) + Constants.FILE_READ_END_MESSAGE;
        logAndWrite(endLoadingLog, timestampLogFilePath);

        // Log and write the start time of job execution
        long startJobTime = System.currentTimeMillis();
        String startJobLog = sdf.format(new Date(startJobTime)) + Constants.MAP_REDUCE_START_MESSAGE;
        logAndWrite(startJobLog, timestampLogFilePath);

        executeJob();

        // Log and write the end time of job execution
        long endJobTime = System.currentTimeMillis();
        String endJobLog = sdf.format(new Date(endJobTime)) + Constants.MAP_REDUCE_END_MESSAGE;
        logAndWrite(endJobLog, timestampLogFilePath);
    }

    private void logAndWrite(String message, String filePath) {
        logger.info(message.trim());
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeData(String header, Collection<? extends CsvWritable> result) {
        try {
            CsvWriter.writeCsv(queryOutputFilePath, header, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void loadData() {
        // Parse infractions CSV
        if (infractionsConsumer() != null) {
            CsvFileIterator.readCsv(arguments, CsvFileType.INFRACTIONS, infractionsConsumer());
        }

        // Parse tickets CSV and count infractions
        if (ticketsConsumer() != null) {
            CsvFileIterator.readCsvParallel(arguments, CsvFileType.TICKETS, ticketsConsumer());
        }
    }

    protected abstract TriConsumer<String[], CsvMappingConfig, Integer> infractionsConsumer();

    protected abstract TriConsumer<String[], CsvMappingConfig, Integer> ticketsConsumer();

    protected abstract void executeJob();
}