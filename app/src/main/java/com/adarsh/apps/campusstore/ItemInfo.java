package com.adarsh.apps.campusstore;

import android.graphics.drawable.Drawable;

/**
 * Created by Adarsh on 14-01-2015.
 */
public class ItemInfo {

    String title;
    String desc;
    Drawable image;
    String price;

    public ItemInfo(String title, String desc, Drawable image,String price) {
            this.title = title;
            this.desc = desc;
            this.image = image;
            this.price=price;
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
    public String getprice(){return price;}
}