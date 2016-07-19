package org.barakahchicago.barakah.test;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import org.barakahchicago.barakah.dao.BarakahDbHelper;
import org.barakahchicago.barakah.model.Message;
import org.barakahchicago.barakah.dao.MessageDAO;

import java.util.ArrayList;

/**
 * Created by bevuk on 11/29/2015.
 */
public class MessageDAOTest extends AndroidTestCase {


    MessageDAO messageDAO;

    BarakahDbHelper dbHelper;
    SQLiteDatabase db;
    private Message testMessage, testMessage2, testMessage3;
    private ArrayList<Message> testMessages;


    public MessageDAOTest() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        testMessage = Message.getTestInstance();

        testMessage2 = Message.getTestInstance();
        testMessage2.setTitle("test2");
        testMessage2.setId("2");
        testMessage2.setDate_created("2017-01-28 00:00:00");
        testMessage2.setEnd_publish("2016-01-29 00:00:00");

        testMessage3 = Message.getTestInstance();
        testMessage3.setTitle("test3");
        testMessage3.setId("3");
        testMessage3.setEnd_publish("2018-02-28 00:00:00");
        testMessage3.setDate_created("2016-02-29 00:00:00");

        testMessages = new ArrayList<Message>();

        testMessages.add(testMessage);
        testMessages.add(testMessage2);
        testMessages.add(testMessage3);

//passig null to database name. to use memory database
        dbHelper = BarakahDbHelper.newInstanceForTest(getContext(), null);
        db = dbHelper.getWritableDatabase();
        messageDAO = new MessageDAO(getContext(), dbHelper, db);

    }

    public void testPreconditions() {
        assertNotNull(messageDAO);
        assertEquals(3, testMessages.size());
    }

    public void testAdd() {

        assertEquals(testMessages.size(), messageDAO.add(testMessages));


    }

    public void testGetAll() {
        assertEquals(testMessages.size(), messageDAO.add(testMessages));
        assertEquals(testMessages.size(), messageDAO.getAll().size());
    }

    public void testCallingGetAllOnAnEmptyDb_ShouldReturn() {
        assertEquals(0, messageDAO.getAll().size());
    }

    public void testWhenAnEmptyListIsAdded_ShouldReturnZero() {
        assertEquals(0, messageDAO.add(new ArrayList<Message>()));
    }

    public void testUpdate() {
        messageDAO.add(testMessages);
        String updatedTitle = "title 1 updated";
        testMessages.get(0).setTitle(updatedTitle);

        assertEquals(testMessages.size(), messageDAO.update(testMessages));

        //we already know the first article is the earliest.
        assertEquals(updatedTitle, messageDAO.getPublishedMessages().get(0).getTitle());

    }

    public void testWhenUpdateWithEmptyList_ShouldReturnZero() {
        assertEquals(0, messageDAO.update(new ArrayList<Message>()));
    }

    public void testGetPublishedArticles() {
        //changing start date of one of the test articles to earlier date than now.
        testMessages.get(2).setEnd_publish("2010-02-28 00:00:00");
        messageDAO.add(testMessages);

        assertEquals(testMessages.size() - 1, messageDAO.getPublishedMessages().size());

    }

    public void testWhenThereAreNoPublishedArticles_ShouldReturnAnEmptyList() {
        //setting start Date of all events to an old date
        testMessages.get(0).setEnd_publish("2010-02-28 00:00:00");
        testMessages.get(1).setEnd_publish("2010-02-28 00:00:00");
        testMessages.get(2).setEnd_publish("2010-02-28 00:00:00");
        messageDAO.add(testMessages);

        assertEquals(0, messageDAO.getPublishedMessages().size());
    }

    public void testAddOrUpdate() {
        messageDAO.add(testMessages);

        Message oldMessage = Message.getTestInstance();

        Message newMessage = Message.getTestInstance();
        newMessage.setId("4");

        Message oldButUpdatedMessage = Message.getTestInstance();

        // Changing from 2015-12-30 00:00:00 to "2016-01-01 00:00:00"

        String updatedDate = "2016-01-01 00:00:00";
        String updatedTitle = "update title";
        oldButUpdatedMessage.setLast_updated(updatedDate);
        oldButUpdatedMessage.setTitle(updatedTitle);

        ArrayList<Message> newList = new ArrayList<>();
        newList.add(newMessage);
        newList.add(oldMessage);
        newList.add(oldButUpdatedMessage);

        //only two rows sholuld be affected one is adde another is updated
        assertEquals(2, messageDAO.addOrUpdate(newList));
        assertEquals(testMessages.size() + 1, messageDAO.getPublishedMessages().size());
        assertEquals(updatedTitle, messageDAO.getPublishedMessages().get(0).getTitle());

    }

    public void testWhenAddOrUpdateIsCalledWithAnEmptyList_ShouldReturnZero() {
        assertEquals(0, messageDAO.addOrUpdate(new ArrayList<Message>()));
    }

    public void testWhenAddOrUpdateIsCalledOnAnEmptyDB_ShouldAddAll() {
        assertEquals(testMessages.size(), messageDAO.addOrUpdate(testMessages));
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        dbHelper.close();
        db.close();
    }
}
