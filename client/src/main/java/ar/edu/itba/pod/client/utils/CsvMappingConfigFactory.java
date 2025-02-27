package ar.edu.itba.pod.client.utils;

import java.io.IOException;

public class CsvMappingConfigFactory {

    public static CsvMappingConfig getTicketConfig(String inPath, String city) throws IOException {
        String filePath = inPath + "/tickets" + city.toUpperCase() + ".json";
        return CsvMappingConfig.fromJson(filePath);
    }

    public static CsvMappingConfig getInfractionConfig(String inPath, String city) throws IOException {
        String filePath = inPath + "/infractions" + city.toUpperCase() + ".json";
        return CsvMappingConfig.fromJson(filePath);
    }
}