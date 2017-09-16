package com.taitsmith.swolemate.data;

import java.util.List;

/**
 * All of the relevant info to turn data from the SQLite db into an object. A group makes up each
 * {@link Session} object.
 */

public class Workout {
    public String date, thoughts;
    public int weight, reps;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public String getThoughts() {
        return thoughts;
    }

    public void setThoughts(String thoughts) {
        this.thoughts = thoughts;
    }
}
