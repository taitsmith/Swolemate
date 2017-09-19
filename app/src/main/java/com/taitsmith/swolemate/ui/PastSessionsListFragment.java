package com.taitsmith.swolemate.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.PastSessionsAdapter;
import com.taitsmith.swolemate.data.Session;

import java.util.List;

/**
 * Fragment that displays a list of past sessions (one single trip to the gym consisting of
 * multiple workouts) in the main activity.
 */

public class PastSessionsListFragment extends Fragment {
    private static List<Session> sessionList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.past_workouts_fragment, container, false);
        final GridView gridView = rootView.findViewById(R.id.past_workouts_grid_view);

        final PastSessionsAdapter adapter = new PastSessionsAdapter(getContext(), sessionList);

        gridView.setAdapter(adapter);

        return rootView;
    }

    public static void setSessionList(List<Session> list) {
        sessionList = list;
    }
}
