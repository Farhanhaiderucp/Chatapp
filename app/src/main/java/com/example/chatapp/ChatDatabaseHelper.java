package com.example.chatapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChatDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "chat.db";
    private static final int DATABASE_VERSION = 1;

    public static final String USER_TABLE = "users";
    public static final String MESSAGE_TABLE = "messages";

    public static final String USER_ID = "id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public static final String MESSAGE_ID = "id";
    public static final String SENDER = "sender";
    public static final String RECEIVER = "receiver";
    public static final String MESSAGE = "message";
    public static final String TIMESTAMP = "timestamp";

    public ChatDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE + " (" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USERNAME + " TEXT, " +
                PASSWORD + " TEXT)";
        String CREATE_MESSAGE_TABLE = "CREATE TABLE " + MESSAGE_TABLE + " (" +
                MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SENDER + " TEXT, " +
                RECEIVER + " TEXT, " +
                MESSAGE + " TEXT, " +
                TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP)";

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_MESSAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MESSAGE_TABLE);
        onCreate(db);
    }
}
