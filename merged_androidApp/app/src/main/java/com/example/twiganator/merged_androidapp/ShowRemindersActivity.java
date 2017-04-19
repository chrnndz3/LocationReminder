package com.example.twiganator.merged_androidapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.util.Log;

/**
 * Created by twiganator on 4/19/17.
 */

public class ShowRemindersActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showreminder);

        TextView reminders = (TextView) findViewById(R.id.showReminders);

        RemindersDatabase obj_select = new RemindersDatabase(this);
        String result = obj_select.checkDatabase();
//        Log.d("VALUES",result);

        reminders.setText(result);
    }

}
