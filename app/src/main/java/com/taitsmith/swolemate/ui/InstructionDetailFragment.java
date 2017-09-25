package com.taitsmith.swolemate.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.taitsmith.swolemate.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.taitsmith.swolemate.activities.SwolemateApplication.gifList;

/**
 * Fragment to display the specific instructions of a workout selected from the instruction list. Either
 * displayed in its own activity or in the detail of a master/detail on tablets.
 */

public class InstructionDetailFragment extends Fragment {
    @BindView(R.id.instructionDescriptionImageView)
    ImageView imageView;
    @BindView(R.id.instructionDetailWorkoutDescription)
    TextView descriptionView;
    @BindView(R.id.instructionDetailWorkoutName)
    TextView nameView;

    private static int instructionPosition;
    private int drawableInt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.instruction_detail_fragment, container, false);
        ButterKnife.bind(this, rootView);

        if (savedInstanceState == null) {
            drawableInt = gifList.get(instructionPosition);

        } else {
            if (savedInstanceState.containsKey("POSITION")) {
                instructionPosition = savedInstanceState.getInt("POSITION");
                drawableInt = gifList.get(instructionPosition);
            }
        }

        Glide.with(this).load(drawableInt).into(imageView);

        return rootView;
    }

    public static void setInstruction(int position) {
        instructionPosition = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("POSITION", instructionPosition);
    }
}
