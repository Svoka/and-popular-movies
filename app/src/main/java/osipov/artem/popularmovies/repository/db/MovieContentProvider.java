package osipov.artem.popularmovies.repository.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import osipov.artem.popularmovies.repository.db.PopularMoviesContract.MovieEntry;

/**
 * Created by Artem Osipov on 26/02/2018.
 * ao@enlighted.ru
 */

public class MovieContentProvider extends ContentProvider {



    private static final int CODE_MOVIE_DIR = 100;
    private static final int CODE_MOVIE_ITEM = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private SQLiteDatabase mDatabase;

    private static final UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PopularMoviesContract.AUTHORITY, PopularMoviesContract.PATH_MOVIES, CODE_MOVIE_DIR);
        uriMatcher.addURI(PopularMoviesContract.AUTHORITY, PopularMoviesContract.PATH_MOVIES + "/#", CODE_MOVIE_ITEM);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        PopularMoviesDbHelper mPopularMoviesDbHelper = new PopularMoviesDbHelper(getContext());
        mDatabase = mPopularMoviesDbHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
            @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = sUriMatcher.match(uri);

        if (code >= CODE_MOVIE_DIR && code <= CODE_MOVIE_ITEM) {

            final Context context = getContext();
            if (context == null) {
                return null;
            }

            switch (code) {
                case CODE_MOVIE_DIR:
                    return mDatabase.query(MovieEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                case CODE_MOVIE_ITEM:
                    String id = uri.getPathSegments().get(1);
                    String mSelection = "_id=?";
                    String[] mSelectionArgs = new String[]{id};
                    return mDatabase.query(MovieEntry.TABLE_NAME, projection, mSelection, mSelectionArgs, null, null, sortOrder);
                default:
                    return null;
            }

        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);

        switch (match) {
            case CODE_MOVIE_DIR:
                // directory
                return "vnd.android.cursor.dir" + "/" + PopularMoviesContract.AUTHORITY + "/" + PopularMoviesContract.PATH_MOVIES;
            case CODE_MOVIE_ITEM:
                // single item type
                return "vnd.android.cursor.item" + "/" + PopularMoviesContract.AUTHORITY + "/" + PopularMoviesContract.PATH_MOVIES;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE_DIR:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }
                long id = mDatabase.insertWithOnConflict(MovieEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                if (id > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return ContentUris.withAppendedId(MovieEntry.MOVIES_URI, id);
                }
            case CODE_MOVIE_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
            @Nullable String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_MOVIE_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                 int deletedCount = mDatabase.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                 getContext().getContentResolver().notifyChange(uri, null);
                return deletedCount;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
            @Nullable String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_MOVIE_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }

                String id = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};


                int updatedCount = mDatabase.update(MovieEntry.TABLE_NAME, values, mSelection, mSelectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return updatedCount;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }


    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] valuesArray) {
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE_DIR:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }

                for (ContentValues values:valuesArray) {

                    long id = mDatabase.insertWithOnConflict(MovieEntry.TABLE_NAME, null, values,
                            SQLiteDatabase.CONFLICT_IGNORE);
                    if (id < 0) {
                        mDatabase.update(MovieEntry.TABLE_NAME, values, MovieEntry.COLUMN_NAME_ID + "=?",
                                new String[]{values.getAsString(MovieEntry.COLUMN_NAME_ID)});
                    }
                }

                getContext().getContentResolver().notifyChange(uri, null);

                return valuesArray.length;

            case CODE_MOVIE_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
}
