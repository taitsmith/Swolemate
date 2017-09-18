package com.taitsmith.swolemate.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.activities.MainActivity;
import com.taitsmith.swolemate.data.PastSessionsAdapter;

/**
 * Fragment that displays a list of past sessions (one single trip to the gym consisting of
 * multiple workouts) in the main activity.
 */

public class PastSessionsListFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.past_workouts_fragment, container, false);
        final GridView gridView = rootView.findViewById(R.id.past_workouts_grid_view);

        final PastSessionsAdapter adapter = new PastSessionsAdapter(getContext(), MainActivity.makeUpSomeData());

        gridView.setAdapter(adapter);

        return rootView;
    }
}
