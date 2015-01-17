package com.adarsh.apps.campusstore;

import android.graphics.drawable.Drawable;

/**
 * Created by Adarsh on 14-01-2015.
 */
public class ItemInfo {

    String title;
    String user;
    Drawable image;
    String price;
    String desc;

    public ItemInfo(String title, String user, String desc, Drawable image,String price) {
        this.title = title;
        this.user = user;
        this.image = image;
        this.price=price;
        this.desc=desc;
    }

    public String getTitle() {
        return title;
    }

    public String getUser() {
        return user;
    }
    public String getDesc() { return desc; }
    public Drawable getImage() {
        return image;
    }
    public String getprice(){return price;}
}