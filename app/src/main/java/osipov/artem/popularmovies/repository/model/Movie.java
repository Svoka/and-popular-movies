package osipov.artem.popularmovies.repository.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;
import java.util.Date;
import osipov.artem.popularmovies.BuildConfig;

/**
 * Created by Artem Osipov on 21/02/2018.
 * ao@enlighted.ru
 */
@Entity(tableName = Movie.TABLE_NAME)
public class Movie {
    public static final String TABLE_NAME = "movies";

    @SerializedName("original_title")
    private String title = "";

    @SerializedName("poster_path")
    private String poster = "";

    @SerializedName("backdrop_path")
    private String backdrop = "";

    @PrimaryKey
    private String id = "";

    @SerializedName("vote_average")
    private Double rating = 0d;

    private String overview = "";

    @Ignore @SerializedName("release_date")
    private Date releaseDate;

    private String releaseYear = "";
    private Integer popularIndex = -1;
    private Integer mostRatedIndex = -1;
    private Boolean favourite = false;


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

    public Integer getPopularIndex() {
        return popularIndex;
    }

    public void setPopularIndex(final Integer popularIndex) {
        this.popularIndex = popularIndex;
    }

    public Integer getMostRatedIndex() {
        return mostRatedIndex;
    }

    public void setMostRatedIndex(final Integer mostRatedIndex) {
        this.mostRatedIndex = mostRatedIndex;
    }

    public Boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(final Boolean favourite) {
        this.favourite = favourite;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(final String releaseYear) {
        this.releaseYear = releaseYear;
    }

}
