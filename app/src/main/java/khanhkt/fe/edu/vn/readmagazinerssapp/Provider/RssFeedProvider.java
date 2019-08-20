package khanhkt.fe.edu.vn.readmagazinerssapp.Provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class RssFeedProvider extends ContentProvider {
    //define uri for content provider
    public static final String PROVIDER_NAME = "khanhkt.fe.edu.vn.Feeds";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + PROVIDER_NAME + "/feeds");
    //declare variable to store feed Attr
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String LINK = "link";
    public static final String PUBLICDATE = "pubDate";
    //declare variable to access DB
    private SQLiteDatabase db;
    private static final String DATABASENAME = "RSSFeeds.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLENAME = "tbl_Feed";
    //define all columns in table
    private static final String FEED_COLUMN_ID ="id";
    private static final String FEED_COLUMN_TITLE = "title";
    private static final String FEED_COLUMN_DESP = "description";
    private static final String FEED_COLUMN_LINK = "link";
    private static final String FEED_COLUMN_DATE = "pubDate";

    public static final int FEEDS = 1;
    public static final int FEED_ID = 2;

    public static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "feeds", FEEDS);
        uriMatcher.addURI(PROVIDER_NAME, "feeds/#", FEED_ID);
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        RssFeedOpenHelper dbHelper = new RssFeedOpenHelper(context);
        db = dbHelper.getWritableDatabase();

        if (db == null) {
            return false;
        }
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(TABLENAME);
        if(uriMatcher.match(uri) == FEED_ID) {
            builder.appendWhere(ID + uri.getPathSegments().get(1));
        }//end if feed id
        if (sortOrder == null || sortOrder == "") {
            sortOrder = PUBLICDATE;
        }//end if sort order
        //traversal
        Cursor cursor = builder.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long rowId = db.insert(TABLENAME, "", contentValues);
        //Insert is success
        if (rowId > 0) {
            Uri mUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(mUri, null);

            return mUri;
        }//end if rowID larger than 0
        throw new SQLException("Failed to insert new row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int row = db.delete(TABLENAME, "", null);

        return row;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
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
