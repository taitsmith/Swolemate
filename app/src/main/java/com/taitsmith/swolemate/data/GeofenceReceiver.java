package com.taitsmith.swolemate.data;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.activities.AddWorkoutActivity;
import com.taitsmith.swolemate.activities.MainActivity;

import static android.os.Build.VERSION.SDK;
import static android.os.Build.VERSION.SDK_INT;

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

        if (Build.VERSION.SDK_INT >= 26) {
// The id of the channel.
            String id = "my_channel_01";
// The user-visible name of the channel.
            CharSequence name = ("beep");
// The user-visible description of the channel.
            String description = "beeeep";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            channel = new NotificationChannel(id, name, importance);
// Configure the notification channel.
            channel.setDescription(description);
            channel.enableLights(true);
// Sets the notification light color for notifications posted to this
// channel, if the device supports this feature.
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(channel);
        }


        Intent notificationIntent = new Intent(context, AddWorkoutActivity.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MainActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        // Check the transition type to display the relevant icon image
        if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) {
            builder.setSmallIcon(R.drawable.add_workout)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(context.getString(R.string.geofence_notification_enter))
                    .addAction(R.drawable.add_workout, "Add Workout", notificationPendingIntent)
                    .setChannel("my_channel_01")
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

        // Issue the notification
        notificationManager.notify(293487, builder.build());
    }
}
