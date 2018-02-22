package osipov.artem.popularmovies.repository.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Artem Osipov on 22/02/2018.
 * ao@enlighted.ru
 */

public class ApiProvider {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    public MoviesApi getMoviesApi() {
        return retrofit.create(MoviesApi.class);
    }






}
