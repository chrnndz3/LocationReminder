package com.example.twiganator.merged_androidapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import java.util.List;

import java.util.Arrays;

/**
 * Created by vaish on 4/25/2017.
 */

public class TodoListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);
    }

    public void onSubmitClick(View v){
        EditText numbers = (EditText) findViewById(R.id.number);
        EditText list = (EditText) findViewById(R.id.reminderMessage);

        String numbers_str = numbers.getText().toString();
        String list_str = list.getText().toString();

        if(numbers_str.contains(","))
        {
            List<String> allNumbers = Arrays.asList(numbers_str.split(","));
            if(allNumbers.size() > 0 ){
                for(String number : allNumbers){
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(number, null, list_str, null, null);
                }
            }
        }
        else
        {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(numbers_str, null, list_str, null, null);
        }
    }
}
