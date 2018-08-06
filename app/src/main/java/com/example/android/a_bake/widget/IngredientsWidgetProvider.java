package com.example.android.a_bake.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.a_bake.R;
import com.example.android.a_bake.ui.MainActivity;

import java.util.Set;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link IngredientsWidgetProviderConfigureActivity IngredientsWidgetProviderConfigureActivity}
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {
    public static final String INGREDIENTS_KEY = "ingredients";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_provider);

        // Set the title
        String widgetText = IngredientsWidgetProviderConfigureActivity.loadRecipePref(context, appWidgetId);
        views.setTextViewText(R.id.recipe_name, widgetText);

        AppWidgetManager.getInstance(context);

        // Set the list of ingredients
        Set<String> ingredientsSet = IngredientsWidgetProviderConfigureActivity
                .loadRecipeIngredients(context, appWidgetId);
        String[] ingredients = ingredientsSet.toArray(new String[ingredientsSet.size()]);
        Intent serviceIntent = new Intent(context, WidgetService.class);
        serviceIntent.putExtra(INGREDIENTS_KEY, ingredients);
        views.setRemoteAdapter(R.id.ingredients_list, serviceIntent);
        views.setEmptyView(R.id.ingredients_list, R.id.empty_text);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.recipe_name);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.ingredients_list);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            IngredientsWidgetProviderConfigureActivity.deleteRecipePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

