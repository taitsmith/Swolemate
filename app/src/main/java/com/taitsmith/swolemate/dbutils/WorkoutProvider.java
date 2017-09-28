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


/**
 * Content provider for ezpz {@link WorkoutDbContract} access
 */

@SuppressWarnings("ConstantConditions") //getContentResolver() likes to give a NPE warning.
public class WorkoutProvider extends ContentProvider {
    WorkoutDbHelper dbHelper;

    public static final String UNKNOWN_URI = "Unknown Uri: ";

    public static final int CODE_WORKOUT = 100;
    public static final int CODE_LOCATION = 101;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(CONTENT_AUTHORITY, WorkoutDbContract.WorkoutEntry.TABLE_NAME, CODE_WORKOUT);
        matcher.addURI(CONTENT_AUTHORITY, WorkoutDbContract.GymLocationEntry.TABLE_NAME, CODE_LOCATION);

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
                rowUpdated = db.update(WorkoutDbContract.WorkoutEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case CODE_LOCATION:
                rowUpdated = db.update(WorkoutDbContract.GymLocationEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
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
        long id;

        switch (match) {
            case CODE_WORKOUT:
                id = db.insert(WorkoutDbContract.WorkoutEntry.TABLE_NAME,
                        null, contentValues);
                break;
            case CODE_LOCATION:
                id = db.insert(WorkoutDbContract.GymLocationEntry.TABLE_NAME,
                        null,
                        contentValues);
                break;
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }

        if (id > 0) {
            returnUri = ContentUris.withAppendedId(CONTENT_URI, 0);
        } else {
            throw new SQLException("Failed to insert into: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return  returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted;
        int match = uriMatcher.match(uri);

        switch (match) {
            case CODE_WORKOUT:
                rowsDeleted = db.delete(WorkoutDbContract.WorkoutEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_LOCATION:
                rowsDeleted = db.delete(WorkoutDbContract.GymLocationEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
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
                returnCursor = db.query(WorkoutDbContract.WorkoutEntry.TABLE_NAME,
                        projection,
                        COLUMN_DATE + "=?", //we want workouts grouped by same date to display as
                        selectionArgs, //a completed 'session'
                        null,
                        null,
                        null);
                break;
            case CODE_LOCATION:
                returnCursor = db.query(WorkoutDbContract.GymLocationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
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
