package com.taitsmith.swolemate.dbutils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *Handles all the work of creating the database.
 */

public class WorkoutDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "workouts.db";
    private static final int DATABASE_VERSION = 1;

    public WorkoutDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_SQL = "CREATE TABLE" + WorkoutDbContract.WorkoutEntry.TABLE_NAME + "(" +
                WorkoutDbContract.WorkoutEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WorkoutDbContract.WorkoutEntry.COLUMN_DATE + " TEXT NOT NULL, " + //timestamp will be stored as LocalDate.now();
                WorkoutDbContract.WorkoutEntry.COLUMN_WORKOUT_NAME + " TEXT NOT NULL, " +
                WorkoutDbContract.WorkoutEntry.COLUMN_WEIGHT + " INTEGER NOT NULL, " +
                WorkoutDbContract.WorkoutEntry.COLUMN_REPS + " INTEGER NOT NULL, " +
                WorkoutDbContract.WorkoutEntry.COLUMN_THOUGHTS + " TEXT" + ");";

        sqLiteDatabase.execSQL(CREATE_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
