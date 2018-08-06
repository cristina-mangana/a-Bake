package com.example.android.a_bake;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.a_bake.ui.MainActivity;
import com.example.android.a_bake.ui.StepListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Test that checks that the StepListActivity is started by clicking on an item of the RecyclerView
 * in MainActivity
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityIntentTest {

    // Rule that helps to initialize Espresso-Intents before each test annotated with @Test and
    // releases Espresso-Intents after each test run.
    @Rule
    public IntentsTestRule<MainActivity> mTestRule = new IntentsTestRule<>(MainActivity.class);

    /**
     * Clicks on a RecyclerView item and checks it opens up the StepListActivity with the correct
     * cake data details.
     */
    @Test
    public void clickRecyclerViewItem_OpensStepListActivity() {

        // Get a reference to a specific item and click it.
        // Test item click on RecyclerView: https://developer.android.com/training/testing/espresso/lists
        onView(withId(R.id.recipes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Check if the intent has a not null extra.
        intended(allOf(
                hasComponent(StepListActivity.class.getName()),
                hasExtra(equalTo(MainActivity.CAKE_KEY), notNullValue())
        ));

        // Check that the Activity opens with the correct cake name displayed
        onView(withId(R.id.cake_name)).check(matches(withText(MainActivity.mCakeNameTesting)));
    }
}
