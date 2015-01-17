package com.adarsh.apps.campusstore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Outline;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
/*
Modified by Girish on 15-1-15.
 */

public class myitems extends ActionBarActivity implements NavigationDrawerCallbacks {

    private Toolbar mToolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<ItemInfo> iteminfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myitems);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar1);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view1);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        iteminfo = new ArrayList<ItemInfo>();

        /*Drawable d = getResources().getDrawable(R.drawable.ic_launcher);
          iteminfo.add(new ItemInfo(
                    "Item "  ,
                    "POSTED BY: USER ",
                    d, "Rs. 5000"

            ));*/


        refreshPostList();
        mAdapter = new MainAdapter(iteminfo);
        mRecyclerView.setAdapter(mAdapter);
        //refreshPostList();
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer1);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer1, (DrawerLayout) findViewById(R.id.drawer1), mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }
    private void refreshPostList() {

       ;
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                "Items");

        setProgressBarIndeterminateVisibility(true);


        query.findInBackground(new FindCallback<ParseObject>()

                               {

                                   @SuppressWarnings("unchecked")
                                   @Override
                                   public void done (List < ParseObject > itemList, ParseException e){
                                       setProgressBarIndeterminateVisibility(false);
                                       if (e == null) {

iteminfo.clear();
                                           for (final ParseObject item : itemList) {
                                               Log.d("test","inside");
                                               if (ParseUser.getCurrentUser().getUsername().toUpperCase().equals(item.getString("postedby").toUpperCase())) {
                                                   Log.d("test","success");
                                                   ParseFile imageFile = (ParseFile) item.get("image");
                                                   imageFile.getDataInBackground(new GetDataCallback() {
                                                       @Override
                                                       public void done(byte[] bytes, ParseException e) {
                                                           if (e == null) {
                                                               Log.d("test",
                                                                       "We've got data in data.");
                                                               Toast.makeText(myitems.this, "Loaded", Toast.LENGTH_LONG);
                                                               // Decode the Byte[] into
                                                               // Bitmap
                                                               CommonResources.bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                               Drawable d = new BitmapDrawable(getResources(), CommonResources.bmp);
                                                               iteminfo.add(new ItemInfo(
                                                                       item.getString("name").toUpperCase(),

                                                                       "Posted by: " + item.getString("postedby").toUpperCase(), item.getString("description"),
                                                                       d,
                                                                       "Rs. " + item.getString("price")

                                                               ));

                                                               // Close progress dialog

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

                                   }

                               }

        );

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(myitems.this,"Refreshed",Toast.LENGTH_LONG);
            refreshPostList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
