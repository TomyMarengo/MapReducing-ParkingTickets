package ar.edu.itba.pod.client.utils;

import java.text.SimpleDateFormat;

public class Constants {
    public static final String TIMESTAMP_LOGS_FILE_TEMPLATE = "/time%d.txt";
    public static final String QUERY_OUTPUT_FILE_TEMPLATE = "/query%d.csv";
    public static final String TIMESTAMP_LOGS_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss:SSS";
    public static final String INFRACTIONS_DATE_FORMAT = "yyyy-MM-dd";
    public static final SimpleDateFormat infractionsDateFormat = new SimpleDateFormat(Constants.INFRACTIONS_DATE_FORMAT);
    public static final String ARGUMENTS_DATE_FORMAT = "dd/MM/yyyy";
    public static final String INFRACTIONS_MAP = "infractionsMap";
    public static final String TICKETS_BY_INFRACTION_MAP = "ticketsByInfractionMap";
    public static final String QUERY_1_JOB_TRACKER_NAME = "totalTicketsByInfraction";
    public static final String MAP_REDUCE_START_MESSAGE = " - Inicio del trabajo map/reduce\n";
    public static final String MAP_REDUCE_END_MESSAGE = " - Fin del trabajo map/reduce\n";
    public static final String FILE_READ_START_MESSAGE = " - Inicio de la lectura del archivo\n";
    public static final String FILE_READ_END_MESSAGE = " - Fin de lectura del archivo\n";

}
