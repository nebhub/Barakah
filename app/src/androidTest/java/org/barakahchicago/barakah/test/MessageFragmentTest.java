package org.barakahchicago.barakah.test;

import android.support.v4.app.FragmentManager;
import android.test.ActivityInstrumentationTestCase2;

import org.barakahchicago.barakah.model.Message;
import org.barakahchicago.barakah.dao.MessageDAO;
import org.barakahchicago.barakah.ui.MessagesFragment;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by bevuk on 12/1/2015.
 */
public class MessageFragmentTest extends ActivityInstrumentationTestCase2<FragmentContainerActivity> {

    private FragmentContainerActivity fragmentContainerActivity;
    private FragmentManager fragmentManager;
    private ArrayList<Message> testArticles;
    private Message testMessage, testMessage2;
    private MessagesFragment messagesFragment;
    private MessageDAO messageDAO;

    public MessageFragmentTest() {
        super(FragmentContainerActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // startActivity(new Intent(Intent.ACTION_MAIN), null, null);
        fragmentContainerActivity = getActivity();

        fragmentManager = fragmentContainerActivity.getSupportFragmentManager();

        messagesFragment = (MessagesFragment) fragmentManager.findFragmentByTag(MessagesFragment.TAG);


        testArticles = new ArrayList<Message>();


        testMessage = Message.getTestInstance();
        testMessage.setTitle("test1");
        testMessage.setId("1");
        testMessage.setDate_created("2015-10-30 00:00:00");
        testMessage.setEnd_publish("2017-13-30 00:00:00");

        testMessage2 = Message.getTestInstance();
        testMessage2.setTitle("test2");
        testMessage2.setId("2");
        testMessage2.setDate_created("2015-10-30 00:00:00");
        testMessage2.setEnd_publish("2015-12-30 00:00:00");


        testArticles.add(testMessage);
        testArticles.add(testMessage2);


    }


    public void testPrecondition() {
        assertNotNull("Activity is null: ", fragmentContainerActivity);
        assertNotNull("fragmentManager is null: ", fragmentManager);
        assertEquals("testMessages items are not added", 2, testArticles.size());


    }

    public void testArticlesAreLoaded() {


        assertNotNull("message data not initialized: ", messagesFragment.getMessages());


    }

    public void testAdapterIsNotNull() {

        assertNotNull(messagesFragment.getAdapter());
        assertEquals(messagesFragment.getMessages().size(), messagesFragment.getAdapter().getItemCount());

    }

    public void testRecyclerViewIsLoaded() {
        assertNotNull("Recycler view is not initialized: ", messagesFragment.getRecyclerView());
        assertNotNull("Adapter is not set for recycler view: ", messagesFragment.getRecyclerView().getAdapter());

    }


    public void testUpdateView() {

        //    eventsFragment.setEvents(testEvents);
        messageDAO = Mockito.mock(MessageDAO.class);
        when(messageDAO.getPublishedMessages()).thenReturn(testArticles);
        messagesFragment.setMessageDAO(messageDAO);

        messagesFragment.updateView();

        verify(messageDAO, atLeast(1)).getPublishedMessages();


        assertNotNull("ArticleDAO is not set: ", messagesFragment.getMessageDAO());

        //check values are updated
        assertEquals("Articles are not updated", testArticles.size(), messagesFragment.getMessages().size());
        assertEquals("Adapter is not updated", testArticles.size(), messagesFragment.getAdapter().getItemCount());


    }
}
