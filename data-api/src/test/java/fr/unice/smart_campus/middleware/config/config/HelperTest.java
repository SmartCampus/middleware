package fr.unice.smart_campus.middleware.config.config;


import fr.unice.smart_campus.middleware.Helper;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by cyrilcecchinel on 30/06/2014.
 */

public class HelperTest extends TestCase {
    @Test
    public void testTimestampToDate(){
        Calendar cal = new GregorianCalendar();
        cal.set(2014, Calendar.JUNE, 30, 20, 45, 00);
        Date date = cal.getTime();
        assertEquals("Test timestamp to date conversion", Helper.getDateFromTimestamp(cal.getTimeInMillis() / 1000).getTime(), (date.getTime()/1000)*1000);
    }
}
