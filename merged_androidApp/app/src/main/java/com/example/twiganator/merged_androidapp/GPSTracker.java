package com.example.twiganator.merged_androidapp;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Handler;

import com.example.twiganator.merged_androidapp.MainActivity;

public class GPSTracker extends Service implements LocationListener {

    LocationInfo locationInfo_obj = new LocationInfo();
    CalculateDistance distance_obj = new CalculateDistance();
    MainActivity main_obj = new MainActivity();

    private final Context mContext;
    boolean isGPSEnabled = false; // flag for GPS status
    boolean isNetworkEnabled = false; // flag for network status
    boolean canGetLocation = false; // flag for GPS status

    Location location;
    double latitude;
    double longitude;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {

            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); // getting GPS status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER); // getting network status

            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.d("Error", "no network provider is enabled");
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    try {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    }catch (SecurityException e){
                        Log.d("error", "cannot enable network");
                    }

                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        try{
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }catch(SecurityException e){
                            Log.d("error", "cannot enable network");
                        }

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

//                            String msg = "Current Latitude: " + latitude
//                                    + "Current Longitude: " + longitude;
//
//                            Log.d("CURRENT LOCATION", msg);
                        }
                    }
                }

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        try {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        }catch (SecurityException e){
                            Log.d("error", "cannot enable gps");
                        }

                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            try{
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            }catch (SecurityException e){
                                Log.d("error", "cannot enable gps");
                            }


                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

//                                String msg = "Current Latitude: " + latitude
//                                        + "Current Longitude: " + longitude;
//
//                                Log.d("CURRENT LOCATION", msg);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        return longitude;
    }

//    public void setLatitude(Double lat){
//        this.latitude = lat;
//    }
//
//    public void setLongitude(Double lon){
//        this.longitude = lon;
//    }

    /**
     * Function to check GPS enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        String str = String.valueOf(MainActivity.reminderBtnclicked);
        Log.d("REMINDER BOOLEAN", str);
        //if(main_obj.getReminderBtnclicked() != true){
            String msg = "New Latitude: " + location.getLatitude()
                    + " New Longitude: " + location.getLongitude();

//        location.setLatitude(location.getLatitude());
//        location.setLatitude(location.getLongitude());

            Log.d("NEW LOCATION --- ", msg);


            RemindersDatabase obj_reminders = new RemindersDatabase(this.mContext);
            Map<String, String[]> results = obj_reminders.checkDatabase();

            if(results.isEmpty()){
                Log.d("DATABASE IS EMPTY", "is empty");
            }
            else{

                for (Map.Entry<String, String[]> entry : results.entrySet()) {
                    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                    String [] values = entry.getValue();
                    String subject = entry.getKey();

                    Integer reminderID = Integer.parseInt(values[7]);
                    Double latval = Double.parseDouble(values[3]);
                    Double lonval = Double.parseDouble(values[4]);
                    Integer radiusval = Integer.parseInt(values[2]);
                    String reminders = values[1];

                    Log.d("REMINDER ID IN LOCATION", String.valueOf(reminderID));
                    Double dist = distance_obj.distanceBetweenGeoPoints(location.getLatitude(),location.getLongitude(), latval, lonval);

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                    Date date = new Date();
                    String dateTime = dateFormat.format(date);
                    String currentDate = dateTime.substring(0,dateTime.indexOf(" "));
                    String currentTime = dateTime.substring(dateTime.indexOf(" ")+1);


                    if(dist <= radiusval || (values[5].equals(currentDate) && values[6].equals(currentTime))){

                        try{
                            Thread.sleep(5000);


                        }catch (InterruptedException e){
                            Log.d("wait", "wait");
                        }

                        Notifications notifications_obj = new Notifications(this.mContext);
                        notifications_obj.sendNotification(reminderID, subject, reminders, this.mContext);
                    }
                }
            }
            MainActivity.reminderBtnclicked = false;
            Log.d("REMINDER BOOLEAN FALSE", String.valueOf(MainActivity.reminderBtnclicked));
        }


    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
