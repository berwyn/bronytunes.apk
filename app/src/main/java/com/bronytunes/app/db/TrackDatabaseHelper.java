package com.bronytunes.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bronytunes.model.Song;

import timber.log.Timber;

/**
 * Created by berwyn on 17.01.15.
 */
public class TrackDatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE = "tracks";

    public static final String COLUMN_ID          = "id";
    public static final String COLUMN_NAME        = "name";
    public static final String COLUMN_ARTIST_ID   = "artistId";
    public static final String COLUMN_ALBUM_NAME  = "albumName";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_LYRICS      = "lyrics";

    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_STMT =
            "CREATE TABLE " + TABLE + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_ARTIST_ID + " INTEGER," +
                    COLUMN_ALBUM_NAME + " INTEGER," +
                    COLUMN_DESCRIPTION + " TEXT," +
                    COLUMN_LYRICS + " TEXT" +
                    ");";

    public TrackDatabaseHelper(Context context) {
        super(context, TABLE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STMT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion > DATABASE_VERSION) {
            // lolwtf? We only have one version :D
            Timber.e("This can't happen D: - DB version " + oldVersion);
        } else {
            // This is only useful for <= 1
            db.execSQL("DROP TABLE IF EXISTS " + TABLE + ";");
        }
    }

    /**
     * Given a list of tracks, save them to the database
     *
     * @param songs The songs to save to the DB
     */
    public void saveTrack(Song... songs) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (Song song : songs) {
                ContentValues cv = new ContentValues(6);

                cv.put("id", song.id);
                cv.put("name", song.name);
                cv.put("artistId", song.artistId);
                cv.put("albumName", song.albumName);
                cv.put("description", song.description);
                cv.put("lyrics", song.lyrics);

                db.update(TABLE, cv, null, null);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * A convenience method for content providers to store prepared
     * content values in the track database. This is, essentially,
     * a wrapper for
     * {@link android.database.sqlite.SQLiteDatabase#update(String, android.content.ContentValues, String, String[])}
     *
     * @param contentValues The content values to store, they should be for a {@link com.bronytunes.model.Song}
     */
    public int saveTrack(ContentValues contentValues, String selection, String[] selectionArgs) {
        int retVal = 0;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            retVal = db.update(TABLE, contentValues, selection, selectionArgs);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return retVal;
    }

    /**
     * A convenience method that inserts a given set of content values into the underlying
     * {@link android.database.sqlite.SQLiteDatabase}
     *
     * @param cv The values to insert
     * @return The id of the inserted row
     */
    public long insert(ContentValues cv) {
        return getWritableDatabase().insert(TABLE, null, cv);
    }

    public Cursor getTrack(int id) {
        return getReadableDatabase().query(
                TABLE,
                new String[]{"id", "name", "artistId", "albumName", "description", "lyrics"},
                null, null, null, null, null
        );
    }
}
