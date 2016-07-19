package org.barakahchicago.barakah.dao;

public class BarakahContract {


    public BarakahContract() {

    }

    public static abstract class Event {

        public static final String TABLE_NAME = "events";

        /*
            Id field required by android
         */
        public static final String _ID = "_id";

        /*
            Count field required by android
         */
        public static final String _COUNT = "_count";

        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_START_DATE = "start_date";
        public static final String COLUMN_NAME_END_DATE = "end_date";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_DATE_CREATED = "date_created";
        public static final String COLUMN_NAME_LAST_UPDATED = "last_updated";

        /*
            Arrays of table columns
         */
        public static String[] PROJECTION = {BarakahContract.Event.COLUMN_NAME_ID,
                BarakahContract.Event.COLUMN_NAME_TITLE,
                BarakahContract.Event.COLUMN_NAME_START_DATE,
                BarakahContract.Event.COLUMN_NAME_END_DATE,
                BarakahContract.Event.COLUMN_NAME_LOCATION,
                BarakahContract.Event.COLUMN_NAME_DESCRIPTION,
                BarakahContract.Event.COLUMN_NAME_IMAGE,
                BarakahContract.Event.COLUMN_NAME_LAST_UPDATED
        };

        /*
            Create statement
         */
        public static String CREATE_STATEMENT = "CREATE TABLE IF NOT EXISTS "
                + BarakahContract.Event.TABLE_NAME + " (" + "  "
                + BarakahContract.Event._ID + " INTEGER PRIMARY KEY," + "  "
                + BarakahContract.Event.COLUMN_NAME_ID + " TEXT," + "  "
                + BarakahContract.Event.COLUMN_NAME_TITLE + " TEXT," + "  "
                + BarakahContract.Event.COLUMN_NAME_START_DATE + " TEXT," + "  "
                + BarakahContract.Event.COLUMN_NAME_END_DATE + " TEXT," + "  "
                + BarakahContract.Event.COLUMN_NAME_LOCATION + " TEXT," + "  "
                + BarakahContract.Event.COLUMN_NAME_DESCRIPTION + " TEXT," + "  "
                + BarakahContract.Event.COLUMN_NAME_IMAGE + " TEXT," + "  "
                + BarakahContract.Event.COLUMN_NAME_DATE_CREATED + " TEXT," + "  "
                + BarakahContract.Event.COLUMN_NAME_LAST_UPDATED + " TEXT )";

        public static String DELETE_STATEMENT = "DROP TABLE IF EXISTS "
                + BarakahContract.Event.TABLE_NAME;
    }

    public static abstract class Article {

        public static final String TABLE_NAME = "articles";

        /*
            Id field required by android
         */
        public static final String _ID = "_id";

        /*
            Count field required by android
         */
        public static final String _COUNT = "_count";

        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_BODY = "body";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_DATE_CREATED = "date_created";
        public static final String COLUMN_NAME_LAST_UPDATED = "last_updated";
        public static final String COLUMN_NAME_END_PUBLISH = "end_publish";

        /*
            Create statement
         */
        public static String CREATE_STATEMENT = "CREATE TABLE IF NOT EXISTS "
                + BarakahContract.Article.TABLE_NAME + " (" + "  "
                + BarakahContract.Article._ID + " INTEGER PRIMARY KEY," + "  "
                + BarakahContract.Article.COLUMN_NAME_ID + " TEXT," + "  "
                + BarakahContract.Article.COLUMN_NAME_TITLE + " TEXT," + "  "
                + BarakahContract.Article.COLUMN_NAME_AUTHOR + " TEXT," + "  "
                + BarakahContract.Article.COLUMN_NAME_BODY + " TEXT," + "  "
                + BarakahContract.Article.COLUMN_NAME_IMAGE + " TEXT," + "  "
                + BarakahContract.Article.COLUMN_NAME_DATE_CREATED + " TEXT," + "  "
                + BarakahContract.Article.COLUMN_NAME_LAST_UPDATED + " TEXT, " + " "
                + BarakahContract.Article.COLUMN_NAME_END_PUBLISH + " TEXT)";

        public static String DELETE_STATEMENT = "DROP TABLE IF EXISTS "
                + BarakahContract.Event.TABLE_NAME;
    }

    public static abstract class Message {

        public static final String TABLE_NAME = "messages";
        /*
           Id field required by android
        */
        public static final String _ID = "_id";

        /*
           Count field required by android
        */
        public static final String _COUNT = "_count";

        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_MESSAGE = "message";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_AUDIO = "audio";
        public static final String COLUMN_NAME_VIDEO = "video";
        public static final String COLUMN_NAME_DATE_CREATED = "date_created";
        public static final String COLUMN_NAME_LAST_UPDATED = "last_updated";
        public static final String COLUMN_NAME_END_PUBLISH = "end_publish";


        /*
           Create statement
        */
        public static String CREATE_STATEMENT = "CREATE TABLE IF NOT EXISTS "
                + BarakahContract.Message.TABLE_NAME + " (" + "  "
                + BarakahContract.Message._ID + " INTEGER PRIMARY KEY," + "  "
                + BarakahContract.Message.COLUMN_NAME_ID + " TEXT," + "  "
                + BarakahContract.Message.COLUMN_NAME_TITLE + " TEXT," + "  "
                + BarakahContract.Message.COLUMN_NAME_MESSAGE + " TEXT," + "  "
                + BarakahContract.Message.COLUMN_NAME_IMAGE + " TEXT," + "  "
                + BarakahContract.Message.COLUMN_NAME_AUDIO + " TEXT," + "  "
                + BarakahContract.Message.COLUMN_NAME_VIDEO + " TEXT," + "  "
                + BarakahContract.Message.COLUMN_NAME_DATE_CREATED + " TEXT," + "  "
                + BarakahContract.Message.COLUMN_NAME_LAST_UPDATED + " TEXT, " + " "
                + BarakahContract.Message.COLUMN_NAME_END_PUBLISH + " TEXT)";

        public static String DELETE_STATEMENT = "DROP TABLE IF EXISTS "
                + BarakahContract.Event.TABLE_NAME;

    }
}
