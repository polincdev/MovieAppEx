package com.polinc.movieappex.ui.movies;

import static androidx.test.espresso.action.ViewActions.click;
import static org.junit.Assert.*;

import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.SmallTest;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

import com.polinc.movieappex.R;
import com.polinc.movieappex.util.*;

//Requires        testInstrumentationRunner "com.polinc.movieappex.main.HiltTestRunner"
@SmallTest
@HiltAndroidTest // 1
public class MoviesActivityTest  {

    @Rule
    public HiltAndroidRule hiltAndroidRule = new HiltAndroidRule(this); // 5


    @Before
    public void setUp(){
        hiltAndroidRule.inject();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testNavigation(){
       //  FrameLayout frameLayout=new FrameLayout();

    }
}