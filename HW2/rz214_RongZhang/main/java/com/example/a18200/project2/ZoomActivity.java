package com.example.a18200.project2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

import static java.security.AccessController.getContext;

public class ZoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent i=getIntent();
        String path=(String)i.getSerializableExtra("path");
        ImageView photo=(ImageView)findViewById(R.id.Zoom);
        if(path.equals("default"))
        {
            photo.setImageResource(R.drawable.a);
        }
        else{
            Bitmap image = getBitmap(path);
            photo.setImageBitmap(image);
        }
        photo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    private Bitmap getBitmap(String pathString)
    {
        Bitmap bitmap = null;
        try
        {
            File file = new File(pathString);
            if(file.exists())
            {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        } catch (Exception e)
        {
        }
        return bitmap;
    }

}
