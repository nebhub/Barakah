package org.barakahchicago.barakah.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import org.barakahchicago.barakah.service.BarakahService;
import org.barakahchicago.barakah.R;

public class SettingsActivity extends PreferenceActivity implements
        OnSharedPreferenceChangeListener {
    /*
    Tag used fot logging
    */
    private static final String LOG_TAG = "SETTINGS ACTIVITY";

    /*
      Preference key for setting alarm
    */
    public String pref_alarm_set;

    /*
      Preference key for notification
    */
    public String pref_notifications_key;

    /*
      Preference key for auto update
    */
    public String pref_auto_update_key;

    /*
      Instance of SharedPreference
    */
    private SharedPreferences sharedPref;

    /*
      AlarmManager
    */
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref_alarm_set = getResources().getString(R.string.pref_key_alarm_set);
        pref_notifications_key = getResources().getString(R.string.pref_key_notifications);
        pref_auto_update_key = getResources().getString(R.string.pref_key_auto_update);

        addPreferencesFromResource(R.xml.preference);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);


        int index = (getAutoUpdateValueIndex(sharedPref.getString(pref_auto_update_key, "2")));

        getPreferenceScreen().findPreference(pref_auto_update_key).setSummary(getResources().getStringArray(R.array.pref_auto_update_entries)[index]);
        boolean notification = sharedPref.getBoolean(pref_notifications_key, true);
        if (notification)
            getPreferenceScreen().findPreference(pref_notifications_key).setSummary("On");
        else {
            getPreferenceScreen().findPreference(pref_notifications_key).setSummary("Off");

        }

    }

    private int getAutoUpdateValueIndex(String indexString) {


        if (indexString.equals("1")) {
            return 0;
        } else if (indexString.equals("2")) {
            return 1;
        } else if (indexString.equals("4")) {
            return 2;
        } else if (indexString.equals("12")) {
            return 3;
        } else if (indexString.equals("24")) {
            return 4;
        } else if (indexString.equals("0")) {
            return 5;
        } else {
            return 1;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        LinearLayout root = (LinearLayout) findViewById(android.R.id.list)
                .getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(
                R.layout.settings_toolbar, root, false);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    /*
      Sets alarm based on user preferences
    */
    public boolean setAlarm() {

        Intent intent = new Intent(this, BarakahService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, intent, 0);

        int interval = Integer.parseInt(sharedPref.getString(pref_auto_update_key, "2"));

        Log.i(LOG_TAG, " Interval Value " + interval);

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                interval * 3600 * 1000, interval * 3600 * 1000, pi);
        // alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
        // 10000, 10000, pi);

        Log.i(LOG_TAG, "Alarm set for every" + interval
                + "hours");

        sharedPref.edit().putBoolean(pref_alarm_set, true).commit();

        Log.i(LOG_TAG, "ALARM SET");
        return true;
    }

    public boolean cancelAlarm() {
        Intent alarm = new Intent(this, BarakahService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, alarm, 0);
        alarmManager = (AlarmManager) this
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pi);

        sharedPref.edit().putBoolean(pref_alarm_set, false).commit();
        Log.i(LOG_TAG, "ALARM canceled");
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (key.equals(pref_auto_update_key)) {
            String pref_value = sharedPreferences.getString(key, "2");

            Log.i(LOG_TAG, "Changed key " + key + "  " + " Value : " + pref_value);

            Log.i(LOG_TAG, "Auto Update Settings Changed");
            if (pref_value.equals("0")) {

                cancelAlarm();
                Log.i(LOG_TAG, "Auto Update setting " + sharedPref.getString(key, "2") + "  Canceling alarm");
            } else {
                Log.i(LOG_TAG, "Auto Update setting " + sharedPref.getString(key, "2") + "Something else selected");
                cancelAlarm();
                setAlarm();
            }
            int index = getAutoUpdateValueIndex(pref_value);
            Log.i(LOG_TAG, " Index = " + index);
            getPreferenceScreen().findPreference(key).setSummary(getResources().getStringArray(R.array.pref_auto_update_entries)[index]);
        } else if (key.equals(pref_notifications_key)) {

            boolean notification = sharedPreferences.getBoolean(key, true);
            if (notification)
                getPreferenceScreen().findPreference(key).setSummary("On");
            else {
                getPreferenceScreen().findPreference(key).setSummary("Off");

            }
        }

    }

}
