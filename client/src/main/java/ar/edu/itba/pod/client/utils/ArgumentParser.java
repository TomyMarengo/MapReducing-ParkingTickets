package ar.edu.itba.pod.client.utils;

import ar.edu.itba.pod.client.exceptions.ClientIllegalArgumentException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.function.BiConsumer;
public class ArgumentParser {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.ARGUMENTS_DATE_FORMAT);

    private static final Map<String, BiConsumer<String, Arguments.Builder>> OPTIONS = Map.ofEntries(
            Map.entry("-Dquery", (argValue, argBuilder) -> argBuilder.query(Integer.parseInt(argValue))),
            Map.entry("-Daddresses", (argValue, argBuilder) ->  argBuilder.addresses(argValue.split(";"))),
            Map.entry("-DinPath", (argValue, argBuilder) -> argBuilder.inPath(argValue)),
            Map.entry("-DoutPath", (argValue, argBuilder) -> argBuilder.outPath(argValue)),
            Map.entry("-Dcity", (argValue, argBuilder) -> argBuilder.city(argValue)),
            Map.entry("-Dn", (argValue, argBuilder) -> argBuilder.n(Integer.parseInt(argValue))),
            Map.entry("-Dfrom", (argValue, argBuilder) -> {
                try {
                    Date fromDate = dateFormat.parse(argValue);
                    argBuilder.from(fromDate);
                } catch (ParseException e) {
                    System.out.println("Error parsing date for -Dfrom argument: " + e.getMessage());
                }
            }),
            Map.entry("-Dto", (argValue, argBuilder) -> {
                try {
                    Date toDate = dateFormat.parse(argValue);
                    argBuilder.to(toDate);
                } catch (ParseException e) {
                    System.out.println("Error parsing date for -Dto argument: " + e.getMessage());
                }
            }),
            Map.entry("-Dseparator", (argValue, argBuilder) -> argBuilder.separator(argValue))
    );

    private static void invalidArgument(String arg, Arguments.Builder argBuilder) {
        throw new IllegalArgumentException("The argument " + arg + " is not valid");
    }

    public static Arguments parse(String[] args) {
        Arguments.Builder arguments = new Arguments.Builder();
        for (String arg : args) {
            String[] parts = arg.split("=");
            if (parts.length != 2 || !parts[0].startsWith("-D")) {
                throw new ClientIllegalArgumentException("Arguments must have the format -Dargument=value");
            }
            try {
                OPTIONS.getOrDefault(parts[0], ArgumentParser::invalidArgument).accept(parts[1], arguments);
            } catch (Exception e) {
                throw new ClientIllegalArgumentException(e.getMessage());
            }
        }
        return arguments.build();
    }
}