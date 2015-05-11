package com.rideout.app.mobile.rideout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Class to sign up a new user.
 */
public class SignUp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    /**
     * On click method to sign up a new user.
     * @param view the sign up activity view.
     */
    public void signUp(View view) {

        final EditText nameField = (EditText) findViewById(R.id.name);
        final EditText emailField = (EditText) findViewById(R.id.email);
        final EditText phoneField = (EditText) findViewById(R.id.phoneNumber);
        final EditText passwordField = (EditText) findViewById(R.id.signUpPassword);

        final String name = nameField.getText().toString();
        final String email = emailField.getText().toString();
        final String phone = phoneField.getText().toString();
        final String password = passwordField.getText().toString();

        // Check to make sure a username and password were entered
        if (name.length() == 0 || password.length() == 0 || email.length () == 0 || phone.length() == 0) {
            Toast.makeText(getApplicationContext(), "Invalid field",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new Parse User object
        ParseUser user = new ParseUser();

        // Set the user's username and password
        user.setEmail(email);
        user.put("phone", phone);
        user.put("name", name);
        user.setPassword(password);
        user.setUsername(email);

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "Sign Up Success",
                            Toast.LENGTH_LONG).show();
                    returnToLoginActivity();
                } else {
                    Log.e("Sign up failed", e.getMessage());
                    Toast.makeText(getApplicationContext(), "Sign Up Failed",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Returns user to login activity, after
     * successful account creation.
     */
    private void returnToLoginActivity() {
        Intent returnToLogin = new Intent(this, Login.class);
        startActivity(returnToLogin);
        finish();
    }

}
