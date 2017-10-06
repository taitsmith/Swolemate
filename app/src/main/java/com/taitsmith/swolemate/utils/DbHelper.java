package com.taitsmith.swolemate.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *Handles all the work of creating the database.
 */

class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "workouts.db";
    private static final int DATABASE_VERSION = 1;

    DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_WORKOUTS = "CREATE TABLE " + DbContract.WorkoutEntry.TABLE_NAME + "("
                + DbContract.WorkoutEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DbContract.WorkoutEntry.COLUMN_DATE + " TEXT NOT NULL, "  //timestamp will be stored as LocalDate.now();
                + DbContract.WorkoutEntry.COLUMN_WORKOUT_NAME + " TEXT NOT NULL, "
                + DbContract.WorkoutEntry.COLUMN_WEIGHT + " INTEGER NOT NULL, "
                + DbContract.WorkoutEntry.COLUMN_REPS + " INTEGER NOT NULL, "
                + DbContract.WorkoutEntry.COLUMN_SETS + " INTEGER NOT NULL, "
                + DbContract.WorkoutEntry.COLUMN_THOUGHTS + " TEXT" + ");";

        final String CREATE_LOCATIONS = "CREATE TABLE " + DbContract.GymLocationEntry.TABLE_NAME + "("
                + DbContract.GymLocationEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DbContract.GymLocationEntry.COLUMN_LOCATION_NAME + " TEXT NOT NULL, "
                + DbContract.GymLocationEntry.COLUMN_LOCATION_LAT + " INTEGER NOT NULL, "
                + DbContract.GymLocationEntry.COLUMN_LOCATION_LONG + " INTEGER NOT NULL, "
                + DbContract.GymLocationEntry.COLUMN_PLACE_ID + " TEXT NOT NULL" + ");";

        sqLiteDatabase.execSQL(CREATE_WORKOUTS);
        sqLiteDatabase.execSQL(CREATE_LOCATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
