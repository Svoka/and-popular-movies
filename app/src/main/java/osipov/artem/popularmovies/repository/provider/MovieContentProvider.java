package osipov.artem.popularmovies.repository.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import osipov.artem.popularmovies.repository.db.PopularDao;
import osipov.artem.popularmovies.repository.db.PopularDatabase;
import osipov.artem.popularmovies.repository.model.Movie;

/**
 * Created by Artem Osipov on 26/02/2018.
 * ao@enlighted.ru
 */

public class MovieContentProvider extends ContentProvider {

    public static final String AUTHORITY = "artem.osipov.popularmovies.provider";
    public static final Uri URI_MOVIE = Uri.parse("content://" + AUTHORITY + "/" + Movie.TABLE_NAME);

    private static final int CODE_MOVIE_DIR = 0;
    private static final int CODE_MOVIE_POPULAR = 1;
    private static final int CODE_MOVIE_MOST_RATED = 2;
    private static final int CODE_MOVIE_FAVOURITES = 3;
    private static final int CODE_MOVIE_ITEM = 4;

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);


    static {
        MATCHER.addURI(AUTHORITY, Movie.TABLE_NAME, CODE_MOVIE_DIR);
        MATCHER.addURI(AUTHORITY, Movie.TABLE_NAME + "/popular", CODE_MOVIE_POPULAR);
        MATCHER.addURI(AUTHORITY, Movie.TABLE_NAME + "/mostrated", CODE_MOVIE_MOST_RATED);
        MATCHER.addURI(AUTHORITY, Movie.TABLE_NAME + "/favourites", CODE_MOVIE_FAVOURITES);
        MATCHER.addURI(AUTHORITY, Movie.TABLE_NAME + "/*", CODE_MOVIE_ITEM);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
            @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = MATCHER.match(uri);
        if (code >= CODE_MOVIE_POPULAR && code <= CODE_MOVIE_ITEM) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }
            PopularDao popularDao = PopularDatabase.getInstance(context).movie();
            final Cursor cursor;

            switch (code) {
                cursor = ;
            }
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case CODE_MOVIE_DIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + Movie.TABLE_NAME;
            case CODE_MOVIE_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + Movie.TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (MATCHER.match(uri)) {
            case CODE_MOVIE_DIR:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }
                final long id = PopularDatabase.getInstance(context).movie().insert(Movie.fromContentValues(values));
                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case CODE_MOVIE_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
            @Nullable String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case CODE_MOVIE_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_MOVIE_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final int count = PopularDatabase.getInstance(context).movie()
                        .deleteById(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
            @Nullable String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case CODE_MOVIE_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_MOVIE_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final Movie movie = Movie.fromContentValues(values);
                movie.id = ContentUris.parseId(uri);
                final int count = PopularDatabase.getInstance(context).movie()
                        .update(movie);
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(
            @NonNull ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final Context context = getContext();
        if (context == null) {
            return new ContentProviderResult[0];
        }
        final PopularDatabase database = PopularDatabase.getInstance(context);
        database.beginTransaction();
        try {
            final ContentProviderResult[] result = super.applyBatch(operations);
            database.setTransactionSuccessful();
            return result;
        } finally {
            database.endTransaction();
        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] valuesArray) {
        switch (MATCHER.match(uri)) {
            case CODE_MOVIE_DIR:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final PopularDatabase database = PopularDatabase.getInstance(context);
                final Movie[] cheeses = new Movie[valuesArray.length];
                for (int i = 0; i < valuesArray.length; i++) {
                    cheeses[i] = Movie.fromContentValues(valuesArray[i]);
                }
                return database.movie().insertAll(cheeses).length;
            case CODE_MOVIE_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
}
