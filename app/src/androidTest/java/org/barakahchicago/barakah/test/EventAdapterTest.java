package org.barakahchicago.barakah.test;

import android.test.AndroidTestCase;

import org.barakahchicago.barakah.model.Event;
import org.barakahchicago.barakah.adapter.EventsAdapter;
import org.barakahchicago.barakah.ui.EventsFragment;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import java.util.ArrayList;

/**
 * Created by bevuk on 11/23/2015.
 */
public class EventAdapterTest extends AndroidTestCase {

    EventsAdapter eventsAdapter;
    Event testEvent, testEvent2;
    ArrayList<Event> testEvents;
    EventsFragment.OnEventSelectedListener listener;

    public EventAdapterTest() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        listener = Mockito.mock(EventsFragment.OnEventSelectedListener.class);

        testEvent = Event.getTestInstance();

        testEvent2 = Event.getTestInstance();
        testEvent.setTitle("test2");
        testEvent.setId("2");
        testEvent.setStart_date("2016-10-30 00:00:00");
        testEvent.setEnd_date("2016-10-30 00:00:00");


        testEvents = new ArrayList<Event>();
        testEvents.add(testEvent);
        testEvents.add(testEvent2);
        eventsAdapter = new EventsAdapter(testEvents, listener);
    }


    public void testPreconditions() {
        assertNotNull(testEvents);
        assertNotNull(listener);
        assertNotNull(eventsAdapter);
    }

    public void testGetItemCount() {
        assertEquals(2, eventsAdapter.getItemCount());
    }


    public void testListenerIsSet() {
        assertNotNull(eventsAdapter.getCallback());
        listener.onClick(testEvent);
        verify(listener, atLeastOnce()).onClick(testEvent);

    }

}
