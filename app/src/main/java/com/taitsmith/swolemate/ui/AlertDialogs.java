package com.taitsmith.swolemate.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.activities.AddWorkoutActivity;
import com.taitsmith.swolemate.activities.MainActivity;
import com.taitsmith.swolemate.utils.HelpfulUtils;

import static com.taitsmith.swolemate.activities.AddWorkoutActivity.saveWorkout;


/**
 * Alert dialogs to talk to the user, inform them of what's happening, etc.
 */

public class AlertDialogs {
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 54;
    private static AlertDialog.Builder builder;

    public static void cancelAddWorkoutDialog(final Context context) {
        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);

        builder.setMessage(context.getString(R.string.dialog_cancel_add_workout));

        builder.setPositiveButton(context.getString(R.string.dialog_cancel_positive),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });

        builder.setNegativeButton(context.getString(R.string.dialog_cancel_negative), null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void saveWorkoutDialog(final Context context, String name, String thoughts, int reps,
                                         int sets, int weights) {
        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);

        StringBuilder messageString = new StringBuilder();

        messageString.append(context.getString(R.string.dialog_save_workout))
                .append(name.concat("\n"))
                .append("Sets: ".concat(Integer.toString(sets)).concat("\n"))
                .append("Reps: " .concat(Integer.toString(reps)).concat("\n"))
                .append("Weight: ".concat(Integer.toString(weights)).concat("\n"));

        if (thoughts.isEmpty()) {
            messageString.append("No thoughts on this workout");
        } else {
            messageString.append(thoughts.concat("\n"));
        }

        String message = messageString.toString();

        builder.setMessage(message);

        builder.setPositiveButton(context.getString(R.string.dialog_save_positive),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveWorkout(context);
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });

        builder.setNegativeButton(context.getString(R.string.dialog_save_negative), null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void informPermissionsDialog(final Activity activity) {
        builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);

        builder.setMessage(activity.getString(R.string.inform_permissions_message));
        builder.setPositiveButton(activity.getString(R.string.inform_permission_positive),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                PERMISSION_REQUEST_FINE_LOCATION);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void aboutDialog(Context context) {
        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);

        builder.setMessage(context.getString(R.string.about_message));
        builder.setPositiveButton(context.getString(R.string.dialog_got_it), null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void weeklySummaryDialog(Context context, int[] counts) {
        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);

        builder.setMessage(context.getString(R.string.summary_message, counts[0], counts[1]));
        builder.setPositiveButton(context.getString(R.string.dialog_got_it), null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void weeklySummaryNoWorkoutsDialog(final Context context) {
        builder = new AlertDialog.Builder(context);

        builder.setMessage(context.getString(R.string.dialog_summary_no_workouts));
        builder.setPositiveButton(context.getString(R.string.dialog_summary_add_workout), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, AddWorkoutActivity.class);
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton(context.getString(R.string.dialog_summary_dont_add_workout), null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void deleteSessionDialog(final Context context, final int position) {
        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);


        builder.setMessage(context.getString(R.string.dialog_delete_message));
        builder.setPositiveButton(context.getString(R.string.dialog_delete_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HelpfulUtils.deleteSessionAndWorkouts(position, context);
            }
        });
        builder.setNegativeButton(context.getString(R.string.dialog_delete_negative), null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void deleteGymLocationDialog(final Context context) {
        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);

        builder.setMessage(context.getString(R.string.dialog_delete_gym_message));
        builder.setPositiveButton(context.getString(R.string.dialog_delete_gym_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
}
