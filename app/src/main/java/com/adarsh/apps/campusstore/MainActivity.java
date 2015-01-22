package com.adarsh.apps.campusstore;
import com.adarsh.apps.campusstore.MainAdapter;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.Filterable;
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
/*
Modified by Girish on 15-1-15.
 */

public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks, SearchView.OnQueryTextListener {

    private Toolbar mToolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SearchView searchView;
    List<ItemInfo> iteminfo;
    ArrayAdapter<ItemInfo> itemAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    String  APPLICATION_ID="Go2QLMXo9VPZC597FxSUZvuqIUAJ0xxtu5CHAEla";
    String CLIENT_KEY="nZ8M2KeOBWCBgcOdFCcX4MSqz9AwlM8mQMjqtQn0";
    ProgressDialog ringProgressDialog=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);


        ParseUser user = ParseUser.getCurrentUser();
        if(user==null){loadLoginView();}
        else{

            setContentView(R.layout.activity_main_topdrawer);
            ringProgressDialog= ProgressDialog.show(MainActivity.this, "Please wait ...", "Loading items..", true);
            ringProgressDialog.show();
        NavigationDrawerFragment.mCurrentSelectedPosition = 0;
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Trending Items");
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        View addButton = (View) findViewById(R.id.imageButton);

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
                Intent i = new Intent(MainActivity.this, CreateActivity.class);
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
        //itemAdapter = new ArrayAdapter<ItemInfo>(this,R.layout.list_item_layout,"");

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.myPrimaryColor);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPostList();
            }
        });

        refreshPostList();
    }}
    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Start typing to search...");
        setupSearchView(searchItem);
        /*int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        {
            searchItem.setOnActionExpandListener(new MenuItemCompat.OnActionExpandListener()
            {

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item)
                {
                    // Do something when collapsed
                    Log.i("test", "onMenuItemActionCollapse " + item.getItemId());
                    return true; // Return true to collapse action view
                }

                @Override
                public boolean onMenuItemActionExpand(MenuItem item)
                {
                    // TODO Auto-generated method stub
                    Log.i("test", "onMenuItemActionExpand " + item.getItemId());
                    return true;
                }
            });
        } else
        {
            // do something for phones running an SDK before froyo
            searchView.setOnCloseListener(new SearchView.OnCloseListener()
            {

                @Override
                public boolean onClose()
                {
                    Log.i("test", "mSearchView on close ");
                    // TODO Auto-generated method stub
                    return false;
                }
            });
        }*/

        return super.onCreateOptionsMenu(menu);



    }

    private void setupSearchView(MenuItem searchItem) {

        if (isAlwaysExpanded()) {
            searchView.setIconifiedByDefault(false);
        } else {
            searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
                    | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        }



        searchView.setOnQueryTextListener(this);
    }

    public boolean onQueryTextChange(String newText) {
        final int size = iteminfo.size();
        Log.d("test","SIZE IS" + size);
        for (int i = size - 1; i >= 0; i--) {
            if ((iteminfo.get(i).getUser().contains(newText)== false) && (iteminfo.get(i).getTitle().contains(newText)==false) && (iteminfo.get(i).getprice().contains(newText) == false) && (iteminfo.get(i).getDesc().contains(newText) == false)) {
                iteminfo.remove(i);
                //notifyItemRemoved(i);
                mAdapter = new MainAdapter(iteminfo);
                mRecyclerView.setAdapter(mAdapter);
            }
        }

        return false;
    }

    public boolean onQueryTextSubmit(String query) {
        final int size = iteminfo.size();
        Log.d("test","SIZE IS" + size);
        List<ItemInfo> temp=new ArrayList<ItemInfo>();
        for (int i = size - 1; i >= 0; i--) {
            temp.set(i,iteminfo.get(i));
            if ((iteminfo.get(i).getUser().toLowerCase().contains(query.toLowerCase()) == false) && (iteminfo.get(i).getTitle().toLowerCase().contains(query.toLowerCase()) == false) && (iteminfo.get(i).getprice().toLowerCase().contains(query.toLowerCase()) == false) && (iteminfo.get(i).getDesc().toLowerCase().contains(query.toLowerCase()) == false)) {


                iteminfo.remove(i);
                //notifyItemRemoved(i);
                mAdapter = new MainAdapter(iteminfo);
                mRecyclerView.setAdapter(mAdapter);
            }
            else if ((temp.get(i).getUser().toLowerCase().contains(query.toLowerCase())) || (temp.get(i).getTitle().toLowerCase().contains(query.toLowerCase())) || (temp.get(i).getprice().toLowerCase().contains(query.toLowerCase())) || (temp.get(i).getDesc().toLowerCase().contains(query.toLowerCase())))
            {
                iteminfo.add(temp.get(i));
                mAdapter = new MainAdapter(iteminfo);
                mRecyclerView.setAdapter(mAdapter);
            }
        }

        return false;
    }

    public boolean onClose() {

        return false;
    }

    protected boolean isAlwaysExpanded() {
        return false;
    }



    @Override
    public void onNavigationDrawerItemSelected(int position) {

        if(position==2){startActivity(new Intent(MainActivity.this,myitems.class));}
        else if(position==1){startActivity(new Intent(MainActivity.this,latestitems.class));}
        else if(position==3){startActivity(new Intent(MainActivity.this,AboutActivity.class));}
        else if(position==4){ParseUser.logOut();

            loadLoginView();}
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
        swipeRefreshLayout.setRefreshing(true);

        //ParseQuery<ParseObject> query = ParseQuery.getQuery("Items");
        //query.whereEqualTo("author", ParseUser.getCurrentUser());
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                "Items");


        ringProgressDialog.show();
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
                                    iteminfo.add(new ItemInfo(item.getObjectId(),
                                            item.getString("name").toUpperCase(),

                                            "Posted by: "+item.getString("postedby").toUpperCase(), item.getString("description"),
                                            d,
                                            "Rs. "+item.getString("price")

                                    ));
                                    mAdapter = new MainAdapter(iteminfo);
                                    mRecyclerView.setAdapter(mAdapter);
                                    ringProgressDialog.dismiss();
                                    // Close progress dialog
                                    swipeRefreshLayout.setRefreshing(false);

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
        if (id == R.id.search)
        {}
        return super.onOptionsItemSelected(item);
    }


}


