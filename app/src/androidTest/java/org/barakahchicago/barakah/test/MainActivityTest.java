package org.barakahchicago.barakah.test;

import android.app.Instrumentation;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.test.ActivityInstrumentationTestCase2;

import org.barakahchicago.barakah.ui.EventsFragment;
import org.barakahchicago.barakah.ui.MainActivity;
import org.barakahchicago.barakah.ui.MainPagerFragment;

/**
 * Created by bevuk on 11/20/2015.
 */

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mainActivity;
    private SharedPreferences sharedPreferences;
    private FragmentManager fragmentManager;
    private EventsFragment.OnEventSelectedListener onEventClickListener;
    private Instrumentation instrumentaion;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        instrumentaion = getInstrumentation();
        mainActivity = getActivity();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        fragmentManager = mainActivity.getSupportFragmentManager();


    }

    public void testPreconditions() {
        assertNotNull(mainActivity);
        assertNotNull(sharedPreferences);
        assertNotNull(fragmentManager);
    }


    public void testUserIsSignedIn() {
        assertTrue("User is not Signed In", sharedPreferences.getBoolean(mainActivity.signedIn, false));

    }

    public void test_whenSignedIn_ShowMainPagerFragment() {
        assertTrue("User is not Signed In", sharedPreferences.getBoolean("signed_in", false));
        Fragment fragment = fragmentManager.findFragmentByTag(MainPagerFragment.TAG);
        assertNotNull(fragment);
        assertEquals("Main Pager Fragment not loaded: ", MainPagerFragment.TAG, fragment.getTag());

    }

    public void testAlarmIsSet() {
        assertTrue("Alarm is not set", sharedPreferences.getBoolean("alarm_set", false));
        //assertFalse("Alarm is set", sharedPreferences.getBoolean("alarm_set",tru));
    }

   /* public void testOnClickEvents_LoadsEventDetailFragment(){


        Event e  = Event.getTestInstance();
        mainActivity.onClick(e);
        Fragment fragment = fragmentManager.findFragmentByTag(EventDetailFragment.TAG);
        instrumentaion.waitForIdleSync();
        assertNull(fragment);
       // assertEquals("Event Detail Fragment Not Loaded: ", "event_detail", EventDetailFragment.TAG);

    }
*/


}
