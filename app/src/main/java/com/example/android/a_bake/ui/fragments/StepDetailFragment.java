package com.example.android.a_bake.ui.fragments;

import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.a_bake.R;
import com.example.android.a_bake.model.RecipeStep;
import com.example.android.a_bake.ui.adapters.IngredientsAdapter;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cristina on 03/08/2018.
 * This fragment displays the details of a step to make a cake.
 */
public class StepDetailFragment extends Fragment {

    private static final String POSITION_KEY = "position";
    private static final String LIST_KEY = "list";
    private static final String PLAYER_POSITION_KEY = "player_position";
    private static final String WINDOW_KEY = "window_position";
    private static final String PLAY_WHEN_READY_KEY = "play_when_ready";
    private static final int DEFAULT_WINDOW = -1;
    private static final String CHECKED_KEY = "checked_array";

    // Binding views
    @BindView(R.id.step_video)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.step_image)
    ImageView mStepImage;
    @BindView(R.id.step_title)
    TextView mStepTitleTextView;
    @BindView(R.id.step_description)
    TextView mStepDescriptionTextView;
    @BindView(R.id.ingredients_list)
    RecyclerView mIngredientsRecyclerView;

    private SimpleExoPlayer mExoPlayer;
    private Uri mVideoUri;
    private long mExoPlayerCurrentPosition;
    private int mCurrentWindowIndex = DEFAULT_WINDOW;
    /* true if ExoPlayer is playing, false if it's paused */
    private boolean mPlayWhenReady;

    // Array to store the state of the ingredient checkboxes
    public static boolean[] checkedIngredients;

    // Mandatory empty constructor
    public StepDetailFragment() {
    }

    /**
     * Constructor method which allows to pass information in the construction.
     *
     * @param position is the index of the tab using the fragment
     * @return a {@link StepDetailFragment} object
     */
    public static StepDetailFragment getInstance(int position, List list) {
        StepDetailFragment object = new StepDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION_KEY, position);
        bundle.putParcelableArrayList(LIST_KEY, (ArrayList) list);
        object.setArguments(bundle);

        return object;
    }

    // Inflates the correct view
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail_fragment, container, false);
        ButterKnife.bind(this, rootView);

        // Get the index of the tab using the fragment
        int position = getArguments().getInt(POSITION_KEY);

        // Set title font
        Typeface typeFacePacificoRegular = Typeface.createFromAsset(getContext().getAssets(),
                "PacificoRegular.ttf");
        mStepTitleTextView.setTypeface(typeFacePacificoRegular);

        // Set the media height on landscape on mobile
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if (!isTablet) {
            //Get the device screen dimensions
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int screenHeight = getResources().getDisplayMetrics().heightPixels;
            if (screenWidth > screenHeight) {
                ViewGroup.LayoutParams paramsImage = mStepImage.getLayoutParams();
                ViewGroup.LayoutParams paramsVideo = mPlayerView.getLayoutParams();
                paramsImage.height = screenHeight;
                paramsVideo.height = screenHeight;
            }
        }

        if (position == 0) {
            // Show the list of ingredients
            List<List<String>> ingredients = (ArrayList) getArguments().getParcelableArrayList(LIST_KEY);
            if (checkedIngredients == null) {
                checkedIngredients = new boolean[ingredients.get(0).size()];
            }
            // Hide the video
            mPlayerView.setVisibility(View.GONE);
            // Hide the image
            mStepImage.setVisibility(View.GONE);
            // Set the title
            mStepTitleTextView.setText(getString(R.string.ingredients_detail_label));
            // Hide the description
            mStepDescriptionTextView.setVisibility(View.GONE);
            // Set the list of ingredients
            mIngredientsRecyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            mIngredientsRecyclerView.setLayoutManager(linearLayoutManager);
            IngredientsAdapter adapter = new IngredientsAdapter(getContext(), ingredients);
            mIngredientsRecyclerView.setAdapter(adapter);
            mIngredientsRecyclerView.setNestedScrollingEnabled(false);
            // Set divider
            DividerItemDecoration dividerItemDecoration =
                    new DividerItemDecoration(mIngredientsRecyclerView.getContext(),
                            linearLayoutManager.getOrientation());
            dividerItemDecoration.setDrawable(ContextCompat.getDrawable(
                    mIngredientsRecyclerView.getContext(), R.drawable.divider));
            mIngredientsRecyclerView.addItemDecoration(dividerItemDecoration);
        } else {
            position--;
            // Show the proper step
            ArrayList<RecipeStep> steps = getArguments().getParcelableArrayList(LIST_KEY);
            RecipeStep recipeStep = steps.get(position);
            // Set the title
            String stepNumber = String.valueOf(recipeStep.getStepId());
            String stepLabel = getString(R.string.step_label) + " " + stepNumber + ": ";
            String stepName = recipeStep.getShortDescription();
            String title = stepLabel + stepName;
            mStepTitleTextView.setText(title);
            if (!recipeStep.getVideoUrl().isEmpty()) {
                // Set the video
                mPlayerView.setVisibility(View.VISIBLE);
                mStepImage.setVisibility(View.GONE);
                mVideoUri = Uri.parse(recipeStep.getVideoUrl());
                initializePlayer();
            } else if (!recipeStep.getThumbnailUrl().isEmpty()) {
                // Set the image
                mPlayerView.setVisibility(View.GONE);
                mStepImage.setVisibility(View.VISIBLE);
                Picasso.get().load(recipeStep.getThumbnailUrl()).error(R.drawable.app_name)
                        .into(mStepImage);
            } else {
                mPlayerView.setVisibility(View.GONE);
                mStepImage.setVisibility(View.VISIBLE);
                Picasso.get().load(R.drawable.app_name).into(mStepImage);
            }
            // Set the description
            String description = recipeStep.getLongDescription();
            // Find first letter
            if (description != null && !description.isEmpty()) {
                int firstIndex = firstIndexOfLetter(description);
                description = description.substring(firstIndex);
            }
            mStepDescriptionTextView.setText(description);
        }
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mExoPlayerCurrentPosition = savedInstanceState.getLong(PLAYER_POSITION_KEY);
            mCurrentWindowIndex = savedInstanceState.getInt(WINDOW_KEY);
            mPlayWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY_KEY);
            checkedIngredients = savedInstanceState.getBooleanArray(CHECKED_KEY);
        }
    }

    // Fires when a configuration change occurs and activity needs to save state
    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putLong(PLAYER_POSITION_KEY, mExoPlayerCurrentPosition);
        savedInstanceState.putInt(WINDOW_KEY, mCurrentWindowIndex);
        savedInstanceState.putBoolean(PLAY_WHEN_READY_KEY, mPlayWhenReady);
        savedInstanceState.putBooleanArray(CHECKED_KEY, checkedIngredients);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Initialize ExoPlayer
     */
    private void initializePlayer() {
        if (mExoPlayer == null && mVideoUri != null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "ClassicalMusicQuiz");
            MediaSource mediaSource = new ExtractorMediaSource(mVideoUri,
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            if (mCurrentWindowIndex != DEFAULT_WINDOW) {
                mExoPlayer.seekTo(mCurrentWindowIndex, mExoPlayerCurrentPosition);
                mExoPlayer.setPlayWhenReady(mPlayWhenReady);
            }
            mExoPlayer.prepare(mediaSource);
        }
    }

    /**
     * Helper method to find the first letter in a string
     * Help from: https://stackoverflow.com/questions/7957944/search-for-capital-letter-in-string
     *
     * @param str is the string we're interested in
     * @return the index of the first letter
     */
    public int firstIndexOfLetter(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isLetter(str.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            // Get the current position
            mExoPlayerCurrentPosition = mExoPlayer.getCurrentPosition();
            mCurrentWindowIndex = mExoPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23) {
            initializePlayer();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    // https://stackoverflow.com/questions/24697951/view-pager-and-fragment-lifecycle
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            releasePlayer();
        } else {
            initializePlayer();
        }
    }
}
