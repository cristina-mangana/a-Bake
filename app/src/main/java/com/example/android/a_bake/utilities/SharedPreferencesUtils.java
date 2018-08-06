package com.example.android.a_bake.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by Cristina on 04/08/2018
 * Helper class to define keys and methods to work with shared preferences values.
 * Help from: http://androidopentutorials.com/android-sharedpreferences-tutorial-and-example/
 */
public class SharedPreferencesUtils {
    public static final String PREFS_NAME = "com.example.android.a_bake.ABAKE_PREFS";
    public static final String RECIPES_NAMES_SET_KEY = "RECIPES_NAMES_SET";
    public static final String INGREDIENTS_SET_KEY = "INGREDIENTS_SET";

    // Help from: https://stackoverflow.com/questions/7057845/save-arraylist-to-sharedpreferences
    public static void saveStringSet(Context context, String key, Set<String> set) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, set);
        editor.apply();
    }

    public static Set<String> getStringSet(Context context, String key) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getStringSet(key, null);
    }
}
