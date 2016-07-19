package org.barakahchicago.barakah.test;

import android.test.AndroidTestCase;

import org.barakahchicago.barakah.model.Message;
import org.barakahchicago.barakah.adapter.MessagesAdapter;
import org.barakahchicago.barakah.ui.MessagesFragment;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

/**
 * Created by bevuk on 11/30/2015.
 */
public class MessagesAdapterTest extends AndroidTestCase {
    MessagesAdapter messagesAdapter;
    Message testMessage, testMessage2;
    ArrayList<Message> testMessages;
    MessagesFragment.OnMessageSelectedListener listener;

    public MessagesAdapterTest() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        listener = Mockito.mock(MessagesFragment.OnMessageSelectedListener.class);

        testMessage = Message.getTestInstance();

        testMessage2 = Message.getTestInstance();
        testMessage.setTitle("test2");
        testMessage.setId("2");
        testMessage.setDate_created("2016-10-30 00:00:00");
        testMessage.setEnd_publish("2016-10-30 00:00:00");


        testMessages = new ArrayList<Message>();
        testMessages.add(testMessage);
        testMessages.add(testMessage2);
        messagesAdapter = new MessagesAdapter(testMessages, listener);
    }


    public void testPreconditions() {
        assertNotNull(testMessages);
        assertNotNull(listener);
        assertNotNull(messagesAdapter);
    }

    public void testGetItemCount() {
        assertEquals(2, messagesAdapter.getItemCount());
    }


    public void testListenerIsSet() {
        assertNotNull(messagesAdapter.getCallback());
        listener.onClick(testMessage);
        verify(listener, atLeastOnce()).onClick(testMessage);

    }
}
