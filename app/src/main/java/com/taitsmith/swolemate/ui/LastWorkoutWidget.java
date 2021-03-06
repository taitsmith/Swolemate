package com.taitsmith.swolemate.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.activities.MainActivity;

import static com.taitsmith.swolemate.activities.SwolemateApplication.sortedDates;

/**
 * Implementation of App Widget functionality.
 */
public class LastWorkoutWidget extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("FROM_WIDGET", true);
        PendingIntent widgetPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.last_workout_widget);
        views.setTextViewText(R.id.widgetSessionsTextView, sortedDates.get(0));

        views.setOnClickPendingIntent(R.id.lastWorkoutWidget, widgetPendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //done in updateWidgetText
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(context, LastWorkoutWidget.class));
        updateWidgetText(context, manager, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void updateWidgetText(Context context, AppWidgetManager manager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, manager, appWidgetId);
        }
    }
}

