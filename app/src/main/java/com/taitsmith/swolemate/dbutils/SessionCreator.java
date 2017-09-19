package com.taitsmith.swolemate.dbutils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.taitsmith.swolemate.data.Session;
import com.taitsmith.swolemate.data.Workout;

import java.util.ArrayList;
import java.util.List;

import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_DATE;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.CONTENT_URI;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.TABLE_NAME;

/**
 * So this seems like a pretty complicated way to get all of the workouts into their respective
 * {@link Session} objects. It was between this and creating a separate DB table to hold just the
 * unique dates, querying that, and then using those results to query the workout table and get
 * sessions that way.
 *
 * What instead happens (as you see below) is that all results are returned from the database. Those
 * are iterated through and unique dates are added to a string list. That list is then iterated through
 * and each date is used to find matching workouts in the table. All workouts matching each date are
 * added to another list of workouts which is stored in the session object.
 *
 * So like a facebook relationship status, 'it's complicated'.
 *
 * TODO UPDATE:
 * I woke up the next day and realised how absolutely terrible this was. It's been replaced but I thought
 * I might as well leave the original here as a warning.
 */

public class SessionCreator {

//    private static List<String> getDates(Context context) {
//        List<String> dateList = new ArrayList<>();
//        ContentResolver resolver = context.getContentResolver();
//         Cursor cursor = resolver.query(CONTENT_URI,
//                null,
//                null,
//                new String[]{"1"},
//                null);
//        try {
//            cursor.moveToFirst();
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//
//        do {
//            String date = cursor.getString(1);
//
//            if (!dateList.contains(date)) {
//                dateList.add(date);
//                Log.d("GETTING RESULTS: ", date);
//            }
//        } while (cursor.moveToNext());
//
//        cursor.close();
//
//        return dateList;
//    }
//
//    public static List<Session> createSessions(Context context) {
//        List<Session> sessionList = new ArrayList<>();
//        List<Workout> workoutList = new ArrayList<>();
//        List<String> dateList = getDates(context);
//
//        ContentResolver resolver = context.getContentResolver();
//
//        for (String s : dateList) {
//            Cursor cursor = resolver.query(CONTENT_URI,
//                    null,
//                    null,
//                    new String[]{s},
//                    null
//            );
//
//            do {
//                workoutList.add(createWorkoutFromCursor(cursor));
//            } while (cursor.moveToNext());
//
//            Session session = new Session();
//            session.setWorkoutList(workoutList);
//            sessionList.add(session);
//
//            cursor.close();
//        }
//
//        return sessionList;
//    }
//
//    private static Workout createWorkoutFromCursor(Cursor cursor) {
//        Workout workout = new Workout();
//
//        cursor.moveToFirst();
//
//        workout.setDate(cursor.getString(1));
//        workout.setName(cursor.getString(2));
//        workout.setWeight(cursor.getInt(3));
//        workout.setReps(cursor.getInt(4));
//        workout.setSets(cursor.getInt(5));
//        workout.setThoughts(cursor.getString(6));
//
//        return workout;
//    }

    public static List<Session> createSessionList(Context context) {
        List<Session> sessionList = new ArrayList<>();
        List<Workout> workoutList = new ArrayList<>();
        Session session = new Session();
        ContentResolver resolver = context.getContentResolver();
        String[] dates = {"1","2","3"};
        
        for (String s : dates) {

            Cursor cursor = resolver.query(CONTENT_URI,
                    null,
                    null, //we want workouts grouped by same date to display as
                    new String[]{s}, //a completed 'session'
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

            session.setWorkoutList(workoutList);
            sessionList.add(session);
            session = new Session();
        }

        return  sessionList;
    }
}
