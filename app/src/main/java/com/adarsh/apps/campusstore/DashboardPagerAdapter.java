package com.adarsh.apps.campusstore;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by srivatsan on 12/5/15.
 */
class DashboardPagerAdapter extends FragmentPagerAdapter {
    Context c;
    String[] tabheadings;
    public DashboardPagerAdapter(FragmentManager fm, Context c) {
        super(fm);
        this.c=c;
        tabheadings=c.getResources().getStringArray(R.array.tabheadings);
        
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            //case 0:CategoriesFragment myFragment=new CategoriesFragment();
            //     return myFragment;
            case 0:ProfileFragment profileFragment=new ProfileFragment();
                 return profileFragment;
            case 1:MyItemsFragment myItemsFragment = new MyItemsFragment();
                return myItemsFragment;
            case 2:FavoritesFragment favoritesFragment = new FavoritesFragment();
                return favoritesFragment;
            default:return null;
        }

    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return tabheadings[position];
    }

    @Override
    public int getCount() {
        return tabheadings.length;
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
