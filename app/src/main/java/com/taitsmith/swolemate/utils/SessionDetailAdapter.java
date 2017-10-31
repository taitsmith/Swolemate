package com.taitsmith.swolemate.utils;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.Workout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

import static android.R.string.no;
import static com.taitsmith.swolemate.activities.SwolemateApplication.realmConfiguration;


/**
 * Recyclerview to hold each individual {@link Workout} that makes up a session.
 */

public class SessionDetailAdapter extends RecyclerView.Adapter<SessionDetailAdapter.WorkoutHolder>{

    private List<Workout> workoutList;

    public SessionDetailAdapter(List<Workout> workoutList){
        this.workoutList = workoutList;
    }

    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    @Override
    public WorkoutHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.workout_detail_list_item, parent, false);

        return new WorkoutHolder(view);
    }

    class WorkoutHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.detailListRepsView)
        TextView repsView;
        @BindView(R.id.detailListSetsView)
        TextView setsView;
        @BindView(R.id.detailListThoughtsView)
        TextView thoughtsView;
        @BindView(R.id.detailListWorkoutNameView)
        TextView nameView;
        @BindView(R.id.detailListWeightView)
        TextView weightView;

        WorkoutHolder(View workoutview){
            super(workoutview);
            ButterKnife.bind(this, workoutview);
        }

        void bind(int position) {
            Workout workout = workoutList.get(position);
            repsView.setText(Integer.toString(workout.getReps()));
            setsView.setText(Integer.toString(workout.getSets()));
            thoughtsView.setText(workout.getThoughts());
            nameView.setText(workout.getName());
            weightView.setText(Integer.toString(workout.getWeight()));
        }
    }

    @Override
    public void onBindViewHolder(WorkoutHolder holder, int position) {
        holder.bind(position);
    }
}
