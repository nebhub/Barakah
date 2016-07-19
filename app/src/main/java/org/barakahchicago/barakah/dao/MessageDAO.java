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
import org.barakahchicago.barakah.model.Message;
import org.barakahchicago.barakah.ui.MainPagerFragment;
import org.barakahchicago.barakah.ui.MessagesFragment;

public class MessageDAO {
    /*
        Tag used for logging
     */
    private static final String LOG_TAG = "MESSAGE DAO";

    /*
       Key used to access shared preference for notification
    */
    private String pref_notifications_key;

    /*
        context
     */
    private Context context;

    /*
        OpenDbHelper instance. Used by this class to access
     */
    private BarakahDbHelper dbHelper;

    /*
        Database instance
     */
    private SQLiteDatabase db;

    /*
        Constructor
     */
    public MessageDAO(Context context) {
        this.context = context;
    }

    public MessageDAO(Context context, BarakahDbHelper dbHelper, SQLiteDatabase db) {
        this.context = context;
        this.dbHelper = dbHelper;
        this.db = db;
        pref_notifications_key = context.getResources().getString(R.string.pref_key_notifications);
    }

    /*
        Queries and returns all messages
     */
    public ArrayList<Message> getAll() {
        ArrayList<Message> messages = new ArrayList<Message>();

        String[] projection = {BarakahContract.Message.COLUMN_NAME_ID,
                BarakahContract.Message.COLUMN_NAME_TITLE,
                BarakahContract.Message.COLUMN_NAME_MESSAGE,
                BarakahContract.Message.COLUMN_NAME_IMAGE,
                BarakahContract.Message.COLUMN_NAME_AUDIO,
                BarakahContract.Message.COLUMN_NAME_VIDEO,
                BarakahContract.Message.COLUMN_NAME_DATE_CREATED,
                BarakahContract.Message.COLUMN_NAME_LAST_UPDATED,
                BarakahContract.Message.COLUMN_NAME_END_PUBLISH

        };
        //Cursor cursor = db.query(BarakahContract.Message.TABLE_NAME, projection,
        //        null, null, null, null, BarakahContract.Message.COLUMN_NAME_DATE_CREATED + " ASC");
        Cursor cursor = db.query(BarakahContract.Message.TABLE_NAME, projection, null, null, null, null, BarakahContract.Message.COLUMN_NAME_DATE_CREATED + " ASC");
        Log.i(LOG_TAG, cursor.getCount() + " rows queried");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Message message;
            while (!cursor.isAfterLast()) {
                message = new Message();
                message.setId(cursor.getString(0));
                message.setTitle(cursor.getString(1));
                message.setMessage(cursor.getString(2));
                message.setImage(cursor.getString(3));
                message.setAudio(cursor.getString(4));
                message.setVideo(cursor.getString(5));
                message.setDate_created(cursor.getString(6));
                message.setLast_updated(cursor.getString(7));
                message.setEnd_publish(cursor.getString(8));
                messages.add(message);

                cursor.moveToNext();
            }

        }

