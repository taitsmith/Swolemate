package com.taitsmith.swolemate.data;

import android.provider.BaseColumns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Holds all of the data for past workouts to be displayed in the main activity fragment.
 *
 */

public class PastWorkoutsMasterList extends BaseAdapter {

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    public static class viewHolder {
        TextView workoutDate;
        TextView workoutsDone;
    }
}
