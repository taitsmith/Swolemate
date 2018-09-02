package com.taitsmith.swolemate.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.ui.InstructionDetailFragment;
import com.taitsmith.swolemate.ui.InstructionSummaryFragment;

public class InstructionSummaryActivity extends AppCompatActivity implements
        InstructionSummaryFragment.OnWorkoutClickListenter{
    private boolean isTwoPane;
    private InstructionDetailFragment detailFragment;
    private InstructionSummaryFragment summaryFragment;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_summary);
        summaryFragment = new InstructionSummaryFragment();
        detailFragment = new InstructionDetailFragment();
        manager = getSupportFragmentManager();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isTwoPane = findViewById(R.id.instruction_detail_fragment) != null;

        if (!isTwoPane) {
            manager.beginTransaction()
                    .add(R.id.instruction_list_fragment, summaryFragment)
                    .commit();
        } else {
            manager.beginTransaction()
                    .add(R.id.instruction_detail_fragment, detailFragment)
                    .add(R.id.instruction_list_fragment, summaryFragment)
                    .commit();
        }
    }

    @Override
    public void onWorkoutSelected(int position) {
        if (isTwoPane) {
            detailFragment = new InstructionDetailFragment();
            InstructionDetailFragment.setInstruction(position);
            manager.beginTransaction()
                    .add(R.id.instruction_detail_fragment, detailFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, InstructionDetailActivity.class);
            intent.putExtra("INSTRUCTION_ID", position);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return true;
    }
}
