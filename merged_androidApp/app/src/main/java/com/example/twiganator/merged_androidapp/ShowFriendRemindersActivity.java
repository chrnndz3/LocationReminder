package com.example.twiganator.merged_androidapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.widget.ArrayAdapter;
import android.app.ListActivity;

/**
 * Created by twiganator on 4/19/17.
 */

public class ShowFriendRemindersActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showfriendreminder);

        MessagesDatabase obj_reminders = new MessagesDatabase(this);
        ArrayList<String[]> results = obj_reminders.checkDatabase();
        String[] resultsToShow = new String[results.size()];
        int pos = 0;

        for(String[] value : results)
        {
            String toShow = "Contact : "+value[0]+"\nMessage : "+value[1]+"\nDate : "+value[2]+"\nTime : "+value[3];
            resultsToShow[pos] = toShow;
            pos = pos + 1;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, resultsToShow);
        setListAdapter(adapter);
    }

}
