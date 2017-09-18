package com.taitsmith.swolemate.data;

import android.content.Context;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taitsmith.swolemate.R;

import java.util.List;

/**
 * Holds all of the data for past sessionsv to be displayed in the main activity fragment.
 *
 */

public class PastSessionsAdapter extends BaseAdapter {
    private Context context;
    private List<Session> sessions;
    private LayoutInflater inflater;

    public PastSessionsAdapter(Context context, List<Session> sessions){
        this.context = context;
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
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Session session = sessions.get(i);

        holder.workoutDate.setText(session.getDate());

        return view;
    }

    private class ViewHolder {
        TextView workoutDate;
    }

    public List<Session> createSessions(Context context) {
        return null;
    }
}
