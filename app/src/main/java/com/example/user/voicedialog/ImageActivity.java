package com.example.user.voicedialog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.user.voicedialog.models.TouchImageView;


public class ImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image);
        Toolbar bar = (Toolbar) findViewById(R.id.toolbar_image_view);

        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TouchImageView touchImageView = (TouchImageView) findViewById(R.id.image1);
        Intent intent = getIntent();
        String filename= intent.getStringExtra("image_src");
        filename= filename+".png";
        touchImageView.setImageURI(Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/" + filename));
    }


}
