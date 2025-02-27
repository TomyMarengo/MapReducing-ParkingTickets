package ar.edu.itba.pod.client.utils;

public class Constants {
    public static final String TIMESTAMP_LOGS_FILE_TEMPLATE = "/time%d.txt";
    public static final String QUERY_OUTPUT_FILE_TEMPLATE = "/query%d.csv";
    public static final String TIMESTAMP_LOGS_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss:SSS";
    public static final String ARGUMENTS_DATE_FORMAT = "dd/MM/yyyy";
    public static final String QUERY_1_JOB_TRACKER_NAME = "totalTicketsByInfraction";
    public static final String QUERY_2_JOB_TRACKER_NAME = "topNInfractionsByCounty";
    public static final String QUERY_3_JOB_TRACKER_NAME = "topNCollectorAgencies";
    public static final String QUERY_4_JOB_TRACKER_NAME = "worstCountyPlates";
    public static final String QUERY_4_JOB_TRACKER_NAME_2 = "worstCountyPlates2";
    public static final String QUERY_5_JOB_TRACKER_NAME = "infractionPairs";
    public static final String MAP_REDUCE_START_MESSAGE = " - Inicio del trabajo map/reduce\n";
    public static final String MAP_REDUCE_END_MESSAGE = " - Fin del trabajo map/reduce\n";
    public static final String FILE_READ_START_MESSAGE = " - Inicio de la lectura del archivo\n";
    public static final String FILE_READ_END_MESSAGE = " - Fin de lectura del archivo\n";
    public static final Integer FIELD_COUNT = 6;
}
