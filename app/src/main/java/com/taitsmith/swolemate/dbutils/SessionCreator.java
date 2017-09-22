package com.taitsmith.swolemate.dbutils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.taitsmith.swolemate.data.Session;
import com.taitsmith.swolemate.data.Workout;

import java.util.ArrayList;
import java.util.List;

import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.CONTENT_URI;

/**
 * Takes the list of unique session dates (hardcoded for now, will be switched to SharedPreferences
 * later).
 */

public class SessionCreator {
    public static List<Session> sessionList;
    public static List<Workout> workoutList;
    private static String[] dates = {"1","2","3", "4","5","6","7","8","9"};

    public static List<Session> createSessionList(Context context) {
        sessionList = new ArrayList<>();
        List<Workout> workoutList = new ArrayList<>();
        Session session = new Session();
        ContentResolver resolver = context.getContentResolver();


        for (String s : dates) {

            Cursor cursor = resolver.query(CONTENT_URI,
                    null,
                    null,
                    new String[]{s},
                    null);

            cursor.moveToFirst();
            session.setDate(cursor.getString(1));

            do {
                Workout workout = new Workout();
                workout.setDate(cursor.getString(1));
                workout.setName(cursor.getString(2));
                workout.setWeight(cursor.getInt(3));
                workout.setReps(cursor.getInt(4));
                workout.setSets(cursor.getInt(5));
                workout.setThoughts(cursor.getString(6));
                workoutList.add(workout);

            } while (cursor.moveToNext());

            session.setWorkoutList(workoutList);
            sessionList.add(session);
            session = new Session();
            cursor.close();
        }

        return  sessionList;
    }

    public static List<Workout> createWorkoutList(Context context,  int position) {
        workoutList = new ArrayList<>();

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(CONTENT_URI,
                null,
                null,
                new String[]{dates[position]},
                null);

        cursor.moveToFirst();

        do {
            Workout workout = new Workout();
            workout.setDate(cursor.getString(1));
            workout.setName(cursor.getString(2));
            workout.setWeight(cursor.getInt(3));
            workout.setReps(cursor.getInt(4));
            workout.setSets(cursor.getInt(5));
            workout.setThoughts(cursor.getString(6));
            workoutList.add(workout);
        } while (cursor.moveToNext());
        cursor.close();

        return workoutList;
    }
}
