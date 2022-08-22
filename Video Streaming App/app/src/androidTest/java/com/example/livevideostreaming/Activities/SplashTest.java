package com.example.livevideostreaming.Activities;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

import android.app.Activity;
import android.app.Instrumentation;
import android.util.Log;
import android.view.View;

import androidx.test.rule.ActivityTestRule;

import com.example.livevideostreaming.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SplashTest {

    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule(Splash.class);
    private Splash screen = null;
     Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(Login.class.getName(),null,false);

    @Before
    public void setUp() throws Exception {
        screen = (Splash) activityTestRule.getActivity();
    }

    @Test
    public void test()
    {
        View view = screen.findViewById(R.id.signInOnBoarding);
        Activity login = getInstrumentation().waitForMonitorWithTimeout(monitor,6000);
        assertNull(login);
    }


    @After
    public void tearDown() throws Exception {
        screen = null;
    }
}