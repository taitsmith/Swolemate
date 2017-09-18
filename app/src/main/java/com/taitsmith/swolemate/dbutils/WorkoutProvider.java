package com.taitsmith.swolemate.dbutils;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.taitsmith.swolemate.R;

import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.CONTENT_AUTHORITY;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.COLUMN_DATE;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.CONTENT_URI;
import static com.taitsmith.swolemate.dbutils.WorkoutDbContract.WorkoutEntry.TABLE_NAME;


/**
 * Content provider for ezpz {@link WorkoutDbContract} access
 */

@SuppressWarnings("ConstantConditions") //getContentResolver() likes to give a NPE warning.
public class WorkoutProvider extends ContentProvider {
    WorkoutDbHelper dbHelper;

    public static final String UNKNOWN_URI = "Unknown Uri: ";

    public static final int CODE_WORKOUT = 100;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(CONTENT_AUTHORITY, TABLE_NAME, CODE_WORKOUT);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new WorkoutDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        //nothing to see here...
        return null;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowUpdated;
        int match = uriMatcher.match(uri);

        switch (match) {
            case CODE_WORKOUT:
                rowUpdated = db.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }

        if (rowUpdated != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowUpdated;
    }

    //TODO maybe it makes more sense to save everything and then bulk insert upon session completion?
    //We'll try this and then if it's clunky and inefficient we'll switch in the next version.
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri;
        int match = uriMatcher.match(uri);

        switch (match) {
            case CODE_WORKOUT:
                long id = db.insert(TABLE_NAME, null, contentValues);

                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        Toast.makeText(getContext(), getContext().getString(R.string.successful_save_toast),
                Toast.LENGTH_SHORT).show();

        return  returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted;
        int match = uriMatcher.match(uri);

        switch (match) {
            case CODE_WORKOUT:
                rowsDeleted = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }
        Log.d("LOG ", "DELETED");
        return rowsDeleted;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor returnCursor;
        int match = uriMatcher.match(uri);

        switch (match) {
            case CODE_WORKOUT:
                returnCursor = db.query(TABLE_NAME,
                        projection,
                        COLUMN_DATE + "=?", //we want workouts grouped by same date to display as
                        selectionArgs, //a completed 'session'
                        null,
                        null,
                        null);
                break;
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return returnCursor;
    }
}
