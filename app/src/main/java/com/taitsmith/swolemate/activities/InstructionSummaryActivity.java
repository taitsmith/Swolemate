package com.taitsmith.swolemate.activities;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.ui.InstructionSummaryFragment;

import java.io.IOException;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.taitsmith.swolemate.activities.SwolemateApplication.gifList;

public class InstructionSummaryActivity extends AppCompatActivity implements
        InstructionSummaryFragment.OnWorkoutClickListenter{
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_summary);
        imageView = (ImageView) findViewById(R.id.testGifView);

        InstructionSummaryFragment instructionSummaryFragment = new InstructionSummaryFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .add(R.id.instruction_fragment, instructionSummaryFragment)
                .commit();
    }

    @Override
    public void onWorkoutSelected(int position) {
        Glide.with(this).asGif().load(R.drawable.benchpress).into(imageView);
    }
}
