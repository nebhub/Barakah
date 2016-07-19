package org.barakahchicago.barakah.ui;

import org.barakahchicago.barakah.service.BarakahService;
import org.barakahchicago.barakah.R;
import org.barakahchicago.barakah.ui.ArticleFragment.OnArticleSelectedListener;
import org.barakahchicago.barakah.ui.EventsFragment.OnEventSelectedListener;
import org.barakahchicago.barakah.ui.LoginFragment.OnLoginListener;
import org.barakahchicago.barakah.ui.MessagesFragment.OnMessageSelectedListener;
import org.barakahchicago.barakah.model.Article;
import org.barakahchicago.barakah.model.Event;
import org.barakahchicago.barakah.model.Message;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity implements OnEventSelectedListener,
        OnArticleSelectedListener, OnMessageSelectedListener, OnSharedPreferenceChangeListener, OnLoginListener {
    /*
    Tag used for logging
     */
    private final String LOG_TAG = "MAIN ACTIVITY";

    /*
        Flag to hold is alarm is set
     */
    public String alarmSet = null;

    /*
        Flag is user signed in
     */
    public String signedIn = null;

    /*
      Username of signed in user
     */
    public String userName = null;

    /*
        Fragment manager user to add and remove fragmnets
     */
    private FragmentManager fragmentManager;

    /*
         A Fragment object
     */
    private Fragment fragment;
    /*
     Users shared preference
     */
    private SharedPreferences sharedPref;

    /*
    Alarm manager user to set and cancel alarms
     */
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        alarmSet = getString(R.string.pref_key_alarm_set);
        signedIn = getString(R.string.pref_key_signed_in);
        userName = getString(R.string.pref_key_username);

        getSupportActionBar().setElevation(0);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        fragmentManager = getSupportFragmentManager();

        if (sharedPref.getBoolean(signedIn, false)) {

            Log.i(LOG_TAG, "Is Alarm Set ? " + sharedPref.getBoolean(alarmSet, false));
            if (!sharedPref.getBoolean(alarmSet, false)) {
                setAlarm();
            }

            if (savedInstanceState == null) {
                download();
            }

            //	deleteDatabase("BarakahAppDb");

            //Load the main Fragment
            fragment = fragmentManager.findFragmentByTag(MainPagerFragment.TAG);
            if (fragment == null) {
                Log.i(LOG_TAG, "Fragment is null");
                fragment = new MainPagerFragment();

				/*
                pass any bundle value passed from notification or other fragments to MainPagerFragment
				 */
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    fragment.setArguments(bundle);
                }

                fragmentManager.beginTransaction().replace(R.id.main_container, fragment, MainPagerFragment.TAG).commit();
                Log.i(LOG_TAG, "Main Pager Fragmet Loaded" + fragment.getId());

            }

        }

        //if logged in is false, show login fragment
        else {
            fragment = fragmentManager.findFragmentByTag(LoginFragment.TAG);

            if (fragment == null) {
                fragment = new LoginFragment();
                fragmentManager.beginTransaction().replace(R.id.main_container, fragment, LoginFragment.TAG).commit();
                Log.i(LOG_TAG, "Login Fragment loaded ");
            }


        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {


            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_feedback) {
            Intent intent = new Intent(this, FeedbackActivity.class);
            startActivity(intent);
            Log.i(LOG_TAG, "Starting feedback activity");
        } else if (id == R.id.action_sign_out) {


            fragment = fragmentManager.findFragmentByTag(LoginFragment.TAG);

            if (fragment == null) {
                fragment = new LoginFragment();

                fragmentManager.beginTransaction().replace(R.id.main_container, fragment, LoginFragment.TAG).commit();
                Log.i(LOG_TAG, "loading SignIn fragment ");
            }
            sharedPref.edit().putBoolean(signedIn, false).putString(userName, "").commit();
            cancelAlarm();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (key.equals(getResources().getString(R.string.pref_key_auto_update))) {
            cancelAlarm();
            setAlarm();
        }

    }

    /*
     Starts a background service that downloads data.
     */
    public void download() {


        Intent intent = new Intent(this, BarakahService.class);
        startService(intent);
    }

    /*
     Handles click event from EventsFragment and opens a EventsDetailFragment
     for the supplied Event
     */
    @Override
    public void onClick(Event event) {
        Fragment fragment;

		/*
         * a new EventDetailFragment instance is created every time, because a
		 * new event object have to be passed every time. Otherwise its going to
		 * get stuck on event data.
		 */
        fragment = EventDetailFragment.newInstance(event);


        fragmentManager.beginTransaction()
                .replace(R.id.main_container, fragment, EventDetailFragment.TAG)
                .addToBackStack(EventDetailFragment.TAG).commit();

    }

    /*
    Handles click event from ArticlesFragment and opens a ArticlesDetailFragment
    for the suppled Article
    */
    @Override
    public void onClick(Article article) {
        Fragment fragment;

		/*
         * a new EventDetailFragment instance is created every time, because a
		 * new event object have to be passed every time. Otherwise its going to
		 * get stuck on article data.
		 */
        fragment = ArticleDetailFragment.newInstance(article);


        fragmentManager.beginTransaction()
                .replace(R.id.main_container, fragment, ArticleDetailFragment.TAG)
                .addToBackStack(ArticleDetailFragment.TAG).commit();

    }
    /*
     Handles click message from MessagesFragment and opens a MessagesDetailFragment
     for the suppled message
     */

    @Override
    public void onClick(Message message) {
        //no detail page

    }

    /*
        sets an alarm based on the alarm interval set on users preference
     */
    public boolean setAlarm() {
        Log.i(LOG_TAG, "Setting Alarm : ");

        String autoUpdate = getResources().getString(R.string.pref_key_auto_update);
        String autoUpdateDefault = getResources().getString(R.string.pref_auto_update_default);


        Intent intent = new Intent(this, BarakahService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, intent, 0);

        int interval = Integer.parseInt(sharedPref.getString(autoUpdate, autoUpdateDefault));

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                interval * 3600 * 1000, interval * 3600 * 1000, pi);


        Log.i(LOG_TAG, "Alarm set for every" + interval
                + "hours");

        sharedPref.edit().putBoolean(alarmSet, true).commit();

        Log.i(LOG_TAG, "ALARM SET");
        return true;
    }

    /*
        cancels alarm if its already set.
     */
    public boolean cancelAlarm() {
        Intent alarm = new Intent(this, BarakahService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, alarm, 0);
        alarmManager = (AlarmManager) this.getSystemService(
                Context.ALARM_SERVICE);
        alarmManager.cancel(pi);

        sharedPref.edit().putBoolean(alarmSet, false).commit();
        Log.i(LOG_TAG, "ALARM canceled");
        return true;
    }

    /*
        calls the download method, which starts the background service
     */
    @Override
    public void onUpdate() {

        download();

    }

