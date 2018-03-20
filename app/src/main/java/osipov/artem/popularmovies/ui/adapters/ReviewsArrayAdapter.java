package osipov.artem.popularmovies.ui.adapters;

import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import osipov.artem.popularmovies.R;
import osipov.artem.popularmovies.repository.db.PopularMoviesContract.MovieEntry;
import osipov.artem.popularmovies.repository.model.Review;
import osipov.artem.popularmovies.repository.model.Trailer;
import osipov.artem.popularmovies.ui.holders.MovieRvHolder;
import osipov.artem.popularmovies.ui.holders.ReviewRvHolder;
import osipov.artem.popularmovies.ui.holders.TrailerRvHolder;


/**
 * Created by Artem Osipov on 21/02/2018.
 * ao@enlighted.ru
 */

public class ReviewsArrayAdapter extends Adapter<ReviewRvHolder> {
    private static final String LOG_TAG = ReviewsArrayAdapter.class.getSimpleName();

    private List<Review> mReviews = new ArrayList<>();

    public ReviewsArrayAdapter() {}

    public void setReviews(List<Review> trailerList) {
        this.mReviews = trailerList;
        notifyDataSetChanged();
    }

   @Override
    public ReviewRvHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
       View v = LayoutInflater.from(parent.getContext())
               .inflate(R.layout.rv_review_item, parent, false);

       return new ReviewRvHolder(v);
   }

    @Override
    public void onBindViewHolder(@NonNull final ReviewRvHolder holder, final int position) {
       holder.bind(mReviews.get(position));
    }

    @Override
    public int getItemCount() {

        return mReviews.size();
    }

}
