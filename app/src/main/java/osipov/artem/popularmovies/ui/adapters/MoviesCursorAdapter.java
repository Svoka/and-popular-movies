package osipov.artem.popularmovies.ui.adapters;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import osipov.artem.popularmovies.repository.db.PopularMoviesContract;
import osipov.artem.popularmovies.repository.db.PopularMoviesContract.MovieEntry;
import osipov.artem.popularmovies.ui.holders.MovieRvHolder;
import osipov.artem.popularmovies.R;
import osipov.artem.popularmovies.repository.model.Movie;


/**
 * Created by Artem Osipov on 21/02/2018.
 * ao@enlighted.ru
 */

public class MoviesCursorAdapter extends Adapter<MovieRvHolder> {
    private static final String LOG_TAG = MoviesCursorAdapter.class.getSimpleName();

    private Cursor mCursor;
    private MoviesArrayAdapterClickListener mListener;

    public MoviesCursorAdapter(MoviesArrayAdapterClickListener listener) {
        this.mListener = listener;
    }

    public void swapCursor(Cursor cursor) {
        this.mCursor = cursor;
        notifyDataSetChanged();
    }

   @Override
    public MovieRvHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
       View v = LayoutInflater.from(parent.getContext())
               .inflate(R.layout.rv_list_item, parent, false);

       return new MovieRvHolder(v);

   }

    @Override
    public void onBindViewHolder(@NonNull final MovieRvHolder holder, final int position) {
        mCursor.moveToPosition(position);
        Integer id = mCursor.getInt(mCursor.getColumnIndex(BaseColumns._ID));
        String title = mCursor.getString(mCursor.getColumnIndex(MovieEntry.COLUMN_NAME_TITLE));
        String poster = mCursor.getString(mCursor.getColumnIndex(MovieEntry.COLUMN_NAME_POSTER));
        holder.bind(id, poster, title, mListener);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }


    public interface MoviesArrayAdapterClickListener{
        void onMovieClicked(int position);
    }

}
