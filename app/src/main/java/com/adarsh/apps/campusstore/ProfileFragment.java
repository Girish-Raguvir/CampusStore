package com.adarsh.apps.campusstore;

/**
 * Created by Adarsh on 20-05-2015.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.net.URL;
import java.util.List;


public class ProfileFragment extends Fragment {
    Context c;
    FrameLayout layout;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (FrameLayout) inflater.inflate(R.layout.activity_userdetails, container, false);
        ring= ProgressDialog.show(c, "Please wait ...", "Loading details..", true);
        ring.show();
        name=(TextView)layout.findViewById(R.id.textView1);
        phno=(TextView)layout.findViewById(R.id.textView2);
        hostel=(TextView)layout.findViewById(R.id.textView3);
        email=(TextView)layout.findViewById(R.id.textView4);

        display("BADASS");
        return layout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        c = activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        //layout.getForeground().mutate().setAlpha(0);
    }

    public void display(final String s)
    { ring.show();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                "Users");
        getActivity().setProgressBarIndeterminateVisibility(true);
        query.findInBackground(new FindCallback<ParseObject>()

                               {

                                   @SuppressWarnings("unchecked")
                                   @Override
                                   public void done (List< ParseObject > itemList, ParseException e){
                                       getActivity().setProgressBarIndeterminateVisibility(false);
                                       if (e == null) {

                                           Log.d("test",
                                                   "Inside" + itemList.size());
                                           for (final ParseObject user : itemList) {

                                               String x = user.getString("username").toUpperCase();
                                               Log.d("test",
                                                       "yes");
                                               if(x.equals(s)){name.setText(s);
                                                   phno.setText(user.getString("phonenumber"));
                                                   email.setText(user.getString("email"));
                                                   hostel.setText("HOSTEL: " + user.getString("hostel"));
                                                   ring.dismiss();
                                                   call=(ImageButton)layout.findViewById(R.id.call);
                                                   contact=(ImageView)layout.findViewById(R.id.contact);

                                                   call.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {

                                                           Intent i = new Intent(Intent.ACTION_DIAL);
                                                           i.setData(Uri.parse("tel:" + user.getString("phonenumber")));
                                                           startActivity(i);
                                                       }
                                                   });
                                                   emailbutton=(ImageButton)layout.findViewById(R.id.email);
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