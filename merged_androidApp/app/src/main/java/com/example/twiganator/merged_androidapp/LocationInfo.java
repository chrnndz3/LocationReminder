package com.example.twiganator.merged_androidapp;

import android.renderscript.Double2;
import android.util.Log;

/**
 * Created by twiganator on 4/17/17.
 */

public class LocationInfo {
    public Double longitude;
    public Double latitude;

    public void setLongitude(Double longitude){
        this.longitude = longitude;
    }

    public Double getLongitude(){
        Log.d("GET", String.valueOf(this.longitude));
        return this.longitude;

    }

    public void setLatitude(Double latitude){
        this.latitude = latitude;
    }

    public Double getLatitude(){
        Log.d("GET", String.valueOf(this.latitude));
        return this.latitude;

    }
}
