package fr.unice.smart_campus.middleware.data_api;


import junit.framework.TestCase;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by cyrilcecchinel on 30/06/2014.
 */

public class HelperTest extends TestCase {
    @Test
    public void testTimestampToDate(){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(2014, Calendar.JUNE, 30, 20, 45, 00);
        Date date = cal.getTime();
        assertEquals("Test timestamp to date conversion", Helper.getDateFromTimestamp(1404153900), date);
    }
}
