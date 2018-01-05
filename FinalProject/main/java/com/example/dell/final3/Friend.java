package com.example.dell.final3;
//Written by Jingxuan Chen Rong Zhang
//Debugged by Xiuqi Ye
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Friend extends AppCompatActivity {
    EditText reFr;
    String userName;
    TextView messD;
    Button accept;
    Button deny;
    List<Member> requestName;
    MemberListAdapter adapter;
    ListView requestList;
    List<String> friendName;
    ListView frienList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        userName=getIntent().getStringExtra("name");
       // messD=(TextView) findViewById(R.id.messageD);
        accept = (Button) findViewById(R.id.accept);
        deny=(Button) findViewById(R.id.Deny);

        requestList =(ListView) findViewById(R.id.requestList);
        requestName=new ArrayList<Member>();
        DatabaseReference requeIn = FirebaseDatabase.getInstance().getReference("request/"+userName);
        Query reS = requeIn.orderByKey();
        reS.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot da:dataSnapshot.getChildren())
                {
                    Member tem = new Member(da.child("from").getValue().toString());
                    requestName.add(tem);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter=new MemberListAdapter(Friend.this,R.layout.member_list,requestName);
        requestList.setAdapter(adapter);

        frienList = (ListView) findViewById(R.id.friendList);
        friendName=new ArrayList<String>();
        DatabaseReference friendIn=FirebaseDatabase.getInstance().getReference("friend/"+userName);
        Query friendS = friendIn.orderByKey();
        friendS.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot da:dataSnapshot.getChildren())
                {
                    friendName.add(da.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final ArrayAdapter<String> friendAdapter=new ArrayAdapter<String>(Friend.this,android.R.layout.simple_expandable_list_item_1,friendName);
        frienList.setAdapter(friendAdapter);



        reFr =(EditText) findViewById(R.id.wanted);
        Button re = (Button) findViewById(R.id.Request);
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference data = FirebaseDatabase.getInstance().getReference();
                DatabaseReference user = data.child("user");
                Query query = user.orderByChild("account").equalTo(reFr.getText().toString());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChildren())
                        {
                            if(reFr.getText().toString().equals(userName))
                            {
                                Toast.makeText(Friend.this,"You cannot add youself",Toast.LENGTH_SHORT).show();
                                reFr.setText("");
                            }
                            else {
                                //DatabaseReference newRe = requ.push();
                                DatabaseReference myfriend = FirebaseDatabase.getInstance().getReference("friend/"+userName);
                                Query isFriend = myfriend.orderByValue().equalTo(reFr.getText().toString());
                                isFriend.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {


                                        if(dataSnapshot.hasChildren())
                                        {
                                            Toast.makeText(Friend.this,"You are friend already",Toast.LENGTH_SHORT).show();
                                            reFr.setText("");
                                        }
                                        else {
                                            DatabaseReference requ = data.child("request");
                                            final DatabaseReference newRe = requ.child(reFr.getText().toString());
                                            Query red = newRe.orderByChild("from").equalTo(userName);
                                            red.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    if (!dataSnapshot.hasChildren()) {
                                                        DatabaseReference newp = newRe.push();
                                                        newp.child("from").setValue(userName);
                                                    }
                                                    Friend.this.finish();
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        }
                        else
                        {
                            Toast.makeText(Friend.this,"Wrong user name",Toast.LENGTH_SHORT).show();
                            reFr.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


        final DatabaseReference friend = FirebaseDatabase.getInstance().getReference().child("request");
        Query query = friend.orderByKey().equalTo(userName);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //if(dataSnapshot.child("from").getValue() != null) {
                //requestName = new ArrayList<Member>();
                requestName.clear();
                for(DataSnapshot a:dataSnapshot.getChildren()) {
                    //Toast.makeText(Friend.this,a.child("from").getValue().toString(),Toast.LENGTH_SHORT).show();
                   Member tem = new Member(a.child("from").getValue().toString());
                    if(requestName.size()==0)
                    {
                        requestName.add(tem);
                        //adapter.notifyDataSetChanged();
                    }
                    else {
                        int i = 0;
                        for (; i < requestName.size(); i++) {
                            Member m = requestName.get(i);
                            //Toast.makeText(Friend.this,"m name:"+m.name+"tem"+tem.name,Toast.LENGTH_SHORT).show();
                            if (m.name.equals(tem.name)) {
                                //requestName.add(tem);
                                break;
                            }
                        }
                        if(i==requestName.size())
                            requestName.add(tem);
                    }
                    //requestName.add(new Member(a.child("from").getValue().toString()));
                    //messD.setText("Friend request from: " + a.child("from").getValue().toString());
                }
                    adapter.notifyDataSetChanged();
                    accept.setVisibility(View.VISIBLE);
                    deny.setVisibility(View.VISIBLE);
                //}
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
               //Toast.makeText(Friend.this,"hhh",Toast.LENGTH_SHORT).show();
                Toast.makeText(Friend.this,dataSnapshot.toString(),Toast.LENGTH_SHORT);
                /*messD.setText("Friend request from: "+dataSnapshot.getValue().toString());
                accept.setVisibility(View.VISIBLE);
                deny.setVisibility(View.VISIBLE);*/

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference table= FirebaseDatabase.getInstance().getReference("friend");
                DatabaseReference newFr = table.child(userName);
                DatabaseReference req=FirebaseDatabase.getInstance().getReference("request");

                final DatabaseReference Myrequ= req.child(userName);

                for(int i=0;i<requestName.size();i++)
                {
                    if(requestName.get(i).checked)
                    {
                        newFr.push().setValue(requestName.get(i).name);
                        FirebaseDatabase.getInstance().getReference().child("friend").child(requestName.get(i).name).push().setValue(userName);
                        friendName.add(requestName.get(i).name);
                        friendAdapter.notifyDataSetChanged();
                        Query delet = Myrequ.orderByChild("from").equalTo(requestName.get(i).name);
                        final int finalI = i;
                        final int finalI1 = i;
                        delet.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot a:dataSnapshot.getChildren())
                                {
                                    if(a.child("from").getValue().toString().equals(requestName.get(finalI).name))
                                    {
                                        Myrequ.child(a.getKey()).removeValue();
                                        requestName.remove(finalI1);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                                //Toast.makeText(Friend.this,a.toString(),Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                }
               // Friend.this.finish();

            }
        });

        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<requestName.size();i++)
                {
                    if(requestName.get(i).checked)
                    {

                        Query delet = FirebaseDatabase.getInstance().getReference("request/"+userName).orderByChild("from").equalTo(requestName.get(i).name);
                        final int finalI = i;
                        delet.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot a:dataSnapshot.getChildren())
                                {
                                    if(a.child("from").getValue().toString().equals(requestName.get(finalI).name))
                                    {
                                        FirebaseDatabase.getInstance().getReference("request/"+userName).child(a.getKey()).removeValue();
                                        requestName.remove(finalI);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                                //Toast.makeText(Friend.this,a.toString(),Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
        });
    }
}
