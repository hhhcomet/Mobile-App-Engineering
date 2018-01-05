package com.example.a18200.project2;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity implements Fragment2.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("from", 2);
//            EditText text1=(EditText) findViewById(R.id.nameIn);
//            if(!TextUtils.isEmpty(text1.getText())) {
//                String t1 = text1.getText().toString();
//                i.putExtra("passvalue1", t1);
//            }
//            EditText text2=(EditText) findViewById(R.id.telIn);
//            if(!TextUtils.isEmpty(text2.getText())) {
//                String t2 = text2.getText().toString();
//                i.putExtra("passvalue2", t2);
//            }
            startActivity(i);
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Fragment2.onActivityResult(requestCode, resultCode, data);
//    }

        @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
