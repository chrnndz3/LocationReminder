package com.example.twiganator.merged_androidapp;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;


/**
 * Created by twiganator on 5/01/17.
 */

public class DisturbActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    DisturbDatabase disturbDatabase_obj = new DisturbDatabase(this);

    Button addStartTime;
    Button addEndTime;

    int hour, minute;
    public boolean s_boo = false;
    public boolean e_boo = false;
    public int s_hour = 0;
    public int e_hour = 0 ;
    public int s_minute = 0;
    public int e_minute = 0;

    public static long full_minutes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("in Disturb Activity","in Disturb Activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disturb);

        EditText show_Stime = (EditText) findViewById(R.id.startTime);
        show_Stime.setVisibility(View.GONE);

        EditText show_Etime = (EditText) findViewById(R.id.endTime);
        show_Etime.setVisibility(View.GONE);

        addStartTime = (Button) findViewById(R.id.addStartTime);
        addEndTime = (Button) findViewById(R.id.addEndTime);

        addStartTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                s_boo = true;
                Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(DisturbActivity.this, DisturbActivity.this, hour, minute, false);
                timePickerDialog.show();
            }
        });

        addEndTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                e_boo = true;
                Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(DisturbActivity.this, DisturbActivity.this, hour, minute, false);
                timePickerDialog.show();
            }
        });
    }

    /**
     * On clicking submit, reminder is added to database and an alert is presented
     * @param v
     */
    public void onSubmitClick(View v){
        if(v.getId() == R.id.submitButton){

            EditText start = (EditText)findViewById(R.id.startTime);
            String start_str = start.getText().toString();

            EditText end = (EditText)findViewById(R.id.endTime);
            String end_str = end.getText().toString();

            disturbDatabase_obj.insertInfo(start_str, end_str);
            Toast.makeText(DisturbActivity.this, "Added disturb times!", Toast.LENGTH_SHORT).show();
        }

        int h_val = Math.abs(s_hour - e_hour) * 60;
        int m_val = Math.abs(s_minute - e_minute);
        Log.d("Hours", Integer.toString(h_val));
        Log.d("MINUTES", Integer.toString(m_val));
        full_minutes = h_val + m_val;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String result = new String();
        String result2 = new String();

        if(s_boo == true){
            EditText s_time = (EditText) findViewById(R.id.startTime);
            s_hour = hourOfDay;
            s_minute = minute;
            result = Integer.toString(hourOfDay) + ":"+Integer.toString(minute);
            s_time.setText(result);
            s_time.setVisibility(View.VISIBLE);
            s_boo = false;
        }

        if(e_boo == true){
            EditText e_time = (EditText) findViewById(R.id.endTime);
            e_hour = hourOfDay;
            e_minute = minute;
            result2 = Integer.toString(hourOfDay) + ":"+Integer.toString(minute);
            e_time.setText(result2);
            e_time.setVisibility(View.VISIBLE);
            e_boo = false;
        }
    }
}

