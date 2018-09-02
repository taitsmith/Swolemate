package com.taitsmith.swolemate.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.utils.SessionDetailAdapter;
import com.taitsmith.swolemate.data.Workout;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.taitsmith.swolemate.activities.SwolemateApplication.realmConfiguration;
import static com.taitsmith.swolemate.utils.HelpfulUtils.workoutListSwipeHandler;

/**
 * Shows details of a selected past workout.
 */

public class WorkoutDetailFragment extends Fragment {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_past_workout_detail, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            sessionDate = savedInstanceState.getString("DATE");
        }

        final Realm realm = Realm.getInstance(realmConfiguration);
        final RealmResults<Workout> realmResults =  realm.where(Workout.class)
                .equalTo("date", sessionDate)
                .findAll();

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        final SessionDetailAdapter adapter = new SessionDetailAdapter(realmResults, getContext());
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback helper = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Workout workout = realmResults.get(viewHolder.getAdapterPosition());
                workoutListSwipeHandler(workout);
                adapter.notifyItemChanged(viewHolder.getAdapterPosition());
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
        };

        ItemTouchHelper ith = new ItemTouchHelper(helper);

        ith.attachToRecyclerView(recyclerView);

        if (toolbar != null) {
            toolbar.setTitle(realmResults.get(0).getDate());
        }
        progressBar.setVisibility(View.INVISIBLE);
        return rootView;
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