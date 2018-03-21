package osipov.artem.popularmovies.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import java.util.Calendar;
import java.util.Date;
import osipov.artem.popularmovies.R;
import osipov.artem.popularmovies.repository.model.Movie;
import osipov.artem.popularmovies.ui.adapters.ReviewsArrayAdapter;
import osipov.artem.popularmovies.ui.adapters.TrailersArrayAdapter;

public class DetailActivity extends AppCompatActivity implements TrailersArrayAdapter.TrailersArrayAdapterClickListener {

    public static final String EXTRA_MOVIE = "EXTRA_MOVIE";

    private MovieDetailViewModel mModel;
    private TrailersArrayAdapter mTrailersArrayAdapter;
    private ReviewsArrayAdapter mReviewsArrayAdapter;

    private FloatingActionButton fab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView trailersRV = findViewById(R.id.rv_trailers);
        trailersRV.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView reviewsRV = findViewById(R.id.rv_reviews);
        reviewsRV.setLayoutManager(new LinearLayoutManager(this));

        mTrailersArrayAdapter = new TrailersArrayAdapter(this);
        mReviewsArrayAdapter = new ReviewsArrayAdapter();

        trailersRV.setAdapter(mTrailersArrayAdapter);
        reviewsRV.setAdapter(mReviewsArrayAdapter);


        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> mModel.changeFavouriteState());

        Intent intent = getIntent();
        Integer dbId = intent.getIntExtra(EXTRA_MOVIE, -1);

        if (dbId == -1) {
            // return back
        }

        mModel = ViewModelProviders.of(this).get(MovieDetailViewModel.class);
        final Observer<Movie> mMovieDetailObserver = this::showDetailMovie;
        final Observer<Boolean> mMovieFavouriteStateObserver = this::changeFavouriteState;

        mModel.getMovie(dbId).observe(this, mMovieDetailObserver);
        mModel.getFavouriteState().observe(this, mMovieFavouriteStateObserver);
    }

    @Override
    public void onTrailerClicked(String source) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + source));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + source));

        if (appIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(appIntent);
        } else {
            startActivity(webIntent);
        }

    }

    private void showDetailMovie(Movie movie) {
        mReviewsArrayAdapter.setReviews(movie.getReviews());
        mTrailersArrayAdapter.setTrailers(movie.getTrailers());

        ImageView backDropImageView = findViewById(R.id.iv_backdrop);
        ImageView posterImageView = findViewById(R.id.iv_poster);
        TextView yearTextView = findViewById(R.id.tv_year);
        TextView ratingTextView = findViewById(R.id.tv_rating);
        TextView descriptionTextView = findViewById(R.id.tv_description);

        Glide.with(this).load(movie.getBackdrop()).into(backDropImageView);
        Glide.with(this).load(movie.getPoster()).into(posterImageView);

        yearTextView.setText(String.format("%s", movie.getReleaseYear()));
        ratingTextView.setText(getString(R.string.rating_value, movie.getRating()));
        descriptionTextView.setText(movie.getOverview());

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setTitle(movie.getTitle());


        changeFavouriteState(movie.getFavourite());

        fab.setVisibility(View.VISIBLE);

    }

    private void changeFavouriteState(Boolean state) {
        if (state) {
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favourite_fill));
        } else {
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favourite_empty));
        }
    }




}
