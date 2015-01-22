package com.adarsh.apps.campusstore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class userdetails extends ActionBarActivity {
    TextView name;
    TextView phno;
    TextView hostel;
    TextView email;
    String oname,ophno,ohostel,oemail;
    ImageButton call;
    ImageButton emailbutton;
    ImageView contact;
    URL url;
    ProgressDialog ring=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oname=getIntent().getStringExtra("owner").toUpperCase();
        //Toast.makeText(userdetails.this,oname, Toast.LENGTH_LONG).show();
        setContentView(R.layout.activity_userdetails);
        ring= ProgressDialog.show(userdetails.this, "Please wait ...", "Loading details..", true);
        ring.show();
        name=(TextView)findViewById(R.id.textView1);
        phno=(TextView)findViewById(R.id.textView2);
        hostel=(TextView)findViewById(R.id.textView3);
        email=(TextView)findViewById(R.id.textView4);

        display(oname);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_userdetails, menu);
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
    public void display(final String s)
    { ring.show();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                "Users");
        setProgressBarIndeterminateVisibility(true);
        query.findInBackground(new FindCallback<ParseObject>()

                               {

                                   @SuppressWarnings("unchecked")
                                   @Override
                                   public void done (List < ParseObject > itemList, ParseException e){
                                       setProgressBarIndeterminateVisibility(false);
                                       if (e == null) {

                                           Log.d("test",
                                                   "Inside"+itemList.size());
                                          for (final ParseObject user : itemList) {

                                               String x = user.getString("username").toUpperCase();
                                               Log.d("test",
                                                       "yes");
                                               if(x.equals(s)){name.setText(s);
                                               phno.setText(user.getString("phonenumber"));
                                                   email.setText(user.getString("email"));
                                                   hostel.setText("HOSTEL: " + user.getString("hostel"));
                                                   ring.dismiss();
                                                   call=(ImageButton)findViewById(R.id.call);
                                                    contact=(ImageView)findViewById(R.id.contact);

                                                   call.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {

                                                           Intent i = new Intent(Intent.ACTION_DIAL);
                                                           i.setData(Uri.parse("tel:"+user.getString("phonenumber")));
                                                           startActivity(i);
                                                       }
                                                   });
                                                   emailbutton=(ImageButton)findViewById(R.id.email);
                                                   emailbutton.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {
                                                           Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                                                   "mailto",user.getString("email"), null));

                                                           intent.putExtra(Intent.EXTRA_SUBJECT, "Campus Store Enquiry....");
                                                           intent.putExtra(Intent.EXTRA_TEXT, "Sent From IITM Campus Store App");

                                                           startActivity(Intent.createChooser(intent, "Send Email"));
                                                       }
                                                   });
                                                   /*try {
                                                       String args="http://photos.iitm.ac.in/byroll.php?roll=MM14B001";
                                                       Bitmap bmp = BitmapFactory.decodeStream((InputStream)new URL(args).getContent());
                                                       contact.setImageBitmap(bmp);
                                                   } catch (java.io.IOException e1) {
                                                       e1.printStackTrace();
                                                   }*/
                                                   ring.dismiss(); break;
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
    }

