package com.adarsh.apps.materialtests;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;

import com.adarsh.apps.campusstore.R;

public class DetailActivity extends ActionBarActivity {
    TextView title;
    TextView desc;
    byte[] byteArray;
    ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        title=(TextView)findViewById(R.id.textView2);
        desc=(TextView)findViewById(R.id.textView3);
        imageview=(ImageView)findViewById(R.id.imageView2);
        String titletext = intent.getStringExtra("key");
        String desctext = intent.getStringExtra("key2");
        title.setText(titletext);
        desc.setText(desctext);
        /*byteArray = getIntent().getByteArrayExtra("byteArray");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        BitmapDrawable ob = new BitmapDrawable(getResources(), bmp);
        imageview.setBackgroundDrawable(ob);*/

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
