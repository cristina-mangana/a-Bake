package com.example.android.a_bake.loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.a_bake.model.Cake;
import com.example.android.a_bake.utilities.QueryUtils;

import java.util.List;

/**
 * Created by Cristina on 01/08/2018.
 * This class loads a list of cakes by using an AsyncTask to perform the network request to
 * the given URL.
 */
public class CakesListLoader extends AsyncTaskLoader<List<Cake>> {

    private String mUrl;

    /* Member variable that will store the Cakes data */
    private List<Cake> mCakes;

    public CakesListLoader(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }

    /*
     * If there already are cached results, just deliver them. Else, force a load.
     */
    @Override
    protected void onStartLoading() {
        if (mCakes != null) {
            deliverResult(mCakes);
        } else {
            forceLoad();
        }
    }

    @Nullable
    @Override
    public List<Cake> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the HTTP request and process the response. Return the list
        return QueryUtils.fetchCakesListData(mUrl);
    }

    /*
     * If the user navigates away from the activity and then returns, avoid extra load by caching
     * existent data.
     */
    @Override
    public void deliverResult(@Nullable List<Cake> data) {
        mCakes = data;
        super.deliverResult(data);
    }
}
