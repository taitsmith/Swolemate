package com.taitsmith.swolemate.ui;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.WorkoutInstructionsAdapter;

/**
 * Fragment that displays a list of all workouts. Users can select a workout from the list to launch
 * a new activity (phone) or display a detail fragment (tablet) for instructions on how to perform
 * the selected workout.
 */

public class InstructionSummaryFragment extends Fragment {
    private TypedArray workoutArray;
    private WorkoutInstructionsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.instruction_list_fragment, container, false);

        workoutArray = getContext().getResources().obtainTypedArray(R.array.workout_list);

        ListView listView = rootView.findViewById(R.id.instructionListRecycler);

        adapter = new WorkoutInstructionsAdapter(getContext(), workoutArray);

        listView.setAdapter(adapter);

        return rootView;
    }
}
