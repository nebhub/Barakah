package org.barakahchicago.barakah.dao;

import java.util.ArrayList;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.barakahchicago.barakah.util.DateUtil;
import org.barakahchicago.barakah.ui.MainActivity;
import org.barakahchicago.barakah.R;
import org.barakahchicago.barakah.model.Event;
import org.barakahchicago.barakah.ui.EventsFragment;
import org.barakahchicago.barakah.ui.MainPagerFragment;

public class EventDAO {

    /*
        Tag used for logging
     */
    private static final String LOG_TAG = "EVENT DAO";
    /*
        context
     */
    Context context;

    /*
        DatabaseHelper used to access database
     */
    BarakahDbHelper dbhelper;

    /*
        Database instance
     */
    SQLiteDatabase db;

    /*
        Key used to access shared preference for notification
     */
    private String pref_notifications_key;

    public EventDAO() {

    }

    public EventDAO(Context context) {
        this.context = context;
        dbhelper = BarakahDbHelper.newInstance(context);
        db = dbhelper.getReadableDatabase();
        pref_notifications_key = context.getResources().getString(R.string.pref_key_notifications);
    }

    public EventDAO(Context context, BarakahDbHelper dbhelper, SQLiteDatabase db) {
        this.context = context;
        this.dbhelper = dbhelper;
        this.db = db;
        pref_notifications_key = context.getResources().getString(R.string.pref_key_notifications);
    }

    public void close() {
        if (db != null) {
            db.close();
        }
    }

