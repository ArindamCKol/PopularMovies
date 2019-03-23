package com.gmail.kol.c.arindam.popularmovies.Helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.content.AsyncTaskLoader;

import com.gmail.kol.c.arindam.popularmovies.Model.Movie;
import com.gmail.kol.c.arindam.popularmovies.Utils.JSONUtils;
import com.gmail.kol.c.arindam.popularmovies.Utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

//Loader helper class to load movie list from json reply from themoviedb
public class MovieLoader extends AsyncTaskLoader <List<Movie>> {
    private String urlString;

    public MovieLoader(@NonNull Context context, String urlString) {
        super(context);
        this.urlString = urlString; //themoviedb query url string
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Movie> loadInBackground() {
        String jsonString = null;
        URL movieQueryUrl = NetworkUtils.createUrl(urlString); //create URL from query string
        try {
            jsonString = NetworkUtils.makeHttpRequest(movieQueryUrl); //get json reply from themoviedb
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Movie> movieList = JSONUtils.getMovieListFromJson(jsonString); //get movie list from json string

        return movieList; //return movie list
    }
}
