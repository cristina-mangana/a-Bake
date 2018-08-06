package com.example.android.a_bake.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.a_bake.R;
import com.example.android.a_bake.model.RecipeStep;
import com.example.android.a_bake.ui.CustomTypefaceSpan;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cristina on 03/08/2018.
 * This adapter provides access to the recipe steps items in the data set, creates views for
 * items, and replaces the content of some of the views with new data when the original item
 * is no longer visible.
 */
public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {
    private Context mContext;
    private List<RecipeStep> mSteps;
    private static StepsAdapterListener mOnClickListener;

    // Handle button click
    public interface StepsAdapterListener {
        void OnClick(View v, int position);
    }

    /**
     * Custom constructor
     *
     * @param steps is the list of {@link RecipeStep} objects to be populated in the RecyclerView
     */
    public StepsAdapter(Context context, List<RecipeStep> steps, StepsAdapterListener listener) {
        mContext = context;
        mSteps = steps;
        mOnClickListener = listener;
    }

    // Provide a reference to the views for each data item.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_step_name) TextView stepName;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.OnClick(v, getAdapterPosition());
                }
            });
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public StepsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.step_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull StepsAdapter.ViewHolder holder, int position) {
        // Set custom text style to steps (excluding the first one, as it is the ingredients step)
        if (position != 0) {
            position--;
            RecipeStep currentStep = mSteps.get(position);
            String stepNumber = String.valueOf(currentStep.getStepId());
            String stepLabel = mContext.getString(R.string.step_label) + " " + stepNumber + ": ";
            String stepName = currentStep.getShortDescription();
            String title = stepLabel + stepName;
            SpannableStringBuilder spannableStepTitle = new SpannableStringBuilder(title);
            spannableStepTitle
                    .setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                            0, stepLabel.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Typeface typeFaceSueEllenFrancisco = Typeface.createFromAsset(mContext.getAssets(),
                    "SueEllenFrancisco.ttf");
            spannableStepTitle.setSpan(new CustomTypefaceSpan(typeFaceSueEllenFrancisco), 0,
                    stepLabel.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStepTitle.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext,
                    R.color.colorPrimaryDark)), 0, stepLabel.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStepTitle.setSpan(new AbsoluteSizeSpan(26, true), 0, stepLabel.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.stepName.setText(spannableStepTitle);
        } else {
            // Ingredients label
            holder.stepName.setText(mContext.getString(R.string.ingredients_label));
        }
    }

    @Override
    public int getItemCount() {
        return mSteps.size() + 1;
    }
}
