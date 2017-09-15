package com.taitsmith.swolemate.data;

import java.util.List;

/**
 * All of the relevant info to turn data from the SQLite db into an object.
 */

public class Workout {
    public String date;
    public List<String> workoutsDones;
    public List<Integer> weights;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getWorkoutsDones() {
        return workoutsDones;
    }

    public void setWorkoutsDones(List<String> workoutsDones) {
        this.workoutsDones = workoutsDones;
    }

    public List<Integer> getWeights() {
        return weights;
    }

    public void setWeights(List<Integer> weights) {
        this.weights = weights;
    }
}
