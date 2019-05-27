package com.gmail.kol.c.arindam.popularmovies;

import android.app.LoaderManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


import com.gmail.kol.c.arindam.popularmovies.Helper.MovieListAdapter;
import com.gmail.kol.c.arindam.popularmovies.Helper.MovieListViewModel;
import com.gmail.kol.c.arindam.popularmovies.Helper.MovieLoader;
import com.gmail.kol.c.arindam.popularmovies.Utils.AppDatabase;
import com.gmail.kol.c.arindam.popularmovies.database.Movie;

import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>,
        MovieListAdapter.MovieOnClickListener, AdapterView.OnItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String CURRENT_PAGE = "current_page";
    private static final String SORT_BY = "sort_by";
    private static final String SPINNER_POSITION = "spinner_position";
    public static final String INTENT_EXTRA_ID = "current_movie";

    //to count pages for url query
    private int currentPage = 1;

    //strings for sort by query
    private String[] queryURL = {"https://api.themoviedb.org/3/movie/popular", "https://api.themoviedb.org/3/movie/top_rated"};
    private String currentURL = queryURL[0];

    //declare spinner & swipe refresh layout
    private Spinner movieSpinner;
    private int spinnerPosition;
    private SwipeRefreshLayout swipeRefreshLayout;
    private  boolean isUserInteracted = false;

    private RecyclerView movieListView;
    private MovieListAdapter movieListAdapter;
    private TextView emptyView;
    private View loadingIndicator;

    //declare database variable
    AppDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create db
        mDB = AppDatabase.getInstance(getApplicationContext());

        if(savedInstanceState != null) {
            currentPage = savedInstanceState.getInt(CURRENT_PAGE);
            currentURL = savedInstanceState.getString(SORT_BY);
            spinnerPosition = savedInstanceState.getInt(SPINNER_POSITION);
        }

        //set up on refresh listener
        swipeRefreshLayout = findViewById(R.id.refresh_movie_list);
        swipeRefreshLayout.setOnRefreshListener(this);

        //setup spinner
        movieSpinner = findViewById(R.id.movie_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        movieSpinner.setAdapter(adapter);
        movieSpinner.setOnItemSelectedListener(this);

        //setup list & loading indicator
        movieListView = findViewById(R.id.rv_movie_list);
        emptyView = findViewById(R.id.empty_view);
        loadingIndicator = findViewById(R.id.loading_indicator);

        //change number of row in gridview to handle screen rotation
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            movieListView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        else{
            movieListView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        movieListAdapter = new MovieListAdapter(this);
        movieListView.setAdapter(movieListAdapter);

        //if favourite movie (2) selected use view model otherwise (0,1) use loader
        if(spinnerPosition<2) {
            //get a reference to the ConnectivityManager to check state of network connectivity
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            //get details on the currently active default data network
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            //if there is a network connection, fetch data
            if (networkInfo != null && networkInfo.isConnected()) {

                //initialize the loader
                getLoaderManager().initLoader(1, null, this).forceLoad();
            } else {
                //hide loading indicator
                loadingIndicator.setVisibility(View.GONE);

                //show empty text message
                emptyView.setText(R.string.no_network);
            }
        } else {showFavouriteMovieList();}
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //save current page & query string during state change
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_PAGE, currentPage);
        outState.putString(SORT_BY, currentURL);
        outState.putInt(SPINNER_POSITION, movieSpinner.getSelectedItemPosition());
        super.onSaveInstanceState(outState);
    }

    //get movie list from themoviedb in a background thread
    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        //build query url from base uri as per current page & sort type
        Uri baseUri = Uri.parse(currentURL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("page",Integer.toString(currentPage));
        uriBuilder.appendQueryParameter("api_key", BuildConfig.API_KEY);
        return new MovieLoader(this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movieList) {
        //hide loading indicator after data loading complete
        loadingIndicator.setVisibility(View.GONE);

        //check that List has no items show empty text, else populate the list
        if (movieList == null || movieList.isEmpty()) {
            emptyView.setText(R.string.no_data);
        } else {
            movieListAdapter.setMovieList(movieList);
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    //set adapter data to null
    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        movieListAdapter.setMovieList(null);
    }

    //on list item click launch movie detail activity & send selected movie object to it
    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetail.class);
        intent.putExtra(INTENT_EXTRA_ID, movie);
        startActivity(intent);
    }

    //if there is action by user set the flag
    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        isUserInteracted = true;
    }

    //spinner item selected reset loader
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

        if(isUserInteracted && position<=1) {
            loadingIndicator.setVisibility(View.VISIBLE);
            currentURL = queryURL[position];
            currentPage = 1;
            getLoaderManager().restartLoader(1, null, this).forceLoad();
        } else if (isUserInteracted && position == 2) {
           showFavouriteMovieList();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //Show favourite movie list using view model
    private void showFavouriteMovieList() {
        loadingIndicator.setVisibility(View.GONE);
        MovieListViewModel viewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);
        viewModel.getFavouriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                movieListAdapter.setMovieList(movies);
            }
        });
    }

    //on down drag increase page no. and reset loader
    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        if(movieSpinner.getSelectedItemPosition()<2) {
            loadingIndicator.setVisibility(View.VISIBLE);
            currentPage++;
            getLoaderManager().restartLoader(1, null, this).forceLoad();
        }
    }
}
