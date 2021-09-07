package com.polinc.movieappex.main;


import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4 ;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RunWith(AndroidJUnit4.class)
public class MyApplicationTest extends TestCase {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getContext();
        assertEquals("com.polinc.movieappex.test", appContext.getPackageName());
    }
    @Test
    public void useAppTargetContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.polinc.movieappex", appContext.getPackageName());
    }

 @Test
  public void testAccessAnotherAppsPrivateDataIsNotPossible() {
         Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String filesDirectory = appContext.getFilesDir().getPath();
        String privateFilePath = filesDirectory + "/data/com.android.cts.appwithdata/private_file.txt";
        try {
            new FileInputStream(privateFilePath);
            fail("Was able to access another app's private data");
        } catch (FileNotFoundException e) {
            // expected
        }
    }


}