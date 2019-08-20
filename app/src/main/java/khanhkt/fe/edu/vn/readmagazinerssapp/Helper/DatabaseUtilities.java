package khanhkt.fe.edu.vn.readmagazinerssapp.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import khanhkt.fe.edu.vn.readmagazinerssapp.Model.RssFeedModel;

public class DatabaseUtilities {
    //define constants for processing db
    private static final String DATABASENAME = "RSSFeeds.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLENAME = "tbl_Feed";
    //define all columns in table
    private static final String FEED_COLUMN_ID ="id";
    private static final String FEED_COLUMN_TITLE = "title";
    private static final String FEED_COLUMN_DESP = "description";
    private static final String FEED_COLUMN_LINK = "link";
    private static final String FEED_COLUMN_DATE = "pubDate";
    //define db variable to access
    private SQLiteDatabase db;

    public DatabaseUtilities(Context context) {
        //create open helper, read db to process
        this.db = new RssFeedOpenHelper(context).getWritableDatabase();
    }

    //define method to get all RssFeed
    public List<RssFeedModel> getAllFeeds() {
        List<RssFeedModel> result = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLENAME,
                null);
        //get column index to get db
        //int idIdx = cursor.getColumnIndex(FEED_COLUMN_ID);
        int titleIdx = cursor.getColumnIndex(FEED_COLUMN_TITLE);
        int desIdx = cursor.getColumnIndex(FEED_COLUMN_DESP);
        int linkIdx = cursor.getColumnIndex(FEED_COLUMN_LINK);
        int dateIdx = cursor.getColumnIndex(FEED_COLUMN_DATE);

        //get data
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(titleIdx);
                String description = cursor.getString(desIdx);
                String link = cursor.getString(linkIdx);
                String pubDate = cursor.getString(dateIdx);

                RssFeedModel feed = new RssFeedModel(title, link, description, pubDate);
                result.add(feed);
            } while(cursor.moveToNext());//get each row
        }//end if check data is existed
        //close cursor if it opened
        if (!cursor.isClosed()) {
            cursor.close();
        }//end if cursor is not closed
        return result;
    }
    //define insert data to table
    public void insert (RssFeedModel feed){
        ContentValues content = new ContentValues();

        content.put(FEED_COLUMN_TITLE, feed.getTitle());
        content.put(FEED_COLUMN_DESP, feed.getDescription());
        content.put(FEED_COLUMN_LINK, feed.getLink());
        content.put(FEED_COLUMN_DATE, feed.getPublicDate());

        db.insert(TABLENAME, null, content);
    }
    //delete all data in table
    public void clear() {
        db.execSQL("DELETE FROM " + TABLENAME);
    }
    //define helper to create DB, Table and drop table
    private class RssFeedOpenHelper extends SQLiteOpenHelper {
        //define constructor
        public RssFeedOpenHelper(Context context) {
            //create DB RSSFeeds
            super(context, DATABASENAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            //create table
            String sql = "CREATE TABLE "
                    + TABLENAME + "("
                    + FEED_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + FEED_COLUMN_TITLE + " TEXT,"
                    + FEED_COLUMN_DESP + " TEXT,"
                    + FEED_COLUMN_LINK + " TEXT,"
                    + FEED_COLUMN_DATE + " TEXT"
                    + ")";
            Log.d(ContentValues.TAG, "SHOW SQL " + sql);
            sqLiteDatabase.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            //update DB
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
            onCreate(sqLiteDatabase);
        }
    }//end if Open Helper
}
