package com.adarsh.apps.campusstore;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.AlertDialog;
import android.os.CountDownTimer;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class DetailActivity extends FragmentActivity{
    TextView title;
    TextView desc;
    byte[] byteArray;
    //ImageView imageview;
    TextView price;
    TextView editItem;
    TextView user;
    ImageButton contact,addfav,del;
    ProgressDialog ringProgressDialog= null;
    private static final int NUM_PAGES = 3;
    private ViewPager mPager;
    int i=0;
    String fav;
    private PagerAdapter mPagerAdapter;
    String  APPLICATION_ID="Go2QLMXo9VPZC597FxSUZvuqIUAJ0xxtu5CHAEla";
    String CLIENT_KEY="nZ8M2KeOBWCBgcOdFCcX4MSqz9AwlM8mQMjqtQn0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
        setContentView(R.layout.activity_detail);
        ringProgressDialog= ProgressDialog.show(DetailActivity.this, "Please wait ...", "Loading details..", true);
        ringProgressDialog.show();
        Intent intent = getIntent();
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
        title=(TextView)findViewById(R.id.textView2);
        user=(TextView)findViewById(R.id.textView3);
        desc=(TextView)findViewById(R.id.description1);
        price=(TextView)findViewById(R.id.price);
        contact=(ImageButton)findViewById(R.id.contact);
        addfav=(ImageButton)findViewById(R.id.addfav);
        del=(ImageButton)findViewById(R.id.delitem);
        editItem=(TextView)findViewById(R.id.EditItem);
        //imageview=(ImageView)findViewById(R.id.imageView2);
        final String titletext = intent.getStringExtra("key");
        final String nametext = intent.getStringExtra("key2");
        final String desctext = intent.getStringExtra("key3");
        final String pricetext=intent.getStringExtra("key4");
        final String idtext=intent.getStringExtra("noteId");
        title.setText(titletext);
        user.setText(nametext);
        price.setText(pricetext);
        desc.setText(desctext);
        final String owner=nametext.split(" ")[2];
        /*Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("picture");*/

        //Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        //ImageView image = (ImageView) findViewById(R.id.imageView2);
        ImageButton edit=(ImageButton)findViewById(R.id.edit);
        LinearLayout editlayout=(LinearLayout)findViewById(R.id.editlayout);
        LinearLayout deletelayout=(LinearLayout)findViewById(R.id.deletelayout);
        final Bitmap bmp=CommonResources.bmp;
        //image.setImageBitmap(bmp);
        ringProgressDialog.dismiss();
        /*byteArray = getIntent().getByteArrayExtra("byteArray");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        BitmapDrawable ob = new BitmapDrawable(getResources(), bmp);
        imageview.setBackgroundDrawable(ob);*/
        if (ParseUser.getCurrentUser().getUsername().toUpperCase().equals(owner)){
            editlayout.setVisibility(View.VISIBLE);
            deletelayout.setVisibility(View.VISIBLE);
        }
       contact.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i=new Intent(DetailActivity.this,userdetails.class);
               i.putExtra("owner",owner);
               startActivity(i);
           }
       });
        del.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
               AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailActivity.this);
               alertDialogBuilder.setMessage("Are you sure you want to remove this item from Campus Store?");

               alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface arg0, int arg1) {
                       ParseObject.createWithoutData("Items", idtext).deleteEventually();
                       Intent i=new Intent(DetailActivity.this,myitems.class);
                       startActivity(i);
                   }
               });

               alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       finish();
                   }
               });

               AlertDialog alertDialog = alertDialogBuilder.create();
               alertDialog.show();

           }
       });
        addfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),idtext,Toast.LENGTH_LONG).show();
//                ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
//
//                // Retrieve the object by id
//                query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
//                    public void done(ParseObject post, ParseException e) {
//                        if (e == null) {
//                            // Now let's update it with some new data.
//                            fav=post.getString("favourites");
//                            if(fav==null)fav="";
                /*ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");

                // Retrieve the object by id
                query.getInBackground("kQGHxFHj0f", new GetCallback<ParseObject>() {
                    public void done(ParseObject post, ParseException e) {
                        if (e == null) {
                            // Now let's update it with some new data.
                            String fav=post.getString("favourites");
                           post.put("favourites",fav+" "+idtext);
>>>>>>> origin/master
                            setProgressBarIndeterminateVisibility(true);
                            post.saveInBackground(new SaveCallback() {
                                public void done(ParseException e) {
                                    setProgressBarIndeterminateVisibility(false);
                                    if (e == null) {
                                        //ItemInfo olditem = new ItemInfo(name, null, desc, null, price);
<<<<<<< HEAD
                                        Toast.makeText(getApplicationContext(), "Saved as favourite!", Toast.LENGTH_SHORT).show();
=======
                                        Toast.makeText(getApplicationContext(), "Saved as favourite", Toast.LENGTH_SHORT).show();
>>>>>>> origin/master
                                        Log.d("test","Saved");
                                    } else {
                                        // The save failed.
                                        Toast.makeText(getApplicationContext(), "Failed to Save", Toast.LENGTH_SHORT).show();
                                        Log.d(getClass().getSimpleName(), "User update error: " + e);
                                    }
                                }
                            });
                        }else{Log.d("test","no item found");}
                    }
<<<<<<< HEAD
=======
                });*/
                final ParseObject u = new ParseObject("Favourites");
                u.put("UserId",ParseUser.getCurrentUser().getObjectId());
                u.put("ItemId",idtext);
                //u.put("objectId",Pa);
                u.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        setProgressBarIndeterminateVisibility(false);
                        if (e == null) {
                            // Saved successfully.
                            //ItemInfo item = new ItemInfo(name,null,desc,null,price);
                            Toast.makeText(getApplicationContext(), "Saved as favourite!", Toast.LENGTH_SHORT).show();
                            //finish();
                        } else {
                            // The save failed.
                            Toast.makeText(getApplicationContext(), "Failed to Save.Try again later.", Toast.LENGTH_SHORT).show();
                            Log.d(getClass().getSimpleName(), "User update error: " + e);
                        }
                    }
                });
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DetailActivity.this,CreateActivity.class);
                i.putExtra("key",titletext);
                i.putExtra("key2",nametext);
                i.putExtra("key3",desctext);
                i.putExtra("key4",pricetext.trim().split(" ")[1]);
                i.putExtra("noteId",idtext);
                CommonResources.bmp=bmp;
                Log.d("test","Editing");
                startActivity(i);
            }
        });
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        change();

    }

    public void change(){
        new CountDownTimer(3000, 3000) {

            public void onTick(long millisUntilFinished) {
                //slideToImage(i);
            }

            public void onFinish() {
                ++i;
                if(i==3)
                    i=0;
                //change();


            }
        }.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.create(0);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
    public void slideToImage(int position){
        mPager.setCurrentItem(position);
    }

}
