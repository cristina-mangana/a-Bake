package com.example.android.a_bake.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.a_bake.R;
import com.example.android.a_bake.utilities.SharedPreferencesUtils;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.a_bake.utilities.SharedPreferencesUtils.INGREDIENTS_SET_KEY;
import static com.example.android.a_bake.utilities.SharedPreferencesUtils.PREFS_NAME;
import static com.example.android.a_bake.utilities.SharedPreferencesUtils.RECIPES_NAMES_SET_KEY;

/**
 * The configuration screen for the {@link IngredientsWidgetProvider IngredientsWidgetProvider} AppWidget.
 * Help from: https://stackoverflow.com/questions/34588501/how-to-create-an-android-widget-with-options
 */
public class IngredientsWidgetProviderConfigureActivity extends Activity {
    @BindView(R.id.recipes_list) ListView mListView;

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private static final String PREF_PREFIX_KEY = "appwidget";

    public IngredientsWidgetProviderConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveRecipePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
        // Get the correct ingredients for this recipe
        Set<String> ingredientsSet = SharedPreferencesUtils.getStringSet(context, text);
        // Set them to preferences
        SharedPreferencesUtils.saveStringSet(context, INGREDIENTS_SET_KEY, ingredientsSet);
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadRecipePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String recipeName = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (recipeName != null) {
            return recipeName;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    // Read the ingredients from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default as an empty set
    static Set<String> loadRecipeIngredients(Context context, int appWidgetId) {
        Set<String> ingredients = SharedPreferencesUtils.getStringSet(context, INGREDIENTS_SET_KEY);
        if (ingredients != null) {
            return ingredients;
        } else {
            return new HashSet<>();
        }
    }

    static void deleteRecipePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.remove(INGREDIENTS_SET_KEY);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.ingredients_widget_provider_configure);
        ButterKnife.bind(this);

        Set<String> namesSet = SharedPreferencesUtils.getStringSet(this, RECIPES_NAMES_SET_KEY);
        // Convert set to array: https://stackoverflow.com/questions/5982447/how-to-convert-setstring-to-string/32179686
        String[] recipes = namesSet.toArray(new String[namesSet.size()]);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, R.layout.widget_configure_activity_list_item,
                        R.id.recipe_name, recipes);
        mListView.setAdapter(adapter);

        // ListView Item Click Listener
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Save the selection
                String recipeName = (String) mListView.getItemAtPosition(position);
                saveRecipePref(getApplicationContext(), mAppWidgetId, recipeName);
                // Create widget
                Context context = IngredientsWidgetProviderConfigureActivity.this;
                // It is the responsibility of the configuration activity to update the app widget
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                IngredientsWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId);
                // Make sure we pass back the original appWidgetId
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }
}

