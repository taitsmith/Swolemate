package com.taitsmith.swolemate.dbutils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.*;

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
        final String CREATE_SQL = "CREATE TABLE " + WorkoutDbContract.WorkoutEntry.TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT NOT NULL, " + //timestamp will be stored as LocalDate.now();
                COLUMN_WORKOUT_NAME + " TEXT NOT NULL, " +
                COLUMN_WEIGHT + " INTEGER NOT NULL, " +
                COLUMN_REPS + " INTEGER NOT NULL, " +
                COLUMN_SETS + " INTEGER NOT NULL, " +
                COLUMN_THOUGHTS + " TEXT" + ");";

        sqLiteDatabase.execSQL(CREATE_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
