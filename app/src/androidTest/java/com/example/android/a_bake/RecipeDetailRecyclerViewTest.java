package com.example.android.a_bake;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import com.example.android.a_bake.ui.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Test that checks that the RecyclerView with the list of steps open the correct step on click
 */
public class RecipeDetailRecyclerViewTest {

    /**
     * The ActivityTestRule is a rule provided by Android used for functional testing of a single
     * activity.
     */
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void openListActivity() {
        // Open StepListActivity first
        onView(withId(R.id.recipes_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    /**
     * Clicks on a RecyclerView item and checks it opens up the Step Detail with the correct
     * details.
     */
    @Test
    public void clickRecyclerViewItem_OpensDetail() {
        onView(withId(R.id.step_list)).perform(RecyclerViewActions
                .actionOnItemAtPosition(0, click()));

        String ingredientsLabel = mActivityTestRule.getActivity().getResources()
                .getString(R.string.ingredients_detail_label);
        onView(allOf(withId(R.id.step_title_detail), withText(ingredientsLabel)))
                .check(matches(isDisplayed()));
    }
}
