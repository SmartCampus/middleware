package fr.unice.smart_campus.middleware;

/*
 * #%L
 * data-api
 * %%
 * Copyright (C) 2015 - 2016 Université de Nice Sophia-Antipolis (UNS) - Centre National de la Recherche Scientifique (CNRS)
 * %%
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * Université de Nice Sophia-Antipolis (UNS) -
 * Centre National de la Recherche Scientifique (CNRS)
 * Copyright © 2015 UNS, CNRS
 * 
 * 
 *   Authors: SmartCampus Team - http://smartcampus.github.io/sc-contacts/
 * 
 *   Architects: Sébastien Mosser – Laboratoire I3S – mosser@i3s.unice.fr
 *               Michel Riveill - Laboratoire I3S - riveill@i3s.unice.fr
 * #L%
 */

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
