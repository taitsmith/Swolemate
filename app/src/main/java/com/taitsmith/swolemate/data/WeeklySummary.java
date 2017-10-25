package com.taitsmith.swolemate.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.taitsmith.swolemate.ui.AlertDialogs;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.taitsmith.swolemate.activities.SwolemateApplication.realmConfiguration;

/**
 * Searches the DB for all workouts done in the last seven days, then displays a summary
 * to the user in the form of an AlertDialog
 */

public class WeeklySummary extends AsyncTask<Void, Void, int[]> {
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
        DateTime dateTime = DateTime.now();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM-dd-yyyy");

        for (int i = 0; i < 7; i++) {
            String s = dateTime.minusDays(i).toString(formatter);
            dates[i] = s;
        }
    }

    //take the list of date strings, search the db for each one. if it exists,
    //get a total count of all the workouts done
    @Override
    protected int[] doInBackground(Void... params) {
        workouts = 0;
        sessions = 0;
        counts = new int[2];
        Realm realm = Realm.getInstance(realmConfiguration);

        RealmResults<Session> realmResults = realm.where(Session.class)
                .in("date", dates)
                .findAll();

        if (realmResults.size() != 0) {
            for (Session session : realmResults) {
                workouts += session.getWorkoutCount();
                sessions++;
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
