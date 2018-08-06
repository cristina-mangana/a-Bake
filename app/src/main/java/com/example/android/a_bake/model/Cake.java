package com.example.android.a_bake.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Cristina on 01/08/2018.
 * This POJO class represents a single cake. Each object has information about the cake, such as
 * number of servings, ingredients and recipe steps.
 */
public class Cake implements Parcelable {
    private int mCakeId, mServings;
    private String mCakeName, mImageUrl;
    /* This list stores one list of ingredients names at position 0 and one list of associated
    quantities with measures at position 1 */
    private List<List<String>> mIngredientsList;
    private List<RecipeStep> mRecipeSteps;

    /**
     * No arguments constructor to construct the object with the setter methods
     */
    public Cake() {
    }

    /**
     * Get the cake id
     */
    public int getCakeId() {
        return mCakeId;
    }

    /**
     * Get the number of servings
     */
    public int getServings() {
        return mServings;
    }

    /**
     * Get the cake name
     */
    public String getCakeName() {
        return mCakeName;
    }

    /**
     * Get the url to download the cake image
     */
    public String getImageUrl() {
        return mImageUrl;
    }

    /**
     * Get the list of ingredients
     */
    public List<List<String>> getIngredientsList() {
        return mIngredientsList;
    }

    /**
     * Get the recipe steps
     */
    public List<RecipeStep> getRecipeSteps() {
        return mRecipeSteps;
    }

    /**
     * Set the cake id
     */
    public void setCakeId(int cakeId) {
        this.mCakeId = cakeId;
    }

    /**
     * Set the number of servings
     */
    public void setServings(int servings) {
        this.mServings = servings;
    }

    /**
     * Set the cake name
     */
    public void setCakeName(String cakeName) {
        this.mCakeName = cakeName;
    }

    /**
     * Set the url to download the cake image
     */
    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }

    /**
     * Set the list of ingredients
     */
    public void setIngredientsList(List<List<String>> ingredientsList) {
        this.mIngredientsList = ingredientsList;
    }

    /**
     * Set the recipe steps
     */
    public void setRecipeSteps(List<RecipeStep> recipeSteps) {
        this.mRecipeSteps = recipeSteps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mCakeId);
        parcel.writeInt(mServings);
        parcel.writeString(mCakeName);
        parcel.writeString(mImageUrl);
        parcel.writeList(mIngredientsList);
        parcel.writeList(mRecipeSteps);
    }

    // De-parcel object
    @SuppressWarnings("unchecked")
    protected Cake(Parcel in) {
        mCakeId = in.readInt();
        mServings = in.readInt();
        mCakeName = in.readString();
        mImageUrl = in.readString();
        mIngredientsList = in.readArrayList(List.class.getClassLoader());
        mRecipeSteps = in.readArrayList(RecipeStep.class.getClassLoader());
    }

    // Creator
    public static final Creator<Cake> CREATOR = new Creator<Cake>() {
        @Override
        public Cake createFromParcel(Parcel in) {
            return new Cake(in);
        }

        @Override
        public Cake[] newArray(int size) {
            return new Cake[size];
        }
    };
}
