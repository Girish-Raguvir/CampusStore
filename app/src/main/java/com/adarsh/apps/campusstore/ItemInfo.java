package com.adarsh.apps.campusstore;

import android.graphics.drawable.Drawable;

/**
 * Created by Adarsh on 14-01-2015.
 */
public class ItemInfo {

    String title;
    String user;
    String id;
    Drawable image;
    String price;
    String desc;
    String cat;

    public ItemInfo(String id,String title, String user, String desc, Drawable image,String price,String cat) {
        this.title = title;
        this.user = user;
        this.id=id;
        this.image = image;
        this.price=price;
        this.desc=desc;
        this.cat=cat;
    }

    public String getTitle() {
        return title;
    }
    public String getId(){return id;}
    public String getUser() {
        return user;
    }
    public String getDesc() { return desc; }
    public Drawable getImage() {
        return image;
    }
    public String getprice(){return price;}
    public String getCat(){return cat;}
}