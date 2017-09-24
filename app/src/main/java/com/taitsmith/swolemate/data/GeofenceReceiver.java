package com.taitsmith.swolemate.data;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.activities.MainActivity;


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

    private void sendNotification(Context context, int transitionType) {
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(context, MainActivity.class);

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
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.common_google_signin_btn_icon_dark))
                    .setContentTitle(context.getString(R.string.app_name));
        } else if (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
            builder.setSmallIcon(R.drawable.add_workout)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.common_google_signin_btn_icon_light))
                    .setContentTitle(context.getString(R.string.app_name));
        }

        // Continue building the notification
        builder.setContentText(context.getString(R.string.accept));
        builder.setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());
    }
}
