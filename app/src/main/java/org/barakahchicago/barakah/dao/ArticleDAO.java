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
import org.barakahchicago.barakah.model.Article;
import org.barakahchicago.barakah.ui.ArticleFragment;
import org.barakahchicago.barakah.ui.MainPagerFragment;

public class ArticleDAO {
    /*
        Tag used for logging
     */
    private static final String LOG_TAG = "ARTICLE DAO";

    /*
        Key used to access shared preference for notification
     */
    private String pref_notifications_key;

    /*
        Context
     */

    private Context context;

    /*

        OpenDbHelper instance. Used by this class to access
     */
    private BarakahDbHelper dbHelper;

    /*
      database instance

     */
    private SQLiteDatabase db;

    /*
        Constructor
     */
    public ArticleDAO(Context context) {
        this.context = context;
    }

    public ArticleDAO(Context context, BarakahDbHelper dbHelper, SQLiteDatabase db) {
        this.context = context;
        this.dbHelper = dbHelper;
        this.db = db;
        pref_notifications_key = context.getResources().getString(R.string.pref_key_notifications);
    }

    /*
           Queries and returns all articles form the database
     */
    public ArrayList<Article> getAll() {
        ArrayList<Article> articles = new ArrayList<Article>();

        String[] projection = {BarakahContract.Article.COLUMN_NAME_ID,
                BarakahContract.Article.COLUMN_NAME_TITLE,
                BarakahContract.Article.COLUMN_NAME_AUTHOR,
                BarakahContract.Article.COLUMN_NAME_BODY,
                BarakahContract.Article.COLUMN_NAME_IMAGE,
                BarakahContract.Article.COLUMN_NAME_DATE_CREATED,
                BarakahContract.Article.COLUMN_NAME_LAST_UPDATED
        };

        Cursor cursor = db.query(BarakahContract.Article.TABLE_NAME, projection, null, null, null, null, BarakahContract.Article.COLUMN_NAME_DATE_CREATED + " ASC");
        Log.i(LOG_TAG, cursor.getCount() + " rows queried");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Article article;
            while (!cursor.isAfterLast()) {
                article = new Article();
                article.setId(cursor.getString(0));
                article.setTitle(cursor.getString(1));
                article.setAuthor(cursor.getString(2));
                article.setBody(cursor.getString(3));
                article.setImage(cursor.getString(4));
                article.setDate_created(cursor.getString(5));
                article.setLast_updated(cursor.getString(6));
                articles.add(article);

                cursor.moveToNext();
            }

        }
        return articles;
    }

    /*
        Queries and returns all articles that are published
     */
    public ArrayList<Article> getPublishedArticles() {
        ArrayList<Article> articles = new ArrayList<Article>();

        String[] projection = {BarakahContract.Article.COLUMN_NAME_ID,
                BarakahContract.Article.COLUMN_NAME_TITLE,
                BarakahContract.Article.COLUMN_NAME_AUTHOR,
                BarakahContract.Article.COLUMN_NAME_BODY,
                BarakahContract.Article.COLUMN_NAME_IMAGE,
                BarakahContract.Article.COLUMN_NAME_DATE_CREATED,
                BarakahContract.Article.COLUMN_NAME_LAST_UPDATED
        };

        Cursor cursor = db.query(BarakahContract.Article.TABLE_NAME, projection,
                "datetime(" + BarakahContract.Article.COLUMN_NAME_END_PUBLISH + ") > datetime('now','localtime') ", null, null, null, BarakahContract.Article.COLUMN_NAME_DATE_CREATED + " ASC");
        Log.i(LOG_TAG, cursor.getCount() + " rows queried");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Article article;
            while (!cursor.isAfterLast()) {
                article = new Article();
                article.setId(cursor.getString(0));
                article.setTitle(cursor.getString(1));
                article.setAuthor(cursor.getString(2));
                article.setBody(cursor.getString(3));
                article.setImage(cursor.getString(4));
                article.setDate_created(cursor.getString(5));
                article.setLast_updated(cursor.getString(6));
                articles.add(article);

                cursor.moveToNext();
            }

        }
        return articles;
    }

    /*
        Notifies user with the supplied list of articles
     */

    private void notify(ArrayList<Article> newList) {
        boolean notify = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(pref_notifications_key, true);
        if (notify) {
            Log.e(LOG_TAG, "notify???????");
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
                contentText.append(DateUtil.getFormattedDateTime(newList.get(0).getDate_created()));
            }
            builder.setContentTitle(title);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(contentText));
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(MainPagerFragment.TAB_INDEX, ArticleFragment.ARTICLE_NOTIFICATION_ID);
            PendingIntent pi = PendingIntent.getActivity(context, ArticleFragment.ARTICLE_NOTIFICATION_ID, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pi);
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(ArticleFragment.ARTICLE_NOTIFICATION_ID, builder.build());
            Log.i(LOG_TAG, "Notification set for " + newList.size() + " articles");
        }
    }

    /*
     Adds a new list of articles into the database.
     Returns the number of rows added.
     */
    public int add(ArrayList<Article> newList) {
        Log.e(LOG_TAG, "add???????");
        int addedRows = 0;
        Log.i(LOG_TAG, "Adding " + newList.size() + " article(s) to " + BarakahContract.Article.TABLE_NAME + " table");

        for (int i = 0; i < newList.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(BarakahContract.Article.COLUMN_NAME_ID, newList.get(i)
                    .getId());
            values.put(BarakahContract.Article.COLUMN_NAME_TITLE, newList.get(i)
                    .getTitle());
            values.put(BarakahContract.Article.COLUMN_NAME_AUTHOR, newList
                    .get(i).getAuthor());
            values.put(BarakahContract.Article.COLUMN_NAME_BODY,
                    newList.get(i).getBody());
            values.put(BarakahContract.Article.COLUMN_NAME_IMAGE, newList.get(i)
                    .getImage());
            values.put(BarakahContract.Article.COLUMN_NAME_DATE_CREATED, newList
                    .get(i).getDate_created());
            values.put(BarakahContract.Article.COLUMN_NAME_LAST_UPDATED, newList
                    .get(i).getLast_updated());
            values.put(BarakahContract.Article.COLUMN_NAME_END_PUBLISH, newList
                    .get(i).getEnd_publish());

            long result = db.insert(BarakahContract.Article.TABLE_NAME, null, values);
            if (result != -1) {
                addedRows++;
            }
            Log.i(LOG_TAG, "New row saved to " + BarakahContract.Article.TABLE_NAME + " table");

        }

        if (newList.size() > 0) {
            notify(newList);
        }

        return addedRows;
    }

    /*
        Updates database with the supplied list of articles.
        Returns the number of rows updated
     */
    public int update(ArrayList<Article> newList) {

        int updatedRows = 0;

        for (int i = 0; i < newList.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(BarakahContract.Article.COLUMN_NAME_ID, newList.get(i)
                    .getId());
            values.put(BarakahContract.Article.COLUMN_NAME_TITLE, newList.get(i)
                    .getTitle());
            values.put(BarakahContract.Article.COLUMN_NAME_AUTHOR, newList
                    .get(i).getAuthor());
            values.put(BarakahContract.Article.COLUMN_NAME_BODY,
                    newList.get(i).getBody());
            values.put(BarakahContract.Article.COLUMN_NAME_IMAGE, newList.get(i)
                    .getImage());
            values.put(BarakahContract.Article.COLUMN_NAME_DATE_CREATED, newList
                    .get(i).getDate_created());
            values.put(BarakahContract.Article.COLUMN_NAME_LAST_UPDATED, newList
                    .get(i).getLast_updated());
            values.put(BarakahContract.Article.COLUMN_NAME_END_PUBLISH, newList
                    .get(i).getEnd_publish());

            String selection = BarakahContract.Message.COLUMN_NAME_ID + " LIKE ?";
            String[] selectionArgs = {newList.get(i).getId()};

            long result = db.update(BarakahContract.Article.TABLE_NAME, values, selection, selectionArgs);
            if (result != -1) {
                updatedRows++;
            }
            Log.i(LOG_TAG, "Finished updating " + newList.size() + " article(s)");

        }

        //notifiy user
        if (newList.size() > 0) {

            notify(newList);

        }


        return updatedRows;
    }

    /*
     Compares the local articles with  parsed articles.
     adds new articles with add() and/or update with update().
     Returns the number of row added + updated.

     */
    public int addOrUpdate(ArrayList<Article> parsedList) {
        ArrayList<Article> dbList = getAll();
        ArrayList<Article> syncedList = new ArrayList<Article>();
        ArrayList<Article> updateList = new ArrayList<Article>();
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
