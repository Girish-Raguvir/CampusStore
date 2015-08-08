package com.adarsh.apps.campusstore;

/**
 * Created by Adarsh on 20-05-2015.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class FavoritesFragment extends Fragment {

    Context c;
    FrameLayout layout;
    private Toolbar mToolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    SwipeRefreshLayout swipeRefreshLayout;
    List<ItemInfo> iteminfo;
    ProgressDialog s=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (FrameLayout) inflater.inflate(R.layout.fragment_favorites, container, false);
        s= ProgressDialog.show(getActivity(), "Please wait ...", "Loading items..", true);
        s.show();
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.my_recycler_view1);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        iteminfo = new ArrayList<ItemInfo>();

        /*Drawable d = getResources().getDrawable(R.drawable.ic_launcher);
          iteminfo.add(new ItemInfo(
                    "Item "  ,
                    "POSTED BY: USER ",
                    d, "Rs. 5000"

            ));*/


        mAdapter = new MainAdapter(iteminfo);
        mRecyclerView.setAdapter(mAdapter);
        //refreshPostList();
        swipeRefreshLayout=(SwipeRefreshLayout)layout.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.myPrimaryColor);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                favload();
            }
        });
        favload();
        return layout;
    }

    String [] a=new String[10000];
    int fl,count;
    public int found(String s)
    {int i;
        for(i=0;i<count;i++)
            if(a[i].equals(s)){Log.d("fav","found");break;}
        if(i>=count)
            return 0;
        else return 1;
    }
    Drawable d1,d2;

    public void favload()
    { //s.show();
        Log.d("fav", "favload");
        swipeRefreshLayout.setRefreshing(true);

        //s.show();

        ParseQuery<ParseObject> queryfav = new ParseQuery<ParseObject>(
                "Favourites");
        s.show();
        //setProgressBarIndeterminateVisibility(true);
        fl=0;
        queryfav.findInBackground(new FindCallback<ParseObject>()

                                  {

                                      @SuppressWarnings("unchecked")
                                      @Override
                                      public void done(List<ParseObject> itemList, ParseException e) {
                                          //setProgressBarIndeterminateVisibility(false);

                                          if (e == null) {

                                              iteminfo.clear();
                                              for (final ParseObject item : itemList) {
                                                  Log.d("test", "inside");
                                                  Log.d("fav", ParseUser.getCurrentUser().getObjectId().toString());
                                                  Log.d("fav", item.getString("UserId"));
                                                  if (ParseUser.getCurrentUser().getObjectId().toString().equals(item.getString("UserId"))) {
                                                      Log.d("fav", "success");
                                                      a[fl] = item.getString("ItemId");
                                                      fl = fl + 1;


                                                  }
                                              }
                                              //s.dismiss();
                                              //swipeRefreshLayout.setRefreshing(false);
                                          } else {
                                              e.printStackTrace();
                                              Log.d(getClass().getSimpleName(), "Error");
                                          }
                                          Log.d("fav", "favloadi" + fl);
                                          count = fl;
                                          Log.d("fav", "favload" + count);
                                          for (fl = 0; fl < count; fl++) {
//                                              Toast.makeText(
//                                                      getApplicationContext(),a[fl], Toast.LENGTH_SHORT
//                                              ).show();
                                              Log.d("fav", a[fl]);
                                          }
                                          Log.d("fav", "favload1");
                                      }

                                  }

        );

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                "Items");
        query.findInBackground(new FindCallback<ParseObject>()

                               {

                                   @SuppressWarnings("unchecked")
                                   @Override
                                   public void done(List<ParseObject> itemList, ParseException e) {
                                       // setProgressBarIndeterminateVisibility(false);
                                       if (e == null) {

                                           iteminfo.clear();
                                           for (final ParseObject item : itemList) {
                                               Log.d("test", "inside");
                                               ParseFile imageFile1 = (ParseFile) item.get("image1");
                                               imageFile1.getDataInBackground(new GetDataCallback() {
                                                   @Override
                                                   public void done(byte[] bytes, ParseException e) {
                                                       if (e == null) {
                                                           // Decode the Byte[] into Bitmap
                                                           CommonResources.bmp1 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                           d1 = new BitmapDrawable(getResources(), CommonResources.bmp1);


                                                       } else {
                                                           e.printStackTrace();
                                                           Log.d("test", "There was a problem downloading the data.");
                                                       }
                                                   }
                                               });
                                               ParseFile imageFile2 = (ParseFile) item.get("image2");
                                               imageFile2.getDataInBackground(new GetDataCallback() {
                                                   @Override
                                                   public void done(byte[] bytes, ParseException e) {
                                                       if (e == null) {
                                                           // Decode the Byte[] into Bitmap
                                                           CommonResources.bmp2 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                           d2 = new BitmapDrawable(getResources(), CommonResources.bmp2);

                                                       } else {
                                                           e.printStackTrace();
                                                           Log.d("test", "There was a problem downloading the data.");
                                                       }
                                                   }
                                               });
                                               if (found(item.getObjectId().toString()) == 1) {
                                                   Log.d("test", "success");

                                                   ParseFile imageFile = (ParseFile) item.get("image");
                                                   imageFile.getDataInBackground(new GetDataCallback() {
                                                       @Override
                                                       public void done(byte[] bytes, ParseException e) {
                                                           if (e == null) {
                                                               Log.d("test",
                                                                       "We've got data in data.");
                                                               Toast.makeText(getActivity(), "Loaded", Toast.LENGTH_LONG);
                                                               // Decode the Byte[] into
                                                               // Bitmap
                                                               CommonResources.bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                               Drawable d = new BitmapDrawable(getResources(), CommonResources.bmp);
                                                               iteminfo.add(new ItemInfo(item.getObjectId(),
                                                                       item.getString("name").toUpperCase(),

                                                                       "Posted by: " + item.getString("postedby").toUpperCase(), item.getString("description"),
                                                                       d, d1, d2,
                                                                       "Rs. " + item.getString("price"),
                                                                       item.getString("category")

                                                               ));
                                                               mAdapter = new MainAdapter(iteminfo);
                                                               mRecyclerView.setAdapter(mAdapter);
                                                               // Close progress dialog
                                                               s.dismiss();
                                                               swipeRefreshLayout.setRefreshing(false);

                                                           } else {
                                                               e.printStackTrace();
                                                               Log.d("test",
                                                                       "There was a problem downloading the data.");
                                                           }
                                                       }
                                                   });

                                               }
                                           }

                                       } else {
                                           e.printStackTrace();
                                           Log.d(getClass().getSimpleName(), "Error");
                                       }
                                       s.dismiss();
                                   }

                               }

        );
        //s.dismiss();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        c = activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        //layout.getForeground().mutate().setAlpha(0);
    }
}

