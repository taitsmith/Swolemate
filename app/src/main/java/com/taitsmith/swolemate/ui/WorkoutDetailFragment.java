package com.taitsmith.swolemate.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.SessionDetailAdapter;
import com.taitsmith.swolemate.data.Workout;

import java.util.List;

/**
 * Shows details of a selected past workout
 */

public class WorkoutDetailFragment extends Fragment {
    TextView dateView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.workout_detail_fragment, container, false);
        dateView = rootView.findViewById(R.id.summaryFragmentDateView);
        RecyclerView recyclerView = rootView.findViewById(R.id.workout_detail_recycler);

        return rootView;
    }

    public static void set(List<Workout> workoutList, Context context) {
        SessionDetailAdapter adapter  = new SessionDetailAdapter(context, workoutList);
        RecyclerView recyclerView;
    }
}
