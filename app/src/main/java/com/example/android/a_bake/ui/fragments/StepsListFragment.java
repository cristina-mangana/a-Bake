package com.example.android.a_bake.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.a_bake.R;
import com.example.android.a_bake.model.Cake;
import com.example.android.a_bake.ui.MainActivity;
import com.example.android.a_bake.ui.adapters.StepsAdapter;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cristina on 02/08/2018.
 * This fragment displays the list of steps to make a cake.
 */
public class StepsListFragment extends Fragment {
    private String mCakeName = "";

    // Binding views
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.iv_cake_photo)
    ImageView mCakeImage;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.cake_name)
    TextView mCakeNameTextView;
    @BindView(R.id.step_list)
    RecyclerView mStepsList;
    @BindView(R.id.nested_sv)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.servings_tv)
    TextView mServingsTextView;
    @BindView(R.id.step_list_label)
    TextView mStepListLabelTextView;

    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    OnStepClickListener mCallback;

    // OnStepClickListener interface, calls a method in the host activity named onStepSelected
    public interface OnStepClickListener {
        void onStepSelected(int position);
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepClickListener");
        }
    }

    // Mandatory empty constructor
    public StepsListFragment() {
    }

    // Inflates the correct view
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail_fragment, container, false);
        ButterKnife.bind(this, rootView);

        Cake cake = getActivity().getIntent().getExtras().getParcelable(MainActivity.CAKE_KEY);

        // Set the toolbar
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        // Hide the title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Add back button
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Set app bar dimensions (aspect ratio 16:9)
        //Get the device screen dimensions
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        ViewGroup.LayoutParams params = mAppBar.getLayoutParams();
        if (screenHeight > screenWidth) {
            params.height = screenWidth * 9 / 16;
        } else {
            params.height = screenHeight / 2;
        }

        // Set the image
        String imageUrl = cake.getImageUrl();
        // Download the image and attach it to the ImageView. If imageLink is null, no image is
        // assigned or is not recognized as such. Then, a default image is assigned.
        if (imageUrl != null && imageUrl.length() > 0) {
            Picasso.get().load(imageUrl).error(R.drawable.img_placeholder).into(mCakeImage);
        } else {
            Picasso.get().load(R.drawable.img_placeholder).into(mCakeImage);
        }

        // Set the cake name
        Typeface typeFaceSueEllenFrancisco = Typeface.createFromAsset(getContext().getAssets(),
                "SueEllenFrancisco.ttf");
        mCakeName = cake.getCakeName();
        mCakeNameTextView.setText(mCakeName);
        mCakeNameTextView.setTypeface(typeFaceSueEllenFrancisco);

        // Set the number of servings
        mServingsTextView.setText(String.valueOf(cake.getServings()));

        // Set the label font
        Typeface typeFacePacificoRegular = Typeface.createFromAsset(getContext().getAssets(),
                "PacificoRegular.ttf");
        mStepListLabelTextView.setTypeface(typeFacePacificoRegular);

        // Set the list of steps
        // Use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mStepsList.setLayoutManager(linearLayoutManager);
        // Create a new adapter that takes the list of steps as input
        final StepsAdapter adapter = new StepsAdapter(getContext(), cake.getRecipeSteps(),
                new StepsAdapter.StepsAdapterListener() {
                    @Override
                    public void OnClick(View v, int position) {
                        // Trigger the callback method and pass in the position that was clicked
                        mCallback.onStepSelected(position);
                    }
                });
        mStepsList.setAdapter(adapter);
        mStepsList.setNestedScrollingEnabled(false);
        // Set divider
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(mStepsList.getContext(),
                        linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(mStepsList.getContext(),
                R.drawable.divider));
        mStepsList.addItemDecoration(dividerItemDecoration);
        return rootView;
    }

    // Inflate toolbar menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            case R.id.action_share:
                if (!mCakeName.isEmpty()) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.shared_text,
                            getString(R.string.app_name), mCakeName));
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
