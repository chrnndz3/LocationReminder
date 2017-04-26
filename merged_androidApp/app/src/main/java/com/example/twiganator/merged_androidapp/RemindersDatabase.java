package com.example.twiganator.merged_androidapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.games.appcontent.AppContentTuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by twiganator on 4/11/17.
 */

public class RemindersDatabase extends SQLiteOpenHelper {

    private static final int DATA_VERSION= 1;
    private static final String DATABASE_NAME = "app.db";
    private static final String TABLE_NAME = "reminders";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_REMINDERS = "reminders";
    private static final String COLUMN_RADIUS = "radius";
    private static final String COLUMN_SUBJECT = "subject";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TIME = "time";

    SQLiteDatabase db;

    private static final String TABLE_CREATE = "create table reminders (id integer primary key not null , " +
            "address text not null, reminders text not null, radius text not null, subject text not null,date text not null,time text not null );";

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

        Log.d("ADDRESS", info.getAddress());
        Log.d("REMINDERS", info.getReminders());
        Log.d("RADIUS", info.getRadius());
        Log.d("SUBJECT", info.getSubject());
        Log.d("LAT",info.getLat());
        Log.d("LON",info.getLon());
        Log.d("DATE", info.getDate());
        Log.d("TIME", info.getTime());

        //latitude and longitude are appended to the address itself
        String address = info.getAddress() + "|"+info.getLat()+";"+info.getLon();

        Log.d("ADDRESS",address);
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_REMINDERS, info.getReminders());
        values.put(COLUMN_RADIUS, info.getRadius());
        values.put(COLUMN_SUBJECT, info.getSubject());
        values.put(COLUMN_DATE, info.getDate());
        values.put(COLUMN_TIME, info.getTime());

        db.insert(TABLE_NAME, null, values);
//        String ans = checkDatabase();
        db.close();
    }

    /**
     * Gets reminders from database and shows on screen
     * @return
     */
    public Map checkDatabase(){
        Map<String, String[]> map = new HashMap<String, String[]>();
        Log.d("INSIDE DATABSE", "INSIDE");
        db = this.getReadableDatabase();
//        String tableString = String.format("Table %s:\n", TABLE_NAME);

        Cursor allRows  = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

//        if (allRows.moveToFirst() ){
//            String[] columnNames = allRows.getColumnNames();
//            do {
//                for (String name: columnNames) {
//                    tableString += String.format("%s: %s\n", name,
//                            allRows.getString(allRows.getColumnIndex(name)));
//                }
//                tableString += "\n";
//
//            } while (allRows.moveToNext());
//        }
        if(allRows != null){
            if(allRows.moveToFirst()){
                do{
                    String address = allRows.getString(allRows.getColumnIndex("address"));
                    String reminders = allRows.getString(allRows.getColumnIndex("reminders"));
                    String radius = allRows.getString(allRows.getColumnIndex("radius"));
                    String subject = allRows.getString(allRows.getColumnIndex("subject"));
                    String time = allRows.getString(allRows.getColumnIndex("time"));
                    String date = allRows.getString(allRows.getColumnIndex("date"));
                    if(address.indexOf("|") != -1) {
                        String latlon = address.substring(address.indexOf("|") + 1);
                        if (latlon.indexOf(";") != -1) {
                            String lat = latlon.substring(0, latlon.indexOf(";"));
                            String lon = latlon.substring(latlon.indexOf(";") + 1);
                            address = address.substring(0, address.indexOf("|"));
                            String[] value = {reminders, address, radius, lat, lon, date, time};
                            map.put(subject, value);
                        }
                    }
                }while (allRows.moveToNext());
            }
        }

//        Log.d("DATABASE", tableString);
//        this.distance();
//        return tableString;
        return map;
    }

    /**
     * clears the database
     */
    public void deleteDatabase(){
        db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
    }

    /**
     * calculates distance between each location from database to the current location
     */
    public void distance() {
        db = this.getWritableDatabase();
        String tableString = String.format("Table %s:\n", TABLE_NAME);
        Cursor allRows = db.rawQuery("SELECT address FROM " + TABLE_NAME, null);
        if (allRows.moveToFirst()) {
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name : columnNames) {
                    String address =  allRows.getString(allRows.getColumnIndex(name));
                    if(address.indexOf("|") != -1){
                        String latlon = address.substring(address.indexOf("|")+1);
                        if(latlon.indexOf(";") != -1){
                            String lat = latlon.substring(0,latlon.indexOf(";"));
                            String lon = latlon.substring(latlon.indexOf(";")+1);

                            Double latval = Double.parseDouble(lat);
                            Double lonval = Double.parseDouble(lon);
                            CalculateDistance obj_distance = new CalculateDistance();
                            obj_distance.distanceBetweenGeoPoints(latval,lonval,40.1125909,-88.2268336);
                        }
                    }
                }
            } while (allRows.moveToNext());
        }
    }

    public Map getFullAddresses(){
        Map<String, String[]> map = new HashMap<String, String[]>();
        Log.d("INSIDE DATABSE", "INSIDE");
        db = this.getReadableDatabase();

        Cursor allRows  = db.rawQuery("select address, reminders from" + TABLE_NAME, null);

        if(allRows != null){
            if(allRows.moveToFirst()){
                do{
                    String address = allRows.getString(allRows.getColumnIndex("address"));

                    if(address.indexOf("|") != -1) {
                        String latlon = address.substring(address.indexOf("|") + 1);
                        if (latlon.indexOf(";") != -1) {
                            String lat = latlon.substring(0, latlon.indexOf(";"));
                            String lon = latlon.substring(latlon.indexOf(";") + 1);
                            String radius = allRows.getString(allRows.getColumnIndex("radius"));
                            address = address.substring(0, address.indexOf("|"));
                            String[] value = {lat, lon, radius};
                            map.put(address, value);
                        }
                    }
                }while (allRows.moveToNext());
            }
        }

        return map;
    }

}//class
