package com.rideout.app.mobile.rideout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Class to sign up a new user.
 */
public class SignUp extends Activity {

    private static final int camera_request = 1;
    private static final int gallery_request = 2;
    ImageView viewImage;
    Button b;

    //private static ByteArrayOutputStream stream = new ByteArrayOutputStream();
    //private static byte[] bytearray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        b = (Button) findViewById(R.id.btnSelectPhoto);
        viewImage=(ImageView)findViewById(R.id.viewImage);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, camera_request);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, gallery_request);

                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == camera_request) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bm;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bm = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    //bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    //bytearray = stream.toByteArray();
                    viewImage.setImageBitmap(bm);

                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bm.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == gallery_request) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path of image from gal", picturePath + "");
                //thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
                //bytearray = stream.toByteArray();
                viewImage.setImageBitmap(thumbnail);
            }
        }
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
        //ParseFile file = new ParseFile("picture.png", bytearray);
        //file.saveInBackground();


        // Set the user's username and password
        user.setEmail(email);
        user.put("phone", phone);
        user.put("name", name);
        //user.put("File", file);
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
