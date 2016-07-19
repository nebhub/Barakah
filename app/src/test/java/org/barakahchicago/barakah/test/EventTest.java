package org.barakahchicago.barakah.test;

import org.barakahchicago.barakah.model.Event;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by bevuk on 11/30/2015.
 */
public class EventTest {

    private Event event1;
    private Event event2;

    @Before
    public  void setUp(){
        event1 = Event.getTestInstance();
        event2 = Event.getTestInstance();

    }

    @Test
    public void testEquals_ShouleReturnFalse_WhenEventsHaveTheSameIds(){
        assertTrue(event1.isEqualTo(event2));
    }
    @Test
    public void testEquals_ShouldRetunrFalse_WhenEventsHaveDifferentIds(){
        event2.setId("10");
        assertFalse(event1.isEqualTo(event2));
    }
    @Test
    public void testEquals_ShouldRetunrFalse_WhenEitherOneOfTheTwoEventsHaveInvalidId(){
        event2.setId(null);
        assertFalse(event1.isEqualTo(event2));

        event2.setId("");
        assertFalse(event1.isEqualTo(event2));
    }

}
