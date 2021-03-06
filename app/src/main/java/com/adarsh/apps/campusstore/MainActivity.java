package com.adarsh.apps.campusstore;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
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
    List<ItemInfo> iteminfo, allItems;
    // on scroll
    private static int current_page = 1;
    private int ival = 0;
    private int loadLimit = 5;  // TODO make these 10
    private final int ITEMS_PER_PAGE = 5;


    ArrayAdapter<ItemInfo> itemAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    String  APPLICATION_ID="Go2QLMXo9VPZC597FxSUZvuqIUAJ0xxtu5CHAEla";
    String CLIENT_KEY="nZ8M2KeOBWCBgcOdFCcX4MSqz9AwlM8mQMjqtQn0";
    ProgressDialog ringProgressDialog=null;
    PopupWindow popupMessage;
    LinearLayout layoutOfPopup;
    String cat;static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("mainactivity",
                "We're inside oncreate");
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
        ParseUser user = ParseUser.getCurrentUser();
        Intent i=getIntent();
        cat=i.getStringExtra("cat");
        context=this.getApplicationContext();
        if(user==null){loadLoginView();}
        else{

            setContentView(R.layout.activity_main_topdrawer);
            ringProgressDialog= ProgressDialog.show(MainActivity.this, "Please wait ...", "Loading items..", true);
            ringProgressDialog.show();
        NavigationDrawerFragment.mCurrentSelectedPosition = 1;
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
            if(cat.contentEquals("Trending Items")==true)
            {getSupportActionBar().setTitle("Trending Items");cat=null;}
            else
                getSupportActionBar().setTitle(cat);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setOnScrollListener(new MainAdapter.EndlessRecyclerOnScrollListener(
                                          (LinearLayoutManager)mLayoutManager) {
            @Override
            public void onLoadMore() {
                loadMoreItems();
            }
        });
        //View addButton = (View) findViewById(R.id.imageButton);


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
        /*addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(i);
            }
        });*/

            final FloatingActionsMenu multiple = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
            multiple.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {

                @Override
                public void onMenuExpanded() {
                    // TODO Auto-generated method stub
                    FrameLayout overlay=(FrameLayout)findViewById(R.id.mainFrame);
                    final int color = 0xCCFFFFFF;
                    final Drawable drawable = new ColorDrawable(color);
                    overlay.setForeground(drawable);
                    Animation animation = new AlphaAnimation(0.0f, 1.0f);
                    animation.setDuration(200);
                    overlay.startAnimation(animation);

                }

                @Override
                public void onMenuCollapsed() {
                    // TODO Auto-generated method stub
                    FrameLayout overlay=(FrameLayout)findViewById(R.id.mainFrame);
                    final int color = 0x00000000;
                    final Drawable drawable = new ColorDrawable(color);
                    overlay.setForeground(drawable);
                }
            });

            final FloatingActionButton additem = (FloatingActionButton) findViewById(R.id.additem);
            additem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, CreateActivity.class);
                    startActivity(i);
                }
            });

        iteminfo = new ArrayList<ItemInfo>();
        allItems = new ArrayList<ItemInfo>();


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
                Toast.makeText(getApplicationContext(),"Please enter feedback.",Toast.LENGTH_LONG);
                refreshPostList();
            }
        });

        refreshPostList();


    }
    }
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
                    return false;
                }
            });
        }*/
        return super.onCreateOptionsMenu(menu);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setupSearchView(MenuItem searchItem) {

        if (isAlwaysExpanded()) {
            searchView.setIconifiedByDefault(false);
        } else {
            searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM |
                                            MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        }
        searchView.setOnQueryTextListener(this);
    }
    // TODO modify this function to include the 'allItems' field.
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
    // TODO modify this function to include the 'allItems' field.
    public boolean onQueryTextSubmit(String query) {
        final int size = allItems.size();
        Log.d("test","SIZE IS" + size);
        List<ItemInfo> temp=new ArrayList<ItemInfo>();
        for (int i = size - 1; i >= 0; i--) {
            temp.set(i,allItems.get(i));
            if (!(allItems.get(i).getUser().toLowerCase().contains(query.toLowerCase())) &&
                !(allItems.get(i).getTitle().toLowerCase().contains(query.toLowerCase())) &&
                !(allItems.get(i).getprice().toLowerCase().contains(query.toLowerCase())) &&
                !(allItems.get(i).getDesc().toLowerCase().contains(query.toLowerCase()))) {
                allItems.remove(i);
                //notifyItemRemoved(i);
                mAdapter = new MainAdapter(allItems);
                mRecyclerView.setAdapter(mAdapter);
            }
            /*else if ((temp.get(i).getUser().toLowerCase().contains(query.toLowerCase())) ||
                     (temp.get(i).getTitle().toLowerCase().contains(query.toLowerCase())) ||
                     (temp.get(i).getprice().toLowerCase().contains(query.toLowerCase())) ||
                     (temp.get(i).getDesc().toLowerCase().contains(query.toLowerCase())))
            {
                allItems.add(temp.get(i));
                mAdapter = new MainAdapter(allItems);
                mRecyclerView.setAdapter(mAdapter);
            }*/
        }

        return false;
    }

    protected boolean isAlwaysExpanded() {
        return false;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Log.d("mainactivity",
                "We're inside onnavigationdraweritemselected");

        if(position==1){startActivity(new Intent(MainActivity.this,DashboardActivity.class));
            Log.d("test",
                    "3");}
        else if(position==0){startActivity(new Intent(MainActivity.this,categories.class));
            Log.d("test",
                    "0");}

        else if(position==2){startActivity(new Intent(MainActivity.this,AboutActivity.class));
            Log.d("test",
                    "4");}
        else if(position==3) {
            View parentView = findViewById(R.id.drawer);
            LayoutInflater layoutInflater
                    = (LayoutInflater)getBaseContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            final View popupView = layoutInflater.inflate(R.layout.popuplayout, null);
            final PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);
            Button btnDismiss = (Button)popupView.findViewById(R.id.sendfeed);
            btnDismiss.setOnClickListener(new Button.OnClickListener(){

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    String s= ((EditText)popupView.findViewById(R.id.editTextfeed)).getText().toString();
                    if(!s.isEmpty())
                    {postfeed(s); popupWindow.dismiss();}
                    else
                        Toast.makeText(getApplicationContext(),"Please enter feedback.",Toast.LENGTH_LONG);

                }});
            ImageButton btnclose=(ImageButton)popupView.findViewById(R.id.close);
            btnclose.setOnClickListener(new Button.OnClickListener(){

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    popupWindow.dismiss();
                }});


            popupWindow.setFocusable(true);
            popupWindow.update();
            mNavigationDrawerFragment.closeDrawer();
            }

        else if(position==4){ParseUser.logOut();

            loadLoginView();}

    }
    private void postfeed(String s)
    {
        if (!s.isEmpty()) {


            final ParseObject post = new ParseObject("Feedback");


            post.put("Feedback", s);
            post.put("User",ParseUser.getCurrentUser().getUsername());

            setProgressBarIndeterminateVisibility(true);
            post.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    setProgressBarIndeterminateVisibility(false);
                    if (e == null) {
                        // Saved successfully.

                        Toast.makeText(getApplicationContext(), "Thank you for your time!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // The save failed.
                        Toast.makeText(getApplicationContext(), "Failed to Save", Toast.LENGTH_SHORT).show();
                        Log.d(getClass().getSimpleName(), "User update error: " + e);
                    }
                }
            });


        }
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }
    int county=0;Drawable d2,d1;
    private void refreshPostList() {
        swipeRefreshLayout.setRefreshing(true);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Items");
        ringProgressDialog.show();

        query.findInBackground(new FindCallback<ParseObject>() {
            @SuppressWarnings("unchecked")
            @Override
            public void done (final List<ParseObject> itemList, ParseException e){
                if (e == null) {
                    // If there are results, update the list of posts and notify the adapter
                    iteminfo.clear();
                    //allItems.clear();
                    allItems = new ArrayList<ItemInfo>(itemList.size());
                    for (int i = 0; i < itemList.size(); ++i) allItems.add(null);
                    ival = 0;
                    loadLimit = 5;
                    for (int i = 0; i < itemList.size(); ++i) {
                        final int index = i;

                        final ParseObject item = itemList.get(index);


                        ParseFile imageFile = (ParseFile) item.get("image");
                        imageFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                if (e == null) {
                                    // Decode the Byte[] into Bitmap
                                    CommonResources.bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    Drawable d = new BitmapDrawable(getResources(), CommonResources.bmp);
                                    ParseFile imageFile2 = (ParseFile) item.get("image2");
                                    CommonResources.bmp2=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                                    d2 = new BitmapDrawable(getResources(), CommonResources.bmp1);
                                    /*imageFile2.getDataInBackground(new GetDataCallback() {
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
                                    });*/
                                    ParseFile imageFile1 = (ParseFile) item.get("image1");
                                    CommonResources.bmp1=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                                    d1 = new BitmapDrawable(getResources(), CommonResources.bmp1);
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
                                    ItemInfo newItem = new ItemInfo(
                                            item.getObjectId(),
                                            item.getString("name"),
                                            "Posted by: " + item.getString("postedby"),
                                            item.getString("description"),
                                            d,d1,d2,
                                            "Rs. " + item.getString("price"),
                                            item.getString("category")
                                    );
                                    ++county;
                                    allItems.set(index, newItem);
                                    if (county == itemList.size()) {
                                        //Arrays.sort(allItems.toArray());
                                        int j;
                                        for (j = 0; j < allItems.size() && iteminfo.size() <= loadLimit; ++j) {
                                            if(cat!=null)
                                            {if (allItems.get(j).getCat().equals(cat))
                                                iteminfo.add(allItems.get(j));}
                                            else
                                                iteminfo.add(allItems.get(j));
                                        }
                                        ival = j;
                                        ringProgressDialog.dismiss();
                                        // Close progress dialog
                                        swipeRefreshLayout.setRefreshing(false);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    e.printStackTrace();
                                    Log.d("test", "There was a problem downloading the data.");
                                }
                            }
                        });

                    }
                } else {
                    e.printStackTrace();
                    Log.d(getClass().getSimpleName(), "Error");
                    return ;
                }
                ringProgressDialog.dismiss();
            }
        });

        /*swipeRefreshLayout.setRefreshing(false);
        mAdapter = new MainAdapter(iteminfo);
        mRecyclerView.setAdapter(mAdapter);*/
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //Toast.makeText(MainActivity.this,"Refreshed",Toast.LENGTH_LONG).show();
            refreshPostList();

            return true;
        }
        if (id == R.id.search)
        {}
        return super.onOptionsItemSelected(item);
    }

    private void loadMoreItems() {
        Log.e("vals", "ival-"+ival+";loadlimit-"+loadLimit+";iteminfo size-"+iteminfo.size()+";allitems size-"+allItems.size());
        if (ival < allItems.size()) {
            Toast.makeText(this, "Loading more items", Toast.LENGTH_SHORT).show();
            loadLimit += ITEMS_PER_PAGE;
            if (loadLimit > allItems.size()) loadLimit = allItems.size();
            int i;
            for (i = ival; i < allItems.size() && iteminfo.size() <= loadLimit; ++i)
                if (cat==null || allItems.get(i).getCat().equals(cat))
                    iteminfo.add(allItems.get(i));
            ival = i;
            mAdapter.notifyDataSetChanged();
        }
    }
}


