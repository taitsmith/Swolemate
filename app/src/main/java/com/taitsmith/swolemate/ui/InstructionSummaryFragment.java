package com.taitsmith.swolemate.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.utils.WorkoutInstructionsAdapter;

/**
 * Fragment that displays a list of all workouts. Users can select a workout from the list to launch
 * a new activity (phone) or display a detail fragment (tablet) for instructions on how to perform
 * the selected workout.
 */

public class InstructionSummaryFragment extends Fragment {
    OnWorkoutClickListenter listenter;
    private TypedArray workoutArray;
    private WorkoutInstructionsAdapter adapter;

    public interface OnWorkoutClickListenter {
        void onWorkoutSelected(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listenter = (OnWorkoutClickListenter) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                + "Must implement OnWorkoutClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_instruction_list, container, false);

        workoutArray = getContext().getResources().obtainTypedArray(R.array.workout_list);

        final ListView listView = rootView.findViewById(R.id.instructionListRecycler);

        adapter = new WorkoutInstructionsAdapter(getContext(), workoutArray);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                listenter.onWorkoutSelected(position);
            }
        });

        return rootView;
    }
}
