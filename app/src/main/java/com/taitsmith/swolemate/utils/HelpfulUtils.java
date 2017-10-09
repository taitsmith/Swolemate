package com.taitsmith.swolemate.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import com.google.android.gms.location.places.Place;
import com.taitsmith.swolemate.data.Session;
import com.taitsmith.swolemate.data.Workout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.taitsmith.swolemate.activities.SwolemateApplication.sessionDates;
import static com.taitsmith.swolemate.activities.SwolemateApplication.sharedPreferences;
import static com.taitsmith.swolemate.activities.SwolemateApplication.sortedDates;
import static com.taitsmith.swolemate.activities.SwolemateApplication.workoutArray;
import static com.taitsmith.swolemate.utils.DbContract.*;

/**
 * A home for all friendly and helpful utilities that involve interacting with the database through
 * the content provider.
 *
 * createSessionList takes the list of Dates from shared preferences and finds all {@link Workout}
 * matching each date and turns them into a List, then adds that list to another list of {@link Session}.
 * There's a lot of lists being thrown around here, but it makes sense. This is used to populate the
 * ListView in {@link com.taitsmith.swolemate.activities.MainActivity}.
 *
 * createWorkoutList is called from the onCreateView of {@link com.taitsmith.swolemate.ui.WorkoutDetailFragment}
 * to fill the recycler view with all workouts completed in the selected session. It's passed an int,
 * gets a date from the sessionDates set, finds all workouts with that date and returns a List of them.
 *
 * addLocation is called when PlacePicker returns valid data as far as setting a location for a gym.
 * It just adds the location (name, lat, long) to the database to be queried later for Geofencing
 * purposes.
 *
 * makeUpWorkouts just makes some fake data to populate the list in main activity for display and
 * testing purposes
 */

public class HelpfulUtils {
    private static List<Session> sessionList;
    private static List<Workout> workoutList;
    private static String[] dateArray;

    public static List<Session> createSessionList(Context context) {
        sessionList = new ArrayList<>();
        List<Workout> workoutList = new ArrayList<>();
        Session session = new Session();
        ContentResolver resolver = context.getContentResolver();

        for (String s : sortedDates) {
            Log.d("LOG SESSION DATE", s);

            Cursor cursor = resolver.query(WorkoutEntry.CONTENT_URI,
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
        dateArray = sortedDates.toArray(new String[sortedDates.size()]);

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(WorkoutEntry.CONTENT_URI,
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

    public static void addLocation(Context context, Place place) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        String name = (String) place.getName();
        String id = place.getId();
        double placeLong = place.getLatLng().longitude;
        double placeLat = place.getLatLng().latitude;

        values.put(DbContract.GymLocationEntry.COLUMN_LOCATION_NAME, name);
        values.put(GymLocationEntry.COLUMN_LOCATION_LAT, placeLat);
        values.put(GymLocationEntry.COLUMN_LOCATION_LONG, placeLong);
        values.put(GymLocationEntry.COLUMN_PLACE_ID, id);

        resolver.insert(GymLocationEntry.CONTENT_URI, values);
    }

    //instead of manually going in and entering a ton of fake workouts for testing, we'll
    //create X sessions containing Y workouts each.
    public static void makeUpWorkouts(Context context) {
        String[] fakeDates = {"2017-09-13", "2017-09-14", "2017-09-15", "2017-09-16", "2017-09-17",
                "2017-09-18", "2017-09-19", "2017-09-20", "2017-09-21"};

        Random r = new Random();
        for (int i = 0; i < 9; i++) {
            ContentValues values = new ContentValues();
            ContentResolver resolver = context.getContentResolver();
            for (int j = 0; j < 4; j++) {
                values.put(WorkoutEntry.COLUMN_DATE, fakeDates[i]);
                values.put(WorkoutEntry.COLUMN_WEIGHT, 420);
                values.put(WorkoutEntry.COLUMN_REPS, r.nextInt(50));
                values.put(WorkoutEntry.COLUMN_SETS, r.nextInt(5));
                values.put(WorkoutEntry.COLUMN_THOUGHTS, "i am so strong");
                values.put(WorkoutEntry.COLUMN_WORKOUT_NAME, workoutArray.getString(r.nextInt(6)));

                sessionDates.add(fakeDates[i]);

                resolver.insert(WorkoutEntry.CONTENT_URI, values);
            }
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("DATES", sessionDates);
        editor.apply();
    }

    public static void updateSessionDates(String date) {
        sessionDates.add(date);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("DATES", sessionDates);
        editor.apply();

        sortedDates.add(date);
        Collections.sort(sortedDates);
        Collections.reverse(sortedDates);
    }
}
