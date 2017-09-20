package com.taitsmith.swolemate.dbutils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import com.taitsmith.swolemate.data.Session;
import com.taitsmith.swolemate.data.Workout;

import java.util.ArrayList;
import java.util.List;

import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.CONTENT_URI;

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
 *
 * TODO SECOND UPDATE:
 * The original way this was done was too terrible to look at so I deleted it. If you're really curious
 * you can look back through the history. But I'd recommend against it.
 */

public class SessionCreator {
    public static List<Session> sessionList;

    public static List<Session> createSessionList(Context context) {
        sessionList = new ArrayList<>();
        List<Workout> workoutList = new ArrayList<>();
        Session session = new Session();
        ContentResolver resolver = context.getContentResolver();
        String[] dates = {"1","2","3"};

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
}
