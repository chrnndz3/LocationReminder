package com.example.twiganator.merged_androidapp;

/**
 * Created by twiganator on 4/18/17.
 */

import android.location.Location;
import android.util.Log;

public class CalculateDistance {

    public double distanceBetweenGeoPoints(double startLat, double startLong, double endLat, double endLong){
        Log.d("INITIAL LAT",Double.toString(startLat));
        Log.d("INITIAL LON", Double.toString(startLong));
        Log.d("FINAL LAT", Double.toString(endLat));
        Log.d("FINAL LON", Double.toString(endLong));
        Log.d("DISTANCE BETWEEN POINTS", "CALCULATING DISTANCE");
        float arrayResults[] = new float[5];
        Location.distanceBetween(startLat, startLong, endLat, endLong,arrayResults);
        double result = 0.000621371 * arrayResults[0];
        Log.d("RESULT",Double.toString(result));
        return result;
    }
}