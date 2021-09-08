package com.polinc.movieappex.ui.login;

import android.app.Activity;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.polinc.movieappex.R;
import com.polinc.movieappex.utils.LiveDataTestUtil;

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

@RunWith(RobolectricTestRunner.class) // 3
public class LoginActivityViewTest extends TestCase {

      @Rule
    public ActivityScenarioRule activityScenarioRule = new ActivityScenarioRule(LoginActivity.class);

    FragmentScenario fragmentScenario;




    @Test
    public void testActScenarioRuleStates(){

        ActivityScenario activityScenario=activityScenarioRule.getScenario();
        activityScenario.onActivity(new ActivityScenario.ActivityAction() {
            @Override
            public void perform(Activity activity) {

            }
        });
    }/*
    @Test
    public void testActScenarioLaunchStates(){
        try(ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class)) {
            scenario.moveToState(Lifecycle.State.RESUMED);    // Moves the activity state to State.RESUMED.
            scenario.moveToState(Lifecycle.State.STARTED);    // Moves the activity state to State.STARTED.
            scenario.moveToState(Lifecycle.State.CREATED);    // Moves the activity state to State.CREATED.
            scenario.moveToState(Lifecycle.State.DESTROYED);  // Moves the activity state to State.DESTROYED.
        }
    }

    @Test
    public void testRoboStates(){
        ActivityController<LoginActivity> controller = Robolectric.buildActivity(LoginActivity.class);
        controller.create().start().resume();  // Moves the activity state to State.RESUMED.
        controller.pause();    // Moves the activity state to State.STARTED. (ON_PAUSE is an event).
        controller.stop();     // Moves the activity state to State.CREATED. (ON_STOP is an event).
        controller.destroy();  // Moves the activity state to State.DESTROYED.

    }
*/

}