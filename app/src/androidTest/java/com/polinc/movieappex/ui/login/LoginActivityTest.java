package com.polinc.movieappex.ui.login;

import static org.junit.Assert.*;

import android.app.Activity;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import junit.framework.TestCase;

import org.junit.After;
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
import hilt_aggregated_deps._com_polinc_movieappex_ui_login_LoginActivity_GeneratedInjector;

//Requires        testInstrumentationRunner "com.polinc.movieappex.main.HiltTestRunner"
@SmallTest
@HiltAndroidTest // 1
 public class LoginActivityTest  {

    @Rule
    public HiltAndroidRule hiltAndroidRule = new HiltAndroidRule(this); // 5

    @Inject
    CustomLoginFragmentFactory fragmentFactory;

    @Before
    public void setUp(){
        hiltAndroidRule.inject();

    }

    @After
    public void tearDown() throws Exception {


    }
}