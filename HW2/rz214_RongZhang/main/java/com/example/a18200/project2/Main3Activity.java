package com.example.a18200.project2;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Main3Activity extends AppCompatActivity implements Fragment3.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("from", 3);
            TextView name = (TextView) findViewById(R.id.nameOut);
            i.putExtra("name", name.getText().toString());
            startActivity(i);
        }
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
