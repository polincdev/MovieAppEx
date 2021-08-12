package com.polinc.movieappex.ui.search;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;

import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.polinc.movieappex.R;

import com.polinc.movieappex.databinding.ActivitySearchBinding;
import com.polinc.movieappex.main.Consts;
import com.polinc.movieappex.main.MyApplication;
import com.polinc.movieappex.models.Movie;
import com.polinc.movieappex.models.MoviesWraper;
import com.polinc.movieappex.room.MoviesRetrieveController;
import com.polinc.movieappex.util.FileClient;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;

public class SearchActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        StorageDialogFragment.NoticeDialogListener {

        DrawerLayout drawer;
        ActivitySearchBinding binding;
        SearchRecyclerFragment searchRecyclerFragment;
        ProgressBar loadingProgressBar;
        MoviesRetrieveController moviesRetrieveController;

        enum STORE{SQLLite, SharedPref, AppPref, Files,DataStore, All};
        enum MODE{Save, Load, Clean};
        int currentMode=-1;
        private Gson _gson;
        WorkRequest notWorkRequest ;
        String CHANNEL_ID=Consts.PACK_PATH+"."+System.currentTimeMillis();
        RxDataStore<Preferences> dataStore;
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

        _gson=  new GsonBuilder()
                .setLenient()
                .create();

        if (savedInstanceState == null) {
            searchRecyclerFragment= SearchRecyclerFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                     .replace(R.id.search_frag_container, searchRecyclerFragment )
                    .commitNow();
            //Init notifications
            createNotificationChannel();
            //Init datastore
             dataStore =  new RxPreferenceDataStoreBuilder(this, /*name=*/ "settings").build();
           //Init worker
              notWorkRequest =   new OneTimeWorkRequest.Builder(NotificationWorker.class)  .build();

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
            WorkManager.getInstance(this) .enqueue(notWorkRequest);

            Toast.makeText(getApplicationContext(), "Worker started", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_gallery) {
            Toast.makeText(getApplicationContext(), "Gallery is clicked", Toast.LENGTH_SHORT).show();

        }  else if (id == R.id.nav_share) {
            Toast.makeText(getApplicationContext(), "Share is clicked", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_send) {
            Toast.makeText(getApplicationContext(), "Send is clicked", Toast.LENGTH_SHORT).show();

        }
        //Bottom
        else if (id == R.id.men_sea_bot_one) {

            currentMode=MODE.Save.ordinal();
            showNoticeDialog();

        }
        else if (id == R.id.men_sea_bot_two) {
            currentMode=MODE.Load.ordinal();
            showNoticeDialog();

        }
        else if (id == R.id.men_sea_bot_three) {
            currentMode=MODE.Clean.ordinal();
            showNoticeDialog();
         }
        DrawerLayout drawer = binding.drawerLayout;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void saveToDB()
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                moviesRetrieveController.saveMovies(searchRecyclerFragment.adapter.toCurrentMovies());

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Movies saved to DB" , Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    void saveToSharedPrefs()
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPref = SearchActivity.this.getSharedPreferences(
                        Consts.PACK_PATH , Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(Consts.MOVIES , writeAsJson(searchRecyclerFragment.adapter.toCurrentMovies()));
                editor.apply();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Movies saved to SharedPrefs" , Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    void saveToAppPrefs()
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("saveToAppPrefs "+ writeAsJson(searchRecyclerFragment.adapter.toCurrentMovies()));

                SharedPreferences sharedPref =
                        PreferenceManager.getDefaultSharedPreferences(SearchActivity.this );

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(Consts.MOVIES , writeAsJson(searchRecyclerFragment.adapter.toCurrentMovies()));
                editor.apply();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Movies saved to AppPrefs" , Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    void saveToDataStore()
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Preferences.Key<String> EXAMPLE_DS_KEY = PreferencesKeys.stringKey(Consts.MOVIES);
               //Datastore generaten onCreate
                Single<Preferences> updateResult =  dataStore.updateDataAsync(prefsIn -> {
                    MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
                    mutablePreferences.set(EXAMPLE_DS_KEY,  writeAsJson(searchRecyclerFragment.adapter.toCurrentMovies()));
                    return Single.just(mutablePreferences);
                });

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Movies saved to DataStore" , Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    void saveToFiles()
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {


                System.out.println("saveToFiles "+ new String(writeAsJson(searchRecyclerFragment.adapter.toCurrentMovies()).getBytes()));

                   try (
                       FileOutputStream fos = SearchActivity.this.openFileOutput( Consts.PACK_PATH, Context.MODE_PRIVATE)) {
                         FileClient.writeToFile(fos,writeAsJson(searchRecyclerFragment.adapter.toCurrentMovies()));
                  } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Movies saved to files" , Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    void loadFromDB()
    {
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

    void loadFromSharedPref(){
        SharedPreferences sharedPref = this.getSharedPreferences(
                 Consts.PACK_PATH , Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        String serList = sharedPref.getString(Consts.MOVIES, null);
        System.out.println("serList="+" "+serList);
        List<Movie> movies=null;
        if(serList!=null){
            movies= readFromJson(  serList   );

            for(Movie mov: movies)
                System.out.println("movies="+" "+mov.getTitle());

           ArrayList<Movie> finalMovies = (ArrayList<Movie>)movies;
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "SharedPrefs loaded", Toast.LENGTH_SHORT).show();
                    onMovieBatchGetSuccess(finalMovies);
                }
            });

            return;
        }
        Toast.makeText(getApplicationContext(), "SharedPrefs load failed", Toast.LENGTH_SHORT).show();
        return;

    }
    void loadFromAppPref(){
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(SearchActivity.this );


        SharedPreferences.Editor editor = sharedPref.edit();
        String serList = sharedPref.getString(Consts.MOVIES, null);
        System.out.println("serList="+" "+serList);
        List<Movie> movies=null;
        if(serList!=null){
            movies= readFromJson(  serList   );

            for(Movie mov: movies)
                System.out.println("movies="+" "+mov.getTitle());

            ArrayList<Movie> finalMovies = (ArrayList<Movie>)movies;
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "AppPrefs loaded", Toast.LENGTH_SHORT).show();
                    onMovieBatchGetSuccess(finalMovies);
                }
            });

