package osipov.artem.popularmovies.repository.model;

import android.os.Parcel;
import com.google.gson.annotations.SerializedName;
import java.util.Date;
import osipov.artem.popularmovies.BuildConfig;

/**
 * Created by Artem Osipov on 21/02/2018.
 * ao@enlighted.ru
 */

public class Movie implements android.os.Parcelable {
    @SerializedName("original_title")
    private String title = "";

    @SerializedName("poster_path")
    private String poster = "";

    @SerializedName("backdrop_path")
    private String backdrop = "";

    private String id = "";

    @SerializedName("vote_average")
    private Double rating = 0d;

    private String overview = "";

    @SerializedName("release_date")
    private Date releaseDate;

    public Movie(final String id, final String title, final String poster) {
        this.id = id;
        this.title = title;
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getPoster() {
        return BuildConfig.PICTURE_URL_185+poster;
    }

    public void setPoster(final String poster) {
        this.poster = poster;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getBackdrop() {
        return BuildConfig.PICTURE_URL_500+backdrop;
    }

    public void setBackdrop(final String backdrop) {
        this.backdrop = backdrop;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(final Double rating) {
        this.rating = rating;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(final String overview) {
        this.overview = overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(final Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    // Auto-generated, to be removed on the Stage 2
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.poster);
        dest.writeString(this.backdrop);
        dest.writeString(this.id);
        dest.writeValue(this.rating);
        dest.writeString(this.overview);
        dest.writeLong(this.releaseDate != null ? this.releaseDate.getTime() : -1);
    }

    protected Movie(Parcel in) {
        this.title = in.readString();
        this.poster = in.readString();
        this.backdrop = in.readString();
        this.id = in.readString();
        this.rating = (Double) in.readValue(Double.class.getClassLoader());
        this.overview = in.readString();
        long tmpReleaseDate = in.readLong();
        this.releaseDate = tmpReleaseDate == -1 ? null : new Date(tmpReleaseDate);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
