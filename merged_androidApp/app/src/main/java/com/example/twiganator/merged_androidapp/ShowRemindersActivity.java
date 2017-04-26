package com.example.twiganator.merged_androidapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;

import java.util.List;
import java.util.Map;
import android.widget.ArrayAdapter;
import android.app.ListActivity;

/**
 * Created by twiganator on 4/19/17.
 */

public class ShowRemindersActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showreminder);

        RemindersDatabase obj_reminders = new RemindersDatabase(this);
        Map<String, String[]> results = obj_reminders.checkDatabase();
        String[] resultsToShow = new String[results.size()];
        int pos = 0;
        for(String key : results.keySet())
        {
            String[] values = results.get(key);
            String address = values[1];
            Double latval = Double.parseDouble(values[3]);
            Double lonval = Double.parseDouble(values[4]);
            CalculateDistance obj_distance = new CalculateDistance();
            String distance = Double.toString(obj_distance.distanceBetweenGeoPoints(latval,lonval,40.1125909,-88.2268336));

            String toSHow = key.toUpperCase() + "\nReminder : " + values[0]+ "\nLocation : "
                    + address+"\nDate : "+values[5]+"\nTime : "+values[6];
            resultsToShow[pos] = toSHow;
            pos = pos + 1;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, resultsToShow);
        setListAdapter(adapter);
    }

}
