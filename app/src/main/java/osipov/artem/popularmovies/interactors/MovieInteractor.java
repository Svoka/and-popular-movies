package osipov.artem.popularmovies.interactors;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.util.Log;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.operators.completable.CompletableToSingle;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import javax.inject.Inject;
import osipov.artem.popularmovies.repository.api.ApiProvider;
import osipov.artem.popularmovies.repository.db.PopularMoviesContract.MovieEntry;
import osipov.artem.popularmovies.repository.model.Movie;
import osipov.artem.popularmovies.repository.model.MoviesListResponse;
import osipov.artem.popularmovies.repository.model.Review;
import osipov.artem.popularmovies.repository.model.ReviewsListResponse;
import osipov.artem.popularmovies.repository.model.Trailer;
import osipov.artem.popularmovies.repository.model.TrailersListResponse;
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
    private String selection = column+" > -1";

    public Observable<Cursor> getMoviesList() {


        return mPreferredViewInteractor.preferredView
                .subscribeOn(Schedulers.trampoline())
                .doOnNext(aPreferredView -> {


                    if (aPreferredView == PreferredViewInteractor.ARG_VIEW_HIGH_RATED.intValue()) {
                        column = MovieEntry.COLUMN_NAME_MOST_RATED_INDEX;
                        selection = column+" > -1";
                    }

                    if (aPreferredView == PreferredViewInteractor.ARG_VIEW_POPULAR.intValue()) {
                        column = MovieEntry.COLUMN_NAME_POPULAR_INDEX;
                        selection = column+" > -1";
                    }

                    if (aPreferredView == PreferredViewInteractor.ARG_VIEW_FAVOURITE.intValue()) {
                        column = MovieEntry.COLUMN_NAME_FAVOURITE;
                        selection = column+" = 1";
                    }



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
                                selection,
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
            final String[] column = new String[]{MovieEntry.COLUMN_NAME_POPULAR_INDEX};

            if (mPreferredViewInteractor.getPreferredView() == PreferredViewInteractor.ARG_VIEW_POPULAR) {
                call = mApiProvider.getMoviesApi().listPopularMovies();
                column[0] = MovieEntry.COLUMN_NAME_POPULAR_INDEX;
            } else {
                call = mApiProvider.getMoviesApi().listHighRatedMovies();
                column[0] = MovieEntry.COLUMN_NAME_MOST_RATED_INDEX;
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

                        contentValues.put(column[0], i);
//                        contentValues.put(MovieEntry.COLUMN_NAME_MOST_RATED_INDEX, i);

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


    public Single<Movie> getMovieDetail(Integer movieTableId){
        return Single.create(emitter -> {


            Cursor cursor = mApplication.getApplicationContext()
                    .getContentResolver()
                    .query(
                            Uri.parse(MovieEntry.MOVIES_URI + "/" + movieTableId),
                            null,
                            null,
                            null,
                            null
                    );

            Movie movie = new Movie();
            if (cursor != null && cursor.getCount() >0) {
                cursor.moveToFirst();

                movie.setId(cursor.getInt(cursor.getColumnIndex(BaseColumns._ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_TITLE)));
                movie.setPoster(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_POSTER)));
                movie.setBackdrop(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_BACKDROP)));
                movie.setReleaseYear(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_RELEASE_YEAR)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_OVERVIEW)));
                movie.setRating(cursor.getDouble(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_RATING)));


                movie.setFavourite(cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_FAVOURITE))> 0);

                String movieId = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_ID));
                Single.zip(getTrailers(movieId), getReviews(movieId), ReviewsAndTrailers::new)
                        .subscribe(
                                reviewsAndTrailers -> {
                                    movie.setTrailers(reviewsAndTrailers.trailers);
                                    movie.setReviews(reviewsAndTrailers.reviews);

                                    emitter.onSuccess(movie);
                                },
                                throwable -> emitter.onSuccess(movie)
                        );


            }
        });


    }




    private Single<List<Trailer>> getTrailers(String movieId) {
        return Single.create(emitter -> mApiProvider.getMoviesApi().listTrailers(movieId)
                .enqueue(new Callback<TrailersListResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TrailersListResponse> call, @NonNull Response<TrailersListResponse> response) {
                        TrailersListResponse trailersListResponse = response.body();
                        emitter.onSuccess(trailersListResponse.getTrailerResultList());
                    }

                    @Override
                    public void onFailure( @NonNull Call<TrailersListResponse> call,  @NonNull Throwable t) {
                        emitter.onError(t);
                    }
                }));
    }

    private Single<List<Review>> getReviews(String movieId) {
        return Single.create(emitter -> mApiProvider.getMoviesApi().listReviews(movieId)
                .enqueue(new Callback<ReviewsListResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ReviewsListResponse> call, @NonNull Response<ReviewsListResponse> response) {
                        ReviewsListResponse reviewsListResponse = response.body();
                        emitter.onSuccess(reviewsListResponse.getReviewItemList());
                    }

                    @Override
                    public void onFailure( @NonNull Call<ReviewsListResponse> call,  @NonNull Throwable t) {
                        emitter.onError(t);
                    }
                }));
    }

    public Single<Boolean> setFavouriteState(Integer movieTableId, Boolean isFavourite) {
        return Single.fromCallable(()->{
            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieEntry.COLUMN_NAME_FAVOURITE, isFavourite);

            mApplication.getApplicationContext()
                    .getContentResolver()
                    .update(
                            Uri.parse(MovieEntry.MOVIES_URI + "/" + movieTableId),
                            contentValues,
                            null,
                            null
                    );

            return true;
        });
    }

    // helper class for zip of reviews and trailers
    public static class ReviewsAndTrailers{
        public List<Trailer> trailers;
        public List<Review> reviews;

        public ReviewsAndTrailers(List<Trailer> trailers, List<Review> reviews) {
            this.trailers = trailers;
            this.reviews = reviews;
        }
    }

}
