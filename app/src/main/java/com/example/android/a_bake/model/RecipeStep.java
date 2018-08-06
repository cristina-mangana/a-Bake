package com.example.android.a_bake.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Cristina on 01/08/2018.
 * This POJO class represents a single recipe step for a specific cake. Each object has information
 * about that step, such as id, short and long description, url for downloading a video and url for
 * downloading an image thumbnail.
 */
public class RecipeStep implements Parcelable {
    private int mStepId;
    private String mShortDescription, mLongDescription, mVideoUrl, mThumbnailUrl;

    /**
     * No arguments constructor to construct the object with the setter methods
     */
    public RecipeStep() {
    }

    /**
     * Get the step id
     */
    public int getStepId() {
        return mStepId;
    }

    /**
     * Get the short description
     */
    public String getShortDescription() {
        return mShortDescription;
    }

    /**
     * Get the long description
     */
    public String getLongDescription() {
        return mLongDescription;
    }

    /**
     * Get the url to download the step video
     */
    public String getVideoUrl() {
        return mVideoUrl;
    }

    /**
     * Get the url to download the step image
     */
    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    /**
     * Set the step id
     */
    public void setStepId(int stepId) {
        this.mStepId = stepId;
    }

    /**
     * Set the short description
     */
    public void setShortDescription(String shortDescription) {
        this.mShortDescription = shortDescription;
    }

    /**
     * Set the long description
     */
    public void setLongDescription(String longDescription) {
        this.mLongDescription = longDescription;
    }

    /**
     * Set the url to download the step video
     */
    public void setVideoUrl(String videoUrl) {
        this.mVideoUrl = videoUrl;
    }

    /**
     * Set the url to download the step image
     */
    public void setThumbnailUrl(String thumbnailUrl) {
        this.mThumbnailUrl = thumbnailUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mStepId);
        parcel.writeString(mShortDescription);
        parcel.writeString(mLongDescription);
        parcel.writeString(mVideoUrl);
        parcel.writeString(mThumbnailUrl);
    }

    // De-parcel object
    protected RecipeStep(Parcel in) {
        mStepId = in.readInt();
        mShortDescription = in.readString();
        mLongDescription = in.readString();
        mVideoUrl = in.readString();
        mThumbnailUrl = in.readString();
    }

    // Creator
    public static final Creator<RecipeStep> CREATOR = new Creator<RecipeStep>() {
        @Override
        public RecipeStep createFromParcel(Parcel in) {
            return new RecipeStep(in);
        }

        @Override
        public RecipeStep[] newArray(int size) {
            return new RecipeStep[size];
        }
    };
}
