package com.taitsmith.swolemate.activities;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.ui.AlertDialogs;

import java.time.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_DATE;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_REPS;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_SETS;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_THOUGHTS;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_WEIGHT;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_WORKOUT_NAME;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.CONTENT_URI;
import static java.security.AccessController.getContext;

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

    private static ContentResolver resolver;

    private static String date, thoughts, name;
    private static int weight, sets, reps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);
        ButterKnife.bind(this);

        resolver = getContentResolver();

        date = LocalDate.now().toString();
        Log.d("LOG: ", date);

        dateTv.setText(getString(R.string.add_workout_date, date.substring(6)));
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

    public static void saveWorkout() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_WORKOUT_NAME, name);
        contentValues.put(COLUMN_WEIGHT, weight);
        contentValues.put(COLUMN_REPS, reps);
        contentValues.put(COLUMN_SETS, sets);
        contentValues.put(COLUMN_THOUGHTS, thoughts);

        resolver.insert(CONTENT_URI, contentValues);
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
