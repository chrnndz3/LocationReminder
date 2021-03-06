package com.example.twiganator.merged_androidapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Button;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by vaish on 4/25/2017.
 */

/**
 * Settings reminders for friends
 */
public class RemindFriendActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Button addDate;
    Button addTime;

    int day, month, year, hour, minute;

    AutoCompleteTextView text;
    ArrayList<String> names = new ArrayList<String>();
    HashMap<String, String> contactsList = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remindfriend);

        EditText dateTextBox = (EditText) findViewById(R.id.date);
        dateTextBox.setVisibility(View.GONE);

        EditText time = (EditText) findViewById(R.id.time);
        time.setVisibility(View.GONE);

        addDate = (Button) findViewById(R.id.addDate);
        addTime = (Button) findViewById(R.id.addTime);

        text=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
        Intent in = getIntent();
        names =(ArrayList<String>) in.getSerializableExtra("names");
        contactsList =(HashMap<String, String>) in.getSerializableExtra("contactData");
        ArrayAdapter adapter = new
                ArrayAdapter(this,android.R.layout.simple_list_item_1,names);

        text.setAdapter(adapter);
        text.setThreshold(1);

        //shows the date picker
        addDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(RemindFriendActivity.this, RemindFriendActivity.this, year,month,day);
                datePickerDialog.show();
            }
        });

        //shows the time picker
        addTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(RemindFriendActivity.this, RemindFriendActivity.this, hour, minute, false);
                timePickerDialog.show();
            }
        });
    }

    /**
     * Saves the dates picked by the user
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
     * Saves the time picked by the user
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

    /**
     * Adding the reminder to the database
     * @param v
     */
    public void onSubmitClick(View v) {
        if (v.getId() == R.id.submitButton) {
//            EditText subj = (EditText)findViewById(R.id.number);
//            String number_Str = subj.getText().toString();

            String number_Str  = "";
            String contact_name = text.getText().toString();
            for(String key : contactsList.keySet()){
                if(key.equalsIgnoreCase(contact_name)){
                    number_Str = contactsList.get(key);
                    Log.d("NUMBER",number_Str);
                }
            }

            EditText rad = (EditText)findViewById(R.id.reminderMessage);
            String message_str = rad.getText().toString();

            EditText date = (EditText) findViewById(R.id.date);
            String date_str = date.getText().toString();

            EditText time = (EditText) findViewById(R.id.time);
            String time_str = time.getText().toString();

            MessagesDatabase obj_database = new MessagesDatabase(this);

            if(date_str.matches("")){
                date_str = "EMPTY";
                Log.d("DATE IS EMPTY", "FOUND");
            }

            if(time_str.matches("")){
                time_str = "EMPTY";
                Log.d("TIME IS EMPTY", "FOUND");
            }

            if(number_Str == ""){
                number_Str = "2179040244";
            }
            obj_database.insertInfo(number_Str, message_str, date_str, time_str);
//            obj_database.deleteDatabase();

            Toast.makeText(RemindFriendActivity.this, "Added reminder!", Toast.LENGTH_SHORT).show();
        }
    }
}