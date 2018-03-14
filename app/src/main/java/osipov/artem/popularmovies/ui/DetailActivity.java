package osipov.artem.popularmovies.ui;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import java.util.Calendar;
import java.util.Date;
import osipov.artem.popularmovies.R;
import osipov.artem.popularmovies.repository.model.Movie;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "EXTRA_MOVIE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        Integer dbId = intent.getIntExtra(EXTRA_MOVIE, -1);


        Movie movie = intent.getParcelableExtra(EXTRA_MOVIE);

        ImageView backDropImageView = findViewById(R.id.iv_backdrop);
        ImageView posterImageView = findViewById(R.id.iv_poster);
        TextView yearTextView = findViewById(R.id.tv_year);
        TextView ratingTextView = findViewById(R.id.tv_rating);
        TextView descriptionTextView = findViewById(R.id.tv_description);

        Glide.with(this).load(movie.getBackdrop()).into(backDropImageView);
        Glide.with(this).load(movie.getPoster()).into(posterImageView);


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(movie.getReleaseDate());

        yearTextView.setText(String.format("%s", calendar.get(Calendar.YEAR)));
        ratingTextView.setText(getString(R.string.rating_value, movie.getRating()));
        descriptionTextView.setText(movie.getOverview());

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setTitle(movie.getTitle());


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }




}
