package org.barakahchicago.barakah.test;

import org.barakahchicago.barakah.util.DateUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by bevuk on 11/20/2015.
 */
public class DateUtilTest {
    String dateString;
    String invalidDateString;
    String emptyString;

    @Before
    public void setUp() {

         dateString = "2015-09-02 00:00:00";
         invalidDateString = "2015-50-02 00:00:00";
        emptyString="";
    }

    @Test
    public void testGetLocalDateTime_ShouldReturnLocalDateTimeObject(){
        assertNotNull("Local DateTime object not created: ",DateUtil.getLocalDateTime(dateString));
    }
    @Test
    public void testGetLocalDateTime_ShouldReturnNullOnInvalidString(){
        assertNull("Method didn't return null:", DateUtil.getLocalDateTime(invalidDateString));
    }
    @Test
    public void testGetLocalDateTime_ShouldRetunrnNullOnNullDateString(){
        assertNull("Method didn't return null:", DateUtil.getLocalDateTime(""));
        assertNull("Method didn't return null:", DateUtil.getLocalDateTime(null));
    }

    @Test
    public void testGetFormattedDateTime_ShouldReturnFormattedDateTime(){

        String actual = DateUtil.getFormattedDateTime(dateString);

        String expected = "Wed, 2 Sep 12:00 AM";

        assertEquals("DateTime conversion test failed: ", expected, actual);

    }
    @Test
    public void testGetFormattedDateTime_InvalidDateString_ShouldReturnAnEmptyString(){
        //invalid Date

        String actual = DateUtil.getFormattedDateTime(invalidDateString);

        assertEquals("DateTime convertion test Failed: ", emptyString, actual);
    }
    @Test
    public void testGetFormattedDate_ShouldReturnFormattedDate(){
        String expected = "Wed, Sep 2";
        String actual = DateUtil.getFormattedDate(dateString);
        assertEquals("Date Conversion failed: " ,expected,actual);
    }
    @Test
    public void testGetFormattedDate_InvalidDateTimeString_ShouldReturnAnEmptyString(){
        assertEquals("Date convertion test Failed: ",emptyString, DateUtil.getFormattedDate(invalidDateString));
    }
    @Test
    public void testGetFormattedTime_ShouldReturnFormattedTime(){
        String expected = "12:00 AM";
        String actual = DateUtil.getFormattedTime(dateString);
        assertEquals("Date Conversion failed: ", expected,actual);
    }
    @Test
    public void testGetFormattedTime_InvalidDateTimeString_ShouldReturnAnEmptyString(){
        String actual = DateUtil.getFormattedTime(invalidDateString);
        assertEquals("Date convertion test Failed: ", emptyString, actual);
    }

    @Test
    public void testGetTimeInMillies_ShouldTimeInMillies(){
        long expected = 1441170000000L;
        long actual = DateUtil.getTimeInMillis(dateString);
        assertEquals("Date Conversion failed: ", expected,actual);
    }
    @Test
    public void testGetTimeInMillies_InvalidDateTimeString_ShouldReturnAnEmptyString(){
        long actual = DateUtil.getTimeInMillis(invalidDateString);
        assertEquals("Date convertion test Failed: ", 0, actual);
    }
}
