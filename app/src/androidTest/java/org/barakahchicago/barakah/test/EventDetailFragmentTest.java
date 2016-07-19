package org.barakahchicago.barakah.test;

import android.test.ActivityInstrumentationTestCase2;

import org.barakahchicago.barakah.model.Event;
import org.barakahchicago.barakah.ui.EventDetailFragment;

/**
 * Created by bevuk on 12/1/2015.
 */
public class EventDetailFragmentTest extends ActivityInstrumentationTestCase2<FragmentContainerActivity> {
    FragmentContainerActivity activity;
    EventDetailFragment eventDetailFragment;
    Event event;

    public EventDetailFragmentTest() {
        super(FragmentContainerActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        activity = getActivity();
        eventDetailFragment = (EventDetailFragment) activity.getSupportFragmentManager().findFragmentByTag(EventDetailFragment.TAG);


    }

    public void testPreconditions() {
        assertNotNull(activity);
        assertNotNull(eventDetailFragment);

    }

    public void testFragmentArgumentIsPassed() {

        assertNotNull(eventDetailFragment.getArguments().getParcelable(EventDetailFragment.PARCELABLE_KEY));
        assertNotNull(eventDetailFragment.getEvent());
    }

    public void testDateIsSetToFragmentViews() {
        assertEquals(eventDetailFragment.getEvent().getTitle(), eventDetailFragment.getTitle().getText());
        assertEquals(eventDetailFragment.getEvent().getLocation(), eventDetailFragment.getLocation().getText());

    }

}
