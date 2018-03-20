package osipov.artem.popularmovies.repository.model;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import osipov.artem.popularmovies.BuildConfig;

/**
 * Created by Artem Osipov on 21/02/2018.
 * ao@enlighted.ru
 */
public class Movie {
    @SerializedName("original_title")
    private String title = "";

    @SerializedName("poster_path")
    private String poster = "";

    @SerializedName("backdrop_path")
    private String backdrop = "";

    private Integer id = -1;

    @SerializedName("vote_average")
    private Double rating = 0d;

    private String overview = "";

    private Date releaseDate;

    private String releaseYear = "";
    private Integer popularIndex = -1;
    private Integer mostRatedIndex = -1;
    private Boolean favourite = false;


    private List<Trailer> trailers = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();

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

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
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

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
