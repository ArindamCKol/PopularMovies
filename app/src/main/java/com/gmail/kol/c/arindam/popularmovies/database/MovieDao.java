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
    LiveData<List<Movie>> LoadAllMovie(); // get all movie item / row from DB

    @Query("SELECT * FROM favourite_movie WHERE movieID = :movieID")
    Movie loadMovieByID (int movieID); // get single movie item / row from db

    @Insert
    void insertFavouriteMovie (Movie movie); // insert single movie item / row in db

    @Delete
    void deleteFavouriteMovie (Movie movie); // delete single movie item / row in db
}
