package org.barakahchicago.barakah.service;

import java.net.InetAddress;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.barakahchicago.barakah.dao.BarakahDbHelper;
import org.barakahchicago.barakah.R;
import org.barakahchicago.barakah.dao.ArticleDAO;
import org.barakahchicago.barakah.dao.EventDAO;
import org.barakahchicago.barakah.dao.MessageDAO;
import org.barakahchicago.barakah.model.Article;
import org.barakahchicago.barakah.model.Event;
import org.barakahchicago.barakah.model.Message;


public class BarakahService extends Service {


    /*
        Action string used for sending broadcasts
     */
    public static final String BROADCAST_ACTION = "org.barakahchicago.barakah.BROADCAST_ACTION";
    /*
        Success status String used with broadcast to signal download success
     */
    public static final String STATUS_DONE = "org.barakahchicago.barakah.STATUS_DONE";
    /*
        Failure status String used with broadcast to signal download failure
     */
    public static final String STATUS_FAIL = "org.barakahchicago.barakah.STATUS_FAIL";
    /*
        Key value used to map  status Strings STATUS_DONE or  STATUS_FAIL
     */
    public static final String STATUS = "org.barakahchicago.barakah.STATUS";
    /*
        Tag used for logging
     */
    private static final String LOG_TAG = "BARAKAH SERVICE";
    /*
        Url used to access the webservice
     */
    private static final String URL = "http://www.barakahchicago.org/bcwebservice/webservice.php/all";


    /*
       FOR TESTING ONLY
       Url used to access the webservice
    */
    private static final String TEST_URL = "http://www.barakahchicago.org/bcwebservice/webservice.php/test";


    /*
      FOR TESTING ONLY
      Url used to access the webservice
   */
    private static final String TEST_URL_2 = "http://www.barakahchicago.org/bcwebservice/webservice.php/testNewApi";

    /*
        EvendDAO object used to add or update database with new events
     */
    private EventDAO eventDAO;

    /*
        ArticleDAO object used to add or update database with new articles
     */
    private ArticleDAO articleDAO;

    /*
        MessageDAO object used to add or update database with new messages
     */
    private MessageDAO messageDAO;
    /*
        DatabaseHelper used to access database
     */
    private BarakahDbHelper dbHelper;

    /*
        Database instance
     */
    private SQLiteDatabase db;


    @Override
    public void onCreate() {

        super.onCreate();

        dbHelper = BarakahDbHelper.newInstance(getApplicationContext());
        db = dbHelper.getWritableDatabase();
        eventDAO = new EventDAO(getApplicationContext(), dbHelper, db);
        articleDAO = new ArticleDAO(getApplicationContext(), dbHelper, db);
        messageDAO = new MessageDAO(getApplicationContext(), dbHelper, db);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        parseData(TEST_URL_2);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /*
        Connecst to the internet and parses JSON data from the webservice
     */
    public int parseData(String url) {


        // Start dowloading data
        Ion.with(getApplicationContext()).load(url).asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {

                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        JsonArray events = null;
                        JsonArray articles = null;
                        JsonArray messages = null;
                        if (e != null) {
                            /*
                                a cannot update notification
                             */
                            boolean notify = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(getResources().getString(R.string.pref_key_notifications), true);
                            if (notify) {
                             /*   Log.i(LOG_TAG, "Norifiacations enabled");


                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
                                builder.setSmallIcon(R.drawable.ic_launcher);
                                builder.setContentTitle("Cannot sync data");
                                builder.setContentText("Check your network connection and try again.");

                                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                manager.notify(4, builder.build());
                                */
                            }

                            Intent intent = new Intent(BROADCAST_ACTION);
                            intent.putExtra(STATUS, STATUS_FAIL);
                            Log.i(LOG_TAG, "Failure broadcast sent");
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);


                        }

                        if (result != null && result.isJsonObject())

                        {
                            Log.i(LOG_TAG, "an object found");

                            events = result.getAsJsonArray("events");
                            articles = result.getAsJsonArray("articles");
                            messages = result.getAsJsonArray("messages");


                            if (events != null) {
                                eventDAO.addOrUpdate(parseEvents(events));
                            }
                            if (articles != null) {
                                articleDAO.addOrUpdate(parseArticles(articles));
                            }
                            if (messages != null) {
                                messageDAO.addOrUpdate(parseMessages(messages));
                            }
                            Intent intent = new Intent(BROADCAST_ACTION);
                            intent.putExtra(STATUS, STATUS_DONE);

                            LocalBroadcastManager.getInstance(
                                    getApplicationContext()).sendBroadcastSync(
                                    intent);
                            Log.i(LOG_TAG, "Broadcast sent");

                        }

                        stopSelf();

                    }


                });


        return 0;
    }

    /*
      Checks if there is an internet connection.

    */
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }

    }

    /*
        parses evensts Json array into list of Event objects using Gson.
    */
    private ArrayList<Event> parseEvents(JsonArray events) {
        Event parsedEvent;
        ArrayList<Event> eventList = new ArrayList<Event>();
        Gson gson = new Gson();

        Log.i("Json Log", "Event Array found");
        for (int i = 0; i < events.size(); i++) {
            JsonObject eventObject = (JsonObject) events
                    .get(i);
            parsedEvent = gson.fromJson(
                    eventObject.toString(), Event.class);
            eventList.add(parsedEvent);

        }

        Log.i(LOG_TAG, "Finished parsing...");

        return eventList;
    }

    /*
        parses articles Json array into list of Article objects using Gson.
     */
    private ArrayList<Article> parseArticles(JsonArray articles) {
        Article parsedArticle;
        ArrayList<Article> articleList = new ArrayList<Article>();
        Gson gson = new Gson();

        Log.i("Json Log", "Article Array found");
        for (int i = 0; i < articles.size(); i++) {
            JsonObject articleObject = (JsonObject) articles
                    .get(i);
            parsedArticle = gson.fromJson(
                    articleObject.toString(), Article.class);

            articleList.add(parsedArticle);

        }

        Log.i(LOG_TAG, "Finished parsing...");

        return articleList;
    }

    /*
     parses messages Json array into list of Message objects using Gson.
     */
    private ArrayList<Message> parseMessages(JsonArray messages) {
        Message parsedMessages;
        ArrayList<Message> messageList = new ArrayList<Message>();
        Gson gson = new Gson();

        Log.i("Json Log", "Message Array found");
        for (int i = 0; i < messages.size(); i++) {
            JsonObject messageObject = (JsonObject) messages
                    .get(i);
            parsedMessages = gson.fromJson(
                    messageObject.toString(), Message.class);

            messageList.add(parsedMessages);

        }

        Log.i(LOG_TAG, "Finished parsing...");

        return messageList;
    }
}
