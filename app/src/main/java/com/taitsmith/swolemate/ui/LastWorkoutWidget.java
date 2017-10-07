package com.taitsmith.swolemate.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.activities.SessionDetailActivity;
import com.taitsmith.swolemate.data.Workout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

import static com.taitsmith.swolemate.activities.SwolemateApplication.sortedDates;
import static com.taitsmith.swolemate.utils.HelpfulUtils.createWorkoutList;

/**
 * Implementation of App Widget functionality.
 */
public class LastWorkoutWidget extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Intent intent = new Intent(context, SessionDetailActivity.class);
        PendingIntent widgetPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.last_workout_widget);
        views.setTextViewText(R.id.widgetSessionsTextView, sortedDates.get(0));

        views.setOnClickPendingIntent(R.id.widgetWorkoutsTextView, widgetPendingIntent);

        List<Workout> workoutList = createWorkoutList(context, 0);

        views.setTextViewText(R.id.widgetWorkoutsTextView, Integer.toString(workoutList.size()));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void updateWidgetText(Context context, AppWidgetManager manager, int[] appWidgetIds) {

    }
}

