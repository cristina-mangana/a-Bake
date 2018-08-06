package com.example.android.a_bake.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.a_bake.R;
import com.example.android.a_bake.model.Cake;
import com.example.android.a_bake.ui.adapters.StepsPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.a_bake.ui.StepListActivity.POSITION_KEY;

public class StepDetailActivity extends AppCompatActivity {
    @BindView(R.id.cake_name) TextView mCakeNameTextView;
    @BindView(R.id.pager) ViewPager mPager;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tabDots) TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);

        if (getIntent() == null && !getIntent().hasExtra(MainActivity.CAKE_KEY)) {
            showError();
        }

        int position = 0;
        if (getIntent().hasExtra(POSITION_KEY)) {
            position = getIntent().getExtras().getInt(POSITION_KEY);
        }

        Cake cake = getIntent().getExtras().getParcelable(MainActivity.CAKE_KEY);

        // Toolbar settings
        setSupportActionBar(mToolbar);
        // Add back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // Add the title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Typeface typeFaceSueEllenFrancisco = Typeface.createFromAsset(getAssets(),
                "SueEllenFrancisco.ttf");
        mCakeNameTextView.setText(cake.getCakeName());
        mCakeNameTextView.setTypeface(typeFaceSueEllenFrancisco);

        // Create an adapter that knows which fragment should be shown on each page
        StepsPagerAdapter adapter = new StepsPagerAdapter(getSupportFragmentManager(),
                cake.getIngredientsList(), cake.getRecipeSteps());

        // Set the adapter onto the view pager
        mPager.setAdapter(adapter);

        mPager.setCurrentItem(position);

        // Add ViewPager Indicator
        // From: https://stackoverflow.com/questions/20586619/android-viewpager-with-bottom-dots
        mTabLayout.setupWithViewPager(mPager, true);
    }

    // Handle back button on toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Helper method to finish the activity when an error occur.
     */
    private void showError() {
        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
        finish();
    }
}
