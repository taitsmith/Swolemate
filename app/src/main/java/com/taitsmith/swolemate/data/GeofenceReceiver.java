package com.taitsmith.swolemate.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Listening for some things.
 */

public class GeofenceReceiver extends BroadcastReceiver {
    public static final String TAG = GeofenceReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "received.");
    }
}
