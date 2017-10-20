package com.taitsmith.swolemate.ui;

import android.content.Context;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.SessionDetailAdapter;
import com.taitsmith.swolemate.data.Workout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.taitsmith.swolemate.utils.HelpfulUtils.createWorkoutList;

/**
 * Shows details of a selected past workout
 */

public class WorkoutDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Workout>> {
    @BindView(R.id.workout_detail_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.detailFragmentProgress)
    ProgressBar progressBar;
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    static int sessionPosition;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_past_workout_detail, container, false);
        ButterKnife.bind(this, rootView);

        if (savedInstanceState != null) {
            sessionPosition = savedInstanceState.getInt("POSITION");
        }

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        LoaderManager.LoaderCallbacks<List<Workout>> callbacks = this;
        getActivity().getSupportLoaderManager().restartLoader(31, null, callbacks);
    }

    @Override
    public Loader<List<Workout>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Workout>>(getContext()) {
            List<Workout> workoutList = null;

            @Override
            protected void onStartLoading() {
                progressBar.setVisibility(View.VISIBLE);
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
                    workoutList = createWorkoutList(getContext(), sessionPosition);
                } catch (CursorIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {
                    Log.e("Loader error: ", e.toString());
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
        if (data != null && data.size() != 0) {
            final SessionDetailAdapter adapter = new SessionDetailAdapter(data);
            recyclerView.setAdapter(adapter);
            if (toolbar != null) {
                toolbar.setTitle(data.get(0).getDate());
            }
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            toolbar.setTitle(getString(R.string.workout_summary_error));
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Workout>> loader) {
        //just taking up space
    }

    public static void setSessionPosition(int position) {
        sessionPosition = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("POSITION", sessionPosition);
    }
}