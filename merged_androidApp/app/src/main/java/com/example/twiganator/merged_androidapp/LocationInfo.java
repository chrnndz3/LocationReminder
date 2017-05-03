package com.example.twiganator.merged_androidapp;

import android.renderscript.Double2;
import android.util.Log;

/**
 * Created by twiganator on 4/17/17.
 */

public class LocationInfo {
    public Double longitude;
    public Double latitude;

    public void setLong(Double longitude){
        this.longitude = longitude;
    }

    public Double getLong(){
        return this.longitude;

    }

    public void setLat(Double latitude){
        this.latitude = latitude;
    }

    public Double getLat(){
        return this.latitude;

    }
}
