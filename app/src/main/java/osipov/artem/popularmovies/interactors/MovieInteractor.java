package osipov.artem.popularmovies.interactors;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.Calendar;
import java.util.List;


import javax.inject.Inject;
import osipov.artem.popularmovies.repository.api.ApiProvider;
import osipov.artem.popularmovies.repository.db.PopularMoviesContract;
import osipov.artem.popularmovies.repository.db.PopularMoviesContract.MovieEntry;
import osipov.artem.popularmovies.repository.model.Movie;
import osipov.artem.popularmovies.repository.model.MoviesListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Artem Osipov on 05/03/2018.
 * ao@enlighted.ru
 */

public class MovieInteractor {

    private Application mApplication;
    private PreferredViewInteractor mPreferredViewInteractor;
    private ApiProvider mApiProvider;

    @Inject
    public MovieInteractor(Application app, PreferredViewInteractor prefferedViewInteractor){
        mApplication = app;
        mPreferredViewInteractor = prefferedViewInteractor;
        mApiProvider = new ApiProvider();
    }

    private String column = MovieEntry.COLUMN_NAME_POPULAR_INDEX;
    public Observable<Cursor> getMoviesList() {


        return mPreferredViewInteractor.preferredView
                .subscribeOn(Schedulers.trampoline())
                .doOnNext(aPreferredView -> {
                    column = MovieEntry.COLUMN_NAME_POPULAR_INDEX;
                })
                .flatMapSingle(count -> getMovieCount())
                .flatMapSingle(count -> {
                    if (count > 0) {
                        return Single.just(count);
                    } else {
                        return loadMoviesFromNetwork();
                    }
                }).flatMapSingle(aCount -> Single.fromCallable(() -> mApplication
                        .getContentResolver()
                        .query(
                                MovieEntry.MOVIES_URI,
                                null,
                                column+" > -1",
                                null,
                                MovieEntry.COLUMN_NAME_RATING+" DESC"
                        )))
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void updateMovieList() {
        loadMoviesFromNetwork().doOnSuccess(aInt -> mPreferredViewInteractor.preferredView.repeat())
                .subscribeOn(Schedulers.trampoline())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private Single<Integer> getMovieCount() {
        return Single.fromCallable(() -> {
            Cursor cursor = mApplication.getApplicationContext()
                    .getContentResolver()
                    .query(
                            MovieEntry.MOVIES_URI,
                            new String[]{MovieEntry._ID},
                            column+" > -1",
                            null,
                            MovieEntry.COLUMN_NAME_RATING+" DESC"
                    );
            if (cursor!=null) {
                int count = cursor.getCount();
                cursor.close();
                return count;
            } else {
                return 0;
            }
        });
    }

    private Single<Integer> loadMoviesFromNetwork() {
        return Single.create(emitter -> {
            Call<MoviesListResponse> call;
            if (mPreferredViewInteractor.getPreferredView() == PreferredViewInteractor.ARG_VIEW_POPULAR) {
                call = mApiProvider.getMoviesApi().listPopularMovies();
            } else {
                call = mApiProvider.getMoviesApi().listHighRatedMovies();
            }

            call.enqueue(new Callback<MoviesListResponse>() {
                @Override
                public void onResponse(@NonNull final Call<MoviesListResponse> call, @NonNull final Response<MoviesListResponse> response) {

                    List<Movie> movies = response.body().getResults();

                    Calendar calendar = Calendar.getInstance();

                    ContentValues[] contentValuesArray = new ContentValues[movies.size()];

                    for (int i=0; i< movies.size(); i++) {
                        Movie movie = movies.get(i);
                        ContentValues contentValues = new ContentValues();

                        contentValues.put(MovieEntry.COLUMN_NAME_ID, movie.getId());
                        contentValues.put(MovieEntry.COLUMN_NAME_TITLE, movie.getTitle());
                        contentValues.put(MovieEntry.COLUMN_NAME_BACKDROP, movie.getBackdrop());
                        contentValues.put(MovieEntry.COLUMN_NAME_POSTER, movie.getPoster());
                        contentValues.put(MovieEntry.COLUMN_NAME_RATING, movie.getRating());
                        contentValues.put(MovieEntry.COLUMN_NAME_OVERVIEW, movie.getOverview());

                        contentValues.put(MovieEntry.COLUMN_NAME_POPULAR_INDEX, i);
                        contentValues.put(MovieEntry.COLUMN_NAME_MOST_RATED_INDEX, i);

//                        calendar.setTime(movie.getReleaseDate());
                        contentValues.put(MovieEntry.COLUMN_NAME_RELEASE_YEAR, String.format("%s", calendar.get(Calendar.YEAR)));

                        contentValuesArray[i] = contentValues;
                    }

                    int insertCount = mApplication.getApplicationContext().getContentResolver().bulkInsert(MovieEntry.MOVIES_URI, contentValuesArray);
                    emitter.onSuccess(insertCount);

                }
                @Override
                public void onFailure(@NonNull final Call<MoviesListResponse> call, @NonNull final Throwable t) {
                    emitter.onError(t);
                }
            });
        });
    }


    public Single<Movie> getMovieDetail(String movieId){
        return Single.fromCallable(()-> {

            Cursor cursor = mApplication.getApplicationContext()
                    .getContentResolver()
                    .query(
                            Uri.parse(PopularMoviesContract.PATH_MOVIES + "/"+movieId),
                            null,
                            null,
                            null,
                            null
                    );

            return new Movie();
        });
    }

}
