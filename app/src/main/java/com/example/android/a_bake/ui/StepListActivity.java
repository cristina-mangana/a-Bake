package com.example.android.a_bake.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.android.a_bake.R;
import com.example.android.a_bake.model.Cake;
import com.example.android.a_bake.ui.fragments.StepDetailFragment;
import com.example.android.a_bake.ui.fragments.StepsListFragment;

public class StepListActivity extends AppCompatActivity
        implements StepsListFragment.OnStepClickListener{

    /* Intent extra key */
    public final static String POSITION_KEY = "position";

    private Cake mCake;

    /* Boolean to know if the device is a phone or a tablet */
    private boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);

        if (getIntent() == null && !getIntent().hasExtra(MainActivity.CAKE_KEY)) {
            showError();
        }

        mCake = getIntent().getExtras().getParcelable(MainActivity.CAKE_KEY);

        isTablet = getResources().getBoolean(R.bool.isTablet);

        if (isTablet) {
            if (savedInstanceState == null) {
                // Add initial fragment
                FragmentManager fragmentManager = getSupportFragmentManager();
                StepDetailFragment stepDetailFragment =
                        StepDetailFragment.getInstance(0, mCake.getIngredientsList());
                // Add the fragment to its container using a transaction
                fragmentManager.beginTransaction()
                        .add(R.id.detail_fragment, stepDetailFragment)
                        .commit();
            }
        }
    }

    /**
     * Helper method to finish the activity when an error occur.
     */
    private void showError() {
        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
        finish();
    }

    // Handles click events on StepsListFragment
    @Override
    public void onStepSelected(int position) {
        if (isTablet) {
            StepDetailFragment stepDetailFragment;
            if (position == 0) {
                stepDetailFragment =
                        StepDetailFragment.getInstance(position, mCake.getIngredientsList());
            } else {
                stepDetailFragment =
                        StepDetailFragment.getInstance(position, mCake.getRecipeSteps());
            }
            // Replace the old fragment with a new one
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment, stepDetailFragment)
                    .commit();
        } else {
            Intent openActivityDetail = new Intent(getApplicationContext(),
                    StepDetailActivity.class);
            openActivityDetail.putExtra(MainActivity.CAKE_KEY, mCake);
            openActivityDetail.putExtra(POSITION_KEY, position);
            startActivity(openActivityDetail);
        }
    }
}
