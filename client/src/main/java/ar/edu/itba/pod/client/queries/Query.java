package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.client.Client;
import ar.edu.itba.pod.client.utils.Arguments;
import ar.edu.itba.pod.client.utils.Constants;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Query {
    protected HazelcastInstance hazelcastInstance;
    protected Arguments arguments;
    private static Logger logger = LoggerFactory.getLogger(Client.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIMESTAMP_LOGS_DATE_FORMAT);

    public void execute(HazelcastInstance hazelcastInstance, Arguments arguments) {
        this.hazelcastInstance = hazelcastInstance;
        this.arguments = arguments;

        // Log and write the start time of data loading
        long startLoadingTime = System.currentTimeMillis();
        String startLoadingLog = sdf.format(new Date(startLoadingTime)) + Constants.FILE_READ_START_MESSAGE;
        logAndWrite(startLoadingLog);

        loadData();

        // Log and write the end time of data loading
        long endLoadingTime = System.currentTimeMillis();
        String endLoadingLog = sdf.format(new Date(endLoadingTime)) + Constants.FILE_READ_END_MESSAGE;
        logAndWrite(endLoadingLog);

        // Log and write the start time of job execution
        long startJobTime = System.currentTimeMillis();
        String startJobLog = sdf.format(new Date(startJobTime)) + Constants.MAP_REDUCE_START_MESSAGE;
        logAndWrite(startJobLog);

        executeJob();

        // Log and write the end time of job execution
        long endJobTime = System.currentTimeMillis();
        String endJobLog = sdf.format(new Date(endJobTime)) + Constants.MAP_REDUCE_END_MESSAGE;
        logAndWrite(endJobLog);
    }

    private void logAndWrite(String message) {
        logger.info(message.trim());
        try (FileWriter writer = new FileWriter(arguments.getOutPath() + Constants.TIMESTAMP_LOGS_FILE, true)) {
            writer.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void loadData();

    protected abstract void executeJob();
}