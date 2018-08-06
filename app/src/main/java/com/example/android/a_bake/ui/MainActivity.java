package com.example.android.a_bake.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.a_bake.R;
import com.example.android.a_bake.loaders.CakesListLoader;
import com.example.android.a_bake.model.Cake;
import com.example.android.a_bake.ui.adapters.CakeAdapter;
import com.example.android.a_bake.utilities.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.a_bake.ui.fragments.StepDetailFragment.checkedIngredients;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Cake>>, SwipeRefreshLayout.OnRefreshListener{

    public static final String LOG_TAG = MainActivity.class.getName();

    /* String to get the cake name for testing purposes */
    public static String mCakeNameTesting;

    /**
     * URL for requesting cake data
     */
    private static final String REQUEST_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    /* Intent extra key */
    public final static String CAKE_KEY = "cake";

    public final static String SEPARATOR = "---";

    /* Adapter for the list of cakes */
    private CakeAdapter mAdapter;

    /* Unique identifier of the main Loader */
    private static final int ID_LOADER = 23;

    // Binding views
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.app_name) TextView mAppName;
    @BindView(R.id.refresh) SwipeRefreshLayout mRefresh;
    @BindView(R.id.recipes_list) RecyclerView mRecyclerView;
    @BindView(R.id.empty_text) TextView mEmptyView;
    @BindView(R.id.loading_spinner) ProgressBar mLoadingIndicator;

    /* Boolean to know whether or not the layout is refreshing */
    private boolean isRefreshing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Bind the views
        ButterKnife.bind(this);

        //Toolbar settings
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Load the font as a TypeFace object
        Typeface typeFacePacificoRegular = Typeface.createFromAsset(getAssets(),
                "PacificoRegular.ttf");
        Typeface typeFaceSueEllenFrancisco = Typeface.createFromAsset(getAssets(),
                "SueEllenFrancisco.ttf");
        // Set custom fonts
        String appName = getString(R.string.app_name);
        SpannableString spannableTitle = new SpannableString(appName);
        spannableTitle.setSpan(new CustomTypefaceSpan(typeFaceSueEllenFrancisco), 0,
                1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableTitle.setSpan(new CustomTypefaceSpan(typeFacePacificoRegular),
                2, appName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Set title
        mAppName.setText(spannableTitle);

        // Progress bar color
        // https://stackoverflow.com/questions/26962136/indeterminate-circle-progress-bar-on-android-is-white-despite-coloraccent-color
        if (mLoadingIndicator.getIndeterminateDrawable() != null) {
            mLoadingIndicator.getIndeterminateDrawable()
                    .setColorFilter(ContextCompat.getColor(this, R.color.colorAccent),
                            android.graphics.PorterDuff.Mode.SRC_IN);
        }

        // RecyclerView settings
        // Use a grid layout manager
        final GridLayoutManager mLayoutManager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.column_number));
        mRecyclerView.setLayoutManager(mLayoutManager);
        // Create a new adapter that takes an empty list of cakes as input
        final List<Cake> mCakesList = new ArrayList<>();
        mAdapter = new CakeAdapter(this, mCakesList, new CakeAdapter.CakeAdapterListener() {
            @Override
            public void OnClick(View v, int position) {
                // Re-start the list of checked ingredients
                if (checkedIngredients != null) {
                    checkedIngredients = null;
                }
                Intent openActivitySteps = new Intent(getApplicationContext(),
                        StepListActivity.class);
                openActivitySteps.putExtra(CAKE_KEY, mCakesList.get(position));
                startActivity(openActivitySteps);
            }
        });
        // Set the adapter on the {@link RecyclerView} so the list can be populated in the UI
        mRecyclerView.setAdapter(mAdapter);

        // Refresh Layout on pulling down
        mRefresh.setOnRefreshListener(this);

        // Initialize the loader
        getSupportLoaderManager().initLoader(ID_LOADER, null, this);
    }

    /**
     * Called by the {@link android.support.v4.app.LoaderManager} when a new Loader needs to be
     * created.
     */
    @NonNull
    @Override
    public Loader<List<Cake>> onCreateLoader(int id, @Nullable Bundle args) {
        return new CakesListLoader(this, REQUEST_URL);
    }

    /**
     * Called when a Loader has finished loading its data.
     */
    @Override
    public void onLoadFinished(@NonNull Loader<List<Cake>> loader, List<Cake> cakes) {
        // Hide the refresh icon
        if (isRefreshing) {
            mRefresh.setRefreshing(false);
            isRefreshing = false;
        }
        // Hide the loading indicator
        mLoadingIndicator.setVisibility(View.GONE);
        // Hide empty state text
        mEmptyView.setVisibility(View.GONE);
        // Clear the adapter of previous data
        mAdapter.clear();
        // Show the recyclerView
        mRecyclerView.setVisibility(View.VISIBLE);

        // If there is a valid list of {@link Cake}s, then add them to the adapter's
        // data set. This will trigger the RecyclerView to update.
        if (cakes != null && !cakes.isEmpty()) {
            mAdapter.addAll(cakes);
            // Testing only
            mCakeNameTesting = cakes.get(0).getCakeName();
            // Save to preferences
            saveToPreferences(cakes);
        } else {
            // Show empty state text
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Called when a created loader is being reset.
     */
    @Override
    public void onLoaderReset(@NonNull Loader<List<Cake>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    // Listen to refreshes made by the user
    @Override
    public void onRefresh() {
        mRefresh.setRefreshing(true);
        isRefreshing = true;
        restartLoader();
    }

    // Restart the Loader to set new data
    public void restartLoader() {
        getSupportLoaderManager().restartLoader(ID_LOADER, null, this);
    }

    /**
     * Helper method to store the cakes names and ingredients in the Shared Preferences file
     */
    private void saveToPreferences(List<Cake> cakes) {
        Set<String> namesSet = new HashSet<>();
        for (int i = 0; i < cakes.size(); i++) {
            String recipeName = cakes.get(i).getCakeName();
            namesSet.add(recipeName);
            Set<String> ingredientsSet = new HashSet<>();
            List<String> ingredientsNames = cakes.get(i).getIngredientsList().get(0);
            List<String> ingredientsQuantities = cakes.get(i).getIngredientsList().get(1);
            for (int j = 0; j < ingredientsNames.size(); j++) {
                ingredientsSet.add(ingredientsNames.get(j) + SEPARATOR + ingredientsQuantities.get(j));
            }
            SharedPreferencesUtils.saveStringSet(this, recipeName, ingredientsSet);
        }
        SharedPreferencesUtils.saveStringSet(this,
                SharedPreferencesUtils.RECIPES_NAMES_SET_KEY, namesSet);
    }
}
