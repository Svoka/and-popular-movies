package osipov.artem.popularmovies.repository.model;

import java.util.List;

/**
 * Created by Artem Osipov on 22/02/2018.
 * ao@enlighted.ru
 */

public class MoviesListResponse {

    private List<Movie> results;

    public MoviesListResponse(final List<Movie> results) {
        this.results = results;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(final List<Movie> results) {
        this.results = results;
    }
}
