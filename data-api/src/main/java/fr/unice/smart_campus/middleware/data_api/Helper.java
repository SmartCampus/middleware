package fr.unice.smart_campus.middleware.data_api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {

	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static char charSeparatorDates = '/';

	/**
	 * Parse the dates string and get timestamp of each date
	 *
	 * @param date Date string (may contains two dates)
	 * @return TimeRange object which contains one or two dates
	 * @throws ParseException if the parsing is not working
	 */
	public static TimeRange getTimestamps (String date) throws ParseException {

		long first;
		long second = 0L;

		int indexSeparator = date.indexOf(charSeparatorDates);
		if (indexSeparator == -1) {
			// If there is only one date in the string parameter
			first = getTimestamp(date);
		} else {
			// If there is two dates in the string parameter
			first = getTimestamp(date.substring(0, indexSeparator));
			second = getTimestamp(date.substring(indexSeparator + 1));
		}

		return new TimeRange(first, second);
	}

	/**
	 * Parse the date string and get the timestamp
	 *
	 * @param date Date string
	 * @return the timestamp of the date string parameter
	 * @throws ParseException if the parsing is not working
	 */
	private static long getTimestamp (String date) throws ParseException {
		Date d = sdf.parse(date);
		return d.getTime() / 1000;
	}

    /**
     * Convert the timestamp value to a date object
     * @param timestamp Timestamp
     * @return the date corresponding to the timestamp parameter
     */
    public static Date getDateFromTimestamp (long timestamp){
        return new Date(timestamp * 1000);
    }
}
