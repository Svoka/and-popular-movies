package osipov.artem.popularmovies.ui.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import osipov.artem.popularmovies.R;
import osipov.artem.popularmovies.repository.model.Review;
import osipov.artem.popularmovies.repository.model.Trailer;
import osipov.artem.popularmovies.ui.adapters.MoviesCursorAdapter.MoviesArrayAdapterClickListener;

/**
 * Created by Artem Osipov on 21/02/2018.
 * ao@enlighted.ru
 */

public class ReviewRvHolder extends ViewHolder {
    private TextView authorTextView;
    private TextView contentTextView;

    public ReviewRvHolder(final View itemView) {
        super(itemView);

        authorTextView =  itemView.findViewById(R.id.tv_author);
        contentTextView =  itemView.findViewById(R.id.tv_content);
    }

    public void bind(final Review review) {


        authorTextView.setText(review.getAuthor());
        contentTextView.setText(review.getContent());

    }
}