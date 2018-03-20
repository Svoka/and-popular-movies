package osipov.artem.popularmovies.repository.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by artemosipov on 20/03/2018.
 */

public class Trailer {

    @SerializedName("key")
    private String source ="";

    @SerializedName("name")
    private String title ="";

    public Trailer(String source, String title) {
        this.source = source;
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
