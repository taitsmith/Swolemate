package com.taitsmith.swolemate.utils;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Schema layout for database to hold workout information. Database will contain columns to hold
 * specific information relevant to each individual workout that makes up a session (one trip to the
 * gym). Columns are workout name, weight used, reps done and 'thoughts', which allows users to
 * write down some optional notes on how they felt or anything else they feel like keeping track of.
 * The date column will store a timestamp which allows for sorting by session (important for card view
 * in main activity and detail view).
 */

public class WorkoutDbContract {
    private static String PATH_WORKOUTS = "workouts";
    private static String PATH_LOCATIONS = "locations";

    public static final String CONTENT_AUTHORITY = "com.taitsmith.swolemate";

    private static Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class WorkoutEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_WORKOUTS)
                .build();

        public static final String TABLE_NAME = "workouts";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_WORKOUT_NAME = "name";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_REPS = "reps";
        public static final String COLUMN_SETS = "sets";
        public static final String COLUMN_THOUGHTS = "thoughts";

    }

    public static final class GymLocationEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_LOCATIONS)
                .build();

        public static final String TABLE_NAME = "locations";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_LOCATION_NAME = "name";
        public static final String COLUMN_LOCATION_LAT = "lat";
        public static final String COLUMN_LOCATION_LONG = "long";
        public static final String COLUMN_PLACE_ID = "placeId";

    }
}
