package com.taitsmith.swolemate.data;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taitsmith.swolemate.R;

/**
 * To display list of workouts, allows users to select one and view its contents.
 */

public class WorkoutInstructionsAdapter extends BaseAdapter{
    private TypedArray workoutList;
    private Context context;
    private LayoutInflater inflater;

    public WorkoutInstructionsAdapter(Context context, TypedArray workoutList) {
        this.context = context;
        this.workoutList = workoutList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return workoutList.length();
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;

        if (view == null) {
            view = inflater.inflate(R.layout.workout_instruction_list_item, null);

            holder = new ViewHolder();

            holder.workoutName = view.findViewById(R.id.instructionListWorkoutName);
            holder.workoutType = view.findViewById(R.id.instructionListWorkoutType);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        holder.workoutName.setText(workoutList.getString(i));
        holder.workoutType.setText("get huge muscles");

        return view;
    }

    private class ViewHolder {
        TextView workoutName;
        TextView workoutType;
    }
}
