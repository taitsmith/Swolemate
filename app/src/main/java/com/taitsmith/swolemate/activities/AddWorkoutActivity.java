package com.taitsmith.swolemate.activities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.taitsmith.swolemate.R;

import java.time.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_DATE;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_REPS;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_SETS;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_WEIGHT;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_WORKOUT_NAME;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.CONTENT_URI;

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
    @BindView(R.id.doneButton)
    Button doneButton;

    private String date, thoughts, name;
    private int weight, sets, reps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);
        ButterKnife.bind(this);

        date = LocalDate.now().toString().substring(6);

        dateTv.setText(getString(R.string.add_workout_date, date));
    }

    @OnClick(R.id.doneButton)
    public void saveWorkout() {
        if (!inputIsValid()) {
            Toast.makeText(this, getString(R.string.double_check_toast), Toast.LENGTH_SHORT).show();
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_DATE, date);
            contentValues.put(COLUMN_WORKOUT_NAME, name);
            contentValues.put(COLUMN_WEIGHT, weight);
            contentValues.put(COLUMN_REPS, reps);
            contentValues.put(COLUMN_SETS, sets);

            ContentResolver resolver = getContentResolver();

            resolver.insert(CONTENT_URI, contentValues);
        }
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
