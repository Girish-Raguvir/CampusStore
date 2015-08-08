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
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class MyItemsFragment extends Fragment {

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
    String  APPLICATION_ID="Go2QLMXo9VPZC597FxSUZvuqIUAJ0xxtu5CHAEla";
    String CLIENT_KEY="nZ8M2KeOBWCBgcOdFCcX4MSqz9AwlM8mQMjqtQn0";
    ProgressDialog ringProgressDialog=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (FrameLayout) inflater.inflate(R.layout.fragment_my_items, container, false);
        ringProgressDialog= ProgressDialog.show(getActivity(), "Please wait ...", "Loading items..", true);
        ringProgressDialog.show();
        Parse.initialize(getActivity(), APPLICATION_ID, CLIENT_KEY);
        NavigationDrawerFragment.mCurrentSelectedPosition=3;
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
                refreshPostList();
            }
        });
        // refreshPostList();
        refreshPostList();
        return layout;
    }

    Drawable d1,d2;

    private void refreshPostList() {
        // s.show();

        swipeRefreshLayout.setRefreshing(true);

        //s.show();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                "Items");

        ringProgressDialog.show();
        //setProgressBarIndeterminateVisibility(true);
        //favload();
        query.findInBackground(new FindCallback<ParseObject>()

                               {

                                   @SuppressWarnings("unchecked")
                                   @Override
                                   public void done (List < ParseObject > itemList, ParseException e){
                                       //setProgressBarIndeterminateVisibility(false);
                                       if (e == null) {

                                           iteminfo.clear();
                                           for (final ParseObject item : itemList) {
                                               Log.d("test", "inside");
                                               if (ParseUser.getCurrentUser().getUsername().toUpperCase().equals(item.getString("postedby").toUpperCase())) {
                                                   Log.d("test","success");
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
                                                                       d,d1,d2,
                                                                       "Rs. " + item.getString("price"),
                                                                       item.getString("category")

                                                               ));
                                                               mAdapter = new MainAdapter(iteminfo);
                                                               mRecyclerView.setAdapter(mAdapter);
                                                               // Close progress dialog
                                                               ringProgressDialog.dismiss();
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
                                       ringProgressDialog.dismiss();
                                   }

                               }

        );

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