        return messages;
    }

    /*
        Queries and returns all messages that are published
     */
    public ArrayList<Message> getPublishedMessages() {
        ArrayList<Message> messages = new ArrayList<Message>();

        String[] projection = {BarakahContract.Message.COLUMN_NAME_ID,
                BarakahContract.Message.COLUMN_NAME_TITLE,
                BarakahContract.Message.COLUMN_NAME_MESSAGE,
                BarakahContract.Message.COLUMN_NAME_IMAGE,
                BarakahContract.Message.COLUMN_NAME_AUDIO,
                BarakahContract.Message.COLUMN_NAME_VIDEO,
                BarakahContract.Message.COLUMN_NAME_DATE_CREATED,
                BarakahContract.Message.COLUMN_NAME_LAST_UPDATED,
                BarakahContract.Message.COLUMN_NAME_END_PUBLISH

        };
        Cursor cursor = db.query(BarakahContract.Message.TABLE_NAME, projection,
                "datetime (" + BarakahContract.Message.COLUMN_NAME_END_PUBLISH + ") > datetime('now','localtime') ", null, null, null, BarakahContract.Message.COLUMN_NAME_DATE_CREATED + " ASC");
        Log.i(LOG_TAG, cursor.getCount() + " rows queried");

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Message message;
            while (!cursor.isAfterLast()) {
                message = new Message();
                message.setId(cursor.getString(0));
                message.setTitle(cursor.getString(1));
                message.setMessage(cursor.getString(2));
                message.setImage(cursor.getString(3));
                message.setAudio(cursor.getString(4));
                message.setVideo(cursor.getString(5));
                message.setDate_created(cursor.getString(6));
                message.setLast_updated(cursor.getString(7));
                message.setEnd_publish(cursor.getString(8));

                Log.i(LOG_TAG, "end publish :" + message.getEnd_publish() + " of " + message.getTitle());
                messages.add(message);

                cursor.moveToNext();
            }

        }

        return messages;
    }

    /*
        Notify user with the supplied list of messages
    */
    private void notify(ArrayList<Message> newList) {
        boolean notify = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(pref_notifications_key, true);
        if (notify) {
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
                contentText.append(newList.get(0).getMessage());
            }

            builder.setContentTitle(title);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(contentText));
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(MainPagerFragment.TAB_INDEX, MessagesFragment.MESSAGES_NOTIFIFCATION_ID);
            PendingIntent pi = PendingIntent.getActivity(context, MessagesFragment.MESSAGES_NOTIFIFCATION_ID, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pi);
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(MessagesFragment.MESSAGES_NOTIFIFCATION_ID, builder.build());
            Log.i(LOG_TAG, "Notification set for " + newList.size() + " messages");
        }
    }

    /*
     Adds a new list of messages into the database.
     Returns the number of rows added.
     */
    public int add(ArrayList<Message> newList) {

        int addedRows = 0;

        Log.i(LOG_TAG, "Adding " + newList.size() + " message(s) to " + BarakahContract.Message.TABLE_NAME + " table");
        Log.i(LOG_TAG, "New row " + newList.get(0).getTitle());
        for (int i = 0; i < newList.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(BarakahContract.Message.COLUMN_NAME_ID, newList.get(i)
                    .getId());
            values.put(BarakahContract.Message.COLUMN_NAME_TITLE, newList.get(i)
                    .getTitle());
            values.put(BarakahContract.Message.COLUMN_NAME_MESSAGE, newList
                    .get(i).getMessage());
            values.put(BarakahContract.Message.COLUMN_NAME_IMAGE,
                    newList.get(i).getImage());
            values.put(BarakahContract.Message.COLUMN_NAME_AUDIO, newList.get(i)
                    .getAudio());
            values.put(BarakahContract.Message.COLUMN_NAME_VIDEO, newList.get(i)
                    .getVideo());
            values.put(BarakahContract.Message.COLUMN_NAME_DATE_CREATED, newList
                    .get(i).getDate_created());
            values.put(BarakahContract.Message.COLUMN_NAME_LAST_UPDATED, newList
                    .get(i).getLast_updated());
            values.put(BarakahContract.Message.COLUMN_NAME_END_PUBLISH, newList
                    .get(i).getEnd_publish());

            long result = db.insert(BarakahContract.Message.TABLE_NAME, null, values);
            if (result != -1) {
                addedRows++;
            }
            Log.i(LOG_TAG, "New row saved to " + BarakahContract.Message.TABLE_NAME + " table");
            Log.i(LOG_TAG, "end publish :" + newList.get(i).getEnd_publish() + " of " + newList.get(i).getTitle());

        }
        if (newList.size() > 0) {


            notify(newList);


        }
        Log.i(LOG_TAG, "Finished Adding " + newList.size() + " messsages");
        Log.i(LOG_TAG, "Finished Adding real" + addedRows + " messsages");
        Log.i(LOG_TAG, "published messages " + getPublishedMessages().size());
        Log.i(LOG_TAG, "All messages " + getAll().size());
        return addedRows;
    }

    /*
        Updates database with the supplied list of messages.
        Returns the number of rows updated
     */
    public int update(ArrayList<Message> newList) {
        int updateRows = 0;
        Log.i(LOG_TAG, "Updating " + newList.size() + "message(s) from" + BarakahContract.Message.TABLE_NAME + "table");

        for (int i = 0; i < newList.size(); i++) {

            ContentValues values = new ContentValues();
            values.put(BarakahContract.Message.COLUMN_NAME_ID, newList.get(i)
                    .getId());
            values.put(BarakahContract.Message.COLUMN_NAME_TITLE, newList.get(i)
                    .getTitle());
            values.put(BarakahContract.Message.COLUMN_NAME_MESSAGE, newList
                    .get(i).getMessage());
            values.put(BarakahContract.Message.COLUMN_NAME_IMAGE,
                    newList.get(i).getImage());
            values.put(BarakahContract.Message.COLUMN_NAME_AUDIO, newList.get(i)
                    .getAudio());
            values.put(BarakahContract.Message.COLUMN_NAME_VIDEO, newList.get(i)
                    .getVideo());
            values.put(BarakahContract.Message.COLUMN_NAME_DATE_CREATED, newList
                    .get(i).getDate_created());
            values.put(BarakahContract.Message.COLUMN_NAME_LAST_UPDATED, newList
                    .get(i).getLast_updated());
            values.put(BarakahContract.Message.COLUMN_NAME_END_PUBLISH, newList
                    .get(i).getEnd_publish());

            String selection = BarakahContract.Message.COLUMN_NAME_ID + " LIKE ?";
            String[] selectionArgs = {newList.get(i).getId()};

            long result = db.update(BarakahContract.Message.TABLE_NAME, values, selection, selectionArgs);
            if (result != -1) {
                updateRows++;
            }
            Log.i(LOG_TAG, "Finished updating " + newList.size() + " message(s)");

        }
        if (newList.size() > 0) {


            notify(newList);


        }
        return updateRows;
    }

    /*
     Compares the local articles with  parsed messages.
     adds new messages with add() and/or update with update().
     Returns the number of row added + updated.

     */
    public int addOrUpdate(ArrayList<Message> parsedList) {
        ArrayList<Message> dbList = getAll();
        ArrayList<Message> syncedList = new ArrayList<Message>();
        ArrayList<Message> updateList = new ArrayList<>();
        Log.i(LOG_TAG, "Number of saved items : " + dbList.size());
        if (parsedList.size() == 0) {
            return 0;
        }
        if (dbList.size() == 0) {

            add(parsedList);
            return parsedList.size();

        }

        for (int i = 0; i < parsedList.size(); i++) {
            boolean exists = false;
            for (int j = 0; j < dbList.size(); j++) {
                if (parsedList.get(i).isEqualTo(dbList.get(j))) {
                    exists = true;


                    if (DateUtil.getLocalDateTime(parsedList.get(i).getLast_updated()).isAfter(DateUtil.getLocalDateTime(dbList.get(j).getLast_updated()))) {
                        Log.i(LOG_TAG, "updated version of " + parsedList.get(i).getTitle() + " found ");
                        updateList.add(parsedList.get(i));
                    } else {

                        break;
                    }

                }

            }
            if (exists) {
                continue;
            }
            syncedList.add(parsedList.get(i));
        }
        Log.i(LOG_TAG, "New row to be added " + syncedList.size());
        Log.i(LOG_TAG, "New row to be updated " + updateList.size());
        if (syncedList.size() > 0) {
            add(syncedList);
        }
        if (updateList.size() > 0) {
            update(updateList);
        }
        return syncedList.size() + updateList.size();
    }

}
