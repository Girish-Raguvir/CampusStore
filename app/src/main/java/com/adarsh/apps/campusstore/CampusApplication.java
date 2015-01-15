package com.adarsh.apps.campusstore;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Adarsh on 13-01-2015.
 * Modified by Girish on 15-1-15.
 */
public class CampusApplication extends Application {
    //String  APPLICATION_ID="Bz8UjorQLzSsSWS8yMHziGVeeLykWIZuy8d2il8o";
    //String CLIENT_KEY="NeB1zLdKdiDC2MQkguOhCPNEHGESamPvnnP3uSW6";
    String  APPLICATION_ID="Go2QLMXo9VPZC597FxSUZvuqIUAJ0xxtu5CHAEla";
    String CLIENT_KEY="nZ8M2KeOBWCBgcOdFCcX4MSqz9AwlM8mQMjqtQn0";
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);

        //ParseObject testObject = new ParseObject("TestObject");
        //testObject.put("foo", "bar");
       // testObject.saveInBackground();
    }

}
