package com.taitsmith.swolemate.data;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

import static com.taitsmith.swolemate.activities.SwolemateApplication.realmConfiguration;

/**
 * A group of {@link Workout} objects with the same timestamp will make up each session. This is
 * needed for display in the main activity and for detailed view activity.
 */

public class Session extends RealmObject{
    private String date;
    private int workoutCount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getWorkoutCount(String date) {
        Realm realm = Realm.getInstance(realmConfiguration);
        RealmResults<Workout> realmResults = realm.where(Workout.class)
                .equalTo("name", date)
                .findAll();
        return realmResults.size();
    }

    public void setWorkoutCount(int workoutCount) {
        this.workoutCount = workoutCount;
    }
}
