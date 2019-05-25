package com.gmail.kol.c.arindam.popularmovies.Helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.kol.c.arindam.popularmovies.database.Movie;
import com.gmail.kol.c.arindam.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

//recycler view adapter for movie list
public class MovieListAdapter extends RecyclerView.Adapter <MovieListAdapter.MovieViewHolder> {

    private List<Movie> movieList = new ArrayList<>();
    private MovieOnClickListener movieOnClickListener;

    //constructor with item click listener
    public MovieListAdapter(MovieOnClickListener movieOnClickListener) {
        this.movieOnClickListener = movieOnClickListener;
    }

    //view holder for list item, also implements click listener for item
    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView moviePoster;
        public TextView errorText;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.iv_poster);
            errorText = itemView.findViewById(R.id.error_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Movie currentMovie = movieList.get(getAdapterPosition());
            movieOnClickListener.onClick(currentMovie);
        }
    }

    //item click listener interface with onclick method
    public interface MovieOnClickListener {
        void onClick(Movie movie);
    }

    @NonNull
    @Override
    public MovieListAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_list_item, viewGroup, false);
        return new MovieViewHolder(view);
    }

    //bind data to view holder items
    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.MovieViewHolder viewHolder, int position) {

        // if movie does not have poster show title else show the poster
        if (movieList.get(position).getMoviePosterPath().equals("null")) {
            viewHolder.moviePoster.setVisibility(View.GONE);
            viewHolder.errorText.setVisibility(View.VISIBLE);
            viewHolder.errorText.setText(movieList.get(position).getMovieTitle());
        } else {
            viewHolder.moviePoster.setVisibility(View.VISIBLE);
            viewHolder.errorText.setVisibility(View.GONE);
            String posterPath = "https://image.tmdb.org/t/p/w185" + movieList.get(position).getMoviePosterPath();
            Picasso.get().load(posterPath).into(viewHolder.moviePoster);
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void  setMovieList (List <Movie> movies) {
        movieList = movies;
        notifyDataSetChanged();
    }
}
