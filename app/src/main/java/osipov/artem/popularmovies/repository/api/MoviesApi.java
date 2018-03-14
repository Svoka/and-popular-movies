package osipov.artem.popularmovies.repository.api;

import osipov.artem.popularmovies.BuildConfig;
import osipov.artem.popularmovies.repository.model.MoviesListResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Artem Osipov on 22/02/2018.
 * ao@enlighted.ru
 */

public interface MoviesApi {
    @GET("3/movie/popular?api_key="+ BuildConfig.API_KEY)
    Call<MoviesListResponse> listPopularMovies();

    @GET("3/movie/top_rated?api_key="+ BuildConfig.API_KEY)
    Call<MoviesListResponse> listHighRatedMovies();

    @GET("3/movie/{id}/videos?api_key="+ BuildConfig.API_KEY)
    Call<MoviesListResponse> listTrailers(@Path("id") String id);

    @GET("3/movie/{id}/reviews?api_key="+ BuildConfig.API_KEY)
    Call<MoviesListResponse> listReviews(@Path("id") String id);
}
