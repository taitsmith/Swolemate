package com.taitsmith.swolemate.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taitsmith.swolemate.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Recyclerview to hold each individual {@link Workout} that makes up a session.
 */

public class SessionDetailAdapter extends RecyclerView.Adapter<SessionDetailAdapter.WorkoutHolder>{
    private Context context;
    private List<Workout> workoutList;

    public SessionDetailAdapter(Context context, List<Workout> workoutList){
        this.workoutList = workoutList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    @Override
    public WorkoutHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.detail_list_item, parent, false);

        return new WorkoutHolder(view);
    }

    class WorkoutHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.detailListReps)
        TextView repsView;
        @BindView(R.id.detailListSets)
        TextView setsView;
        @BindView(R.id.detailListThoughts)
        TextView thoughtsView;
        @BindView(R.id.detailListWorkoutName)
        TextView nameView;
        @BindView(R.id.detailListWeight)
        TextView weightView;

        public WorkoutHolder(View workoutview){
            super(workoutview);
            ButterKnife.bind(context, workoutview);
        }

        void bind(int position) {
            Workout workout = workoutList.get(position);
            repsView.setText(workout.getReps());
            setsView.setText(workout.getSets());
            thoughtsView.setText(workout.getThoughts());
            nameView.setText(workout.getName());
            weightView.setText(workout.getWeight());
        }

    }

    @Override
    public void onBindViewHolder(WorkoutHolder holder, int position) {
        holder.bind(position);
    }
}