            return;
        }
        Toast.makeText(getApplicationContext(), "AppPrefs load failed", Toast.LENGTH_SHORT).show();
        return;

    }

    void loadFromDataStore(){

        Preferences.Key<String> EXAMPLE_DS_KEY = PreferencesKeys.stringKey(Consts.MOVIES);

        Flowable<String> counterFlow =  dataStore.data().map(prefs -> prefs.get(EXAMPLE_DS_KEY));
        counterFlow.subscribe(serList -> {
                System.out.println("serList="+" "+serList);
                    List<Movie> movies=null;
                    if(serList!=null){
                        movies= readFromJson(  serList   );

                        for(Movie mov: movies)
                            System.out.println("movies="+" "+mov.getTitle());

                        ArrayList<Movie> finalMovies = (ArrayList<Movie>)movies;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "DataStore loaded", Toast.LENGTH_SHORT).show();
                                onMovieBatchGetSuccess(finalMovies);
                            }
                        });

                        return;
                    }
                    Toast.makeText(getApplicationContext(), "DataStore load failed", Toast.LENGTH_SHORT).show();
                    return;

        }, this::onDBFailed) ;



    }

    void loadFromFiles(){
        File file = new File(this.getFilesDir(),  Consts.PACK_PATH);
        String serList = readFile(file);

        System.out.println("serList="+" "+serList);
        List<Movie> movies=null;
        if(serList!=null){
            movies= readFromJson(  serList   );

            for(Movie mov: movies)
                System.out.println("movies="+" "+mov.getTitle());

            ArrayList<Movie> finalMovies = (ArrayList<Movie>)movies;
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "Files loaded", Toast.LENGTH_SHORT).show();
                    onMovieBatchGetSuccess(finalMovies);
                }
            });

            return;
        }
        Toast.makeText(getApplicationContext(), "Files load failed", Toast.LENGTH_SHORT).show();
        return;

    }

