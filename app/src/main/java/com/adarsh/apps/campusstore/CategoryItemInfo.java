package com.adarsh.apps.campusstore;

import android.graphics.drawable.Drawable;

/**
 * Created by Adarsh on 20-02-2015.
 */
public class CategoryItemInfo {

    String title;
    Drawable image;

    public CategoryItemInfo(String title, Drawable image) {
        this.title = title;

        this.image = image;

    }

    public String getTitle() {
        return title;
    }

    public Drawable getImage() {
        return image;
    }

}