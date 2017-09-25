package com.taitsmith.swolemate.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.ui.InstructionDetailFragment;

public class InstructionDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_detail);
        int instruction;

        if (getIntent().hasExtra("INSTRUCTION_ID")) {
            instruction = getIntent().getIntExtra("INSTRUCTION_ID", 0);
            InstructionDetailFragment detailFragment = new InstructionDetailFragment();
            detailFragment.setInstruction(instruction);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .add(R.id.instruction_detail_fragment, detailFragment)
                    .commit();
        }
    }
}
