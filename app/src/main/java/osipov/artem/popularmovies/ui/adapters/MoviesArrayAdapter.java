package osipov.artem.popularmovies.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import osipov.artem.popularmovies.ui.holders.MovieRvHolder;
import osipov.artem.popularmovies.R;
import osipov.artem.popularmovies.repository.model.Movie;


/**
 * Created by Artem Osipov on 21/02/2018.
 * ao@enlighted.ru
 */

public class MoviesArrayAdapter extends Adapter<MovieRvHolder> {
    private static final String LOG_TAG = MoviesArrayAdapter.class.getSimpleName();

    private List<Movie> mMovieList = new ArrayList<>();
    private MoviesArrayAdapterClickListener mListener;

    public MoviesArrayAdapter(MoviesArrayAdapterClickListener listener) {
        this.mListener = listener;
    }

    public void setItems(List<Movie> movieList) {
        this.mMovieList = movieList;
        notifyDataSetChanged();
    }

   @Override
    public MovieRvHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
       View v = LayoutInflater.from(parent.getContext())
               .inflate(R.layout.rv_list_item, parent, false);

       return new MovieRvHolder(v);

   }

    @Override
    public void onBindViewHolder(final MovieRvHolder holder, final int position) {
        holder.bind(mListener, mMovieList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }


    public interface MoviesArrayAdapterClickListener{
        void onMovieClicked(int position);
    }

}
