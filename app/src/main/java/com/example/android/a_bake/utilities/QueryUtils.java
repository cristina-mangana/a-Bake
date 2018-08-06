package com.example.android.a_bake.utilities;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.a_bake.model.Cake;
import com.example.android.a_bake.model.RecipeStep;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.a_bake.ui.MainActivity.LOG_TAG;

/**
 * Created by Cristina on 01/08/2018.
 * Helper methods related to requesting and receiving cake data.
 */
public final class QueryUtils {

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the data set and return a List of {@link Cake}.
     */
    public static List<Cake> fetchCakesListData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request", e);
        }

        // Extract relevant fields from the JSON response and return it
        return extractListDataFromJson(jsonResponse);
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the whole JSON response from
     * the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
                    Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Cake} objects that has been built up from parsing a JSON response.
     */
    private static List<Cake> extractListDataFromJson(String movieJSON) {

        /* Key value for the "id" integer.*/
        final String ID_KEY = "id";

        /* Key value for the cake "name" string.*/
        final String NAME_KEY = "name";

        /* Key value for the "ingredients" array.*/
        final String INGREDIENTS_LIST_KEY = "ingredients";

        /* Key value for the "quantity" integer.*/
        final String QUANTITY_KEY = "quantity";

        /* Key value for the "measure" string.*/
        final String MEASURE_KEY = "measure";

        /* Key value for the "ingredient" name string.*/
        final String INGREDIENT_KEY = "ingredient";

        /* Key value for the "steps" array.*/
        final String STEPS_KEY = "steps";

        /* Key value for the step "shortDescription" string.*/
        final String SHORT_DESCRIPTION_KEY = "shortDescription";

        /* Key value for the step long "description" name string.*/
        final String LONG_DESCRIPTION_KEY = "description";

        /* Key value for the "videoURL" string. This string is the url to download the step video.*/
        final String VIDEO_URL_KEY = "videoURL";

        /* Key value for the "thumbnailURL" name string. This string is the url to download the step
        image.*/
        final String THUMBNAIL_URL_KEY = "thumbnailURL";

        /* Key value for the number of "servings" integer.*/
        final String SERVINGS_KEY = "servings";

        /* Key value for the "image" string.*/
        final String IMAGE_KEY = "image";

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(movieJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding cakes to
        List<Cake> cakes = new ArrayList<>();

        try {
            JSONArray results = new JSONArray(movieJSON);

            // Loop through each feature in the array
            for (int i = 0; i < results.length(); i++) {
                // Create an empty Cake Object so that we can start adding information about it
                Cake cake = new Cake();

                // Get the cake JSONObject at position i
                JSONObject cakeObject = results.getJSONObject(i);

                // Extract "id"
                if (cakeObject.has(ID_KEY)) {
                    int id = cakeObject.getInt(ID_KEY);
                    cake.setCakeId(id);
                }

                // Extract cake "name"
                if (cakeObject.has(NAME_KEY)) {
                    String name = cakeObject.getString(NAME_KEY);
                    cake.setCakeName(name);
                }

                // Extract the list of ingredients
                if (cakeObject.has(INGREDIENTS_LIST_KEY)) {
                    JSONArray ingredientsArray = cakeObject.getJSONArray(INGREDIENTS_LIST_KEY);

                    // Create an empty ArrayList of ingredient names
                    List<String> ingredientNamesList = new ArrayList<>();
                    // Create an empty ArrayList of ingredient quantities
                    List<String> ingredientQuantitiesList = new ArrayList<>();

                    // Loop through the array
                    for (int j = 0; j < ingredientsArray.length(); j++) {
                        // Get the ingredient JSONObject at position j
                        JSONObject ingredientObject = ingredientsArray.getJSONObject(j);

                        // If the ingredient has a name, extract all its properties and add to the
                        // proper list
                        if (ingredientObject.has(INGREDIENT_KEY)) {
                            String ingredient = ingredientObject.getString(INGREDIENT_KEY);
                            ingredientNamesList.add(ingredient);
                            // Create an empty StringBuilder to add the quantity with its associated
                            // measure
                            StringBuilder quantity = new StringBuilder();
                            if (ingredientObject.has(QUANTITY_KEY)) {
                                String ingredient_quantity = ingredientObject.getString(QUANTITY_KEY);
                                quantity.append(ingredient_quantity);
                            }
                            if (ingredientObject.has(MEASURE_KEY)) {
                                String ingredient_measure = ingredientObject.getString(MEASURE_KEY);
                                quantity.append(ingredient_measure);
                            }
                            ingredientQuantitiesList.add(quantity.toString());
                        }
                    }
                    List<List<String>> ingredients = new ArrayList<>();
                    // Names go at position [0]
                    ingredients.add(ingredientNamesList);
                    // Quantities go at position [1]
                    ingredients.add(ingredientQuantitiesList);
                    // Add the list of ingredients to the Cake object
                    cake.setIngredientsList(ingredients);
                }

                if (cakeObject.has(STEPS_KEY)) {
                    // Create an empty ArrayList that we can start adding cakes to
                    List<RecipeStep> steps = new ArrayList<>();

                    JSONArray stepsArray = cakeObject.getJSONArray(STEPS_KEY);
                    // Loop through the array
                    for (int j = 0; j < stepsArray.length(); j++) {
                        // Create an empty Step Object so that we can start adding information to it
                        RecipeStep step = new RecipeStep();

                        // Get the step JSONObject at position j
                        JSONObject stepObject = stepsArray.getJSONObject(j);

                        // Extract step "id"
                        if (stepObject.has(ID_KEY)) {
                            int stepId = stepObject.getInt(ID_KEY);
                            step.setStepId(stepId);
                        }

                        // Extract step short description
                        if (stepObject.has(SHORT_DESCRIPTION_KEY)) {
                            String shortDescription = stepObject.getString(SHORT_DESCRIPTION_KEY);
                            step.setShortDescription(shortDescription);
                        }

                        // Extract step long description
                        if (stepObject.has(LONG_DESCRIPTION_KEY)) {
                            String longDescription = stepObject.getString(LONG_DESCRIPTION_KEY);
                            step.setLongDescription(longDescription);
                        }

                        // Extract video Url
                        if (stepObject.has(VIDEO_URL_KEY)) {
                            String videoUrl = stepObject.getString(VIDEO_URL_KEY);
                            step.setVideoUrl(videoUrl);
                        }

                        // Extract thumbnail Url
                        if (stepObject.has(THUMBNAIL_URL_KEY)) {
                            String thumbnailUrl = stepObject.getString(THUMBNAIL_URL_KEY);
                            step.setThumbnailUrl(thumbnailUrl);
                        }

                        // Add step Object to list of steps
                        steps.add(step);
                    }
                    // Add the list of steps to the Cake object
                    cake.setRecipeSteps(steps);
                }

                // Extract number of "servings"
                if (cakeObject.has(SERVINGS_KEY)) {
                    int servings = cakeObject.getInt(SERVINGS_KEY);
                    cake.setServings(servings);
                }

                // Extract cake "image"
                if (cakeObject.has(IMAGE_KEY)) {
                    String image = cakeObject.getString(IMAGE_KEY);
                    cake.setImageUrl(image);
                }

                // Add cake Object to list of cakes
                cakes.add(cake);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
        }

        // Return the list of cakes
        return cakes;
    }
}
