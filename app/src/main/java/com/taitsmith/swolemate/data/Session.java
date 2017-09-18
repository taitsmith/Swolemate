package com.taitsmith.swolemate.data;

import java.util.List;

/**
 * A group of {@link Workout} objects with the same timestamp will make up each session. This is
 * needed for display in the main activity and for detailed view activity.
 */

public class Session {
    private List<Workout> workoutList;
    private String date;

    public List<Workout> getWorkoutList() {
        return workoutList;
    }

    public void setWorkoutList(List<Workout> workoutList) {
        this.workoutList = workoutList;
    }

    public static Session createSession() {
        return null;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
