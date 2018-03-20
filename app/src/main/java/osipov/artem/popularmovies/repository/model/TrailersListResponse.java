package osipov.artem.popularmovies.repository.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artem Osipov on 22/02/2018.
 * ao@enlighted.ru
 */

public class TrailersListResponse {

    @SerializedName("results")
    List<Trailer> trailerResultList = new ArrayList<>();


    public List<Trailer> getTrailerResultList() {
        return trailerResultList;
    }
}
