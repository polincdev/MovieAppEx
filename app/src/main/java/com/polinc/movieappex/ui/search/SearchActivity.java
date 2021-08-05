package com.polinc.movieappex.ui.search;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.Menu;
import android.view.View;

import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.polinc.movieappex.R;

import com.polinc.movieappex.databinding.ActivitySearchBinding;
import com.polinc.movieappex.main.MyApplication;
import com.polinc.movieappex.models.Movie;
import com.polinc.movieappex.models.MoviesWraper;
import com.polinc.movieappex.room.MoviesRetrieveController;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;

public class SearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    ActivitySearchBinding binding;
    SearchRecyclerFragment searchRecyclerFragment;
    ProgressBar loadingProgressBar;
    MoviesRetrieveController moviesRetrieveController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar=binding.toolbar;
        setSupportActionBar(binding.toolbar);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        moviesRetrieveController =  ((MyApplication)this.getApplication()).moviesRetrieveController;


        if (savedInstanceState == null) {
            searchRecyclerFragment= SearchRecyclerFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                     .replace(R.id.search_frag_container, searchRecyclerFragment )
                    .commitNow();
        }

        loadingProgressBar = binding.progressBar;
        loadingProgressBar.setVisibility(View.GONE);
        drawer = binding.drawerLayout;

        binding.drawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open right drawer

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                else
                drawer.openDrawer(GravityCompat.START);
            }
        });

        FloatingActionButton fab = binding.appBarSearchContainer.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        drawer.addDrawerListener(toggle);

        toggle.syncState();


        NavigationView navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView bottomNavigationView =binding.bottomNavigationView;
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Toast.makeText(getApplicationContext(), "Camera is clicked", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_gallery) {
            Toast.makeText(getApplicationContext(), "Gallery is clicked", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(getApplicationContext(), "Slideshow is clicked", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_manage) {
            Toast.makeText(getApplicationContext(), "Tools is clicked", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_share) {
            Toast.makeText(getApplicationContext(), "Share is clicked", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_send) {
            Toast.makeText(getApplicationContext(), "Send is clicked", Toast.LENGTH_SHORT).show();

        }
        //Bottom
        else if (id == R.id.men_sea_bot_one) {

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    moviesRetrieveController.saveMovies(searchRecyclerFragment.adapter.getCurrentMovies());

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Movies saved", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }
        else if (id == R.id.men_sea_bot_two) {
          /*  LiveData<List<Movie>> result =  moviesRetrieveController.getMoviesAll();
            result.observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(List<Movie> movies) {
                    onMovieBatchGetSuccess(movies);
                }
            });*/
            ListenableFuture<List<Movie>> future =  moviesRetrieveController.getMoviesAll();
            future.addListener(new Runnable() {
                @Override
                public void run() {
                    List<Movie> movies=null;
                    try {
                        movies = future.get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    List<Movie> finalMovies = movies;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            onMovieBatchGetSuccess(finalMovies);
                        }
                    });
                }
            },
               MoreExecutors.directExecutor()

            );


        }
        else if (id == R.id.men_sea_bot_three) {


            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    moviesRetrieveController.cleanDB( );

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "DB cleaned", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });


        }
        DrawerLayout drawer = binding.drawerLayout;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void onMovieBatchGetSuccess(List<Movie> movies)
       {
             Toast.makeText(getApplicationContext(), "Movies retrieved "+movies.size(), Toast.LENGTH_SHORT).show();
        }
    void onMovieBatchSaveSuccess()
    {
        Toast.makeText(getApplicationContext(), "Movies saved", Toast.LENGTH_SHORT).show();
    }
    private void onDBFailed(Throwable e) {

       e.printStackTrace();
         Toast.makeText(getApplicationContext(), "Operation failed", Toast.LENGTH_SHORT).show();

    }
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_out, R.anim.activity_in);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.searchView);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint("Type your keyword here");
       // searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();
         searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                final List<Movie> filteredModelList = filter(searchRecyclerFragment.movies, newText);
                searchRecyclerFragment.adapter.replaceAll(filteredModelList);
                searchRecyclerFragment.binding.rvMoviesPopular.scrollToPosition(0);
                return true;

            }
        });
     

        return true;
    }

    private static List<Movie> filter(List<Movie> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<Movie> filteredModelList = new ArrayList<>();
        for (Movie model : models) {
            final String text = model.getTitle().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.search:

            return(true);



    }
        return(super.onOptionsItemSelected(item));
    }
}
