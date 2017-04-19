package com.example.twiganator.merged_androidapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by twiganator on 4/11/17.
 */

public class RemindersDatabase extends SQLiteOpenHelper {

    private static final int DATA_VERSION= 1;
    private static final String DATABASE_NAME = "main.db";
    private static final String TABLE_NAME = "reminders_table";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_REMINDERS = "reminders";
    private static final String COLUMN_RADIUS = "radius";
    private static final String COLUMN_SUBJECT = "subject";
    SQLiteDatabase db;

    private static final String TABLE_CREATE = "create table reminders_table (id integer primary key not null , " +
            "address text not null, reminders text not null, radius text not null, subject text not null);";

    public RemindersDatabase(Context context){
        super(context, DATABASE_NAME, null, DATA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(TABLE_CREATE);
        this.db = db;
    }

    @Override
    public  void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(query);
        this.onCreate(db);
    }

    /**
     * This function will only be called from the Reminder form
     * Inserting user's information into the database
     * @param info
     */
    public void insertInfo(UserInputs info){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ADDRESS, info.getAddress());
        values.put(COLUMN_REMINDERS, info.getReminders());
        values.put(COLUMN_RADIUS, info.getRadius());
        values.put(COLUMN_SUBJECT, info.getSubject());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    /**
     * @param address
     * @return
     */
//    public String searchReminders(String address){
//        db = this.getReadableDatabase();
//        String query = "select address, reminders from " + TABLE_NAME; //fetching the values
//        Cursor cursor = db.rawQuery(query, null); //it allows to read and write from the database
//
//        String getAddress;
//        String getReminders = "Reminders not found";
//
//        if(cursor.moveToFirst()){
//            do{
//                getAddress= cursor.getString(0);
//
//                if(getAddress.equals(address)){
//                    getReminders = cursor.getString(1);
//                    break;
//                }
//            }while(cursor.moveToNext());
//        }
//        return getReminders;
//    }
}
