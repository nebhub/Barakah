package org.barakahchicago.barakah.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import org.barakahchicago.barakah.dao.BarakahDbHelper;
import org.barakahchicago.barakah.model.Event;
import org.barakahchicago.barakah.dao.EventDAO;

import java.util.ArrayList;

/**
 * Created by bevuk on 11/24/2015.
 */
public class EventDAOTest extends AndroidTestCase {

    EventDAO eventDAO;
    Context context;
    BarakahDbHelper dbHelper;
    SQLiteDatabase db;
    private Event testEvent, testEvent2;
    private ArrayList<Event> testEvents;
    private Event testEvent3;


    public EventDAOTest() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        testEvent = Event.getTestInstance();

        testEvent2 = Event.getTestInstance();
        testEvent2.setTitle("test2");
        testEvent2.setId("2");
        testEvent2.setStart_date("2017-01-28 00:00:00");
        testEvent2.setEnd_date("2016-01-29 00:00:00");

        testEvent3 = Event.getTestInstance();
        testEvent3.setTitle("test3");
        testEvent3.setId("3");
        testEvent3.setStart_date("2018-02-28 00:00:00");
        testEvent3.setEnd_date("2016-02-29 00:00:00");

        testEvents = new ArrayList<Event>();

        testEvents.add(testEvent);
        testEvents.add(testEvent2);
        testEvents.add(testEvent3);

//passig null to database name. to use memory database
        dbHelper = BarakahDbHelper.newInstanceForTest(getContext(), null);
        db = dbHelper.getWritableDatabase();
        eventDAO = new EventDAO(getContext(), dbHelper, db);

    }

    public void testPreconditions() {
        assertNotNull(eventDAO);
        assertEquals(3, testEvents.size());
    }

    public void testAdd() {

        assertEquals(testEvents.size(), eventDAO.add(testEvents));


    }

    public void testGetAll() {
        assertEquals(testEvents.size(), eventDAO.add(testEvents));
        assertEquals(testEvents.size(), eventDAO.getAll().size());
    }

    public void testCallingGetAllOnAnEmptyDb_ShouldReturn() {
        assertEquals(0, eventDAO.getAll().size());
    }

    public void testWhenAnEmptyListIsAdded_ShouldReturnZero() {
        assertEquals(0, eventDAO.add(new ArrayList<Event>()));
    }

    public void testUpdate() {
        eventDAO.add(testEvents);
        String updatedTitle = "title 1 updated";
        testEvents.get(0).setTitle(updatedTitle);

        assertEquals(testEvents.size(), eventDAO.update(testEvents));

        //we already know the first event is the earliest.
        assertEquals(updatedTitle, eventDAO.getUpcommingEvents().get(0).getTitle());

    }

    public void testWhenUpdateWithEmptyList_ShouldReturnZero() {
        assertEquals(0, eventDAO.update(new ArrayList<Event>()));
    }

    public void testGetUpCommingEvents() {
        //changing start date of one of the test events to earlier date than now.
        testEvents.get(2).setStart_date("2010-02-28 00:00:00");
        eventDAO.add(testEvents);

        assertEquals(testEvents.size() - 1, eventDAO.getUpcommingEvents().size());

    }

    public void testWhenThereAreNoUpcommmingEvents_ShouldReturnAnEmptyList() {
        //setting start Date of all events to an old date
        testEvents.get(0).setStart_date("2010-02-28 00:00:00");
        testEvents.get(1).setStart_date("2010-02-28 00:00:00");
        testEvents.get(2).setStart_date("2010-02-28 00:00:00");
        eventDAO.add(testEvents);

        assertEquals(0, eventDAO.getUpcommingEvents().size());
    }

    public void testAddOrUpdate() {
        eventDAO.add(testEvents);

        Event oldEvent = Event.getTestInstance();

        Event newEvent = Event.getTestInstance();
        newEvent.setId("4");

        Event oldButUpdatedEvent = Event.getTestInstance();
        // Changing from 2015-12-30 00:00:00 to "2016-01-01 00:00:00"
        String updatedDate = "2016-01-01 00:00:00";
        String updatedTitle = "update title";
        oldButUpdatedEvent.setLast_updated(updatedDate);
        oldButUpdatedEvent.setTitle(updatedTitle);

        ArrayList<Event> newList = new ArrayList<>();
        newList.add(newEvent);
        newList.add(oldEvent);
        newList.add(oldButUpdatedEvent);

        //only two rows sholuld be affected one is adde another is updated
        assertEquals(2, eventDAO.addOrUpdate(newList));
        assertEquals(testEvents.size() + 1, eventDAO.getUpcommingEvents().size());
        assertEquals(updatedTitle, eventDAO.getUpcommingEvents().get(0).getTitle());

    }

    public void testWhenAddOrUpdateIsCalledWithAnEmptyList_ShouldReturnZero() {
        assertEquals(0, eventDAO.addOrUpdate(new ArrayList<Event>()));
    }

    public void testWhenAddOrUpdateIsCalledOnAnEmptyDB_ShouldAddAll() {
        assertEquals(testEvents.size(), eventDAO.addOrUpdate(testEvents));
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        dbHelper.close();
        db.close();
    }
}
