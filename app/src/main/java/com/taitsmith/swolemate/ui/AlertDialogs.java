package com.taitsmith.swolemate.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.taitsmith.swolemate.R;
import com.taitsmith.swolemate.activities.MainActivity;

import static com.taitsmith.swolemate.activities.AddWorkoutActivity.saveWorkout;


/**
 * Alert dialogs to confirm save/delete workout data
 */

public class AlertDialogs {
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 54;

    public static void cancelAddWorkoutDialog(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(context.getString(R.string.cancel_add_workout));
        builder.setCancelable(false);

        builder.setPositiveButton(context.getString(R.string.cancel_positive),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });

        builder.setNegativeButton(context.getString(R.string.cancel_negative),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "Workout data will be kept", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void saveWorkoutDialog(final Context context, String name, String thoughts, int reps,
                                         int sets, int weights) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        StringBuilder messageString = new StringBuilder();

        messageString.append(context.getString(R.string.save_workout))
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

        builder.setCancelable(false);

        builder.setPositiveButton(context.getString(R.string.save_positive),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveWorkout();
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });

        builder.setNegativeButton(context.getString(R.string.save_negative),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                builder.create().cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void informPermissions(final Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

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
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(context.getString(R.string.about_message));
        builder.setPositiveButton(context.getString(R.string.about_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                builder.create().cancel();
            }
        });

        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
