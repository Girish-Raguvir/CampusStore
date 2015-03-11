package com.adarsh.apps.campusstore;

import android.graphics.drawable.Drawable;

/**
 * Created by Adarsh on 14-01-2015.
 */
public class ItemInfo implements Comparable<ItemInfo>{

    String title;
    String user;
    String id;
    Drawable image,image1,image2;
    String price;
    String desc;
    String cat;

    public ItemInfo(String id,String title, String user, String desc, Drawable image,Drawable image1,Drawable image2,String price,String cat) {
        this.title = title;
        this.user = user;
        this.id=id;
        this.image = image;
        this.image1=image1;
        this.image2=image2;
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
    public Drawable getImage1() {
        return image1;
    }
    public Drawable getImage2() {
        return image2;
    }
    public String getprice(){return price;}
    public String getCat(){return cat;}

    @Override
    public int compareTo(ItemInfo o) {
        return this.id.compareTo(o.id);
    }
}