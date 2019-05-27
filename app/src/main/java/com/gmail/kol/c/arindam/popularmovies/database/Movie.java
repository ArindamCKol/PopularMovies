package com.gmail.kol.c.arindam.popularmovies.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

//movie class with room database implementation
@Entity (tableName = "favourite_movie") //table name
public class Movie implements Parcelable {
    @PrimaryKey
    private int movieID; //primary key
    private String moviePosterPath;
    private String movieTitle;
    private String movieReleaseDate;
    private String movieBackdropPath;
    private String movieVoteAverage;
    private String moviePlot;

    public Movie(int movieID, String moviePosterPath, String movieTitle, String movieReleaseDate, String movieBackdropPath, String movieVoteAverage, String moviePlot) {
        this.movieID = movieID;
        this.moviePosterPath = moviePosterPath;
        this.movieTitle = movieTitle;
        this.movieReleaseDate = movieReleaseDate;
        this.movieBackdropPath = movieBackdropPath;
        this.movieVoteAverage = movieVoteAverage;
        this.moviePlot = moviePlot;
    }

    //unpack from parcel
    protected Movie(Parcel in) {
        movieID = in.readInt();
        moviePosterPath = in.readString();
        movieTitle = in.readString();
        movieReleaseDate = in.readString();
        movieBackdropPath = in.readString();
        movieVoteAverage = in.readString();
        moviePlot = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getMovieID() {
        return movieID;
    }

    public String getMoviePosterPath() {
        return moviePosterPath;
    }

    public String getMovieTitle() { return movieTitle; }

    public String getMovieReleaseDate() { return movieReleaseDate; }

    public String getMovieBackdropPath() { return movieBackdropPath; }

    public String getMovieVoteAverage() { return movieVoteAverage; }

    public String getMoviePlot() { return moviePlot; }

    @Override
    public int describeContents() {
        return 0;
    }

    //create parcel
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(movieID);
        parcel.writeString(moviePosterPath);
        parcel.writeString(movieTitle);
        parcel.writeString(movieReleaseDate);
        parcel.writeString(movieBackdropPath);
        parcel.writeString(movieVoteAverage);
        parcel.writeString(moviePlot);
    }
}
