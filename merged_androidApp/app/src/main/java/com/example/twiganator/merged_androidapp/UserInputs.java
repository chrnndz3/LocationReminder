package com.example.twiganator.merged_androidapp;

import android.content.Intent;
import android.util.Log;

/**
 * Created by twiganator on 4/18/17.
 */

public class UserInputs {

    public String address;
    public String reminders;
    public String radius;
    public String subject;
    public String lat;
    public String lon;
    public String date;
    public String time;
    public int id;

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

    public String getLat(){
        return this.lat;
    }

    public void setLat(String lat){
        this.lat = lat;
    }

    public String getLon(){
        return this.lon;
    }

    public void setLon(String lon){
        this.lon = lon;
    }


    public void setDate(String date){
        this.date = date;
    }

    public String getDate(){
        return this.date;
    }

    public void setTime(String time){
        this.time = time;
    }

    public String getTime(){
        return this.time;
    }


    public void setID(int id){
        this.id = id;
    }

    public int getID(){
        return this.id;
    }

}
