package com.example.twiganator.merged_androidapp;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Switch;
import android.widget.CompoundButton;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import android.content.Intent;

import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;
import java.util.*;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * Created by twiganator on 4/18/17.
 */

/**
 * Reminder Activity Java Class
 */
public class ReminderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    RemindersDatabase remindersDatabase_obj = new RemindersDatabase(this);
    CalculateDistance distance_obj = new CalculateDistance();
    LocationInfo locationInfo_obj = new LocationInfo();
    UserInputs userInputs_obj = new UserInputs();
    public String lat;
    public String lon;
    Switch mySwitch;
    boolean recur;

    Button addDate;
    Button addTime;

    int day, month, year, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        EditText location = (EditText) findViewById(R.id.location);
        location.setVisibility(View.GONE);

        EditText dateTextBox = (EditText) findViewById(R.id.date);
        dateTextBox.setVisibility(View.GONE);

        EditText time = (EditText) findViewById(R.id.time);
        time.setVisibility(View.GONE);

        addDate = (Button) findViewById(R.id.addDate);
        addTime = (Button) findViewById(R.id.addTime);

        mySwitch = (Switch) findViewById(R.id.mySwitch);

        //set the switch to ON
        mySwitch.setChecked(true);
        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                Log.v("Switch State=", ""+isChecked);
                if(isChecked){
                    setTrue();
                }
                else{
                    setFalse();
                }
            }
        });

        //Shows the date picker widget
        addDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ReminderActivity.this, ReminderActivity.this, year,month,day);
                datePickerDialog.show();
            }
        });

        //shows the time picker widgets
        addTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(ReminderActivity.this, ReminderActivity.this, hour, minute, false);
                timePickerDialog.show();
            }
        });
    }

    public void setTrue(){
        Log.d("INSIDE SET TRUE","TRUE");
        this.recur = true;
    }

    public void setFalse(){
        Log.d("INSIDE SET FALSE","FALSE");
        this.recur = false;
    }

    /**
     * Shows the Place Picker widget
     * @param v
     */
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

    /**
     * Shows the selected place by the user; all information about the location is provided
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
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

                this.lat = latitude;
                this.lon = longitude;

            }
        }
    }

    /**
     * On clicking submit, reminder is added to database and an alert is presented
     * @param v
     */
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

            EditText date = (EditText) findViewById(R.id.date);
            String date_str = date.getText().toString();

            EditText time = (EditText) findViewById(R.id.time);
            String time_str = time.getText().toString();

            //Inserting info into the database
            UserInputs userInfo_obj = new UserInputs();
            userInfo_obj.setAddress(address_str);
            userInfo_obj.setLat(lat);
            userInfo_obj.setLon(lon);
            userInfo_obj.setSubject(subj_str);
            userInfo_obj.setRadius(rad_str);
            userInfo_obj.setReminders(reminderMess_str);
            userInfo_obj.setDate(date_str);
            userInfo_obj.setTime(time_str);

            //remindersDatabase_obj.insertInfo(userInfo_obj);
            remindersDatabase_obj.deleteDatabase();
            Toast.makeText(ReminderActivity.this, "Added reminder!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Saves the date picked by the user and sets it to a text box
     * @param view
     * @param year
     * @param month
     * @param dayOfMonth
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.d("YEAR", Integer.toString(year));
        Log.d("MONTH", Integer.toString(month));
        Log.d("DAY", Integer.toString(dayOfMonth));

        EditText date = (EditText) findViewById(R.id.date);
        String result = Integer.toString(month+1) + "/"+Integer.toString(dayOfMonth)+"/"+Integer.toString(year);
        date.setText(result);
        date.setVisibility(View.VISIBLE);
    }

    /**
     * Saves the time picked by the user and sets it to a text box
     * @param view
     * @param hourOfDay
     * @param minute
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.d("HOUR", Integer.toString(hourOfDay));
        Log.d("MINUTE", Integer.toString(minute));

        EditText time = (EditText) findViewById(R.id.time);
        String result = Integer.toString(hourOfDay) + ":"+Integer.toString(minute);
        time.setText(result);
        time.setVisibility(View.VISIBLE);
    }
}