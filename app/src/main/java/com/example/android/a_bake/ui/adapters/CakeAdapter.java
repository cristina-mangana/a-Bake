package com.example.android.a_bake.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.a_bake.R;
import com.example.android.a_bake.model.Cake;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cristina on 02/08/2018.
 * This adapter provides access to the cake items in the data set, creates views for
 * items, and replaces the content of some of the views with new data when the original item
 * is no longer visible.
 */
public class CakeAdapter extends RecyclerView.Adapter<CakeAdapter.ViewHolder> {
    private Context mContext;
    private List<Cake> mCakes;
    private static CakeAdapterListener mOnClickListener;

    // Handle button click
    public interface CakeAdapterListener {
        void OnClick(View v, int position);
    }

    /**
     * Custom constructor
     *
     * @param cakes is the list of {@link Cake} objects to be populated in the RecyclerView
     */
    public CakeAdapter(Context context, List<Cake> cakes, CakeAdapterListener listener) {
        mContext = context;
        mCakes = cakes;
        mOnClickListener = listener;
    }

    // Provide a reference to the views for each data item.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_cake_name)
        TextView cakeName;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.OnClick(v, getAdapterPosition());
                }
            });
            // Set custom font
            Typeface typeFaceSueEllenFrancisco = Typeface.createFromAsset(view.getContext().getAssets(),
                    "SueEllenFrancisco.ttf");
            cakeName.setTypeface(typeFaceSueEllenFrancisco);
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public CakeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.cake_card_item, parent, false);
        // Set the view dimensions
        ViewGroup.LayoutParams params = itemView.getLayoutParams();
        int width = parent.getWidth() / parent.getResources().getInteger(R.integer.column_number)
                - (int) parent.getResources().getDimension(R.dimen.tinyPadding) * 2;
        params.height = width * 9 / 16;
        return new ViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull CakeAdapter.ViewHolder holder, int position) {
        Cake currentCake = mCakes.get(position);
        // Set the cake name
        holder.cakeName.setText(currentCake.getCakeName());
    }

    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mCakes.size();
    }

    // Clear the adapter data
    public void clear() {
        int size = mCakes.size();
        mCakes.clear();
        notifyItemRangeRemoved(0, size);
    }

    // Add new data to the adapter's data set
    public void addAll(List<Cake> newList) {
        mCakes.addAll(newList);
        notifyDataSetChanged();
    }
}
