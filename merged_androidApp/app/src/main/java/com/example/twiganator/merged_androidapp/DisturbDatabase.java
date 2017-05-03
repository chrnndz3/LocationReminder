package com.example.twiganator.merged_androidapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.SmsManager;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by twiganator on 5/1/17.
 */

public class DisturbDatabase extends SQLiteOpenHelper {
    private static final int DATA_VERSION= 1;
    private static final String DATABASE_NAME = "disturb.db";
    private static final String TABLE_NAME = "doNotDisturb";
    private static final String COLUMN_START_TIME = "start_time";
    private static final String COLUMN_END_TIME = "end_time";
    SQLiteDatabase db;

    private static final String TABLE_CREATE = "create table doNotDisturb (id integer primary key not null , " +
            "start_time text not null, end_time text not null );";

    public DisturbDatabase(Context context){

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


    public void insertInfo(String startTime, String endTime){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Log.d("START TIME", startTime);
        Log.d("END TIME", endTime);

        values.put(COLUMN_START_TIME, startTime);
        values.put(COLUMN_END_TIME, endTime);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }
}
