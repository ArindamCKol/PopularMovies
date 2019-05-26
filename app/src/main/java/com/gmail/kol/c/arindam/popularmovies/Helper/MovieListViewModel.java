package com.gmail.kol.c.arindam.popularmovies.Helper;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.gmail.kol.c.arindam.popularmovies.Utils.AppDatabase;
import com.gmail.kol.c.arindam.popularmovies.database.Movie;

import java.util.List;

public class MovieListViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> favouriteMovies;

    public MovieListViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        favouriteMovies = database.movieDao().LoadAllMovie();
    }

    public LiveData<List<Movie>> getFavouriteMovies() {
        return favouriteMovies;
    }
}
