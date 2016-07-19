package org.barakahchicago.barakah.test;

import org.barakahchicago.barakah.model.Message;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by bevuk on 11/30/2015.
 */
public class MessageTest {
    private Message message1;
    private Message message2;

    @Before
    public  void setUp(){
        message1 = Message.getTestInstance();
        message2 = Message.getTestInstance();

    }

    @Test
    public void testEquals_ShouleReturnFalse_WhenMessagesHaveTheSameIds(){
        assertTrue(message1.isEqualTo(message2));
    }
    @Test
    public void testEquals_ShouldRetunrFalse_WhenMessagesHaveDifferentIds(){
        message2.setId("10");
        assertFalse(message1.isEqualTo(message2));
    }
    @Test
    public void testEquals_ShouldRetunrFalse_WhenEitherOneOfTheTwoMessagesHaveInvalidId(){
        message2.setId(null);
        assertFalse(message1.isEqualTo(message2));

        message2.setId("");
        assertFalse(message1.isEqualTo(message2));
    }
}
