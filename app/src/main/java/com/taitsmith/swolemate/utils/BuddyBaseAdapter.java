package com.taitsmith.swolemate.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.Person;

import java.util.List;

public class BuddyBaseAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Person> buddyList;

    public BuddyBaseAdapter(Context context,List<Person> buddyList) {
        this.buddyList = buddyList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return buddyList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.list_item_buddy, null);

            holder = new ViewHolder();

            view.setTag(holder);

            holder.buddyNameView = view.findViewById(R.id.buddyListNameView);
            holder.buddyLocationView = view.findViewById(R.id.buddyListLocation);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Person buddy = buddyList.get(i);

        holder.buddyNameView.setText(buddy.getName());
        holder.buddyLocationView.setText(buddy.getCityLocation());

        return view;
    }

    private class ViewHolder {
        TextView buddyNameView;
        TextView buddyLocationView;
    }
}
