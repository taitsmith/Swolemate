package com.taitsmith.swolemate.activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.Workout;
import com.taitsmith.swolemate.ui.AlertDialogs;
import com.taitsmith.swolemate.ui.LastWorkoutWidget;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

import static com.taitsmith.swolemate.activities.SwolemateApplication.realmConfiguration;
import static com.taitsmith.swolemate.ui.LastWorkoutWidget.updateWidgetText;
import static com.taitsmith.swolemate.utils.DbContract.WorkoutEntry.COLUMN_DATE;
import static com.taitsmith.swolemate.utils.DbContract.WorkoutEntry.COLUMN_REPS;
import static com.taitsmith.swolemate.utils.DbContract.WorkoutEntry.COLUMN_SETS;
import static com.taitsmith.swolemate.utils.DbContract.WorkoutEntry.COLUMN_THOUGHTS;
import static com.taitsmith.swolemate.utils.DbContract.WorkoutEntry.COLUMN_WEIGHT;
import static com.taitsmith.swolemate.utils.DbContract.WorkoutEntry.COLUMN_WORKOUT_NAME;
import static com.taitsmith.swolemate.utils.DbContract.WorkoutEntry.CONTENT_URI;
import static com.taitsmith.swolemate.utils.HelpfulUtils.createOrUpdateSession;
import static com.taitsmith.swolemate.utils.HelpfulUtils.createSessionList;
import static com.taitsmith.swolemate.utils.HelpfulUtils.getFormattedDate;

public class AddWorkoutActivity extends AppCompatActivity {
    @BindView(R.id.addWorkoutDateTv)
    TextView dateTv;
    @BindView(R.id.selectWorkoutSpinner)
    Spinner workoutSpinner;
    @BindView(R.id.workoutAddSets)
    TextInputEditText workoutSets;
    @BindView(R.id.workoutWeightUsed)
    TextInputEditText workoutWeight;
    @BindView(R.id.workoutAddReps)
    TextInputEditText workoutReps;
    @BindView(R.id.workoutThoughts)
    TextInputEditText workoutThoughts;
    @BindView(R.id.thoughtsLayout)
    TextInputLayout thoughtsLayout;
    @BindView(R.id.saveButton)
    Button saveButton;
    @BindView(R.id.cancelButton)
    Button cancelButton;

    private static String date, thoughts, name;
    private static int weight, sets, reps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);
        ButterKnife.bind(this);

        date = getFormattedDate("long");

        dateTv.setText(getString(R.string.add_workout_date, getFormattedDate("short")));
    }

    @OnClick(R.id.saveButton)
    public void showConfirmation() {
        if (!inputIsValid()) {
            Toast.makeText(this, getString(R.string.double_check_toast), Toast.LENGTH_SHORT).show();
        } else {
            getInputData();
            AlertDialogs.saveWorkoutDialog(this, name, thoughts, reps, sets, weight);
        }
    }

    public static void saveWorkout(Context context) {
        Realm realm = Realm.getInstance(realmConfiguration);
        realm.beginTransaction();
        Workout workout = realm.createObject(Workout.class);

        workout.setDate(date);
        workout.setName(name);
        workout.setWeight(weight);
        workout.setReps(reps);
        workout.setSets(sets);
        workout.setThoughts(thoughts);

        realm.commitTransaction();

        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] widgetIds = manager.getAppWidgetIds(new ComponentName(context, LastWorkoutWidget.class));

        updateWidgetText(context, manager, widgetIds);
        createOrUpdateSession(date);
    }

    @OnClick(R.id.cancelButton)
    public void showCancelDialog() {
        AlertDialogs.cancelAddWorkoutDialog(this);
    }

    public boolean inputIsValid() {
        if (workoutSets.getText().toString().isEmpty()) {
            workoutSets.setError(getString(R.string.blank_input_error));
            return false;
        } else if (workoutWeight.getText().toString().isEmpty()) {
            workoutWeight.setError(getString(R.string.blank_input_error));
            return false;
        } else if (workoutReps.getText().toString().isEmpty()) {
            workoutReps.setError(getString(R.string.blank_input_error));
            return false;
        }

        getInputData();
        return true;
    }

    public void getInputData() {
        weight = Integer.parseInt(workoutWeight.getText().toString());
        sets = Integer.parseInt(workoutSets.getText().toString());
        reps = Integer.parseInt(workoutReps.getText().toString());
        thoughts = workoutThoughts.getText().toString();
        name = workoutSpinner.getSelectedItem().toString();
    }
}
