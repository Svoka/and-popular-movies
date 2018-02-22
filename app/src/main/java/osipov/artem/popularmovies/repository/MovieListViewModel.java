package osipov.artem.popularmovies.repository;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import osipov.artem.popularmovies.repository.api.ApiProvider;
import osipov.artem.popularmovies.repository.model.Movie;
import osipov.artem.popularmovies.repository.model.MoviesListResponse;
import osipov.artem.popularmovies.ui.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Artem Osipov on 22/02/2018.
 * ao@enlighted.ru
 */

public class MovieListViewModel extends ViewModel {

    private MutableLiveData<List<Movie>> mMoviesList;
    private ApiProvider mApiProvider = new ApiProvider();
    private int currentPref = -1;


    public MutableLiveData<List<Movie>> getMoviesList() {
        if (mMoviesList == null) {
            this.mMoviesList = new MutableLiveData<>();
        }
        return this.mMoviesList;
    }

    public void setPreferedView(int preferedView) {

        if (preferedView == currentPref) {
            return;
        } else {
            currentPref = preferedView;
        }

        Log.e("SET_PREFS_CALLED", preferedView+"");

        Call<MoviesListResponse> call;

        if (preferedView == MainActivity.ARG_VIEW_POPULAR) {
            call = mApiProvider.getMoviesApi().listPopularMovies();
        } else {
            call = mApiProvider.getMoviesApi().listHighRatedMovies();
        }

        call.enqueue(new Callback<MoviesListResponse>() {
            @Override
            public void onResponse(@NonNull final Call<MoviesListResponse> call, @NonNull final Response<MoviesListResponse> response) {
                mMoviesList.setValue(response.body().getResults());
            }
            @Override
            public void onFailure(@NonNull final Call<MoviesListResponse> call, @NonNull final Throwable t) {

            }
        });


    }
}
