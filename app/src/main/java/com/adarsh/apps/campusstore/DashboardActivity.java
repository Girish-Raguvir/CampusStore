package com.adarsh.apps.campusstore;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class DashboardActivity extends ActionBarActivity implements NavigationDrawerCallbacks {
    private ViewPager pager;
    private SlidingTabLayout mtabs;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new DashboardPagerAdapter(getSupportFragmentManager(), getApplicationContext()));
        mtabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mtabs.setDistributeEvenly(true);
        mtabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.myAccentColor);
            }
        });
        mtabs.setViewPager(pager);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if(position==0){startActivity(new Intent(DashboardActivity.this,categories.class));}


        else if(position==2){startActivity(new Intent(DashboardActivity.this,AboutActivity.class));}
        else if(position==3) {
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
                        Toast.makeText(getApplicationContext(), "Please enter feedback.", Toast.LENGTH_LONG);

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

        else if(position==4) {
            ParseUser.logOut();

            //loadLoginView();}
        }
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
}
