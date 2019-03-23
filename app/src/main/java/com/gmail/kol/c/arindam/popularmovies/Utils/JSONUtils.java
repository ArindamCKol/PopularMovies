package com.gmail.kol.c.arindam.popularmovies.Utils;

import android.text.TextUtils;
import android.util.Log;

import com.gmail.kol.c.arindam.popularmovies.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class JSONUtils {

    //static identifier for json objects
    private static final String RESULTS = "results";
    private static final String ID = "id";
    private static final String POSTER = "poster_path";
    private static final String TITLE = "title";
    private static final String RELEASE_DATE = "release_date";
    private static final String BACKDROP = "backdrop_path";
    private static final String VOTE = "vote_average";
    private static final String PLOT = "overview";

    //blank constructor for final class
    public JSONUtils() { }

    //create list of Movie objects by parsing json string
    public static List<Movie> getMovieListFromJson(String jsonString) {
        //if the JSON string is empty or null, then return null.
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }

        List<Movie> movieList = new ArrayList<>();

        //parsing json string
        try {

            //create a json Object from the json string
            JSONObject baseJsonResponse = new JSONObject(jsonString);

            //extract the json array with the key "results",
            JSONArray resultArray = baseJsonResponse.getJSONArray(RESULTS);

            //get results array length
            int count = resultArray.length();

            //from article array extract individual movie details
            for (int i = 0; i < count; i++) {

                JSONObject currentMovie = resultArray.getJSONObject(i);

                //extract integer value for key "id"
                int currentMovieId = currentMovie.optInt(ID);

                //extract string value for key "poster_path"
                String currentPosterPath = currentMovie.optString(POSTER);

                //extract string value for key "title"
                String currentTitle = currentMovie.optString(TITLE);

                //extract string value for key "release_date"
                String currentReleaseDate = currentMovie.optString(RELEASE_DATE);

                //extract string value for key "backdrop_path"
                String currentBockdop = currentMovie.optString(BACKDROP);

                //extract double value for key "vote_average"
                String currentVote = Double.toString(currentMovie.optDouble(VOTE));

                //extract string value for key "overview"
                String currentPlot = currentMovie.optString(PLOT);

                //create Movie object and add to list
                Movie movie = new Movie(currentMovieId,currentPosterPath,currentTitle,currentReleaseDate,currentBockdop,currentVote,currentPlot);
                movieList.add(movie);
            }

        } catch (JSONException e) {
            // catch the exception here & print a log message
            Log.e("JSONUtils", "Problem parsing json results", e);
        }

        // Return the list of Movies
        return movieList;
    }
}
