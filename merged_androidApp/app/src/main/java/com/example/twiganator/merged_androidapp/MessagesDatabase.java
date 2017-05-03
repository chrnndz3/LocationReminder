package com.example.twiganator.merged_androidapp;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by vaish on 4/25/2017.
 */

public class MessagesDatabase extends SQLiteOpenHelper {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private static final int DATA_VERSION= 1;
    private static final String DATABASE_NAME = "messages.db";
    private static final String TABLE_NAME = "reminder_friends";
    private static final String COLUMN_CONTACT = "contact";
    private static final String COLUMN_MESSAGE = "message";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TIME = "time";
    SQLiteDatabase db;

    private static final String TABLE_CREATE = "create table reminder_friends (id integer primary key not null , " +
            "contact text not null, message text not null,date text not null,time text not null );";

    public MessagesDatabase(Context context){
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


    public void insertInfo(String contact, String message, String date, String time){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

//        Log.d("CONTACT", contact);
//        Log.d("MESSAGE", message);
//        Log.d("DATE", date);
//        Log.d("TIME", time);

        values.put(COLUMN_CONTACT, contact);
        values.put(COLUMN_MESSAGE, message);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    /**
     * Gets reminders from database and shows on screen
     * @return
     */
    public ArrayList<String[]> checkDatabase(){
//        Log.d("CHECKING DATABSE", "INSIDE");

        db = this.getWritableDatabase();
        Cursor allRows  = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        ArrayList<String[]> results = new ArrayList<String[]>();

        if(allRows != null){
            if(allRows.moveToFirst()){
                do{
                    String contact = allRows.getString(allRows.getColumnIndex("contact"));
                    String message = allRows.getString(allRows.getColumnIndex("message"));
                    String time = allRows.getString(allRows.getColumnIndex("time"));
                    String date = allRows.getString(allRows.getColumnIndex("date"));
                    String[] value = {contact, message, date, time};
                    results.add(value);
                }while (allRows.moveToNext());
            }
        }
        return results;
    }

    public void sendMessages(){
        db = this.getWritableDatabase();
//        Log.d("INSIDE SENDMESSAGES", "RETRIEVING RECORDS");
        ArrayList<String[]> results = this.checkDatabase();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date date = new Date();
        String dateTime = dateFormat.format(date);
        String currentDate = dateTime.substring(0,dateTime.indexOf(" "));
        String currentTime = dateTime.substring(dateTime.indexOf(" ")+1);

//        Log.d("CURRENT DATE",currentDate);
//        Log.d("CURRENT TIME",currentTime);

        for(String[] value : results){
//            Log.d("INSIDE FOR LOOP", value[0]);
//            Log.d("DATE",value[2]);
//            Log.d("TIME", value[3]);
            if(value[2].equals(currentDate)  || value[2].equalsIgnoreCase("EMPTY")){
                if(value[3].equals(currentTime) || value[3].equalsIgnoreCase("EMPTY")){
                    Log.d("FOUND MATCH CONDITION",value[2]);
//                    SendMessageActivity send_obj = new SendMessageActivity();
//                    send_obj.phoneNo = value[0];
//                    send_obj.message = value[1];
//                    send_obj.sendSMSMessage();
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(value[0], null, value[1], null, null);

//                    String sql = "DELETE FROM " + TABLE_NAME + " WHERE contact = "+value[0]+" and message = "+value[1]+" and time = "+value[3]+" and date = "+value[2];
//                    db.rawQuery(sql, null);

                    db.delete(TABLE_NAME,
                            COLUMN_CONTACT + "=? AND " + COLUMN_MESSAGE + "=? AND " +
                                    COLUMN_DATE + "=? AND " + COLUMN_TIME + "=?",
                            new String[] {value[0], value[1],value[2], value[3] });

                }
            }
        }

    }

    /**
     * clears the database
     */
    public void deleteDatabase(){
        db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
    }

}
