package com.taitsmith.swolemate.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.activities.AddWorkoutActivity;
import com.taitsmith.swolemate.activities.MainActivity;
import com.taitsmith.swolemate.data.Geofencer;


/**
 * Listening for some things, doing some other things when it hears those first things. We will be
 * listening for {@link Geofencer} entry/exits to send appropriate notifications. Upon entry to the
 * designated gym location, we will prompt users to open the app and start recording workouts. When
 * the exit geofence is triggered we'll give the user a chance to review all the workouts in their
 * session as well as giving them some sort of encouraging message.
 */

public class GeofenceReceiver extends BroadcastReceiver {
    public static final String TAG = GeofenceReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e(TAG, Integer.toString(geofencingEvent.getErrorCode()));
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        sendNotification(context, geofenceTransition);
    }

    //When the user gets to the gym, ask them if they want to record some workouts
    //When they leave, show a message saying something like 'great job'
    public static void sendNotification(Context context, int transitionType) {
        NotificationChannel channel;
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //so we can show notifications on Oreo+
        if (Build.VERSION.SDK_INT >= 26) {

            String id = "geofence_notification_channel";

            //both are user-visible
            CharSequence name = (context.getString(R.string.geofence_channel_name));
            String description = context.getString(R.string.geofence_channel_description);

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            channel = new NotificationChannel(id, name, importance);

            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }

        Intent notificationIntent = new Intent(context, AddWorkoutActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        // make sure we show the correct message based on enter/exit
        if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) {
            builder.setSmallIcon(R.drawable.add_workout)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(context.getString(R.string.geofence_notification_enter))
                    .addAction(R.drawable.add_workout, context.getString(R.string.geofence_notificatin_add_workout),
                            notificationPendingIntent)
                    .setContentIntent(notificationPendingIntent);
        } else if (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
            builder.setSmallIcon(R.drawable.add_workout)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(context.getString(R.string.geofence_notification_exit));
        }

        // Continue building the notification
        builder.setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);
        builder.setChannelId("geofence_notification_channel");

        // Issue the notification
        notificationManager.notify(45, builder.build());
    }
}
