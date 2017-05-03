package com.example.twiganator.merged_androidapp;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.ProgressDialog;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by twiganator on 5/2/17.
 */

public class Nearby extends ListActivity{

    Button btnHit;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);
        btnHit = (Button) findViewById(R.id.submitBtn);

        btnHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText type = (EditText) findViewById(R.id.type);
                EditText keyword = (EditText) findViewById(R.id.keyword);
                EditText radius = (EditText) findViewById(R.id.radius);
                String type_str = type.getText().toString();
                String keyword_str = keyword.getText().toString();
                int val = Integer.parseInt(radius.getText().toString());
                double double_val = (double) val * 1609.34;
                int meters_radius = (int) double_val ;
                String  latitude = "";
                String longitude = "";
                GPSTracker tracker = new GPSTracker(Nearby.this);

                if (!tracker.canGetLocation()) {
                    tracker.showSettingsAlert();
                } else {
                    latitude = Double.toString(tracker.getLatitude());
                    longitude = Double.toString(tracker.getLongitude());
                }

                String api_key = "AIzaSyD3WlCpxsHL9HDVHyOdUHWtsoF4Tlq4U68";
                String url_str = "";

                if(type_str.isEmpty() && keyword_str.isEmpty()) {
                    Toast.makeText(Nearby.this, "Both fields are empty!", Toast.LENGTH_SHORT).show();
                }
                else if(type_str.isEmpty()){
                    url_str = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=" + meters_radius + "&keyword=" + keyword_str + "&key=" + api_key;
                }else if(keyword_str.isEmpty()){
                    url_str = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=" + meters_radius + "&type=" + type_str + "&key=" + api_key;
                }
                else{
                    url_str = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=" + meters_radius + "&type=" + type_str + "&keyword=" + keyword_str + "&key=" + api_key;
                }

                Log.d("Current lat", latitude);
                Log.d("Current lat", longitude);
                new JsonTask().execute(url_str);
                //"https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=40.112592499999984,-88.22691015625&radius=50000&keyword=Walgreens&key=AIzaSyD3WlCpxsHL9HDVHyOdUHWtsoF4Tlq4U68"
            }
        });
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(Nearby.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                try{
                     URL url = new URL(params[0]);
                    try {
                        connection = (HttpURLConnection) url.openConnection();
                        connection.connect();
                    }catch (IOException e){
                        Log.d("ERROR", "no connection");
                    }
                }
                catch (MalformedURLException e){
                    Log.d("URL", "malformed");
                }

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
            Log.d("RESULTS", result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray results = jsonObject.getJSONArray("results");

                Map<String, ArrayList<String>> values = new HashMap<String, ArrayList<String>>();

                int count = 0;
                for(int i = 0 ; i < results.length() ; i++){
                    JSONObject res = (JSONObject)results.get(i);
                    String name = "";
                    String address = "";

                    if(!res.getString("name").isEmpty()){
                        name = res.getString("name");
                        Log.d("NAME OF PLACE", res.getString("name"));

                        if(!res.getString("vicinity").isEmpty()){
                            address = res.getString("vicinity");
                            Log.d("ADDRESS OF PLACE", res.getString("vicinity"));
                        }
                        count += 1;
                        if(count <= 6){
                            addValues(values, name, address);
                        }
                    }
                }

                Iterator it = values.keySet().iterator();
                ArrayList<String> tempList = null;
                String [] toDisplay = new String[6];
                String key = "";
                int pos = 0;

                while (it.hasNext()) {
                    key = it.next().toString();
                    tempList = values.get(key);
                    if (tempList != null) {
                        for (String value: tempList) {
                            Log.d(key ,value);
                            toDisplay[pos] = key.toString() + ":\n" + value.toString();
                            pos++;
                        }
                    }
                }

                //Displaying in the front end
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Nearby.this, android.R.layout.simple_list_item_1, toDisplay);
                setListAdapter(adapter);

                Log.d("COUNT", Integer.toString(count));
            }catch (JSONException e){
                Log.d("json error", "");
            }
        }

        private void addValues(Map<String, ArrayList<String>> hashMap, String key, String value) {

            ArrayList tempList = null;
            if (hashMap.containsKey(key)) {
                tempList = hashMap.get(key);
                if(tempList == null)
                    tempList = new ArrayList();
                tempList.add(value);
            } else {
                tempList = new ArrayList();
                tempList.add(value);
            }
            hashMap.put(key,tempList);
        }
    }
}
