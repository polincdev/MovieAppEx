package com.polinc.movieappex.main;

import static org.junit.Assert.*;


import com.polinc.movieappex.net.MoviesGetController;
import com.polinc.movieappex.room.MoviesRetrieveController;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.HiltTestApplication;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import javax.inject.Inject;

@HiltAndroidTest // 1
@Config(application = HiltTestApplication.class) // 2
@RunWith(RobolectricTestRunner.class) // 3
@LooperMode(LooperMode.Mode.PAUSED)  // 4
public class MyApplicationHiltTest extends TestCase {

    @Rule
    public HiltAndroidRule hiltAndroidRule = new HiltAndroidRule(this); // 5

    @Test
    public void whenApplicationIsInvoked() { // 7
        assertTrue(true);
    }

    @Inject
    public MoviesGetController moviesGetController ;
    @Inject
    public MoviesRetrieveController moviesRetrieveController ;

    @Test
    public void testInjections(){

        assertNull(moviesGetController) ;
        assertNull(moviesRetrieveController);
        hiltAndroidRule.inject();
        assertNotNull(moviesGetController);
        assertNotNull(moviesRetrieveController);
    }

}