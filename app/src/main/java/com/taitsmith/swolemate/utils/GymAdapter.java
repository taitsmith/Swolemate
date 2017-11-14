package com.taitsmith.swolemate.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.GymLocation;

import io.realm.RealmResults;

/**
 * For displaying list of saved gym locations in the My Details fragment
 */

public class GymAdapter extends BaseAdapter {
    private RealmResults<GymLocation> gymList;
    private LayoutInflater inflater;

    public GymAdapter(Context context, RealmResults<GymLocation> gymList) {
        this.gymList = gymList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return gymList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = null;

        if (view == null) {
            view = inflater.inflate(R.layout.list_item_gym, null);
            holder = new ViewHolder();

            holder.gymName = view.findViewById(R.id.gymNameTextView);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        GymLocation location = gymList.get(position);

        holder.gymName.setText(location.getPlaceName());

        return view;
    }

    private class ViewHolder {
        TextView gymName;
    }
}
