package osipov.artem.popularmovies.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import osipov.artem.popularmovies.R;
import osipov.artem.popularmovies.interactors.PreferredViewInteractor;
import osipov.artem.popularmovies.ui.adapters.MoviesCursorAdapter;
import osipov.artem.popularmovies.ui.adapters.MoviesCursorAdapter.MoviesArrayAdapterClickListener;

public class MainActivity extends AppCompatActivity implements MoviesArrayAdapterClickListener{

    private MovieListViewModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mModel = ViewModelProviders.of(this).get(MovieListViewModel.class);
        final RecyclerView moviesRecyclerView = findViewById(R.id.rv_movies);
        moviesRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns()));

        final MoviesCursorAdapter moviesArrayAdapter = new MoviesCursorAdapter(this);
        moviesRecyclerView.setAdapter(moviesArrayAdapter);

        final Observer<Cursor> mMoviesListObserver = moviesArrayAdapter::swapCursor;
        mModel.getMoviesCursor().observe(this, mMoviesListObserver);

    }

    @Override
    public void onMovieClicked(final int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_MOVIE, position);
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
                mModel.setPreferedView(PreferredViewInteractor.ARG_VIEW_HIGH_RATED);
                return true;
            case R.id.action_popular:
                mModel.setPreferedView(PreferredViewInteractor.ARG_VIEW_POPULAR);
                return true;
            case R.id.action_favourite:
                mModel.setPreferedView(PreferredViewInteractor.ARG_VIEW_FAVOURITE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // with the suggestion from The Reviewer :)
    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2; //to keep the grid aspect
        return nColumns;
    }

}
