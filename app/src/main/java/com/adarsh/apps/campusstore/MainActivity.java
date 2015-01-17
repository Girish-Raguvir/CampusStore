package com.adarsh.apps.campusstore;

import android.content.Intent;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
/*
Modified by Girish on 15-1-15.
 */

public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks {

    private Toolbar mToolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<ItemInfo> iteminfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_topdrawer);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        View addButton = (View)findViewById(R.id.imageButton);
        /*ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                // Or read size directly from the view's width/height
                int diameter = getResources().getDimensionPixelSize(R.dimen.diameter);
                outline.setOval(0, 0, diameter, diameter);
            }
        };
        addButton.setOutlineProvider(viewOutlineProvider);
        addButton.setClipToOutline(true);*/
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,CreateActivity.class);
                startActivity(i);
            }
        });

        iteminfo = new ArrayList<ItemInfo>();

        /*Drawable d = getResources().getDrawable(R.drawable.ic_launcher);
          iteminfo.add(new ItemInfo(
                    "Item "  ,
                    "POSTED BY: USER ",
                    d, "Rs. 5000"

            ));*/


        mAdapter = new MainAdapter(iteminfo);
        mRecyclerView.setAdapter(mAdapter);
        refreshPostList();
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        if(position==2){startActivity(new Intent(MainActivity.this,myitems.class));}
        else if(position==1){startActivity(new Intent(MainActivity.this,latestitems.class));}
       // else if(position==0){startActivity(new Intent(MainActivity.this,MainActivity.class));}
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }
    private void refreshPostList() {

        //ParseQuery<ParseObject> query = ParseQuery.getQuery("Items");
        //query.whereEqualTo("author", ParseUser.getCurrentUser());
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                "Items");

        setProgressBarIndeterminateVisibility(true);
        // Locate the objectId from the class


                    /*for(int i = 0;
                    i<3;i++)

                    {
                        iteminfo.add(new ItemInfo(
                                "Item " + i,
                                "POSTED BY: USER " + i,
                                null

                        ));
                    }*/

                    query.findInBackground(new FindCallback<ParseObject>()

                    {

                        @SuppressWarnings("unchecked")
                        @Override
                        public void done (List < ParseObject > itemList, ParseException e){
                        setProgressBarIndeterminateVisibility(false);
                        if (e == null) {
                            // If there are results, update the list of posts
                            // and notify the adapter
                            //iteminfo.clear();
                            iteminfo.clear();
                            for (final ParseObject item : itemList) {
                                //String x= post.getUpdatedAt().toString();// post.getString("title")
                        /*ParseFile photoFile = item.getParseFile("image");
                        final Bitmap bitpic = new Bitmap;
                        photoFile.getDataInBackground(new GetDataCallback() {

                            @Override
                            public void done(byte[] data, ParseException e) {
                                bitpic = BitmapFactory.decodeByteArray(data, 0, data.length);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bitpic.compress(Bitmap.CompressFormat.PNG, 100, stream);


                            }
                        });*/
                        ParseFile imageFile = (ParseFile) item.get("image");
                        imageFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                if (e == null) {
                                    Log.d("test",
                                            "We've got data in data.");
                                    Toast.makeText(MainActivity.this,"Loaded",Toast.LENGTH_LONG);
                                    // Decode the Byte[] into
                                    // Bitmap
                                    CommonResources.bmp = BitmapFactory.decodeByteArray( bytes, 0, bytes.length );
                                    Drawable d = new BitmapDrawable(getResources(), CommonResources.bmp);
                                    iteminfo.add(new ItemInfo(
                                            item.getString("name").toUpperCase(),

                                            "Posted by: "+item.getString("postedby").toUpperCase(), item.getString("description"),
                                            d,
                                            "Rs. "+item.getString("price")

                                    ));
                                    mAdapter = new MainAdapter(iteminfo);
                                    mRecyclerView.setAdapter(mAdapter);
                                    // Close progress dialog

                                } else {
                                    e.printStackTrace();
                                    Log.d("test",
                                            "There was a problem downloading the data.");
                                }
                            }
                        });
                              /* final Bitmap[] bmp = new Bitmap[1];
                                ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>(
                                        "Items");
                                query1.getInBackground(item.getObjectId(),
                                        new GetCallback<ParseObject>() {

                                            public void done(ParseObject object,
                                                             ParseException e) {
                                                // TODO Auto-generated method stub

                                                // Locate the column named "ImageName" and set
                                                // the string
                                                ParseFile fileObject = (ParseFile) object
                                                        .get("image");
                                                fileObject
                                                        .getDataInBackground(new GetDataCallback() {

                                                            public void done(byte[] data,
                                                                             ParseException e) {
                                                                if (e == null) {
                                                                    Log.d("test",
                                                                            "We've got data in data.");
                                                                    Toast.makeText(MainActivity.this,"Loaded",Toast.LENGTH_LONG);
                                                                    // Decode the Byte[] into
                                                                    // Bitmap
                                                                    bmp[0] = BitmapFactory
                                                                            .decodeByteArray(
                                                                                    data, 0,
                                                                                    data.length);


                                                                    // Close progress dialog

                                                                } else {
                                                                    Log.d("test",
                                                                            "There was a problem downloading the data.");
                                                                }
                                                            }
                                                        });
                                            }
                                        });*/
                               /*// Drawable d = new BitmapDrawable(getResources(), bmp[0]);
                                Drawable d = getResources().getDrawable(R.drawable.ic_launcher);*/

                                //Log.e(getClass().getSimpleName(), item.);
                            }
                            //((ArrayAdapter<ItemInfo>) getListAdapter())
                            // .notifyDataSetChanged();

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
            Toast.makeText(MainActivity.this,"Refreshed",Toast.LENGTH_LONG);
            refreshPostList();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


