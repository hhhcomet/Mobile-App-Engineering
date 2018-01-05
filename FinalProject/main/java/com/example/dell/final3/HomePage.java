package com.example.dell.final3;
//written by Feng Rong
//Debugged by Jingxuan Chen
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomePage extends AppCompatActivity {

    ListView listView;
    private List<Travel> travelList=new ArrayList<Travel>();

    @Override
    protected  void onResume()
    {
        super.onResume();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Button add2 = (Button) findViewById(R.id.add2);
        Button friend =(Button) findViewById(R.id.friend);
        Button account =(Button) findViewById(R.id.account);
        final String user = getIntent().getStringExtra("name");

        Intent intent_s = new Intent(this, MyService.class);
        intent_s.putExtra("name",user);
        startService(intent_s);


        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(HomePage.this, Friend.class);
                intent1.putExtra("name",user);
                startActivity(intent1);
            }
        });

        add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, NewTrip.class);
                intent.putExtra("name",user);
                startActivity(intent);
                HomePage.this.finish();

            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(HomePage.this, MyAccount.class);
                intent1.putExtra("name",user);
                startActivity(intent1);
            }
        });
        final TravelAdapter adapter1=new TravelAdapter(HomePage.this,
                R.layout.travel_listview, travelList);
        DatabaseReference travel = FirebaseDatabase.getInstance().getReference("travel/"+user);
        Query tra = travel.orderByKey();
        tra.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot da:dataSnapshot.getChildren())
                {
                   travelList.add(new Travel(da.child("name").getValue().toString(),da.child("start").getValue().toString(),da.child("end").getValue().toString()));

                }
                adapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        listView = (ListView)findViewById(R.id.listViewh);
        listView.setAdapter(adapter1);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(HomePage.this, Details.class);
                intent.putExtra("name",user);
                intent.putExtra("travelName",travelList.get(i).name);
          //      intent.putExtra("position",i);
                intent.putExtra("start",travelList.get(i).getStart());
                intent.putExtra("end",travelList.get(i).getEnd());
                startActivity(intent);
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alert=new AlertDialog.Builder(HomePage.this);
                alert.setTitle(R.string.title);
                alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Travel a = travelList.get(position);
                        DatabaseReference dele = FirebaseDatabase.getInstance().getReference("travel/"+user);
                        Query delet =dele.orderByKey().equalTo(a.getName());
                        delet.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if(dataSnapshot.hasChildren())
                                {
                                    for(DataSnapshot del : dataSnapshot.getChildren())
                                    {
                                        if(del.hasChild("member"))
                                        {
                                            for(DataSnapshot delother: del.child("member").getChildren())
                                            {
                                                DatabaseReference other = FirebaseDatabase.getInstance().getReference("travel/"+delother.getValue()+"/"+a.getName());
                                                other.removeValue();
                                            }
                                        }
                                        del.getRef().removeValue();
                                    }
                                }

                                travelList.remove(position);
                                adapter1.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
                alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.create();
                alert.show();
                return true;
            }
        });


    }
}
