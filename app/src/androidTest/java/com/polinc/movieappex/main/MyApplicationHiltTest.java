package com.polinc.movieappex.main;

import static org.junit.Assert.*;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.SmallTest;

import com.polinc.movieappex.net.MoviesGetController;
import com.polinc.movieappex.room.MoviesRetrieveController;
import com.polinc.movieappex.ui.login.LoginActivity;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import javax.inject.Inject;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.HiltTestApplication;


//Requires        testInstrumentationRunner "com.polinc.movieappex.main.HiltTestRunner"
@SmallTest
@HiltAndroidTest // 1
public class MyApplicationHiltTest   {

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

    @Before
    public void setUp(){
        hiltAndroidRule.inject();
    }

    @Test
    public void testInjections(){

        assertNotNull(moviesGetController);
        assertNotNull(moviesRetrieveController);
    }

}