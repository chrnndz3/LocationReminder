package com.example.twiganator.merged_androidapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.test.mock.MockPackageManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    Button btnShowLocation;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    LocationInfo locationInfo_obj = new LocationInfo();
    CalculateDistance distance_obj = new CalculateDistance();

    // GPSTracker class
    GPSTracker gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

//                // If any permission above not allowed by user, this condition will
//                execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnShowLocation = (Button) findViewById(R.id.button);

        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(MainActivity.this);

                // check if GPS enabled
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // \n is for new line
                    Toast.makeText(MainActivity.this, "Your Location is - \nLat: "
                            + latitude + "\nLong: " + longitude, Toast.LENGTH_SHORT).show();

//                    distance_obj.distanceBetweenGeoPoints(latitude,longitude, locationInfo_obj.getLatitude(), locationInfo_obj.getLongitude());

//                    t.show();
//                    t.cancel();

//                    String s = "Your Location is - \nLat: " + latitude + "\nLong: " + longitude;
//                    Toast t = Toast.makeText(MainActivity.this, "Passwords don't match", Toast.LENGTH_LONG);
//                    t.show();
//                    t.cancel();


                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

            }
        });
    }

    public void onAddReminderClick(View v){
        if(v.getId() == R.id.addReminder){
            Intent i = new Intent(MainActivity.this, ReminderActivity.class);
            startActivity(i);

        }
    }

    public void onClickShow(View v){
        if(v.getId() == R.id.showReminder){
            Intent i = new Intent(MainActivity.this, ShowRemindersActivity.class);
            startActivity(i);
        }
    }
}
