package com.polinc.movieappex.search;

import static com.google.common.truth.Truth.assertThat;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.polinc.movieappex.models.Movie;
import com.polinc.movieappex.room.AppDatabase;
import com.polinc.movieappex.room.MoviesRetrieveController;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.HiltTestApplication;

@HiltAndroidTest // 1
@Config(application = HiltTestApplication.class) // 2
@RunWith(RobolectricTestRunner.class) // 3
@LooperMode(LooperMode.Mode.PAUSED)  // 4
public class SearchActivityTest extends TestCase {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule=new InstantTaskExecutorRule();
    @Rule
    public HiltAndroidRule hiltAndroidRule = new HiltAndroidRule(this); // 5

    MoviesRetrieveController moviesRetrieveController;
    AppDatabase db;
    @Before
    public void setUp() throws Exception {
          db= Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase.class ).allowMainThreadQueries().build();
          moviesRetrieveController=new MoviesRetrieveController(  db);
    }

    @After
    public void tearDown() throws Exception {
        db.close();
    }

    @Test
    public void insertIntoDB(){
        List<Movie> movies=new ArrayList<Movie>();
        Movie  movie =new Movie("0","","","","","",0);
        movies.add(movie);
        moviesRetrieveController.saveMovies(movies);
        //
        List<Movie> allMovies=moviesRetrieveController.getAllMovies();
          assertThat(  allMovies .size()).isAtLeast(1);

    }


    @Test
    public void  deleteFromDB(){

    }
}