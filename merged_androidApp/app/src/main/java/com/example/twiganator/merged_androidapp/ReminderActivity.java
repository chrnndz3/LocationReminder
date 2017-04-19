package com.example.twiganator.merged_androidapp;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import android.content.Intent;

import android.widget.Toast;

/**
 * Created by twiganator on 4/18/17.
 */

public class ReminderActivity extends AppCompatActivity {

    RemindersDatabase remindersDatabase_obj = new RemindersDatabase(this);
    CalculateDistance distance_obj = new CalculateDistance();
    LocationInfo locationInfo_obj = new LocationInfo();
    UserInputs userInputs_obj = new UserInputs();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        EditText location = (EditText) findViewById(R.id.location);
        location.setVisibility(View.GONE);
    }

    public void getLocation(View v){
        Log.d("HERE", "HERE");
        if(v.getId() == R.id.addAddress){

            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(builder.build(this), 1);
            } catch (GooglePlayServicesRepairableException e) {
                Log.d("inside exception", "INSIDE");
            } catch (GooglePlayServicesNotAvailableException e) {
                Log.d("inside exception", "INSIDE");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                Button addAddress = (Button) findViewById(R.id.addAddress);
                addAddress.setVisibility(View.INVISIBLE);

                EditText location = (EditText) findViewById(R.id.location);
                location.setText(place.getAddress());
                location.setVisibility(View.VISIBLE);

                String placename = String.format("%s", place.getName());
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                String address = String.format("%s", place.getAddress());
                Log.d("Place Name:", placename);
                Log.d("Latitude:", latitude);
                Log.d("Longitude", longitude);
                Log.d("Address:", address);
                String lan_lon = latitude + ":" + longitude;

                locationInfo_obj.setLatitude(place.getLatLng().latitude);
                locationInfo_obj.setLongitude(place.getLatLng().latitude);

//                Log.d("GETTING CURRENT LOCTION", "LOCATION");
//                Log.d("GET Lat", locationInfo_obj.getLatitude());
//                Log.d("GET Lon", locationInfo_obj.getLongitude());
//                distance_obj.distanceBetweenGeoPoints(Double.parseDouble(locationInfo_obj.getLatitude()),Double.parseDouble(locationInfo_obj.getLongitude()),
//                        Double.parseDouble(latitude),Double.parseDouble(longitude));
            }
        }
    }

    public void onSubmitClick(View v){
        if(v.getId() == R.id.submitButton){

            EditText subj = (EditText)findViewById(R.id.subject);
            String subj_str = subj.getText().toString();

            EditText rad = (EditText)findViewById(R.id.radius);
            String rad_str = rad.getText().toString();

            EditText reminderMess = (EditText)findViewById(R.id.reminderMessage);
            String reminderMess_str = reminderMess.getText().toString();

            EditText address = (EditText)findViewById(R.id.location);
            String address_str = address.getText().toString();

            //Inserting info into the database
            UserInputs userInfo_obj = new UserInputs();
            userInfo_obj.setAddress(address_str);
            userInfo_obj.setSubject(subj_str);
            userInfo_obj.setRadius(rad_str);
            userInfo_obj.setReminders(reminderMess_str);

            remindersDatabase_obj.insertInfo(userInfo_obj);
//            remindersDatabase_obj.deleteDatabase();
            Toast.makeText(ReminderActivity.this, "Added reminder!", Toast.LENGTH_SHORT).show();
        }
    }
}

