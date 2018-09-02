package com.taitsmith.swolemate.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * A group of {@link Workout} objects with the same timestamp will make up each session. This is
 * needed for display in the main activity and for detailed view activity.
 */

public class Session extends RealmObject{
    private String date;
    private int workoutCount;

    @PrimaryKey
    private int _id;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getWorkoutCount() {
        return workoutCount;
    }

    public void setWorkoutCount(int workoutCount) {
        this.workoutCount = workoutCount;
    }

    public int get_id() {
        return _id;
    }
}