/*
    Handles sign up event from the login screen and loads the SignupFragment to the screen
 */

    @Override
    public void onSignUp() {
        fragment = fragmentManager.findFragmentByTag(SignupFragment.TAG);
        if (fragment == null) {
            fragment = new SignupFragment();
        }

        fragmentManager.beginTransaction().replace(R.id.main_container, fragment, SignupFragment.TAG).addToBackStack(LoginFragment.TAG).commit();
        Log.i(LOG_TAG, "Sign up fragment loaded");
    }

    /*
       Handles ForgotPassword event from the login screen and loads the the ForgotPasswordFragment to the screen
     */
    @Override
    public void onForgotPassword() {
        fragment = fragmentManager.findFragmentByTag(ForgotPasswordFragment.TAG);
        if (fragment == null) {
            fragment = new ForgotPasswordFragment();
        }

        fragmentManager.beginTransaction().replace(R.id.main_container, fragment, ForgotPasswordFragment.TAG).addToBackStack(LoginFragment.TAG).commit();
        Log.i(LOG_TAG, "Forgot Password Fragment loaded");
    }


    /*  Handles signin Event from the SingInFragment.
        It loads the MainPagerFragment to the screen and logs the user on the shared preference file.
        Called after  user signs in is successfull
     */
    @Override
    public void onLogin(String user) {
        fragment = fragmentManager.findFragmentByTag(MainPagerFragment.TAG);

        if (fragment == null) {
            fragment = new MainPagerFragment();
        }
        fragmentManager.beginTransaction().replace(R.id.main_container, fragment, MainPagerFragment.TAG).commit();

        sharedPref.edit().putBoolean(signedIn, true).putString(userName, user).commit();
        Log.i(LOG_TAG, "Singned in " + sharedPref.getString(userName, ""));

        download();

        if (!sharedPref.getBoolean(alarmSet, false)) {
            setAlarm();

        }


    }


}
