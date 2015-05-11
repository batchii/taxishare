package com.rideout.app.mobile.rideout.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Switch;

import com.rideout.app.mobile.rideout.R;


public class SettingsActivity extends ActionBarActivity {

    private Switch switchBtn1, switchBtn2;
    private PopupMenu popupBefore;
    private Spinner spinner;
    private static final String[] paths = {"5 mins", "10 mins", "15 mins"};

    static final int CAPTURE_BEFORE = 11;
    static final int SELECT_BEFORE = 12;
    static final int MENU_CAMERA = Menu.FIRST;
    static final int MENU_GALLERY = Menu.FIRST + 1;

    ImageView beforePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        addListenerOnToggleButton();

        popupBefore = new PopupMenu(this, findViewById(R.id.imageView1));
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
        findViewById(R.id.imageView1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupBefore.show();
            }
        });

        spinner = (Spinner)findViewById(R.id.spinner);
        // spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(SettingsActivity.this,
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }



    private void addListenerOnToggleButton() {
        switchBtn1 = (Switch) findViewById(R.id.allow_Contact);
        switchBtn2 = (Switch) findViewById(R.id.text_social);
        switchBtn1.setChecked(false);
        switchBtn2.setChecked(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
