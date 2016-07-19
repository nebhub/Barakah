package org.barakahchicago.barakah.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BarakahDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BarakahAppDb";
    private static BarakahDbHelper instance = null;
    private static int DATABASE_VERSION = 1;

    /*
        constructor returns an instance of BarakahDbHelper,
        Used by newInstance. To create a new instance use newInstance.
     */
    private BarakahDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    /*
      Used for testing only
      Constructor
    */
    private BarakahDbHelper(Context context, String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);

    }

    /*
        Returns only one instance of BarakahDbHelper
     */
    public static BarakahDbHelper newInstance(Context context) {
        if (instance == null) {
            instance = new BarakahDbHelper(context);
        }
        return instance;
    }

    /*
        Returns only one instance of BarakahDbHelper for testing
     */
    public static BarakahDbHelper newInstanceForTest(Context context, String databaseName) {
        if (instance == null) {
            instance = new BarakahDbHelper(context, databaseName);
        }
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(BarakahContract.Event.CREATE_STATEMENT);
        db.execSQL(BarakahContract.Article.CREATE_STATEMENT);
        db.execSQL(BarakahContract.Message.CREATE_STATEMENT);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(BarakahContract.Event.DELETE_STATEMENT);
        db.execSQL(BarakahContract.Article.DELETE_STATEMENT);
        db.execSQL(BarakahContract.Message.DELETE_STATEMENT);

        onCreate(db);
    }

}
