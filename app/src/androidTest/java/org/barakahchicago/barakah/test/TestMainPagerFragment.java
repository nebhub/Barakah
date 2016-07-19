package org.barakahchicago.barakah.test;

import android.test.ActivityInstrumentationTestCase2;

import org.barakahchicago.barakah.ui.MainActivity;
import org.barakahchicago.barakah.ui.MainPagerFragment;

/**
 * Created by bevuk on 11/21/2015.
 */
public class TestMainPagerFragment extends ActivityInstrumentationTestCase2<MainActivity> {
    MainActivity mainActivity;
    MainPagerFragment mainPagerFragment;

    public TestMainPagerFragment() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mainPagerFragment = (MainPagerFragment) mainActivity.getSupportFragmentManager().findFragmentByTag(MainPagerFragment.TAG);
        mainActivity = getActivity();


    }

    public void testPreconditions() {
        assertNotNull(mainActivity);
        assertNotNull(mainPagerFragment);
    }

    public void testMainPagerFragmentIsLoaded() {
        // Fragment fragment = mainActivity.getSupportFragmentManager().findFragmentByTag(MainPagerFragment.TAG);
        //   assertNotNull("Fragment is null: ", fragment);
        //   assertEquals(fragment.getClass().getSimpleName(), "MainPagerFragment");
    }

}
