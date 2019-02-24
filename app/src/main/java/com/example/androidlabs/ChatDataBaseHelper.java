package com.example.androidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.androidlabs.ChatRoomActivity;


    public class ChatDataBaseHelper extends SQLiteOpenHelper {


        private SQLiteDatabase database;
        public static final String DATABASE_NAME = "ChatDB";
        public static final int VERSION_NUM = 1;
        public static final String TABLE_NAME = "MessageTable";

        public static final String COL_ID = "id";
        public static final String COL_MESSAGE = "message";
        public static final String COL_ROLE = "role";






        public ChatDataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, VERSION_NUM);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                    COL_ID + " INTEGER 	PRIMARY KEY AUTOINCREMENT, " +
                    COL_MESSAGE + " TEXT, " + COL_ROLE + " TEXT )");



        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("Database upgrade", "Old version: " + oldVersion + " New version: " + newVersion);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            database = db;
        }

        //insert message into DB
        public void insertMessage(String message, String role) {
            ContentValues values = new ContentValues();
            values.put(COL_MESSAGE, message);
            values.put(COL_ROLE, role);
            database.insert(TABLE_NAME, null, values);
            Log.d("DATABASE INSERT DATA","=="+ role);
        }


        // read message from BD with role
        public Cursor getMessages(){


            return database.query(false, TABLE_NAME,null,null,null,null,null,null,null) ;
        }
    }

