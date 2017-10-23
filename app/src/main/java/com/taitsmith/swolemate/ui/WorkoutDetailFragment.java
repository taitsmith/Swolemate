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
import io.realm.Realm;
import io.realm.RealmResults;

import static android.R.attr.data;
import static com.taitsmith.swolemate.activities.SwolemateApplication.realmConfiguration;
import static com.taitsmith.swolemate.activities.SwolemateApplication.sessionList;
import static com.taitsmith.swolemate.utils.HelpfulUtils.createWorkoutList;

/**
 * Shows details of a selected past workout
 */

public class WorkoutDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<RealmResults<Workout>> {
    @BindView(R.id.workout_detail_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.detailFragmentProgress)
    ProgressBar progressBar;
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    static String sessionDate;


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
            sessionDate = savedInstanceState.getString("DATE");
        }

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        LoaderManager.LoaderCallbacks<RealmResults<Workout>> callbacks = this;
        getActivity().getSupportLoaderManager().restartLoader(31, null, callbacks);
    }

    @Override
    public Loader<RealmResults<Workout>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<RealmResults<Workout>>(getContext()) {
            RealmResults<Workout> workoutList = null;

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
            public RealmResults<Workout> loadInBackground() {
                Realm realm = Realm.getInstance(realmConfiguration);

                try {
                    return realm.where(Workout.class)
                            .equalTo("date", sessionDate)
                            .findAllAsync();
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            public void deliverResult(RealmResults<Workout> data) {
                workoutList = data;
                super.deliverResult(workoutList);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<RealmResults<Workout>> loader, RealmResults<Workout> data) {
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
    public void onLoaderReset(Loader<RealmResults<Workout>> loader) {
        //just taking up space
    }

    public static void setSessionDate(String date) {
        sessionDate = date;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("DATE", sessionDate);
    }
}