package com.example.twiganator.merged_androidapp;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContentResolverCompat;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    public static boolean reminderBtnclicked = false;
    Button btnReminder;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    LocationInfo locationInfo_obj = new LocationInfo();
    CalculateDistance distance_obj = new CalculateDistance();
    GPSTracker gps;
    MessagesDatabase messages_obj = new MessagesDatabase(this);

    ArrayList<String> names = new ArrayList<String>();
    HashMap<String, String> contactsList = new HashMap<String, String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(getBaseContext(), MyService.class));
        contacts();

        // If no permission is allowed by user, then this will execute every time
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission) != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                messages_obj.sendMessages();

                if(DisturbActivity.full_minutes > 0){
                    try{
                        Thread.sleep(DisturbActivity.full_minutes * 60);
                        Log.d("DO NOT DISTURB IS ON", "DO NOT DISTURB IS ON");
//                        Toast.makeText(MainActivity.this, "Do not disturb is on", Toast.LENGTH_SHORT).show();

                    }catch (InterruptedException e){
                        Log.d("Error", "could not pause activities");
                    }
                    DisturbActivity.full_minutes = 0;
                }
            }

        },0, 10000);

//        btnReminder = (Button) findViewById(R.id.addReminder);
//        btnReminder.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//
//                gps = new GPSTracker(MainActivity.this);
//
//                // check if GPS enabled
//                if(gps.canGetLocation()){
//
//                    double latitude = gps.getLatitude();
//                    double longitude = gps.getLongitude();
//
//                    locationInfo_obj.setLat(latitude);
//                    locationInfo_obj.setLong(longitude);
//
//                    String msg = "Current Latitude: " + locationInfo_obj.getLat() + " Current Longitude: " + locationInfo_obj.getLong();
//                    Log.d("CURRENT LOCATION", msg);
//
//                    Double dist = distance_obj.distanceBetweenGeoPoints(locationInfo_obj.getLat(),locationInfo_obj.getLong(), 45.1125909,-86.2268336);
//
//                    RemindersDatabase obj_reminders = new RemindersDatabase(MainActivity.this);
//                    Map<String, String[]> results = obj_reminders.checkDatabase();
//
//                    if(results.isEmpty()){
//                        Log.d("EMPTY - main activity", "is empty");
//                    }
//
////                    Double dist = distance_obj.distanceBetweenGeoPoints(locationInfo_obj.getLat(),locationInfo_obj.getLat(),45.1125909,-86.2268336);
////
////                    if(dist < 400) {
////                        addNotification();
////                    }
//
//                    // show location button click event
////                    Toast.makeText(MainActivity.this, "Your Location is - \nLat: "
////                            + latitude + "\nLong: " + longitude, Toast.LENGTH_SHORT).show();
//
////                    distance_obj.distanceBetweenGeoPoints(latitude,longitude, locationInfo_obj.getLatitude(), locationInfo_obj.getLongitude());
//
////                    t.show();
////                    t.cancel();
//
////                    String s = "Your Location is - \nLat: " + latitude + "\nLong: " + longitude;
////                    Toast t = Toast.makeText(MainActivity.this, "Passwords don't match", Toast.LENGTH_LONG);
////                    t.show();
////                    t.cancel();
//                    Log.d("ReminderBTN", "TRUE");
//                    Intent i = new Intent(MainActivity.this, ReminderActivity.class);
//                    startActivity(i);
//
//
//                }else{
//
//                    // Ask user to enable GPS in settings
//                    gps.showSettingsAlert();
//                }
//            }
//        });
    }

    public void contacts(){

        ContentResolver cr = getContentResolver();
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
        while (cursor.moveToNext()) {
            try{
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor phones = getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, null, null);
                    while (phones.moveToNext()) {
                        String phoneNumber = phones.getString(phones.getColumnIndex( ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.d("NAME", name);
                        Log.d("NUMBER", phoneNumber);
                        this.names.add(name);
                        this.contactsList.put(name, phoneNumber);
                    }
                    phones.close();
                }
            }catch(Exception e){}
        }
    }

    public void onAddReminderClick(View v){
        if(v.getId() == R.id.addReminder){
            this.reminderBtnclicked = true;
            gps = new GPSTracker(MainActivity.this);

            // check if GPS enabled
            if(gps.canGetLocation()){

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                locationInfo_obj.setLat(latitude);
                locationInfo_obj.setLong(longitude);

                String msg = "Current Latitude: " + locationInfo_obj.getLat() + " Current Longitude: " + locationInfo_obj.getLong();
                Log.d("CURRENT LOCATION", msg);

//                Double dist = distance_obj.distanceBetweenGeoPoints(locationInfo_obj.getLat(),locationInfo_obj.getLong(), 45.1125909,-86.2268336);
//
//                RemindersDatabase obj_reminders = new RemindersDatabase(MainActivity.this);
//                Map<String, String[]> results = obj_reminders.checkDatabase();
//
//                if(results.isEmpty()){
//                    Log.d("EMPTY - main activity", "is empty");
//                }

//                    Double dist = distance_obj.distanceBetweenGeoPoints(locationInfo_obj.getLat(),locationInfo_obj.getLat(),45.1125909,-86.2268336);
//
//                    if(dist < 400) {
//                        addNotification();
//                    }

                // show location button click event
//                    Toast.makeText(MainActivity.this, "Your Location is - \nLat: "
//                            + latitude + "\nLong: " + longitude, Toast.LENGTH_SHORT).show();

//                    distance_obj.distanceBetweenGeoPoints(latitude,longitude, locationInfo_obj.getLatitude(), locationInfo_obj.getLongitude());

//                    t.show();
//                    t.cancel();

//                    String s = "Your Location is - \nLat: " + latitude + "\nLong: " + longitude;
//                    Toast t = Toast.makeText(MainActivity.this, "Passwords don't match", Toast.LENGTH_LONG);
//                    t.show();
//                    t.cancel();
                Log.d("ReminderBTN", "TRUE");
                Intent i = new Intent(MainActivity.this, ReminderActivity.class);
                startActivity(i);


            }else{

                // Ask user to enable GPS in settings
                gps.showSettingsAlert();
            }

        }


    }

    public void onClickShow(View v){
        if(v.getId() == R.id.showReminder){
            Intent i = new Intent(MainActivity.this, ShowRemindersActivity.class);
            startActivity(i);
        }
    }

    public void onClickMessenger(View v){
        if(v.getId() == R.id.messenger){
            Intent i = new Intent(MainActivity.this, SendMessageActivity.class);
            startActivity(i);
        }
    }

    public void onClickFriendReminder(View v){
        if(v.getId() == R.id.setFriendReminder){
            Intent i = new Intent(MainActivity.this, RemindFriendActivity.class);
            i.putExtra("names",this.names);
            i.putExtra("contactData", contactsList);
            startActivity(i);
        }
    }

    public void onClickShowFriendReminder(View v){
        if(v.getId() == R.id.showFriendReminder){
            Intent i = new Intent(MainActivity.this, ShowFriendRemindersActivity.class);
            startActivity(i);
        }
    }

    public void onClickTodoList(View v){
        if(v.getId() == R.id.todoLists){
            Intent i = new Intent(MainActivity.this, TodoListActivity.class);
            i.putExtra("names",this.names);
            i.putExtra("contactData", contactsList);
            startActivity(i);
        }
    }

    public void onClickDisturb(View v){
        if(v.getId() == R.id.disturb){
            Intent i = new Intent(MainActivity.this, DisturbActivity.class);
            startActivity(i);
        }
    }

    public void onClickStores(View v){
        if(v.getId() == R.id.stores){
            Intent i = new Intent(MainActivity.this, Nearby.class);
            startActivity(i);
        }
    }

}//class
