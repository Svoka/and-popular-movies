package osipov.artem.popularmovies.repository.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Artem Osipov on 03/03/2018.
 * ao@enlighted.ru
 */

public class PopularMoviesContract {
    public static final String AUTHORITY = "osipov.artem.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "movies";


    private PopularMoviesContract(){}


    public static final class MovieEntry implements BaseColumns{

        public static final Uri MOVIES_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_POSTER = "poster";
        public static final String COLUMN_NAME_BACKDROP = "backdrop";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_NAME_RELEASE_YEAR = "releaseYear";
        public static final String COLUMN_NAME_POPULAR_INDEX = "popularIndex";
        public static final String COLUMN_NAME_MOST_RATED_INDEX = "mostRatedIndex";
        public static final String COLUMN_NAME_FAVOURITE = "favouirite";
    }

    public static final class FeedbackEntry implements BaseColumns {

    }

    public static final class ReviewEntry implements BaseColumns {

    }

}
