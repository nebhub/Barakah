package org.barakahchicago.barakah.ui;

import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.barakahchicago.barakah.R;

public class MainPagerFragment extends Fragment {


    /*
        Key to access tab index from argument
     */
    public static final String TAB_INDEX = "TAB_INDEX";
    /*
    Tag used got fragment transaction
     */
    public static final String TAG = "main_pager";
    /*
        Tag used for logging
    */
    private static final String LOG_TAG = "MAIN PAGER FRAGMENT";
    /*
        ViewPager used to page fragmenrs
     */
    private ViewPager viewpager;
    /*
        An Adapter fot the ViewPger
     */
    private MainPagerAdapter adapter;
    /*
       User to save index of current tab
     */
    private int current_tab = 0;
    private TabLayout tabLayout;

    public MainPagerFragment() {
        Log.i(LOG_TAG, "Fragment created");
    }

    public ViewPager getViewpager() {
        return viewpager;
    }

    public void setViewpager(ViewPager viewpager) {
        this.viewpager = viewpager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(LOG_TAG, "onCreateView");
        View view = inflater.inflate(R.layout.main_pager_fragment, container,
                false);

        Log.i(LOG_TAG, "Initialization for first time in onCreate");
        viewpager = (ViewPager) view.findViewById(R.id.main_pager);
        adapter = new MainPagerAdapter(getChildFragmentManager());
        viewpager.setAdapter(adapter);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewpager);

        Bundle b = getArguments();
        if (b != null) {
            Log.i(LOG_TAG, "Restoring Fragment State From Bundle");
            current_tab = b.getInt(TAB_INDEX);
            Log.i(LOG_TAG, " tab index: " + current_tab);
            viewpager.setCurrentItem(current_tab);
        }


        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (viewpager != null) {
            outState.putInt(TAB_INDEX, viewpager.getCurrentItem());
        }
    }

    public void setTab(int tab) {
        viewpager.setCurrentItem(tab);
    }

    class MainPagerAdapter extends FragmentPagerAdapter {

        Fragment eventFragment;
        Fragment articleFragment;
        Fragment messagesFragment;
        FragmentManager fm;

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
            Log.i(LOG_TAG, "adapter constrructur");
        }

        @Override
        public Fragment getItem(int postion) {

            switch (postion) {
                case 0:

                    eventFragment = new EventsFragment();

                    return eventFragment;
                case 1:

                    articleFragment = new ArticleFragment();

                    return articleFragment;

                case 2:
                    messagesFragment = new MessagesFragment();
                    return messagesFragment;
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {

            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Event";

                case 1:
                    return "Article";
                case 2:
                    return "Messages";

                default:
                    break;
            }

            return "tab";
        }

    }

}
