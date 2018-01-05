package com.example.dell.final3;
// Written by Xiuqi Ye  Feng Rong
// Debugged by Rong Zhang

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.y;
import static com.example.dell.final3.R.id.dest;
import static com.example.dell.final3.R.id.userName;

public class Details extends AppCompatActivity {
    ListView listView;
    ListView listView2;
    private List<Travel> travelList=new ArrayList<Travel>();
    private List<Todo> todoList=new ArrayList<Todo>();
    private List<String> memberList=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
     //   int poi = intent.getIntExtra("position",0);
        final String travel2 = getIntent().getStringExtra("travelName");
        final String user = getIntent().getStringExtra("name");
        final String start = getIntent().getStringExtra("start");
        final String end = getIntent().getStringExtra("end");

        Button ch = (Button) findViewById(R.id.chat);
        ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cha= new Intent(Details.this,chat.class);
                cha.putExtra("travelName",travel2);
                cha.putExtra("name",user);
                if(!memberList.isEmpty())
                startActivity(cha);
                else
                {
                    Toast.makeText(Details.this,"You cannot talk to youself",Toast.LENGTH_SHORT).show();
                }
            }
        });
   //     Log.v("detaildata", "poi,travel,name:" + poi + " " + travel2 + " " + user);
        listView = (ListView)findViewById(R.id.memb);
        final DetailsAdapter1 adapter1=new DetailsAdapter1(Details.this,
                R.layout.details_list_1, memberList);
        listView.setAdapter(adapter1);
        DatabaseReference travel = FirebaseDatabase.getInstance().getReference("travel/"+user+"/"+travel2+"/member");
        Query tra  = travel.orderByKey();
        tra.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot a:dataSnapshot.getChildren())
                {
                memberList.add(new String(a.getValue().toString()));
                }
                adapter1.notifyDataSetChanged();

                Log.v("detaildata", "travel:" +"start");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        listView2 = (ListView)findViewById(dest);
        final TodoListAdapter adapter2=new TodoListAdapter(Details.this,
                R.layout.todo_list, todoList);
        listView2.setAdapter(adapter2);
        DatabaseReference travel3 = FirebaseDatabase.getInstance().getReference("travel/"+user+"/"+travel2+"/destination");
        Query tra1  = travel3.orderByKey();
        tra1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot a:dataSnapshot.getChildren())
                {
                    todoList.add(new Todo(a.child("name").getValue().toString(),a.child("time").getValue().toString()));
                }
                adapter2.notifyDataSetChanged();
                Log.v("detaildata", "travel:" +"start");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        //Log.v("detaildata", "travel:" +travelList.get(poi).getStart());
      //  ini();
       // todoList=travelList.get(poi).destination;
       // memberList=travelList.get(poi).member;

        TextView time=(TextView)findViewById(R.id.time);
        time.setText(start+"------"+end);



        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(Details.this, TodoActivity.class);
                intent.putExtra("name",user);
                intent.putExtra("travelName",travel2);
                intent.putExtra("start",start);
                intent.putExtra("end",end);
                String dest=todoList.get(i).getName();
                intent.putExtra("dest",dest);
                startActivity(intent);
            }
        });

        listView2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
           // going to map activity
                AlertDialog.Builder alert=new AlertDialog.Builder(Details.this);
                alert.setTitle("Search this destination in...");
                alert.setItems(R.array.choose_exp, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0) {

                            Intent mapin=new Intent(Details.this,MapView.class);
                            String name=todoList.get(position).getName();
                            mapin.putExtra("name",name);
                            //Toast.makeText(getApplicationContext(),name , Toast.LENGTH_SHORT).show();
                            startActivity(mapin);
                        }
                        else if(which==1) {
                            String url="https://www.google.com/search?&q="+todoList.get(position).getName();
                            Intent in= new Intent();
                            in.setAction(Intent.ACTION_VIEW);
                            Uri content_url = Uri.parse(url);
                            in.setData(content_url);
                            startActivity(Intent.createChooser(in,"Choose an explorer:"));
                            //Toast.makeText(getApplicationContext(), "You choose 1", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alert.create();
                alert.show();
                //Toast.makeText(getApplicationContext(),"test",Toast.LENGTH_LONG).show();
                return true;
            }
        });



        final Button NewDest=(Button)findViewById(R.id.add);
        NewDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Details.this, newTripMap.class);
                intent.putExtra("name",user);
                intent.putExtra("travelName",travel2);
                intent.putExtra("start",start);
                intent.putExtra("end",end);
                startActivity(intent);
                Details.this.finish();
            }
        });

        final Button DeleteDest=(Button)findViewById(R.id.dele);
        DeleteDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //delete checked destination in the database
                DatabaseReference travel4 = FirebaseDatabase.getInstance().getReference("travel/"+user+"/"+travel2+"/destination");
                for (Todo a:todoList) {
                    if (a.pic) {
                        DatabaseReference nDest = travel4.child(a.getName());
                        nDest.removeValue();
                    }
                }
                Intent intent=new Intent(Details.this, Details.class);
                intent.putExtra("name",user);
                intent.putExtra("travelName",travel2);
                intent.putExtra("start",start);
                intent.putExtra("end",end);
                startActivity(intent);
                Details.this.finish();
            }
        });


    }

}
