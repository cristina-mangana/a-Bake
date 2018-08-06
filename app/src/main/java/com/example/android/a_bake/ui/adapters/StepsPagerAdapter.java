package com.example.android.a_bake.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.a_bake.model.RecipeStep;
import com.example.android.a_bake.ui.fragments.StepDetailFragment;

import java.util.List;

/**
 * Created by Cristina on 03/08/2018.
 * Provides the appropriate {@link android.support.v4.app.Fragment} for a view pager.
 */
public class StepsPagerAdapter extends FragmentPagerAdapter {
    private List<List<String>> mIngredients;
    private List<RecipeStep> mSteps;

    // Constructor
    public StepsPagerAdapter(FragmentManager fm, List<List<String>> ingredients,
                             List<RecipeStep> steps) {
        super(fm);
        mIngredients = ingredients;
        mSteps = steps;
    }

    /**
     * Return the {@link Fragment} that should be displayed for the given page number.
     */
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return StepDetailFragment.getInstance(position, mIngredients);
        } else {
            return StepDetailFragment.getInstance(position, mSteps);
        }
    }

    @Override
    public int getCount() {
        return mSteps.size() + 1;
    }
}
