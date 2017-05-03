package com.example.twiganator.merged_androidapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.HashMap;
import java.util.List;
import android.database.Cursor;
import java.util.*;
import java.util.Arrays;
import android.content.ContentResolver;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TimePicker;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

/**
 * Created by vaish on 4/25/2017.
 */

public class TodoListActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    ArrayList<String> names = new ArrayList<String>();
    ArrayList<HashMap<String,String>> contactData=new ArrayList<HashMap<String,String>>();

    HashMap<String, String> contactsList = new HashMap<String, String>();
    MultiAutoCompleteTextView text1;

    Button addDate;
    Button addTime;

    int day, month, year, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);

        Intent in = getIntent();
        names =(ArrayList<String>) in.getSerializableExtra("names");
        contactsList =(HashMap<String, String>) in.getSerializableExtra("contactData");

        text1=(MultiAutoCompleteTextView)findViewById(R.id.multiAutoCompleteTextView1);
        ArrayAdapter adapter = new
                ArrayAdapter(this,android.R.layout.simple_list_item_1,names);
        text1.setAdapter(adapter);
        text1.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        EditText location = (EditText) findViewById(R.id.location);
        location.setVisibility(View.GONE);

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

                DatePickerDialog datePickerDialog = new DatePickerDialog(TodoListActivity.this, TodoListActivity.this, year,month,day);
                datePickerDialog.show();
            }
        });

        addTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(TodoListActivity.this, TodoListActivity.this, hour, minute, false);
                timePickerDialog.show();
            }
        });
    }

//    public void contacts(){
//        ContentResolver cr = getContentResolver();
//        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
//        while (cursor.moveToNext()) {
//            try{
//                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//                String name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
//                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
//                    Cursor phones = getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, null, null);
//                    while (phones.moveToNext()) {
//                        String phoneNumber = phones.getString(phones.getColumnIndex( ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        HashMap<String,String> map=new HashMap<String,String>();
//                        map.put("name", name);
//                        map.put("number", phoneNumber);
//                        contactData.add(map);
//                        Log.d("NAME", name);
//                        Log.d("NUMBER", phoneNumber);
//                        names.add(name);
//
//                        contactsList.put(name, phoneNumber);
//                    }
//                    phones.close();
//                }
//            }catch(Exception e){}
//        }
//    }

    public void onSubmitClick(View v){
        EditText list = (EditText) findViewById(R.id.reminderMessage);

        String contacts = text1.getText().toString();
        contacts = contacts.substring(0, contacts.length() - 2);
        Log.d("NEW CONTACTS",contacts);

//        String numbers_str = numbers.getText().toString();
        String list_str = list.getText().toString();

        if(contacts.contains(","))
        {
            List<String> allNumbers = Arrays.asList(contacts.split(","));
            if(allNumbers.size() > 0 ){
                Log.d("INSIDE SECOND IF", Integer.toString(allNumbers.size()));
                for(String contact : allNumbers){
                    String number = "";
                    Log.d("CONTACT", contact);
                    for(String key : contactsList.keySet()){
                        if(key.equalsIgnoreCase(contact)){
                            String contactNumber = contactsList.get(key);
                            Log.d("NUMBER",contactNumber);
                        }
                    }
//                    for(HashMap<String, String> elem : contactData){
//
//                        if(elem.containsKey(contact))
//                        {
//                            number = elem.get(contact);
//                            Log.d("NUMBER",number);
//                            Log.d("CONTACT", contact);
////                            SmsManager sms = SmsManager.getDefault();
////                            sms.sendTextMessage(number, null, list_str, null, null);
//                        }
//                    }

                }
            }
        }
//        else
//        {
//            SmsManager sms = SmsManager.getDefault();
//            sms.sendTextMessage(numbers_str, null, list_str, null, null);
//        }
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
            }
        }
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
}