    /*
               Queries and returns all events form the database
         */
    public ArrayList<Event> getAll() {
        ArrayList<Event> events = new ArrayList<Event>();

        Cursor cursor = db.query(BarakahContract.Event.TABLE_NAME, BarakahContract.Event.PROJECTION,
                null, null, null, null, BarakahContract.Event.COLUMN_NAME_START_DATE + " ASC");

        Log.i(LOG_TAG, cursor.getCount() + " rows queried");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            Event event;
            while (!cursor.isAfterLast()) {
                event = new Event();
                event.setId(cursor.getString(0));
                event.setTitle(cursor.getString(1));
                event.setStart_date(cursor.getString(2));
                event.setEnd_date(cursor.getString(3));
                event.setLocation(cursor.getString(4));
                event.setImage(cursor.getString(6));
                event.setDescription(cursor.getString(5));
                event.setLast_updated(cursor.getString(7));

                events.add(event);

                cursor.moveToNext();
            }

        }
        return events;
    }

    /*
           Queries and returns all upcomming events form the database
     */
    public ArrayList<Event> getUpcommingEvents() {
        ArrayList<Event> events = new ArrayList<Event>();


        Cursor cursor = db.query(BarakahContract.Event.TABLE_NAME, BarakahContract.Event.PROJECTION,
                "datetime(" + BarakahContract.Event.COLUMN_NAME_START_DATE + ") > datetime('now','localtime') ", null, null, null, BarakahContract.Event.COLUMN_NAME_START_DATE + " ASC");

        Log.i(LOG_TAG, cursor.getCount() + " rows queried");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            Event event;
            while (!cursor.isAfterLast()) {
                event = new Event();
                event.setId(cursor.getString(0));
                event.setTitle(cursor.getString(1));
                event.setStart_date(cursor.getString(2));
                event.setEnd_date(cursor.getString(3));
                event.setLocation(cursor.getString(4));
                event.setImage(cursor.getString(6));
                event.setDescription(cursor.getString(5));
                event.setLast_updated(cursor.getString(7));

                events.add(event);

                cursor.moveToNext();
            }

        }
        return events;
    }

    /*
            Notifies user with the supplied list of events
         */
    private void notify(ArrayList<Event> newList) {
        boolean notify = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(pref_notifications_key, true);
        if (notify) {
            Log.i(LOG_TAG, "Notification Enabled i update");
            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    context);
            builder.setSmallIcon(R.drawable.ic_launcher);
            String title = null;
            StringBuilder contentText = new StringBuilder();
            if (newList.size() > 1) {
                title = newList.get(0).getTitle();

                for (int i = 1; i < newList.size(); i++) {
                    contentText.append(newList.get(i).getTitle() + "\n");
                }
            } else {
                title = newList.get(0).getTitle();
                contentText.append(DateUtil.getFormattedDateTime(newList.get(0).getStart_date()));
            }
            builder.setContentTitle(title);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(contentText));
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(MainPagerFragment.TAB_INDEX, EventsFragment.EVENT_NOTIFICATION_ID);
            PendingIntent pi = PendingIntent.getActivity(context, EventsFragment.EVENT_NOTIFICATION_ID, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pi);
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(EventsFragment.EVENT_NOTIFICATION_ID, builder.build());
            Log.i(LOG_TAG, "Notification set for " + newList.size() + " events");

        }
    }

    /*
           Adds a list of events to the database
     */
    public int add(ArrayList<Event> newList) {
        int addedRows = 0;

        Log.i(LOG_TAG, "Adding " + newList.size() + " event(s) to " + BarakahContract.Event.TABLE_NAME + " table ");
        dbhelper = BarakahDbHelper.newInstance(context);
        db = dbhelper.getWritableDatabase();
        // if (sync.size()>0){
        for (int i = 0; i < newList.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(BarakahContract.Event.COLUMN_NAME_ID, newList.get(i)
                    .getId());
            values.put(BarakahContract.Event.COLUMN_NAME_TITLE, newList.get(i)
                    .getTitle());
            values.put(BarakahContract.Event.COLUMN_NAME_START_DATE, newList
                    .get(i).getStart_date());

            values.put(BarakahContract.Event.COLUMN_NAME_END_DATE,
                    newList.get(i).getEnd_date());
            values.put(BarakahContract.Event.COLUMN_NAME_LOCATION,
                    newList.get(i).getLocation());
            values.put(BarakahContract.Event.COLUMN_NAME_IMAGE, newList.get(i)
                    .getImage());
            values.put(BarakahContract.Event.COLUMN_NAME_DESCRIPTION, newList
                    .get(i).getDescription());
            values.put(BarakahContract.Event.COLUMN_NAME_DATE_CREATED, newList
                    .get(i).getDate_created());
            values.put(BarakahContract.Event.COLUMN_NAME_LAST_UPDATED, newList
                    .get(i).getLast_updated());

            long result = db.insert(BarakahContract.Event.TABLE_NAME, null, values);
            if (result != -1) {
                addedRows++;
            }

            Log.i(LOG_TAG, "New row saved to " + BarakahContract.Event.TABLE_NAME + " table");

        }

        if (newList.size() > 0) {

            notify(newList);

        }


        Log.i(LOG_TAG, "Finished Adding " + newList.size() + " events");
        return addedRows;
    }

    /*
            Updates database with the supplied list of events.
            Returns the number of rows updated
    */
    public int update(ArrayList<Event> newList) {
        int updatedRows = 0;

        Log.i(LOG_TAG, "Updating " + newList.size() + "event(s) from" + BarakahContract.Event.TABLE_NAME + "table");
        dbhelper = BarakahDbHelper.newInstance(context);
        db = dbhelper.getWritableDatabase();

        for (int i = 0; i < newList.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(BarakahContract.Event.COLUMN_NAME_ID, newList.get(i)
                    .getId());
            values.put(BarakahContract.Event.COLUMN_NAME_TITLE, newList.get(i)
                    .getTitle());
            values.put(BarakahContract.Event.COLUMN_NAME_START_DATE, newList
                    .get(i).getStart_date());
            values.put(BarakahContract.Event.COLUMN_NAME_END_DATE,
                    newList.get(i).getEnd_date());
            values.put(BarakahContract.Event.COLUMN_NAME_LOCATION,
                    newList.get(i).getLocation());
            values.put(BarakahContract.Event.COLUMN_NAME_IMAGE, newList.get(i)
                    .getImage());
            values.put(BarakahContract.Event.COLUMN_NAME_DESCRIPTION, newList
                    .get(i).getDescription());
            values.put(BarakahContract.Event.COLUMN_NAME_DATE_CREATED, newList
                    .get(i).getDate_created());
            values.put(BarakahContract.Event.COLUMN_NAME_LAST_UPDATED, newList
                    .get(i).getLast_updated());

            String selection = BarakahContract.Event.COLUMN_NAME_ID + " LIKE ?";
            String[] selectionArgs = {newList.get(i).getId()};

            long result = db.update(BarakahContract.Event.TABLE_NAME, values, selection, selectionArgs);
            if (result != -1) {
                updatedRows++;
            }
            Log.i(LOG_TAG, "Finished updating " + newList.size() + " event(s)");

        }
        //notifiacction for update
        if (newList.size() > 0) {
            notify(newList);

        }


        return updatedRows;
    }

    /*
      Compares the local articles with  parsed articles.
      adds new events with add() and/or update with update().
      @returns the number of row added + updated.

      */
    public int addOrUpdate(ArrayList<Event> parsedList) {
        ArrayList<Event> dbList = getAll();
        ArrayList<Event> syncedList = new ArrayList<Event>();
        ArrayList<Event> updateList = new ArrayList<>();
        Log.i(LOG_TAG, "Number of saved items : " + dbList.size());
        if (parsedList.size() == 0) {
            return 0;
        }
        if (dbList.size() == 0) {


            return add(parsedList);

        }

        for (int i = 0; i < parsedList.size(); i++) {
            boolean exists = false;
            for (int j = 0; j < dbList.size(); j++) {
                if (parsedList.get(i).isEqualTo(dbList.get(j))) {
                    exists = true;
                    if (DateUtil.getLocalDateTime(parsedList.get(i).getLast_updated()).isAfter(DateUtil.getLocalDateTime(dbList.get(j).getLast_updated()))) {
                        updateList.add(parsedList.get(i));
                        Log.i(LOG_TAG, "updated version of " + parsedList.get(i).getTitle() + " found ");

                    } else {

                        break;

                    }

                }

            }
            if (exists) {
                continue;
            }
            Log.i(LOG_TAG, "New event found: " + parsedList.get(i).getTitle());
            syncedList.add(parsedList.get(i));
        }
        Log.i(LOG_TAG, "New row(s) to be added " + syncedList.size());
        Log.i(LOG_TAG, "New row(s) to be updated " + updateList.size());
        if (syncedList.size() > 0) {
            add(syncedList);
        }
        if (updateList.size() > 0) {
            update(updateList);
        }
        return syncedList.size() + updateList.size();
    }
}
