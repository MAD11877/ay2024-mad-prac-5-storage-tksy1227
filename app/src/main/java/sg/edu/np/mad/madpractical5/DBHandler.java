package sg.edu.np.mad.madpractical5;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.database.Cursor;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "usersdb";
    private static final String TABLE_USER = "User";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_FOLLOWED = "followed";

    private static DBHandler instance;
    private DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DBHandler getInstance(Context context) {
        if (instance == null) {
            instance = new DBHandler(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_FOLLOWED + " INTEGER" + ")";
        db.execSQL(CREATE_USER_TABLE);

// Generate and insert 20 user data
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            String name = "Name" + String.format("%06d", random.nextInt(1000000)); // Generates a 6-digit number
            String description = "Description " + String.format("%08d", random.nextInt(100000000)); // Generates an 8-digit number
            boolean followed = random.nextBoolean();

            ContentValues values = new ContentValues();
            values.put(KEY_NAME, name);
            values.put(KEY_DESCRIPTION, description);
            values.put(KEY_FOLLOWED, followed ? 1 : 0);

            db.insert(TABLE_USER, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if exists and create tables again
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public List<User> getUsers() {
        List<User> userList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User(cursor.getString(1), cursor.getString(2), cursor.getInt(0), cursor.getInt(3) == 1);
                userList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return userList;
    }

    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_DESCRIPTION, user.getDescription());
        values.put(KEY_FOLLOWED, user.isFollowed() ? 1 : 0);

        db.update(TABLE_USER, values, KEY_ID + " = ?", new String[] { String.valueOf(user.getId()) });
        db.close();
    }

}