package osipov.artem.popularmovies.ui.holders;


import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;


import osipov.artem.popularmovies.R;
import osipov.artem.popularmovies.repository.model.Trailer;
import osipov.artem.popularmovies.ui.adapters.TrailersArrayAdapter;

/**
 * Created by Artem Osipov on 21/02/2018.
 * ao@enlighted.ru
 */

public class TrailerRvHolder extends ViewHolder {
    private TextView trailerTitleTextView;

    public TrailerRvHolder(final View itemView) {
        super(itemView);
        trailerTitleTextView =  itemView.findViewById(R.id.tv_title);
    }

    public void bind(Trailer trailer, final TrailersArrayAdapter.TrailersArrayAdapterClickListener listener) {
        trailerTitleTextView.setText(trailer.getTitle());
        itemView.setOnClickListener(view -> listener.onTrailerClicked(trailer.getSource()));
    }
}