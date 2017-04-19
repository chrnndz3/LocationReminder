package com.example.twiganator.merged_androidapp;

import android.util.Log;

/**
 * Created by twiganator on 4/18/17.
 */

public class UserInputs {

    public String address;
    public String reminders;
    public String radius;
    public String subject;

    public void setAddress(String address){
        this.address = address;
    }

    public String getAddress(){
        return this.address;
    }

    public void setReminders(String reminders){
        this.reminders = reminders;
    }

    public String getReminders(){
        return this.reminders;
    }

    public String getRadius(){
        return this.radius;
    }

    public void setRadius(String radius){
        this.radius = radius;
    }

    public String getSubject(){
        return this.subject;
    }

    public void setSubject(String subject){
        this.subject = subject;
    }

//    public String getLatitude(){
//        return this.latitude;
//    }
//
//    public void setLatitude(String lat){
//        this.latitude = lat;
//    }
//
//    public String getLongitude(){
//        return this.longitude;
//    }
//
//    public void setLongitude(String lon){
//        this.longitude = lon;
//    }

//    public void setLanLon(String lan_lon){
//        Log.d("lan and lon", lan_lon);
//        this.lan_lon = lan_lon;
//    }
//
//    public String getLanLon(){
//        Log.d("Inside Getter",this.lan_lon);
//        return this.lan_lon;
//    }

}
