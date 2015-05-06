package com.rideout.app.mobile.rideout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

/**
 * Created by Nikhil on 4/26/2015.
 */
public class Login extends Activity {

    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin); // Set to the login activity
    }

    //public void signUp(View view) {
     //   Intent signUp = new Intent(this, SignUp.class);
      //  startActivity(signUp);
    //}


    public void login(View view) {

        //final EditText usernameField = (EditText) findViewById(R.id.loginUsername);
        //nal EditText passwordField = (EditText) findViewById(R.id.loginPassword);

        //final String username = usernameField.getText().toString();
        //final String password = passwordField.getText().toString();

        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, null, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                } else {
                    Log.d("MyApp", "User logged in through Facebook!");
                }
            }
        });


    }
    //This needs to be put in the onClick method for the login button.


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

}

