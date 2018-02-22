package osipov.artem.popularmovies.repository.api;

import osipov.artem.popularmovies.BuildConfig;
import osipov.artem.popularmovies.repository.model.MoviesListResponse;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Artem Osipov on 22/02/2018.
 * ao@enlighted.ru
 */

public interface MoviesApi {
    @GET("3/movie/popular?api_key="+ BuildConfig.API_KEY)
    Call<MoviesListResponse> listPopularMovies();

    @GET("3/movie/top_rated?api_key="+ BuildConfig.API_KEY)
    Call<MoviesListResponse> listHighRatedMovies();
}
