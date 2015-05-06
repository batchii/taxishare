package com.rideout.app.mobile.rideout.view;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.rideout.app.mobile.rideout.R;

import java.util.ArrayList;
import java.util.List;


public class SigninActivity extends ActionBarActivity {

    private ToggleButton toggleBtn1, toggleBtn2;
    private PopupMenu popupBefore;
    private Spinner spinner;

    static final int CAPTURE_BEFORE = 11;
    static final int SELECT_BEFORE = 12;
    static final int MENU_CAMERA = Menu.FIRST;
    static final int MENU_GALLERY = Menu.FIRST + 1;

    private static final String[] paths = {"5 mins", "10 mins", "15 mins"};

    ImageView beforePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        addListenerOnToggleButton();

        popupBefore = new PopupMenu(this, findViewById(R.id.btnChangeImage));
        popupBefore.getMenu().add(Menu.NONE, MENU_CAMERA, Menu.NONE, "Take a Picture");
        popupBefore.getMenu().add(Menu.NONE, MENU_GALLERY, Menu.NONE, "Choose From Gallery");
        beforePic = (ImageView) findViewById(R.id.imageView1);
        popupBefore.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case MENU_CAMERA:
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, CAPTURE_BEFORE);
                        }
                        break;
                    case MENU_GALLERY:
                        startActivityForResult(new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), SELECT_BEFORE);
                        break;
                }
                return false;
            }
        });
        findViewById(R.id.btnChangeImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupBefore.show();
            }
        });

        spinner = (Spinner)findViewById(R.id.spinner);
        // spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(SigninActivity.this,
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }


    private void addListenerOnToggleButton() {
        toggleBtn1 = (ToggleButton) findViewById(R.id.allow_Contact);
        toggleBtn2 = (ToggleButton) findViewById(R.id.text_social);
        toggleBtn1.setChecked(false);
        toggleBtn2.setChecked(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signin, menu);
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
