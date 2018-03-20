package osipov.artem.popularmovies.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import osipov.artem.popularmovies.R;
import osipov.artem.popularmovies.repository.model.Review;
import osipov.artem.popularmovies.repository.model.Trailer;
import osipov.artem.popularmovies.ui.holders.ReviewRvHolder;
import osipov.artem.popularmovies.ui.holders.TrailerRvHolder;


/**
 * Created by Artem Osipov on 21/02/2018.
 * ao@enlighted.ru
 */

public class TrailersArrayAdapter extends Adapter<TrailerRvHolder> {
    private static final String LOG_TAG = TrailersArrayAdapter.class.getSimpleName();

    private List<Trailer> mTrailers = new ArrayList<>();
    private TrailersArrayAdapterClickListener mListener;

    public TrailersArrayAdapter(TrailersArrayAdapterClickListener listener) {
        this.mListener = listener;
    }

    public void setTrailers(List<Trailer> trailerList) {
        this.mTrailers = trailerList;
        notifyDataSetChanged();
    }

   @Override
    public TrailerRvHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
       View v = LayoutInflater.from(parent.getContext())
               .inflate(R.layout.rv_trailer_item, parent, false);

       return new TrailerRvHolder(v);
   }

    @Override
    public void onBindViewHolder(@NonNull final TrailerRvHolder holder, final int position) {
       holder.bind(mTrailers.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public interface TrailersArrayAdapterClickListener{
        void onTrailerClicked(String source);
    }

}
