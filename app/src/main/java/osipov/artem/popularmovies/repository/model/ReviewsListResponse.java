package osipov.artem.popularmovies.repository.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artem Osipov on 22/02/2018.
 * ao@enlighted.ru
 */

public class ReviewsListResponse {

    @SerializedName("results")
    List<Review> reviewItemList = new ArrayList<>();

    public List<Review> getReviewItemList() {
        return reviewItemList;
    }
}
