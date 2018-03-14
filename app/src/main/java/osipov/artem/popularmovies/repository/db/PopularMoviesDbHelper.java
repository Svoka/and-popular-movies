package osipov.artem.popularmovies.repository.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import osipov.artem.popularmovies.repository.db.PopularMoviesContract.MovieEntry;

/**
 * Created by Artem Osipov on 03/03/2018.
 * ao@enlighted.ru
 */

public class PopularMoviesDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "popularmovies.db";
    public static final int DATABASE_VERSION = 3;

    public PopularMoviesDbHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME +
                " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieEntry.COLUMN_NAME_ID + " BIGINT(10), " +
                MovieEntry.COLUMN_NAME_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_NAME_POSTER + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_NAME_BACKDROP + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_NAME_RATING + " DECIMAL(2,2), " +
                MovieEntry.COLUMN_NAME_OVERVIEW + " TEXT, " +
                MovieEntry.COLUMN_NAME_RELEASE_YEAR + " TEXT, " +
                MovieEntry.COLUMN_NAME_POPULAR_INDEX + " INT(3) DEFAULT -1, " +
                MovieEntry.COLUMN_NAME_MOST_RATED_INDEX + " INT(3) DEFAULT -1, " +
                MovieEntry.COLUMN_NAME_FAVOURITE + " BOOLEAN DEFAULT false, " +
                "UNIQUE("+MovieEntry.COLUMN_NAME_ID+"))"
                ;

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);

    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int i, final int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
