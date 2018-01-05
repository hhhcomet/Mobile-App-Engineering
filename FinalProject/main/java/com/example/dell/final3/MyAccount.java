package com.example.dell.final3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.dell.final3.R.id.account;
import static com.example.dell.final3.R.id.travelName;
import static com.example.dell.final3.R.id.userName;
//Written by Xiuqi Ye Feng Rong
//Debugged by Rong Zhang

public class MyAccount extends AppCompatActivity {
   // private List<String> temp=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        final String user = getIntent().getStringExtra("name");
        final TextView account=(TextView)findViewById(R.id.textAccount);
        final EditText password=(EditText)findViewById(R.id.editpass);
        final EditText email=(EditText)findViewById(R.id.editemail);
        final EditText phone=(EditText)findViewById(R.id.editphone);
        final EditText address=(EditText)findViewById(R.id.editaddress);
        Button edit=(Button)findViewById(R.id.edit);
        account.setText(user);

        DatabaseReference account1 = FirebaseDatabase.getInstance().getReference("user/"+user+"/password");
        Query tra  = account1.orderByKey();
        tra.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                password.setText(dataSnapshot.getValue().toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        DatabaseReference account2 = FirebaseDatabase.getInstance().getReference("user/"+user+"/email");
        Query tra1  = account2.orderByKey();
        tra1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    email.setText(dataSnapshot.getValue().toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        DatabaseReference account3 = FirebaseDatabase.getInstance().getReference("user/"+user+"/phone");
        Query tra3  = account3.orderByKey();
        tra3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
             phone.setText(dataSnapshot.getValue().toString());
              }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        DatabaseReference account4 = FirebaseDatabase.getInstance().getReference("user/"+user+"/address");
        Query tra4  = account4.orderByKey();
        tra4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {address.setText(dataSnapshot.getValue().toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getText().toString().equals(""))
                {
                    Toast.makeText(MyAccount.this,"Please fill in the password.",Toast.LENGTH_SHORT).show();
                }
                else {
                    DatabaseReference nDest = FirebaseDatabase.getInstance().getReference("user/" + user);
                    // DatabaseReference nDest = table.child("destination").child(destName.getText().toString());
                    nDest.child("password").setValue(password.getText().toString());
                    nDest.child("email").setValue(email.getText().toString());
                    nDest.child("phone").setValue(phone.getText().toString());
                    nDest.child("address").setValue(address.getText().toString());
                    Toast.makeText(MyAccount.this,"Edit successfully!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
