package com.polinc.movieappex.ui.login;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import javax.inject.Inject;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.HiltTestApplication;

@HiltAndroidTest // 1
@Config(application = HiltTestApplication.class) // 2
@RunWith(RobolectricTestRunner.class) // 3
@LooperMode(LooperMode.Mode.PAUSED)  // 4
public class LoginActivityTest extends TestCase {

    @Rule
    public HiltAndroidRule hiltAndroidRule = new HiltAndroidRule(this); // 5



    @Test
    public void testTrue(){
      assertTrue(true) ;
    }

    @Inject
    public  LoginViewModelFactory loginViewModelFactory  ;

    @Test
    public void testLoginViewModel(){

        assertNull(loginViewModelFactory) ;
        hiltAndroidRule.inject();
        LoginViewModel loginViewModel=   loginViewModelFactory.create(LoginViewModel.class);
        assertNotNull(loginViewModel);



    }

}