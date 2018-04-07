package com.taitsmith.swolemate.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.Person;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * For displaying a list of {@link com.taitsmith.swolemate.data.Person} in the
 * {@link com.taitsmith.swolemate.ui.BuddyListFragment}
 */

public class BuddyAdapter extends RecyclerView.Adapter<BuddyAdapter.BuddyHolder> {
    private List<Person> buddyList;
    private Context context;
    private int lastposition = -1;

    public BuddyAdapter(List<Person> buddyList, Context context) {
        this.buddyList = buddyList;
        this.context = context;
    }

    @Override
    public BuddyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.list_item_buddy, parent, false);

        return new BuddyHolder(view);
    }

    @Override
    public void onBindViewHolder(BuddyAdapter.BuddyHolder holder, int position) {
        holder.bind(position);
        setAnimation(holder.locationView, position);
    }

    @Override
    public int getItemCount() {
        return buddyList.size();
    }

    class BuddyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.buddyListNameView)
        TextView nameView;
        @BindView(R.id.buddyListLocation)
        TextView locationView;

        BuddyHolder(View buddyView) {
            super(buddyView);
            ButterKnife.bind(this, buddyView);
        }

        void bind(int position) {
            Person person = buddyList.get(position);

            nameView.setText(person.getName());
            locationView.setText(person.getCityLocation());
        }
    }

    private void setAnimation(View view, int position) {
        if (position > lastposition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            view.startAnimation(animation);
            lastposition = position;
        }
    }

    public void addItemsToList(List<Person> toAdd) {
        buddyList.clear();
        buddyList.addAll(toAdd);
        notifyDataSetChanged();
    }
}
