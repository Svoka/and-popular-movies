package osipov.artem.popularmovies.ui.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

import osipov.artem.popularmovies.BuildConfig;
import osipov.artem.popularmovies.R;
import osipov.artem.popularmovies.repository.model.Movie;
import osipov.artem.popularmovies.ui.adapters.MoviesCursorAdapter.MoviesArrayAdapterClickListener;

/**
 * Created by Artem Osipov on 21/02/2018.
 * ao@enlighted.ru
 */

public class MovieRvHolder extends ViewHolder {
    private ImageView posterImageView;

    public MovieRvHolder(final View itemView) {
        super(itemView);

        posterImageView =  itemView.findViewById(R.id.iv_poster);
    }

    public void bind(
            final Integer dbId,
            final String poster,
            final String title,
            final MoviesArrayAdapterClickListener listener) {
        Glide.with((Context) listener).load(poster).into(posterImageView);
        posterImageView.setContentDescription(title);
        itemView.setOnClickListener(view -> listener.onMovieClicked(dbId));
    }
}