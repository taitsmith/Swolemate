package com.taitsmith.swolemate.dbutils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.taitsmith.swolemate.data.Session;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.CONTENT_URI;

/**
 * To create the list of sessions for the main list when the app starts.
 */

public class SessionCreator {

    public static List<Session> createSessions(Context context) {
        List<Session> sessions = new ArrayList<>();

        String dateString = LocalDate.now().toString();

        ContentResolver resolver = context.getContentResolver();

        Cursor cursor = resolver.query(CONTENT_URI,
                null,
                null,
                new String[]{dateString},
                null
                );

        cursor.moveToFirst();

        do {
            Session session = new Session();
            session.setDate(cursor.getString(1));
            sessions.add(session);
            Log.d("LOG ", session.getDate());
        } while (cursor.moveToNext());

        return sessions;
    }
}
