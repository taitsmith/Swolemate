package com.taitsmith.swolemate.ui;

import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.SessionDetailAdapter;
import com.taitsmith.swolemate.data.Workout;

import java.util.ArrayList;
import java.util.List;

import static com.taitsmith.swolemate.dbutils.SessionCreator.createWorkoutList;

/**
 * Shows details of a selected past workout
 */

public class WorkoutDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Workout>> {
    TextView dateView;
    private RecyclerView recyclerView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.past_workout_detail_fragment, container, false);
        dateView = rootView.findViewById(R.id.summaryFragmentDateView);
        recyclerView = rootView.findViewById(R.id.workout_detail_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);

        LoaderManager.LoaderCallbacks<List<Workout>> callbacks = this;
        getActivity().getSupportLoaderManager().initLoader(59, null, callbacks);

        return rootView;
    }

    @Override
    public Loader<List<Workout>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Workout>>(getContext()) {
            List<Workout> workoutList = null;

            @Override
            protected void onStartLoading() {
                if (workoutList != null) {
                    deliverResult(workoutList);
                } else {
                    forceLoad();
                }
            }

            @Override
            public List<Workout> loadInBackground() {
                List<Workout> workoutList = new ArrayList<>();
                try {
                    workoutList = createWorkoutList(getContext(), null);
                } catch (CursorIndexOutOfBoundsException e) {
                    Log.d("LOG ", e.toString());
                }
                return workoutList;
            }

            @Override
            public void deliverResult(List<Workout> data) {
                workoutList = data;
                super.deliverResult(workoutList);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Workout>> loader, List<Workout> data) {
        final SessionDetailAdapter adapter = new SessionDetailAdapter(data);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<List<Workout>> loader) {

    }
}