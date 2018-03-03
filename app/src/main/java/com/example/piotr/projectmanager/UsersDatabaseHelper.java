package com.example.piotr.projectmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.util.Log;

/**
 * Created by Piotr on 03.03.2018.
 */

public class UsersDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "usersDatabase";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";

    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_PASSWORD = "password";

    private static UsersDatabaseHelper sInstance;

    public static synchronized UsersDatabaseHelper getsInstance(Context context){
        if(sInstance == null){
            sInstance = new UsersDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public UsersDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void  onConfigure(SQLiteDatabase db){
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY," +
                KEY_USER_NAME + " TEXT," +
                KEY_USER_PASSWORD + "TEXT" +
                ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion != newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        }
    }
    public String getPassword(User user){
        User selectUser = new User();

        String USER_SELECT_PASSWORD =
                String.format("SELECT %s FROM %s WHERE %s",
                        KEY_USER_PASSWORD,
                        TABLE_USERS,
                        user.uName);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(USER_SELECT_PASSWORD,null);
        try{
            if(cursor.moveToFirst()){
                User newUser = new User();
                newUser.uName = cursor.getString(cursor.getColumnIndex(KEY_USER_NAME));
                newUser.uPassword = cursor.getString(cursor.getColumnIndex(KEY_USER_PASSWORD));
                selectUser = newUser;
            }
        }catch (Exception e){
            Log.d("Database","Error while trying to get from database");
        }finally {
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return selectUser.uPassword;
    }
    //tutaj dodac dodawanie nowego projektu/taskow do bazy

    public long addOrUpdateUser(User user){
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1;
        db.beginTransaction();
        try{
            ContentValues values = new ContentValues();
            values.put(KEY_USER_NAME, user.uName);
            values.put(KEY_USER_PASSWORD, user.uPassword);

            int rows = db.update(TABLE_USERS,values,KEY_USER_NAME + "=?",new String[]{user.uName});

            if(rows == 1){
                String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_USER_ID,TABLE_USERS,KEY_USER_NAME
                );
             Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(user.uName)});
             try{
                 if(cursor.moveToFirst()){
                     userId = cursor.getInt(0);
                     db.setTransactionSuccessful();
                 }
             }finally {
                 if(cursor != null && !cursor.isClosed()){
                     cursor.close();
                 }
             }
            } else {
                userId = db.insertOrThrow(TABLE_USERS,null,values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e){
            Log.d("DATABASE", "Error while trying to add update user");
        }finally {
            db.endTransaction();
        }
        return userId;
    }
}
