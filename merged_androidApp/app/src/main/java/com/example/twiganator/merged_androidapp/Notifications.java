package com.example.twiganator.merged_androidapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.ImageView;

import java.util.Date;

/**
 * Created by twiganator on 4/25/17.
 */

public class Notifications {

    private final Context context;

    //Constructor
    public Notifications(Context context) {
        this.context = context;
    }

    /**
     * Creates notification based on location based and time/date
     * @param notificationID
     * @param subject
     * @param reminders
     * @param context
     */
    public void sendNotification(int notificationID, String subject, String reminders, Context context){

        DismissActivity dis_obj = new DismissActivity();
        PendingIntent dismissIntent = dis_obj.getDismissIntent(notificationID, context);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.r)
                        .setAutoCancel(true)
                        .setContentTitle(subject.toUpperCase())
                        .setContentText(reminders)
                        .addAction(R.drawable.ic_stat_name, "Did it already", dismissIntent);

        //Vibrate when notification is sent
        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);


        //Create notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notificationID, builder.build());

//        RemindersDatabase db = new RemindersDatabase(this.context);
//        db.updateDatabase(notificationID);
    }
}
