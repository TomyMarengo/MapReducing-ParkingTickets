package ar.edu.itba.pod.client.utils;

import java.io.*;


// This class is used to copy a specified number of lines from a CSV file to another CSV file.
// Just for testing purposes.
public class CsvFileCopier {
    public static void main(String[] args) {
        String sourceFilePath = "./ticketsCHI.csv";
        String destFilePath = "./ticketsCHI2.csv";
        int linesToCopy = 10000;

        try (
                BufferedReader reader = new BufferedReader(new FileReader(sourceFilePath));
                BufferedWriter writer = new BufferedWriter(new FileWriter(destFilePath, false))
        ) {
            String line = reader.readLine();
            if (line != null) {
                writer.write(line);
                writer.newLine();
            }

            int lineCount = 0;
            while ((line = reader.readLine()) != null && lineCount < linesToCopy) {
                writer.write(line);
                writer.newLine();
                lineCount++;
            }

            System.out.println("Successfully copied " + (lineCount + 1) + " lines (including header).");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}