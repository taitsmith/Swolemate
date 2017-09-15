package com.taitsmith.swolemate.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.taitsmith.swolemate.R;

/**
 * Fragment that displays a list of past workouts in the main activity.
 */

public class PastWorkoutsListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.past_workouts_fragment, container, false);
        final GridView gridView = rootView.findViewById(R.id.past_workouts_grid_view);

        return rootView;
    }
}
