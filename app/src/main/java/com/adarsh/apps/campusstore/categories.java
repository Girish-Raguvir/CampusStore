package com.adarsh.apps.campusstore;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
/*
Modified by Girish on 15-1-15.
 */

public class categories extends ActionBarActivity implements NavigationDrawerCallbacks {

    private Toolbar mToolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SearchView searchView;
    List<CategoryItemInfo> categories;


    String  APPLICATION_ID="Go2QLMXo9VPZC597FxSUZvuqIUAJ0xxtu5CHAEla";
    String CLIENT_KEY="nZ8M2KeOBWCBgcOdFCcX4MSqz9AwlM8mQMjqtQn0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("test",
                "oncreate in categories");
        super.onCreate(savedInstanceState);
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
        ParseUser user = ParseUser.getCurrentUser();
        //Toast.makeText(getApplicationContext(),user.getObjectId(), Toast.LENGTH_LONG).show();
        if(user==null){loadLoginView();}
        else{

            setContentView(R.layout.activity_categories);

            NavigationDrawerFragment.mCurrentSelectedPosition = 0;
            mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Choose a Category");
            mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
            //mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);
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


            categories = new ArrayList<CategoryItemInfo>();
            categories.add(new CategoryItemInfo("Electronic Gadgets",getResources().getDrawable(R.drawable.electronics)));
            categories.add(new CategoryItemInfo("Books and Stationery",getResources().getDrawable(R.drawable.books)));
            categories.add(new CategoryItemInfo("Cycles and automotive",getResources().getDrawable(R.drawable.cycles)));
            categories.add(new CategoryItemInfo("Musical Instruments",getResources().getDrawable(R.drawable.drums)));
            categories.add(new CategoryItemInfo("Tickets, Gift vouchers, OPO invites",getResources().getDrawable(R.drawable.ic_menu_check)));
            categories.add(new CategoryItemInfo("Others",getResources().getDrawable(R.drawable.ic_menu_check)));



        /*Drawable d = getResources().getDrawable(R.drawable.ic_launcher);
          iteminfo.add(new ItemInfo(
                    "Item "  ,
                    "POSTED BY: USER ",
                    d, "Rs. 5000"

            ));*/


            mAdapter = new CategoryAdapter(categories);
            mRecyclerView.setAdapter(mAdapter);
            //itemAdapter = new ArrayAdapter<ItemInfo>(this,R.layout.list_item_layout,"");

            mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
            mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);



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




    public boolean onClose() {

        return false;
    }

    protected boolean isAlwaysExpanded() {
        return false;
    }



    @Override
    public void onNavigationDrawerItemSelected(int position) {

        if(position==2){startActivity(new Intent(categories.this,myitems.class));}
        else if(position==1){startActivity(new Intent(categories.this,MainActivity.class));}
        else if(position==3){startActivity(new Intent(categories.this,Favorites.class));
        }
        else if(position==4){startActivity(new Intent(categories.this,AboutActivity.class));}
        else if(position==5) {
            mNavigationDrawerFragment.closeDrawer();
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

        else if(position==6){ParseUser.logOut();

            loadLoginView();}
        // else if(position==0){startActivity(new Intent(MainActivity.this,MainActivity.class));}
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

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.search)
        {}
        return super.onOptionsItemSelected(item);
    }


}


