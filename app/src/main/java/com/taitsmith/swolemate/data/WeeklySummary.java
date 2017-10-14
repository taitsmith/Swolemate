package com.taitsmith.swolemate.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.taitsmith.swolemate.ui.AlertDialogs;
import com.taitsmith.swolemate.utils.DbContract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Searches the DB for all workouts done in the last seven days, then displays a summary
 * to the user in the form of an AlertDialog
 */

public class WeeklySummary extends AsyncTask<Context, Void, int[]> {
    private int workouts, sessions;
    private int[] counts;
    private Context context;
    private String[] dates;

    public WeeklySummary(Context context){
        this.context = context;
    }

    //get an array of the dates making up the last seven days which we'll use to
    //query the db.
    @Override
    protected void onPreExecute() {
        dates = new String[7];
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 7; i++) {
            calendar.add(Calendar.DATE, -i);
            dates[i] = dateFormat.format(calendar.getTime());
        }
    }

    //take the list of date strings, search the db for each one. if it exists,
    //get a total count of all the workouts done
    @Override
    protected int[] doInBackground(Context... params) {
        workouts = 0;
        sessions = 0;
        Context context = params[0];
        counts = new int[2];

        ContentResolver resolver = context.getContentResolver();

        for (String s : dates) {

            Cursor cursor = resolver.query(DbContract.WorkoutEntry.CONTENT_URI,
                    null,
                    DbContract.WorkoutEntry.COLUMN_DATE + "=?",
                    new String[]{s},
                    null);

            if (cursor != null && cursor.getCount() != 0) {
                sessions++;
                cursor.moveToFirst();

                do {
                    workouts++;
                } while (cursor.moveToNext());

                cursor.close();
            }
        }

        counts[0] = sessions;
        counts[1] = workouts;

        return counts;
    }

    @Override
    protected void onPostExecute(int[] counts) {
        if (counts[0] == 0) {
            AlertDialogs.weeklySummaryNoWorkouts(context);
        } else {
            AlertDialogs.weeklySummaryDialog(context, counts);
        }
    }
}
