package com.taitsmith.swolemate.data;

import android.net.Uri;

/**
 * Schema layout for database to hold workout information.
 */

public class WorkoutDbContract {
    public static String PATH_WORKOUTS = "workouts";

    public static final String CONTENT_AUTHORITY = "com.taitsmith.swolemate.data";

    public static Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
}
