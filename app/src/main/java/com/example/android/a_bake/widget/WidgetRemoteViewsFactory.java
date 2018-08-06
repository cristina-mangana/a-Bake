package com.example.android.a_bake.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.a_bake.R;
import com.example.android.a_bake.utilities.SharedPreferencesUtils;

import java.util.Set;

import static com.example.android.a_bake.ui.MainActivity.SEPARATOR;
import static com.example.android.a_bake.utilities.SharedPreferencesUtils.INGREDIENTS_SET_KEY;

/**
 * Created by Cristina on 05/05/2018
 * Class that acts as an adapter for connecting a collection view in a widget (ListView) and its
 * data set.
 * Help from: https://www.sitepoint.com/killer-way-to-show-a-list-of-items-in-android-collection-widget/
 */
public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private String[] mIngredients;

    // Constructor
    public WidgetRemoteViewsFactory(Context context, String[] ingredients) {
        mContext = context;
        mIngredients = ingredients;
    }

    // Called when the appwidget is created for the first time.
    @Override
    public void onCreate() {

    }

    // Called whenever the appwidget is updated.
    @Override
    public void onDataSetChanged() {
        Set<String> ingredients = SharedPreferencesUtils.getStringSet(mContext, INGREDIENTS_SET_KEY);
        mIngredients = ingredients.toArray(new String[ingredients.size()]);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mIngredients.length;
    }

    // Handles all the processing work. It returns a RemoteViews object (the single list item).
    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews =
                new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredients_list_item);
        String currentIngredient = mIngredients[position];
        if (currentIngredient.contains(SEPARATOR)) {
            String[] parts = currentIngredient.split(SEPARATOR);
            String name = parts[0];
            String quantity = parts[1];
            remoteViews.setTextViewText(R.id.ingredient_name, name);
            remoteViews.setTextViewText(R.id.ingredient_quantity, quantity);
        }
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    // Returns the number of types of views we have in ListView.
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
