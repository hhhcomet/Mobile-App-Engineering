package com.example.dell.final3;
//Written by Rong Zhang Feng Rong
//Debugged by Xiuqi Ye
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class TodoActivity extends AppCompatActivity {
    ListView listView;
    private List<Member> todoList=new ArrayList<Member>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        Intent intent = getIntent();
        final String travel2 = getIntent().getStringExtra("travelName");
        final String user = getIntent().getStringExtra("name");
        final String start = getIntent().getStringExtra("start");
        final String end = getIntent().getStringExtra("end");
        final String dest = getIntent().getStringExtra("dest");
        final EditText write=(EditText)findViewById(R.id.write);
        DatabaseReference travel3 = FirebaseDatabase.getInstance().getReference("travel/"+user+"/"+travel2+"/destination/"+dest+"/todo");
        Query tra1  = travel3.orderByKey();
        tra1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                for(DataSnapshot a:dataSnapshot.getChildren())
                {

                    Member tem = new Member(a.getValue().toString());
                    todoList.add(tem);
                }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        listView = (ListView)findViewById(R.id.todo);
        final MemberListAdapter adapter2=new  MemberListAdapter(TodoActivity.this,
                R.layout.member_list, todoList);
        listView.setAdapter(adapter2);

        final Button NewDest=(Button)findViewById(R.id.add2);
        NewDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(write.getText().toString().equals(""))
                {
                    Toast.makeText(TodoActivity.this,"Please fill in the information.",Toast.LENGTH_SHORT).show();
                }
                else {
                    DatabaseReference nDest = FirebaseDatabase.getInstance().getReference("travel/"+user+"/"+travel2+"/destination/"+dest+"/todo");
                    nDest.child(write.getText().toString()).setValue(write.getText().toString());
                    TodoActivity.this.finish();
                    Intent intent=new Intent(TodoActivity.this, TodoActivity.class);
                    intent.putExtra("name",user);
                    intent.putExtra("travelName",travel2);
                    intent.putExtra("start",start);
                    intent.putExtra("end",end);
                    intent.putExtra("dest",dest);
                    startActivity(intent);
                }
            }
        });

        final Button DeleteDest=(Button)findViewById(R.id.dele2);
        DeleteDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //delete checked destination in the database
                DatabaseReference travel4 = FirebaseDatabase.getInstance().getReference("travel/"+user+"/"+travel2+"/destination/"+dest+"/todo");
                for (Member a:todoList) {
                    if (a.checked) {
                        DatabaseReference nDest = travel4.child(a.name);
                        nDest.removeValue();
                    }
                }
                TodoActivity.this.finish();
                Intent intent=new Intent(TodoActivity.this, TodoActivity.class);
                intent.putExtra("name",user);
                intent.putExtra("travelName",travel2);
                intent.putExtra("start",start);
                intent.putExtra("end",end);
                intent.putExtra("dest",dest);
                startActivity(intent);

            }
        });

    }
}
