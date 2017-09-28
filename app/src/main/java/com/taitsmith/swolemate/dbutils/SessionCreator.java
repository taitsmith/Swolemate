package com.taitsmith.swolemate.dbutils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.taitsmith.swolemate.data.Session;
import com.taitsmith.swolemate.data.Workout;

import java.util.ArrayList;
import java.util.List;

import static com.taitsmith.swolemate.activities.SwolemateApplication.sessionDates;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.CONTENT_URI;

/**
 * Two methods here: one takes the list of Dates from shared preferences and finds all {@link Workout}
 * matching each date and turns them into a List, then adds that list to another list of {@link Session}.
 * There's a lot of lists being thrown around here, but it makes sense. This is used to populate the
 * ListView in {@link com.taitsmith.swolemate.activities.MainActivity}.
 *
 * The second method
 */

public class SessionCreator {
    public static List<Session> sessionList;
    public static List<Workout> workoutList;
    private static String[] dateArray;

    public static List<Session> createSessionList(Context context) {
        sessionList = new ArrayList<>();
        List<Workout> workoutList = new ArrayList<>();
        Session session = new Session();
        ContentResolver resolver = context.getContentResolver();


        for (String s : sessionDates) {

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
            workoutList = new ArrayList<>();

            cursor.close();
        }

        return  sessionList;
    }

    public static List<Workout> createWorkoutList(Context context,  int position) {
        workoutList = new ArrayList<>();
        dateArray = sessionDates.toArray(new String[sessionDates.size()]);

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(CONTENT_URI,
                null,
                null,
                new String[]{dateArray[position]},
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
