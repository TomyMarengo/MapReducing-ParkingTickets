package ar.edu.itba.pod.client.utils;

import ar.edu.itba.pod.client.exceptions.ClientFileException;
import ar.edu.itba.pod.client.exceptions.ClientIllegalArgumentException;
import ar.edu.itba.pod.api.interfaces.TriConsumer;

import java.io.*;
import java.util.Iterator;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CsvFileIterator implements Iterator<String[]>, Closeable {
    private final BufferedReader reader;
    private String currentLine;

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

        String[] fields = currentLine.split(",");

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

    private static FilenameAndConfig getFilenameAndConfig(Arguments arguments, CsvFileType fileType) {
        CsvMappingConfig config;
        String filename;

        try {
            switch (fileType) {
                case TICKETS -> {
                    config = CsvMappingConfigFactory.getTicketConfig(arguments.getInPath(), arguments.getCity());
                    filename = arguments.getInPath() + "/tickets" + arguments.getCity() + ".csv";
                }
                case INFRACTIONS -> {
                    config = CsvMappingConfigFactory.getInfractionConfig(arguments.getInPath(), arguments.getCity());
                    filename = arguments.getInPath() + "/infractions" + arguments.getCity() + ".csv";
                }
                default -> throw new IllegalArgumentException("Unsupported CSV file type");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load CSV mapping configuration for city: " + arguments.getCity(), e);
        }

        return new FilenameAndConfig(filename, config);
    }

    public static void readCsv(Arguments arguments, CsvFileType fileType, TriConsumer<String[], CsvMappingConfig, Integer> consumer) {
        FilenameAndConfig filenameAndConfig = getFilenameAndConfig(arguments, fileType);
        String filename = filenameAndConfig.filename();
        CsvMappingConfig config = filenameAndConfig.config();

        int id = 0;
        try (CsvFileIterator fileIterator = new CsvFileIterator(filename)) {
            while (fileIterator.hasNext()) {
                consumer.accept(fileIterator.next(), config, id++);
            }
        }
    }

    //TODO: Check if passing the consumer to threads improves something and is observable the improvement
    public static void readCsvParallel(Arguments arguments, CsvFileType fileType, TriConsumer<String[], CsvMappingConfig, Integer> consumer) {
        FilenameAndConfig filenameAndConfig = getFilenameAndConfig(arguments, fileType);
        String filename = filenameAndConfig.filename();
        CsvMappingConfig config = filenameAndConfig.config();

        BlockingQueue<String[]> queue = new LinkedBlockingQueue<>(100);
        int numProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numProcessors);

        // Start CSV processors
        for (int i = 0; i < numProcessors; i++) {
            executorService.submit(new CsvProcessor(queue, consumer, config));
        }

        try (CsvFileIterator fileIterator = new CsvFileIterator(filename)) {
            while (fileIterator.hasNext()) {
                try {
                    queue.put(fileIterator.next());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    executorService.shutdownNow();
                    throw new RuntimeException("Interrupted while reading CSV file", e);
                }
            }
        }

        // Indicate end of processing
        for (int i = 0; i < numProcessors; i++) {
            try {
                queue.put(new String[]{});
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                executorService.shutdownNow();
                throw new RuntimeException("Interrupted while signaling end of CSV processing", e);
            }
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                System.err.println("Executor did not terminate in the specified time.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while awaiting termination of executor service", e);
        }
    }

    private record FilenameAndConfig(String filename, CsvMappingConfig config) {}

    private record CsvProcessor(BlockingQueue<String[]> queue,
                                TriConsumer<String[], CsvMappingConfig, Integer> consumer,
                                CsvMappingConfig config) implements Runnable {
        private static final AtomicInteger idGenerator = new AtomicInteger(0);

        @Override
            public void run() {
                try {
                    while (true) {
                        String[] fields = queue.take();
                        if (fields.length == 0) { // End signal
                            break;
                        }
                        consumer.accept(fields, config, idGenerator.getAndIncrement());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
}