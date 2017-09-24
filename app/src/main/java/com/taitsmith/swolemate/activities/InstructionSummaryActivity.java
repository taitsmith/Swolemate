package com.taitsmith.swolemate.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.ui.InstructionSummaryFragment;

public class InstructionSummaryActivity extends AppCompatActivity {
    private InstructionSummaryFragment instructionSummaryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_summary);

        instructionSummaryFragment = new InstructionSummaryFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .add(R.id.instruction_fragment, instructionSummaryFragment)
                .commit();
    }
}
