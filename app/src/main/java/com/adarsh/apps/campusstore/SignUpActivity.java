package com.adarsh.apps.campusstore;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class SignUpActivity extends ActionBarActivity {
    protected EditText usernameEditText;
    protected EditText passwordEditText;
    protected EditText emailEditText;
    protected EditText phoneEditText;
    protected EditText hostelEditText;
    protected Button signUpButton;
    //String  APPLICATION_ID="Bz8UjorQLzSsSWS8yMHziGVeeLykWIZuy8d2il8o";
    //String CLIENT_KEY="NeB1zLdKdiDC2MQkguOhCPNEHGESamPvnnP3uSW6";
    String  APPLICATION_ID="Go2QLMXo9VPZC597FxSUZvuqIUAJ0xxtu5CHAEla";
    String CLIENT_KEY="nZ8M2KeOBWCBgcOdFCcX4MSqz9AwlM8mQMjqtQn0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    super.onCreate(savedInstanceState);
    //getActionBar().setDisplayHomeAsUpEnabled(true);

        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);



    setContentView(R.layout.activity_sign_up);

    usernameEditText = (EditText)findViewById(R.id.usernameField);
    passwordEditText = (EditText)findViewById(R.id.passwordField);
    emailEditText = (EditText)findViewById(R.id.emailField);
    hostelEditText = (EditText)findViewById(R.id.hostel);
    phoneEditText = (EditText)findViewById(R.id.phonenumber);
    signUpButton = (Button)findViewById(R.id.signupButton);

    signUpButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String hostel = hostelEditText.getText().toString();
            String phone = phoneEditText.getText().toString();
            username = username.trim();
            password = password.trim();
            email = email.trim();
            hostel=hostel.trim();
            phone=phone.trim();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setMessage(R.string.signup_error_message)
                        .setTitle(R.string.signup_error_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            // TODO change this regex if we need to check for branch also (CS, EE etc)
            // and year of joining > 2020. Or take a list of roll numbers from a server or something.
            else if (!username.matches("[a-zA-Z]{2}[01]\\d[bBdD][01](\\d){2}")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setMessage(R.string.signup_invalid_message)
                        .setTitle(R.string.signup_error_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else {
                setProgressBarIndeterminateVisibility(true);

                ParseUser newUser = new ParseUser();
                newUser.setUsername(username);
                newUser.setPassword(password);
                newUser.setEmail(email);
                final ParseObject u = new ParseObject("Users");
                u.put("username",username);
                u.put("password",password);
                u.put("hostel",hostel);
                u.put("phonenumber",phone);
                u.put("email",email);
                u.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        setProgressBarIndeterminateVisibility(false);
                        if (e == null) {
                            // Saved successfully.
                            //ItemInfo item = new ItemInfo(name,null,desc,null,price);
                            //Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                            //finish();
                        } else {
                            // The save failed.
                            //Toast.makeText(getApplicationContext(), "Failed to Save", Toast.LENGTH_SHORT).show();
                            Log.d(getClass().getSimpleName(), "User update error: " + e);
                        }
                    }
                });




                newUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        setProgressBarIndeterminateVisibility(false);

                        if (e == null) {
                            // Success!
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                            builder.setMessage(e.getMessage())
                                    .setTitle(R.string.signup_error_title)
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                });
            }
        }
    });
}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
