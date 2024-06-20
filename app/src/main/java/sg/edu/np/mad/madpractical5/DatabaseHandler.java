package sg.edu.np.mad.madpractical5;

import android.annotation.SuppressLint;
import android.content.ContentValues;

import android.content.Context;

import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;



import java.util.ArrayList;
import java.util.Random;

import android.database.sqlite.SQLiteException;

import android.util.Log;
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "myusers.db";

    private static final String USERS = "users";

    private static final String COLUMN_ID = "id";

    private static final String COLUMN_NAME = "name";

    private static final String COLUMN_DESC = "description";
    private static final String COLUMN_FOLL = "followed";



    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){

        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

    }



    @Override

    public void onCreate(SQLiteDatabase db){



        Log.i("Database Operations", "Creating a Table.");

        try {

            String CREATE_USERS_TABLE = "CREATE TABLE " + USERS + "(" + COLUMN_NAME + " TEXT," + COLUMN_DESC + " TEXT,"
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_FOLL + " TEXT" +")";

            db.execSQL(CREATE_USERS_TABLE);


            for (int i = 0; i<20; i++) {
                Random r = new Random();
                int name = r.nextInt(9999999);
                int description = r.nextInt(9999999);
                boolean followed = r.nextBoolean();
                ContentValues values = new ContentValues();

                values.put(COLUMN_NAME, "Name" + name);
                values.put(COLUMN_DESC, "Description" + description);
                values.put(COLUMN_FOLL, String.valueOf(followed));
                db.insert(USERS, null, values);
            }
            Log.i("Database Operations", "Table created successfully.");

        } catch (SQLiteException e) {

            Log.i("Database Operations", "Error creating table", e);

        }

    }



    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + USERS);
        onCreate(db);
    }

    @SuppressLint("Range")
    public User getUser(String username) {
        SQLiteDatabase db = getReadableDatabase();
        User user = null;
        String query = "SELECT * FROM " + USERS + " WHERE " + COLUMN_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor.moveToFirst()) {
            user = new User();
            user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            user.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESC)));
            user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            user.setFollowed(cursor.getInt(cursor.getColumnIndex(COLUMN_FOLL)) == 1);
        }
        cursor.close();
        //db.close();
        return user;
    }

//  Add a user record

    public User getrUser(int user_id) {
        SQLiteDatabase db = getReadableDatabase();
        User user = new User("johnny", "johndoe", 3, false);
        Cursor cursor = db.query(USERS, new String[] { COLUMN_NAME, COLUMN_DESC, COLUMN_ID, COLUMN_FOLL}, COLUMN_ID + "=?",
                new String[] { String.valueOf(user_id) }, null, null, null, null);

        if (cursor != null)
        {
            cursor.moveToFirst();
            int id = cursor.getInt((int)cursor.getColumnIndex("id"));
            String name = cursor.getString((int)cursor.getColumnIndex("name"));
            String description = cursor.getString((int)cursor.getColumnIndex("description"));
            boolean followed = Boolean.parseBoolean(cursor.getString((int)cursor.getColumnIndex("followed")));
            user.setId(id);
            user.setName(name);
            user.setDescription(description);
            user.setFollowed(followed);
            cursor.close();
        }
//      db.close();
        return user;
    }
    public ArrayList<User> getUsers() {

        SQLiteDatabase db = getWritableDatabase();

        ArrayList<User> userList = new ArrayList<>();

        String query = "SELECT * FROM " + USERS;

        Cursor cursor = db.rawQuery(query, null);



        while (cursor.moveToNext()) {

            int id = cursor.getInt((int)cursor.getColumnIndex("id"));

            String name = cursor.getString((int)cursor.getColumnIndex("name"));

            String description = cursor.getString((int)cursor.getColumnIndex("description"));

            String  followed = cursor.getString((int)cursor.getColumnIndex("followed"));

            User user = new User(name, description, id, Boolean.parseBoolean(followed));

            userList.add(user);

        }

        cursor.close();
        return userList;
    }



//  UPDATE user record

    public void updateUser(User user){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOLL,  user.getFollowed());
        String clause = "id=?";
        String[] args = {String.valueOf(user.getId())};
        db.update(USERS, values, clause, args);
    }
    @Override

    public void close() {
        Log.i("Database Operations", "Database is closed.");
        super.close();
    }
}
