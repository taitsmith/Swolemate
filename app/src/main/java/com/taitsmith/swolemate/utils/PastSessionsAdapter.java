package com.taitsmith.swolemate.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.data.Session;

import io.realm.RealmResults;

/**
 * Holds all of the data for past workout {@link Session} to be displayed in the main activity fragment.
 *
 */

public class PastSessionsAdapter extends BaseAdapter {
    private RealmResults<Session> sessions;
    private LayoutInflater inflater;

    public PastSessionsAdapter(Context context, RealmResults<Session> sessions){
        this.sessions = sessions;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return sessions.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;

        if (view == null) {
            view = inflater.inflate(R.layout.session_list_item, null);

            holder = new ViewHolder();

            holder.workoutDate = view.findViewById(R.id.sessionDateView);
            holder.workoutsCompleted = view.findViewById(R.id.sessionWorkoutsCompleteView);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Session session = sessions.get(i);
        String s = Integer.toString(session.getWorkoutCount());

        holder.workoutDate.setText(session.getDate());
        holder.workoutsCompleted.setText(s);

        return view;
    }

    private class ViewHolder {
        TextView workoutDate;
        TextView workoutsCompleted;
    }
}
