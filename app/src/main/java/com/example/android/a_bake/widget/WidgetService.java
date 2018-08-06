package com.example.android.a_bake.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import static com.example.android.a_bake.widget.IngredientsWidgetProvider.INGREDIENTS_KEY;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        String[] ingredients = intent.getExtras().getStringArray(INGREDIENTS_KEY);
        return new WidgetRemoteViewsFactory(this.getApplicationContext(), ingredients);
    }
}
