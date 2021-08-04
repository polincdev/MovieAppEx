package com.polinc.movieappex.ui.movies;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.polinc.movieappex.R;
import com.polinc.movieappex.data.InitDataSource;
import com.polinc.movieappex.databinding.ActivityMoviesBinding;
import com.polinc.movieappex.ui.intro.IntroSlider;
import com.polinc.movieappex.ui.intro.MyCustomOnboarder;
import com.polinc.movieappex.ui.login.LoginActivity;
import com.polinc.movieappex.ui.search.SearchActivity;


import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.jetbrains.annotations.NotNull;

public class MoviesActivity extends AppCompatActivity   {


    ProgressBar loadingProgressBar;
    private AppBarConfiguration mAppBarConfiguration;
   // PopularRecyclerFragment popularRecyclerFragment;
    DrawerLayout drawer;
    NavController navController;
    String currentFragmentLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

       System.out.println("onCreate 1111111111111111111111111");
        super.onCreate(savedInstanceState);

        //Intro
        Intent i =new Intent(getApplicationContext(), IntroSlider.class);
        startActivity(i);

        //setContentView(R.layout.activity_movies_popular);
        ActivityMoviesBinding binding = ActivityMoviesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //
        loadingProgressBar = binding.progressBar;
        loadingProgressBar.setVisibility(View.GONE);
        Toolbar toolbar = binding.appBarMain.toolbar;
        //
        //popularRecyclerFragment=PopularRecyclerFragment.newInstance();

        //Requires  android:theme="@style/Theme.ScrollApplication.NoActionBar"
        //Requires android:paddingTop="?attr/actionBarSize" on bottom element
         setSupportActionBar(binding.appBarMain.toolbar);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    drawer = binding.drawerLayout;
     NavigationView navigationView = binding.navView;
     BottomNavigationView bottomNavigationView= binding.bottomNavigation;
    // Passing each menu ID as a set of Ids because each
    // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.men_mov_pop, R.id.men_mov_top)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.recycle_frag_container);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                currentFragmentLabel=destination.getLabel().toString();
                //System.out.println("onDestinationChanged: "+destination.getLabel());
            }
        });

        //Disabled for the sake of Navigations. Nav prevent inflation other than via Navs class
    /*    if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    // .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left,R.anim.slide_in_from_left, R.anim.slide_out_to_right)
                    .replace(R.id.recycle_frag_container,popularRecyclerFragment )
                    .commitNow();
        }*/
        //

/*

        // <fragment> for commented line. Otherwise only FragmentContainerView. Never FrameLayout - doesnt recognize app namespace
       // NavController navController = Navigation.findNavController(this, R.id.recycle_frag_container);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager() .findFragmentById(R.id.recycle_frag_container);
          navController = navHostFragment.getNavController();
        //Navigation.findNavController(findViewById(R.id.nav_host_fragment)).navigate(R.id.first_fragment);
         //App bar name
        //NavigationUI.setupActionBarWithNavController(this, navController );
        //Disables as doesnt work with back button
        //NavigationUI.setupWithNavController( navigationView, navController);
        //App bar back button
        // NavigationUI.setupWithNavController(toolbar, navController, mAppBarConfiguration);
       NavigationUI.setupWithNavController( navigationView, navController);
*/
        //LIstener for drawer  clicks and initial loading. Overrides default mech so disabled
        // navigationView.setNavigationItemSelectedListener(this);
       /* navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id=menuItem.getItemId();
                //it's possible to do more actions on several items, if there is a large amount of items I prefer switch(){case} instead of if()
                // Handle navigation view item clicks here.
                switch (menuItem.getItemId()) {

                    case R.id.men_mov_pop: {
                        System.out.println("onNavigationItemSelected 1111111111111111111111111");
                        break;
                    }
                    case R.id.men_mov_top: {
                        System.out.println("onNavigationItemSelected 22222222");
                        break;
                    }

                }
                //This is for maintaining the behavior of the Navigation view
                NavigationUI.onNavDestinationSelected(menuItem,navController);
                //This is for closing the drawer after acting on it
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });*/


    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.recycle_frag_container);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.popular_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.add:

            return(true);
        case R.id.search:
            if(currentFragmentLabel.equals(getResources().getString(R.string.menu_pop)))
                InitDataSource.currentMovieList= InitDataSource.MOVIES_POPULAR;
            else if(currentFragmentLabel.equals(getResources().getString(R.string.menu_top)))
                InitDataSource.currentMovieList= InitDataSource.MOVIES_TOPRATED;
            else if(currentFragmentLabel.equals(getResources().getString(R.string.menu_up)))
                InitDataSource.currentMovieList= InitDataSource.MOVIES_UPCOMING;


            startActivity(new Intent(MoviesActivity.this, SearchActivity.class));
            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            return(true);
        case R.id.about:

            return(true);
        case R.id.exit:

            return(true);
    }
        return(super.onOptionsItemSelected(item));
    }
//Disabled for sake of laoding from LoginActivity
/*void onMovieFetchInitSuccess(MoviesWraper moviesWraper )
{
    movies = new ArrayList<Movie>();
    System.out.println("WYNIK="+moviesWraper.getMovieList().size()+" "+recyclerView);
    moviesWraper.getMovieList().forEach(movie ->  movies.add(movie));
    Snackbar.make(recyclerView,"Added "+moviesWraper.getMovieList().size(),2000 ).show();

    //Despite observeOn(AndroidSchedulers.mainThread()) it is not called on UI due to setAdapter hence separate UI thread. Futher ad is ok
    //Only the original thread that created a view hierarchy can touch its views.
    runOnUiThread(new Runnable() {
        @Override
        public void run() {

        // Create adapter passing in the sample user data
        adapter = new MoviesPopularAdapter(movies);
        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapter);
        // Set layout manager to position the items
         recyclerView.setLayoutManager(new LinearLayoutManager(MoviesPopularActivity.this));
        }
    });
}*/


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelableArrayList(  Consts.MOVIE, (ArrayList<? extends Parcelable>) movies);
        System.out.println("0000000000000000000");
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

}