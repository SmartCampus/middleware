package fr.unice.smart_campus.middleware.dataapi;


import java.text.ParseException;

public class Helper {


    public static TimeRange getTimestamps(String date) throws ParseException {

        long first = 0L;
        long second = 0L;

        return new TimeRange(first, second);
    }

    private static long getTimestamp(String date) {
        return 0L;
    }
}
