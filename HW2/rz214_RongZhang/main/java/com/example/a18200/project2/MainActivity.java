package com.example.a18200.project2;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements Fragment1.OnFragmentInteractionListener,Fragment2.OnFragmentInteractionListener,Fragment3.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i=getIntent();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            int c=i.getIntExtra("change",1);
            if(c==2)
            {
                Intent i1=new Intent(this,Main2Activity.class);
                startActivity(i1);
            }
            else if(c==3)
            {
                Intent i2=new Intent(this,Main3Activity.class);
                i2.putExtra("name",i.getStringExtra("name"));
                startActivity(i2);
            }
        }
        else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            FragmentManager manager=getFragmentManager();
            FragmentTransaction trans=manager.beginTransaction();
            int f=i.getIntExtra("from",1);
            if(f==1)
            {
                trans.replace(R.id.fra1, new Fragment1(),"f1");
//                Fragment fragment1 = getSupportFragmentManager().findFragmentByTag("f2");
//                Fragment fragment2 = getSupportFragmentManager().findFragmentByTag("f3");
//                if(fragment1 != null)
//                    getSupportFragmentManager().beginTransaction().remove(fragment1).commit();
//                if(fragment2!=null)
//                    getSupportFragmentManager().beginTransaction().remove(fragment2).commit();
                trans.commit();
            }
            else if(f==2)
            {
                trans.replace(R.id.fra1, new Fragment1(),"f1");
                trans.replace(R.id.fra2, new Fragment2(), "f2");
                trans.commit();
            }
            else if(f==3)
            {
                trans.replace(R.id.fra1, new Fragment1(),"f1");
                trans.replace(R.id.fra2, new Fragment3(),"f3");
                trans.commit();
            }

        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
