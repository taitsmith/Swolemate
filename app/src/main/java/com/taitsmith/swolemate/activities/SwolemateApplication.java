package com.taitsmith.swolemate.activities;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Set everything up, check permissions, etc.
 */

public class SwolemateApplication extends Application {
    public static final int PERMISSION_REQUEST_FINE_LOCATION = 23;

    @Override
    public void onCreate() {
        super.onCreate();


    }
}
