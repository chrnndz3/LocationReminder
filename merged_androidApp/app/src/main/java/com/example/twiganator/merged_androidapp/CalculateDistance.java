package com.example.twiganator.merged_androidapp;

/**
 * Created by twiganator on 4/18/17.
 */

import android.location.Location;
import android.util.Log;

public class CalculateDistance {

    public void distanceBetweenGeoPoints(double startLat, double startLong, double endLat, double endLong){
        Log.d("INSIDE FUNCTION", "INSIDE");
        float arrayResults[] = new float[5];
        Location.distanceBetween(startLat, startLong, endLat, endLong,arrayResults);
        double result = 0.000621371 * arrayResults[0];
        Log.d("RESULT",Double.toString(result));
    }
}