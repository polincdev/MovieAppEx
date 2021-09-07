package com.polinc.movieappex.ui.search;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import com.polinc.movieappex.R;
import com.polinc.movieappex.models.Movie;
import com.polinc.movieappex.room.AppDatabase;
import com.polinc.movieappex.room.MoviesRetrieveController;

import com.polinc.movieappex.util.LiveDataTestUtil;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import 	androidx.arch.core.executor.testing.InstantTaskExecutorRule;

@RunWith(AndroidJUnit4.class)
public class SearchActivityTest extends TestCase {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule=new InstantTaskExecutorRule();

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