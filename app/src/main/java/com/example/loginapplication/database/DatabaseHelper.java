package com.example.loginapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import com.example.loginapplication.model.User;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "UserManager.db";

    //User Table Name
    private static final String TABLE_USER = "user";

    //User Table Columns Names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    //Create Table SQL Query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "(" +
            COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + "TEXT," +
            COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";

    //Drop Table SQL Query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    //Constructor With "Context" Param
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop Table If Exist
        db.execSQL(DROP_USER_TABLE);

        //Create Table again
        onCreate(db);
    }

    // This method is to create user record (user param)
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_NAME, user.getName());
        contentValues.put(COLUMN_USER_EMAIL, user.getEmail());
        contentValues.put(COLUMN_USER_PASSWORD, user.getPassword());

        db.insert(TABLE_USER, null, contentValues);
        db.close();
    }

    public User Authenticate(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER,
                new String[]{COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_USER_EMAIL, COLUMN_USER_PASSWORD}, COLUMN_USER_EMAIL + "=?", new String[]{user.getEmail()}, null, null, null);

        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            User firstUser = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            if (user.getPassword().equalsIgnoreCase(firstUser.getPassword())) {
                return firstUser;
            }
        }
        return null;
    }

    // This method to update user record
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(user.getId())});
        db.close();
    }

    //This method is to delete user record
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(user.getId())});
        db.close();
    }

    //This method check if the user exist or not
    public boolean isEmailExists(String email) {
        String[] columns = {COLUMN_USER_ID};
        SQLiteDatabase db = this.getReadableDatabase();

        // Selection Criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // Selection argument
        String[] selectionArgument = {email};

        // Query user table with condition
        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgument, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    // This method to check user exist or not

    public boolean checkUser(String email, String password) {
        String[] columns = {COLUMN_USER_ID};
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?" + COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgument = {email, password};

        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgument, null, null, null);

        int countCursor = cursor.getCount();
        cursor.close();
        db.close();

        if (countCursor > 0) {
            return true;
        }
        return false;
    }
}
