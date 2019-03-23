package com.gmail.kol.c.arindam.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.kol.c.arindam.popularmovies.Model.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //get movie object from mainactivity
        Intent intent = getIntent();
        Movie currentMovie = intent.getParcelableExtra(MainActivity.INTENT_EXTRA_ID);
        setTitle(currentMovie.getMovieTitle());

        //declare & assign views
        ImageView backdropImageView = findViewById(R.id.movie_backdrop);
        TextView titleTextView = findViewById(R.id.movie_title);
        TextView releaseDateTextView = findViewById(R.id.release_date);
        TextView voteAverageTextView = findViewById(R.id.vote_average);
        TextView plotTextView = findViewById(R.id.plot);

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
}
