package com.gmail.kol.c.arindam.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM favourite_movie ORDER BY movieID")
    List<Movie> LoadAllMovie();

    @Query("SELECT * FROM favourite_movie WHERE movieID = :movieID")
    Movie loadMovieByID (int movieID);

    @Insert
    void insertFavouriteMovie (Movie movie);

    @Delete
    void deleteFavouriteMovie (Movie movie);
}
