package com.bronytunes.app.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.bronytunes.app.BronyTunesApp;

import javax.inject.Inject;

/**
 * A content provider related to track storage and location.
 */
public class TrackProvider extends ContentProvider {

    public static final UriMatcher URI_MATCHER  = new UriMatcher(UriMatcher.NO_MATCH);
    public static final String     AUTHORITY    = "bronytunes";
    public static final String     TRACK        = "track";
    public static final String     TRACK_SEARCH = "search";

    private static final int CODE_TRACK        = 0;
    private static final int CODE_TRACK_SEARCH = 1;

    static {
        URI_MATCHER.addURI(AUTHORITY, TRACK + "/#", CODE_TRACK);
        URI_MATCHER.addURI(AUTHORITY, TRACK_SEARCH, CODE_TRACK_SEARCH);
    }

    @Inject
    TrackDatabaseHelper trackDB;

    @Override
    public boolean onCreate() {
        BronyTunesApp app = BronyTunesApp.get(getContext());
        app.inject(this);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (URI_MATCHER.match(uri)) {
            case CODE_TRACK:
                return trackDB.getTrack(Integer.valueOf(uri.getLastPathSegment()));
            case CODE_TRACK_SEARCH:
                SQLiteDatabase db = trackDB.getReadableDatabase();
                return db.query(
                        TrackDatabaseHelper.TABLE,
                        projection, selection, selectionArgs,
                        null, null, sortOrder);
        }
        throw new IllegalArgumentException("The provided URI doesn't match any known contracts");
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case CODE_TRACK:
                return "vnd.android.cursor.item/bronytunes-track";
            case CODE_TRACK_SEARCH:
                return "vnd.android.cursor.dir/bronytunes-track";
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = trackDB.insert(values);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = trackDB.getWritableDatabase();
        if (uri.getLastPathSegment().equals(TRACK)) {
            // We've been given selection arguments, so we should use this
            return db.delete(TrackDatabaseHelper.TABLE, selection, selectionArgs);
        } else {
            long id = Long.valueOf(uri.getLastPathSegment());
            return db.delete(TrackDatabaseHelper.TABLE, "id = ?", new String[]{String.valueOf(id)});
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return trackDB.saveTrack(values, selection, selectionArgs);
    }
}
