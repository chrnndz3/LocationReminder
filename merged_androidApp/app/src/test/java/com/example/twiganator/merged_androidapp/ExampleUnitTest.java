package com.example.twiganator.merged_androidapp;

import android.app.Activity;
import android.util.Log;

import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest extends Activity{

    @Test
    public void TestLongititLat() throws Exception {
        UserInputs user_obj = new UserInputs();
        String expectecdLon = "-88.2294206";
        String expectecdLat = "40.1167106";

        user_obj.setLat("40.1167106");
        user_obj.setLon("-88.2294206");

        String outputLat = user_obj.getLat();
        String outputLon = user_obj.getLon();

        assertEquals(expectecdLon, outputLon);
        assertEquals(expectecdLat, outputLat);
    }

    @Test
    public void TestReminders() throws Exception {
        UserInputs user_obj = new UserInputs();
        String expected = "apples, bananas, milk";

        user_obj.setReminders("apples, bananas, milk");

        String outputReminders = user_obj.getReminders();

        assertEquals(expected, outputReminders);
    }

    @Test
    public void TestSubject() throws Exception {
        UserInputs user_obj = new UserInputs();
        String expected = "study";

        user_obj.setSubject("study");

        String outputSubject = user_obj.getSubject();

        assertEquals(expected, outputSubject);
    }

    @Test
    public void TestRadius() throws Exception {
        UserInputs user_obj = new UserInputs();
        String expected = "6";

        user_obj.setRadius("6");

        String outputRadius = user_obj.getRadius();

        assertEquals(expected, outputRadius);
    }

//    @Test
//    public void TestRemindersDatabase() throws Exception {
//        RemindersDatabase obj_reminders = new RemindersDatabase(this);
//        Map<String, String[]> results = obj_reminders.checkDatabase();
//        Set<String> setStr = results.keySet();
////        String[] values = results.get(setStr[0].);
////        String address = values[1];
//        Log.d("set", setStr.toString());
//    }

}