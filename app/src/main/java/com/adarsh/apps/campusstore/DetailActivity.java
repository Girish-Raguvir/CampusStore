package com.adarsh.apps.campusstore;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;

import com.adarsh.apps.campusstore.R;
import com.parse.ParseUser;

public class DetailActivity extends ActionBarActivity {
    TextView title;
    TextView desc;
    byte[] byteArray;
    ImageView imageview;
    TextView price;
    TextView user;
    Button contact,addfav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        title=(TextView)findViewById(R.id.textView2);
        user=(TextView)findViewById(R.id.textView3);
        desc=(TextView)findViewById(R.id.description1);
        price=(TextView)findViewById(R.id.price);
        contact=(Button)findViewById(R.id.contact);
        addfav=(Button)findViewById(R.id.addfav);
        imageview=(ImageView)findViewById(R.id.imageView2);
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
        ImageView image = (ImageView) findViewById(R.id.imageView2);
        ImageButton edit=(ImageButton)findViewById(R.id.edit);
        final Bitmap bmp=CommonResources.bmp;
        image.setImageBitmap(bmp);
        /*byteArray = getIntent().getByteArrayExtra("byteArray");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        BitmapDrawable ob = new BitmapDrawable(getResources(), bmp);
        imageview.setBackgroundDrawable(ob);*/
        if (ParseUser.getCurrentUser().getUsername().toUpperCase().equals(owner)){
            edit.setVisibility(View.VISIBLE);
        }
       contact.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i=new Intent(DetailActivity.this,userdetails.class);
               i.putExtra("owner",owner);
               startActivity(i);
           }
       });
        addfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
}
