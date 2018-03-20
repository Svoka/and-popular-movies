package osipov.artem.popularmovies.ui;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import osipov.artem.popularmovies.App;
import osipov.artem.popularmovies.interactors.MovieInteractor;
import osipov.artem.popularmovies.repository.model.Movie;

/**
 * Created by Artem Osipov on 13/03/2018.
 * ao@enlighted.ru
 */

public class MovieDetailViewModel extends ViewModel {

    private MutableLiveData<Movie> mMovie;
    private MutableLiveData<Boolean> mFavouriteState;


    @Inject MovieInteractor mMovieInteractor;

    public MovieDetailViewModel() {
        App.getInstance().getAppComponent().inject(this);
    }

    public MutableLiveData<Movie> getMovie(Integer movieTableID) {
        if (mFavouriteState == null) {
            mFavouriteState = new MutableLiveData<>();
        }

        if (mMovie == null) {
            mMovie = new MutableLiveData<>();

            mMovieInteractor.getMovieDetail(movieTableID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            movie -> {
                                mMovie.setValue(movie);
                                mFavouriteState.setValue(movie.getFavourite());
                            },
                            throwable -> {
                                    throwable.printStackTrace();
                            }
                    );
        }
        return mMovie;
    }

    public MutableLiveData<Boolean> getFavouriteState() {
        return mFavouriteState;
    }

    public void changeFavouriteState() {
        Boolean newState = !mMovie.getValue().getFavourite();
        mMovieInteractor
                .setFavouriteState(mMovie.getValue().getId(), newState)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        aBoolean -> {
                            mFavouriteState.setValue(newState);
                        }
                );
    }
}
