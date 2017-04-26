package com.example.twiganator.merged_androidapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.Date;

/**
 * Created by twiganator on 4/25/17.
 */

public class Notifications {

    public void sendNotification(String subject, String reminders, Context context){
        int notificationID = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        PendingIntent dismissIntent = DismissActivity.getDismissIntent(notificationID, context);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.r)
                        .setAutoCancel(true)
                        .setContentTitle(subject.toUpperCase())
                        .setContentText(reminders)
                        .addAction(R.drawable.cancel_x, "Dismiss", dismissIntent);

        //Vibrate when notification is sent
        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);


        //Create notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(notificationID, builder.build());
    }
}