private  String readFile(File file) {

    //String serList2 =new FileClient().readFile(file);
    FileInputStream is = null;
    try {
        is = new FileInputStream(file);
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    int  ch=0;
    ByteArrayOutputStream bos=new ByteArrayOutputStream();
    while (true) {
        try {
            if (!((ch = is.read()) != -1)) break;
        } catch (IOException e) {
            e.printStackTrace();
        }
        bos.write(ch);
    }

    return new String(bos.toString());

}
    private String writeAsJson(  	Object obj)  {

        String res = _gson.toJson(obj);
        return  res;
    }
    private   ArrayList<Movie> readFromJson(String obj   )  {
        //DeserilizeMovie object withing lists
        ArrayList<Movie> movList = _gson.fromJson(obj, new TypeToken<ArrayList<Movie>>() {
        }.getType());

        return  movList;
    }

    void cleanDb()
    {
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
    void cleanDataStore()
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                Preferences.Key<String> EXAMPLE_DS_KEY = PreferencesKeys.stringKey(Consts.MOVIES);
                //Datastore generaten onCreate
                Single<Preferences> updateResult =  dataStore.updateDataAsync(prefsIn -> {
                    MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
                    mutablePreferences.remove(EXAMPLE_DS_KEY);

                    return Single.just(mutablePreferences);
                });
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "DB cleaned", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    void cleanAppPrefs()
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPref =
                        PreferenceManager.getDefaultSharedPreferences(SearchActivity.this );

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(Consts.MOVIES , writeAsJson(searchRecyclerFragment.adapter.toCurrentMovies()));
                editor.apply();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "SharedPrefs cleaned", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    void cleanFiles()
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {


                try (FileOutputStream fos = SearchActivity.this.openFileOutput( Consts.PACK_PATH, Context.MODE_PRIVATE)) {
                    fos.write(writeAsJson(searchRecyclerFragment.adapter.toCurrentMovies()).getBytes(StandardCharsets.UTF_8));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "SharedPrefs cleaned", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    void cleanSharedPrefs()
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPref = SearchActivity.this.getSharedPreferences(
                        Consts.PACK_PATH , Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(Consts.MOVIES , writeAsJson(searchRecyclerFragment.adapter.toCurrentMovies()));
                editor.apply();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "SharedPrefs cleaned", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    void onMovieBatchGetSuccess(List<Movie> movies)
       {
           Toast.makeText(getApplicationContext(), "Movies retrieved "+movies.size(), Toast.LENGTH_SHORT).show();
           updateList(movies);

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
                updateList(filteredModelList);
                return true;

            }
        });
     

        return true;
    }
private void updateList(List<Movie> newlList)
{

    searchRecyclerFragment.adapter.replaceAll(newlList);
    searchRecyclerFragment.binding.rvMoviesPopular.scrollToPosition(0);
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

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new StorageDialogFragment();
        dialog.show(getSupportFragmentManager(), "StorageDialogFragment");
    }
    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, int id) {
      System.out.println("onDialogPositiveClick "+id);

      if(id==STORE.SQLLite.ordinal()) {

          if(currentMode==MODE.Save.ordinal())
              saveToDB();
          else  if(currentMode==MODE.Load.ordinal())
               loadFromDB();
           else  if(currentMode==MODE.Clean.ordinal())
                cleanDb();

          sendNotification("Db operation", "Opertion on SQLLite");
        }
      else if(id==STORE.SharedPref.ordinal()) {

          if(currentMode==MODE.Save.ordinal())
              saveToSharedPrefs();
          else  if(currentMode==MODE.Load.ordinal())
              loadFromSharedPref();
          else  if(currentMode==MODE.Clean.ordinal())
              cleanSharedPrefs();

          sendNotification("SharedPref operation", "Opertion on Shared Preferences");
        }
      else if(id==STORE.AppPref.ordinal()) {

          if(currentMode==MODE.Save.ordinal())
              saveToAppPrefs();
          else  if(currentMode==MODE.Load.ordinal())
              loadFromAppPref();
          else  if(currentMode==MODE.Clean.ordinal())
              cleanAppPrefs();

          sendNotification("AppPref operation", "Opertion on Application Preferences");

      }
      else if(id==STORE.Files.ordinal()) {

          if(currentMode==MODE.Save.ordinal())
              saveToFiles();
          else  if(currentMode==MODE.Load.ordinal())
              loadFromFiles();
          else  if(currentMode==MODE.Clean.ordinal())
              cleanFiles();

          sendNotification("Files operation", "Opertion on Filess");

      }
      else if(id==STORE.DataStore.ordinal()) {

          if(currentMode==MODE.Save.ordinal())
              saveToDataStore();
          else  if(currentMode==MODE.Load.ordinal())
              loadFromDataStore();
          else  if(currentMode==MODE.Clean.ordinal())
              cleanDataStore();

          sendNotification("Files operation", "Opertion on Filess");

      }
      else if(id==STORE.All.ordinal()) {

          if(currentMode==MODE.Save.ordinal()) {
              saveToFiles();
              saveToAppPrefs();
              saveToSharedPrefs();
              saveToDB();
              saveToDataStore();
          }
          else  if(currentMode==MODE.Load.ordinal()) {
              Toast.makeText(getApplicationContext(), "Cannot lod from all the sources", Toast.LENGTH_SHORT).show();

          }
          else  if(currentMode==MODE.Clean.ordinal()) {
              cleanSharedPrefs();
              cleanAppPrefs();
              cleanFiles();
              cleanDb();
              cleanDataStore();
          }

          sendNotification("General operation", "Opertion on all targets");

      }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button

    }

    void sendNotification(String textTitle, String textContent)
    {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, SearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_small)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
               .setContentIntent(pendingIntent)
            .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
  // notificationId is a unique int for each notification that you must define
        int notificationId=(int)System.currentTimeMillis() ;
        notificationManager.notify(notificationId, builder.build());
    }



    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
