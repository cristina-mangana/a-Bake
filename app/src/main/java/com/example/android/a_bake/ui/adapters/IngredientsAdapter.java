package com.example.android.a_bake.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.android.a_bake.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.a_bake.ui.fragments.StepDetailFragment.checkedIngredients;

/**
 * Created by Cristina on 04/08/2018.
 * This adapter provides access to a ingredients list in the data set, creates views for
 * items, and replaces the content of some of the views with new data when the original item
 * is no longer visible.
 */
public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
    private Context mContext;
    private List<List<String>> mIngredients;

    /**
     * Custom constructor
     *
     * @param ingredients is the list of {ingredients to be populated in the RecyclerView
     */
    public IngredientsAdapter(Context context, List<List<String>> ingredients) {
        mContext = context;
        mIngredients = ingredients;
    }

    // Provide a reference to the views for each data item.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ingredient_quantity)
        TextView quantityTextView;
        @BindView(R.id.ingredient_name)
        TextView ingredientNameTextView;
        @BindView(R.id.ingredient_checkbox)
        CheckBox checkBox;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public IngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.ingredient_list_item, parent, false);
        return new IngredientsAdapter.ViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull final IngredientsAdapter.ViewHolder holder, final int position) {
        String ingredientName = mIngredients.get(0).get(position);
        String ingredientQuantity = mIngredients.get(1).get(position);
        holder.ingredientNameTextView.setText(ingredientName);
        holder.quantityTextView.setText(ingredientQuantity);
        holder.checkBox.setChecked(checkedIngredients[position]);
        if (holder.checkBox.isChecked()) {
            holder.ingredientNameTextView.setTextColor(ContextCompat.getColor(mContext,
                    R.color.hintDarkText));
            holder.ingredientNameTextView.setTypeface(
                    holder.ingredientNameTextView.getTypeface(), Typeface.ITALIC);
        } else {
            holder.ingredientNameTextView.setTextColor(ContextCompat.getColor(mContext,
                    R.color.secondaryDarkText));
            holder.ingredientNameTextView.setTypeface(Typeface.DEFAULT);
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    holder.ingredientNameTextView.setTextColor(ContextCompat.getColor(mContext,
                            R.color.hintDarkText));
                    holder.ingredientNameTextView.setTypeface(
                            holder.ingredientNameTextView.getTypeface(), Typeface.ITALIC);
                } else {
                    holder.ingredientNameTextView.setTextColor(ContextCompat.getColor(mContext,
                            R.color.secondaryDarkText));
                    holder.ingredientNameTextView.setTypeface(Typeface.DEFAULT);
                }
                checkedIngredients[position] = isChecked;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mIngredients.get(0).size();
    }
}
