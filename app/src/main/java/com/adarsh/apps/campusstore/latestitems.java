package com.adarsh.apps.campusstore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
/*
Modified by Girish on 15-1-15.
 */

public class latestitems extends ActionBarActivity implements NavigationDrawerCallbacks {

    private Toolbar mToolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<ItemInfo> iteminfo;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog ringProgressDialog= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_myitems);
        ringProgressDialog= ProgressDialog.show(latestitems.this, "Please wait ...", "Loading details..", true);
        ringProgressDialog.show();
        NavigationDrawerFragment.mCurrentSelectedPosition=2;
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar1);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Latest Items");
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view1);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this);
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
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer1);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer1, (DrawerLayout) findViewById(R.id.drawer1), mToolbar);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.myPrimaryColor);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPostList();
            }
        });
        refreshPostList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override

    public void onNavigationDrawerItemSelected(int position) {

        if(position==3){startActivity(new Intent(latestitems.this,myitems.class));}
        else if(position==0){startActivity(new Intent(latestitems.this,categories.class));}
        else if(position==4){startActivity(new Intent(latestitems.this,Favorites.class));
        }
        else if(position==5){startActivity(new Intent(latestitems.this,AboutActivity.class));}
        else if(position==6) {
            final FloatingActionButton feedback = (FloatingActionButton) findViewById(R.id.feedback);
            LayoutInflater layoutInflater
                    = (LayoutInflater)getBaseContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            final View popupView = layoutInflater.inflate(R.layout.popuplayout, null);
            final PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.showAtLocation(feedback, Gravity.CENTER, 0, 0);
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

        else if(position==7){ParseUser.logOut();

            loadLoginView();}
        else if(position==1){startActivity(new Intent(latestitems.this,MainActivity.class));}
    }
    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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
    public int check(ParseObject item)
    {   Calendar cal= Calendar.getInstance();
        int i=0;
        //if(item.getDate("updatedAt").toString().split("")[0].equals(new SimpleDateFormat("MMM").format(cal.getTime()))){++i;}
        if((cal.getTime().getDate())-item.getCreatedAt().getDate()<=1){++i;
    }
       // Log.d("test"," "+item.getCreatedAt().getDate());
        return i;
    }
    Drawable d1,d2;
    private void refreshPostList() {
       //ringProgressDialog.show();
        swipeRefreshLayout.setRefreshing(true);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                "Items");
        ringProgressDialog.show();
        //setProgressBarIndeterminateVisibility(true);


        query.findInBackground(new FindCallback<ParseObject>()

                               {

                                   @SuppressWarnings("unchecked")
                                   @Override
                                   public void done (List < ParseObject > itemList, ParseException e){
                                       //setProgressBarIndeterminateVisibility(false);
                                       if (e == null) {

                                           iteminfo.clear();
                                           for (final ParseObject item : itemList) {
                                               Log.d("test","inside");
                                               if (check(item)==1) {
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
                                                               Toast.makeText(latestitems.this, "Loaded", Toast.LENGTH_LONG);
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

                                   }

                               }

        );
     //ringProgressDialog.dismiss();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(latestitems.this,"Refreshed",Toast.LENGTH_LONG);
            refreshPostList();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
