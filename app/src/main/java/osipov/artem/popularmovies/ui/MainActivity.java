package osipov.artem.popularmovies.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import java.util.List;
import osipov.artem.popularmovies.R;
import osipov.artem.popularmovies.repository.MovieListViewModel;
import osipov.artem.popularmovies.repository.model.Movie;
import osipov.artem.popularmovies.ui.adapters.MoviesArrayAdapter;
import osipov.artem.popularmovies.ui.adapters.MoviesArrayAdapter.MoviesArrayAdapterClickListener;

public class MainActivity extends AppCompatActivity implements MoviesArrayAdapterClickListener{

    private RecyclerView moviesRecyclerView;
    private MovieListViewModel mModel;
    private List<Movie> mMovies;
    private SharedPreferences preferences;
    private final String ARG_PREFS = "APP_PREFS";
    private final String ARG_PREFERED_VIEW = "PREFERED_VIEW";
    public static final int ARG_VIEW_HIGH_RATED = 0;
    public static final int ARG_VIEW_POPULAR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences(ARG_PREFS, MODE_PRIVATE);
        mModel = ViewModelProviders.of(this).get(MovieListViewModel.class);
        moviesRecyclerView = findViewById(R.id.rv_movies);
        moviesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        final MoviesArrayAdapter moviesArrayAdapter = new MoviesArrayAdapter(this);
        moviesRecyclerView.setAdapter(moviesArrayAdapter);


        final Observer<List<Movie>> mMoviesListObserver = new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable final List<Movie> movieList) {
                mMovies =  movieList;
                moviesArrayAdapter.setItems(movieList);
            }
        };
        mModel.getMoviesList().observe(this, mMoviesListObserver);
        mModel.setPreferedView(getPreferedView());

    }

    @Override
    public void onMovieClicked(final int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_MOVIE, mMovies.get(position));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies_sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                setPreferedView(ARG_VIEW_HIGH_RATED);
                return true;
            case R.id.action_popular:
                setPreferedView(ARG_VIEW_POPULAR);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private int getPreferedView() {
        return preferences.getInt(ARG_PREFERED_VIEW, 0);
    }

    private void setPreferedView(int preferedView) {
        Editor editor = preferences.edit();
        editor.putInt(ARG_PREFERED_VIEW, preferedView);
        editor.apply();

        mModel.setPreferedView(preferedView);
    }
}
