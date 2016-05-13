package sk.sustr.michal.sayhello;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michal Sustr [michal.sustr@gmail.com] on 5/11/16.
 */
public class FriendSqliteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "FriendDB.sqlite";
    private static final String TAG = "sayhello";

    // Friends table name
    private static final String TABLE_BOOKS = "friends";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DAYS = "days";
    private static final String KEY_LAST_CONTACTED = "lastContacted";

    private static final String[] COLUMNS = {KEY_ID,KEY_NAME,KEY_DAYS,KEY_LAST_CONTACTED};

    public FriendSqliteHelper (Context context) {
        super(context, Environment.getExternalStorageDirectory()
                + File.separator + DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_BOOK_TABLE = "CREATE TABLE friends ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, "+
                "days INTEGER, "+
                "lastContacted INTEGER "+
                ")";

        // create books table
        db.execSQL(CREATE_BOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS books");

        // create fresh books table
        this.onCreate(db);
    }

    public void addFriend(Friend friend){
        //for logging
        Log.d(TAG, "add friend "+ friend.toString());

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_DAYS, friend.getDays()); // get title
        values.put(KEY_LAST_CONTACTED, friend.getLastContacted()); // get title
        values.put(KEY_NAME, friend.getName()); // get title

        // 3. insert
        db.insert(TABLE_BOOKS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    private List<Friend> getFriends(String query) {
        List<Friend> friends = new LinkedList<Friend>();

        // 1. build the query

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Friend friend = null;
        if (cursor.moveToFirst()) {
            do {
                friend = new Friend();
                friend.setId(Integer.parseInt(cursor.getString(0)));
                friend.setName(cursor.getString(1));
                friend.setDays(Integer.parseInt(cursor.getString(2)));
                friend.setLastContacted(Integer.parseInt(cursor.getString(3)));

                // Add book to books
                friends.add(friend);
            } while (cursor.moveToNext());
        }

        // return books
        return friends;
    }

    public List<Friend> getTodayFriends() {
        return getFriends("SELECT * FROM friends WHERE lastContacted-"+MainActivity.getToday()+"+days <= 0");
    }

    public List<Friend> getOtherFriends() {
        return getFriends("SELECT * FROM friends WHERE lastContacted-"+MainActivity.getToday()+"+days > 0");
    }

    public int updateFriend(Friend friend) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("name", friend.getName()); // get title
        values.put("days", friend.getDays()); // get title
        values.put("lastContacted", friend.getLastContacted()); // get title

        // 3. updating row
        int i = db.update(TABLE_BOOKS, //table
            values, // column/value
            KEY_ID+" = ?", // selections
            new String[] { String.valueOf(friend.getId()) }); //selection args

        // 4. close
        db.close();
        return i;
    }

}