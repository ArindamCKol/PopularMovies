package com.gmail.kol.c.arindam.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.gmail.kol.c.arindam.popularmovies.Helper.ReviewListAdapter;
import com.gmail.kol.c.arindam.popularmovies.Helper.TrailerListAdapter;
import com.gmail.kol.c.arindam.popularmovies.Utils.AppDatabase;
import com.gmail.kol.c.arindam.popularmovies.database.Movie;
import com.gmail.kol.c.arindam.popularmovies.Utils.AppExecutors;
import com.gmail.kol.c.arindam.popularmovies.Utils.JSONUtils;
import com.gmail.kol.c.arindam.popularmovies.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MovieDetail extends AppCompatActivity implements TrailerListAdapter.TrailerClickListener{

    String youTubeVideoURL = "https://www.youtube.com/watch?v=";
    String youTubeThumbnailURL = "http://img.youtube.com/vi/\"+videoId+/mqdefault.jpg";
    Switch favouriteSwitch;
    AppDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mDB = AppDatabase.getInstance(getApplicationContext());

        //get movie object from mainactivity
        Intent intent = getIntent();
        final Movie currentMovie = intent.getParcelableExtra(MainActivity.INTENT_EXTRA_ID);
//        boolean isFavourite = intent.getBooleanExtra(MainActivity.IS_FAVOURITE, false);
        setTitle(currentMovie.getMovieTitle());

        //declare & assign views
        ImageView backdropImageView = findViewById(R.id.movie_backdrop);
        TextView titleTextView = findViewById(R.id.movie_title);
        TextView releaseDateTextView = findViewById(R.id.release_date);
        TextView voteAverageTextView = findViewById(R.id.vote_average);
        TextView plotTextView = findViewById(R.id.plot);
        favouriteSwitch = findViewById(R.id.switch_favourite);
        checkFavourite(currentMovie.getMovieID());
        //set data to views
        String backdropURL = "https://image.tmdb.org/t/p/w500" + currentMovie.getMovieBackdropPath();
        Picasso.get()
               .load(backdropURL)
               .placeholder(R.drawable.no_image)
               .into(backdropImageView);

        titleTextView.setText(getString(R.string.show_title, currentMovie.getMovieTitle()));
        releaseDateTextView.setText(getString(R.string.show_date, currentMovie.getMovieReleaseDate()));
        voteAverageTextView.setText(getString(R.string.show_vote, currentMovie.getMovieVoteAverage()));
        plotTextView.setText(getString(R.string.show_plot, currentMovie.getMoviePlot()));
        showReviews(currentMovie.getMovieID());
        showTrailer(currentMovie.getMovieID());

        favouriteSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(favouriteSwitch.isChecked()) {
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDB.movieDao().insertFavouriteMovie(currentMovie);
                        }
                    });
                } else {
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDB.movieDao().deleteFavouriteMovie(currentMovie);
                        }
                    });
                }
            }
        });
    }

    private void checkFavourite (final int movieID) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final Movie movie = mDB.movieDao().loadMovieByID(movieID);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (movie != null) {
                            favouriteSwitch.setChecked(true);
                        }
                    }
                });
            }
        });
    }
    private void showTrailer(final int movieID) {
        RecyclerView trailerList = findViewById(R.id.rv_trailer);
        trailerList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        final TrailerListAdapter trailerListAdapter = new TrailerListAdapter(this);
        trailerList.setAdapter(trailerListAdapter);

        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                String trailerJson = null;
                URL trilerURL = NetworkUtils.createUrl("https://api.themoviedb.org/3/movie/"+movieID+"/videos?api_key=" + BuildConfig.API_KEY);
                try {
                    trailerJson = NetworkUtils.makeHttpRequest(trilerURL);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final List<String> movieTrailers = JSONUtils.getTrailerfromJson(trailerJson);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        trailerListAdapter.setYoutubeKeyList(movieTrailers);
                    }
                });
            }
        });
    }

    //handle action bar back button press
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    public void showReviews(final int movieID) {
        RecyclerView reviewList = findViewById(R.id.rv_reviews);
        reviewList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        final ReviewListAdapter reviewListAdapter = new ReviewListAdapter();
        reviewList.setAdapter(reviewListAdapter);

        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                String reviewJSON = null;
                URL reviewURL = NetworkUtils.createUrl("https://api.themoviedb.org/3/movie/"+movieID+"/reviews?api_key=" + BuildConfig.API_KEY);
                try {
                    reviewJSON = NetworkUtils.makeHttpRequest(reviewURL);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final List<String> movieReviews = JSONUtils.getReviewfromJson(reviewJSON);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(movieReviews.size()>0) {
                            reviewListAdapter.setMovieReviewList(movieReviews);

                        }
                    }
                });
            }
        });
    }

    @Override
    public void onClick(String youtubeKey) {
        String youtubeURI = youTubeVideoURL + youtubeKey;
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeURI));
        startActivity(youtubeIntent);
    }
}
