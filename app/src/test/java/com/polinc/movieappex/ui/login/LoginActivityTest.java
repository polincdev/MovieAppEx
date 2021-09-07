package com.polinc.movieappex.ui.login;

import android.app.Activity;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.polinc.movieappex.R;
import com.polinc.movieappex.utils.LiveDataTestUtil;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import javax.inject.Inject;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.HiltTestApplication;
import kotlin.jvm.JvmField;

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
    public void testLoginViewModelFactoryInjection(){

        assertNull(loginViewModelFactory) ;
        hiltAndroidRule.inject();
        LoginViewModel loginViewModel=   loginViewModelFactory.create(LoginViewModel.class);
        assertNotNull(loginViewModel);
  }

     public  LoginViewModel  loginViewModel;


    @Test
    public void testLoginViewModelLoginEmptyData(){
        //
        loginViewModel= new LoginViewModel(FakeLoginRepositoryImpl.getInstance(new FakeLoginDataSourceImpl()));
        assertNotNull(loginViewModel);
        //
        try { loginViewModel.login("", ""); }
        catch (Exception e){fail("ViewModel login empty failure");};

     }
    @Test
    public void testLoginViewModelLoginNullData(){
        //
        loginViewModel= new LoginViewModel(FakeLoginRepositoryImpl.getInstance(new FakeLoginDataSourceImpl()));
        assertNotNull(loginViewModel);
        //
        try { loginViewModel.login(null , null ); }
        catch (Exception e){fail("ViewModel login null failure");}

    }
    @Test
    public void testLoginViewModelLoginResultLiveData(){
        //
        loginViewModel= new LoginViewModel(FakeLoginRepositoryImpl.getInstance(new FakeLoginDataSourceImpl()));
        assertNotNull(loginViewModel);
        //
         loginViewModel.getLoginResult().setValue(new LoginResult(R.string.login_failed));
        try {
            assertEquals(  LiveDataTestUtil.getOrAwaitValue(loginViewModel.getLoginResult()).getError(), new LoginResult(R.string.login_failed).getError());
        } catch (InterruptedException e) {
            fail("testLoginViewModelLoginResultLiveData failure");
        }
    }


}