package de.iubh.fernstudium.iwmb.iubhtodoapp;

import org.joda.time.DateTime;
import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    @Test
    public void addition_isCorrect() throws Exception {
        DateTime jodaDate = DateTime.now().withTimeAtStartOfDay();
        Timestamp from = new Timestamp(jodaDate.toDate().getTime());
        Timestamp to = new Timestamp(jodaDate.plusDays(1).minusSeconds(1).toDate().getTime());
        System.out.println(from);
        System.out.println(to);
    }
}