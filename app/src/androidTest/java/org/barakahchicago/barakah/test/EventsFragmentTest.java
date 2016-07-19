package org.barakahchicago.barakah.test;

import android.support.v4.app.FragmentManager;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ImageView;
import android.widget.TextView;

import static org.mockito.Mockito.*;

import org.barakahchicago.barakah.dao.EventDAO;
import org.barakahchicago.barakah.model.Event;
import org.barakahchicago.barakah.ui.EventsFragment;
import org.mockito.Mockito;

import java.util.ArrayList;

/**
 * Created by bevuk on 11/23/2015.
 */
public class EventsFragmentTest extends ActivityInstrumentationTestCase2<FragmentContainerActivity> {
    FragmentContainerActivity fragmentContainerActivity;
    EventsFragment eventsFragment;
    FragmentManager fragmentManager;
    Event testEvent, testEvent2;
    ArrayList<Event> testEvents;
    EventDAO eventDAO;

    TextView title;
    TextView date;
    TextView time;
    TextView location;
    ImageView eventImage;


    public EventsFragmentTest() {
        super(FragmentContainerActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // startActivity(new Intent(Intent.ACTION_MAIN), null, null);
        fragmentContainerActivity = getActivity();

        fragmentManager = fragmentContainerActivity.getSupportFragmentManager();


        eventsFragment = (EventsFragment) fragmentManager.findFragmentByTag(EventsFragment.TAG);

        testEvents = new ArrayList<Event>();


        testEvent = new Event();
        testEvent.setTitle("test1");
        testEvent.setId("1");
        testEvent.setLocation("location");
        testEvent.setDate_created("");
        testEvent.setStart_date("2015-10-30 00:00:00");
        testEvent.setEnd_date("2015-10-30 00:00:00");
        testEvent.setLast_updated("2015-10-30 00:00:00");
        testEvent.setDate_created("2015-10-30 00:00:00");
        testEvent.setImage("http://www.keenthemes.com/preview/metronic/theme/assets/global/plugins/jcrop/demos/demo_files/image1.jpg");

        testEvent2 = new Event();
        testEvent.setTitle("test2");
        testEvent.setId("2");
        testEvent.setLocation("location");
        testEvent.setDate_created("");
        testEvent.setStart_date("2015-10-30 00:00:00");
        testEvent.setEnd_date("2015-10-30 00:00:00");
        testEvent.setLast_updated("2015-10-30 00:00:00");
        testEvent.setDate_created("2015-10-30 00:00:00");
        testEvent.setImage("");

        testEvents.add(testEvent);
        testEvents.add(testEvent2);


    }


    public void testPrecondition() {
        assertNotNull("Activity is null: ", fragmentContainerActivity);
        // assertNotNull("event Fragment is null: ", eventsFragment);
        assertNotNull("fragmentManager is null: ", fragmentManager);
        assertEquals("testEvents items are not added", 2, testEvents.size());


    }

    public void testEventsAreLoaded() {

        assertNotNull("event data not initialized: ", eventsFragment.getEvents());


    }

    public void testAdapterIsNotNull() {

        assertNotNull(eventsFragment.getAdapter());
        assertEquals(eventsFragment.getEvents().size(), eventsFragment.getAdapter().getItemCount());

    }

    public void testRecyclerViewIsLoaded() {
        assertNotNull("Recycler view is not initialized: ", eventsFragment.getRecyclerView());
        assertNotNull("Adapter is not set for recycler view: ", eventsFragment.getRecyclerView().getAdapter());

    }


    public void testUpdateView() {

        //    eventsFragment.setEvents(testEvents);
        eventDAO = Mockito.mock(EventDAO.class);
        when(eventDAO.getUpcommingEvents()).thenReturn(testEvents);
        eventsFragment.setEventDAO(eventDAO);

        eventsFragment.updateView();

        verify(eventDAO, atLeast(1)).getUpcommingEvents();


        assertNotNull("eventcontroller is not set: ", eventsFragment.getEventDAO());

        //check values are updated
        assertEquals("Events are not updated", testEvents.size(), eventsFragment.getEvents().size());
        assertEquals("Adapter is not updated", testEvents.size(), eventsFragment.getAdapter().getItemCount());


    }


}
