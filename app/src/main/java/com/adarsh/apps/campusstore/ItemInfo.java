package com.adarsh.apps.campusstore;

import android.graphics.drawable.Drawable;

/**
 * Created by Adarsh on 14-01-2015.
 */
public class ItemInfo {

    String title;
    String desc;
    Drawable image;

    public ItemInfo(String title, String desc, Drawable image) {
            this.title = title;
            this.desc = desc;
            this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public Drawable getImage() {
        return image;
    }
}