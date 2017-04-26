package com.example.twiganator.merged_androidapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Button;

import java.util.Calendar;

/**
 * Created by vaish on 4/25/2017.
 */

public class RemindFriendActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Button addDate;
    Button addTime;

    int day, month, year, hour, minute;

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

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.d("HOUR", Integer.toString(hourOfDay));
        Log.d("MINUTE", Integer.toString(minute));

        EditText time = (EditText) findViewById(R.id.time);
        String result = Integer.toString(hourOfDay) + ":"+Integer.toString(minute);
        time.setText(result);
        time.setVisibility(View.VISIBLE);
    }


    public void onSubmitClick(View v) {
        if (v.getId() == R.id.submitButton) {
            EditText subj = (EditText)findViewById(R.id.number);
            String number_Str = subj.getText().toString();

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

            obj_database.insertInfo(number_Str, message_str, date_str, time_str);
//            obj_database.deleteDatabase();

            Toast.makeText(RemindFriendActivity.this, "Added reminder!", Toast.LENGTH_SHORT).show();
        }
    }
}
