package com.rideout.app.mobile.rideout;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;
import com.rideout.app.mobile.rideout.MainActivity.MainActivity;


public class Login extends Activity {

    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void signUp(View view) {
        Intent signUp = new Intent(this, SignUp.class);
        startActivity(signUp);
    }

    public void login(View view) {

        final EditText usernameField = (EditText) findViewById(R.id.loginUsername);
        final EditText passwordField = (EditText) findViewById(R.id.loginPassword);

        final String username = usernameField.getText().toString();
        final String password = passwordField.getText().toString();

        ParseUser.logInInBackground(username, password, new LogInCallback() {

            @Override
            public void done(ParseUser user, com.parse.ParseException e) {
                if (user != null) {
                    Intent createAccount = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(createAccount);
                    finish();
                } else {
                    // Clear fields since login failed
                    usernameField.setText("");
                    passwordField.setText("");

                    // Alert user of failure
                    Toast.makeText(getApplicationContext(), "Login Failed",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}

