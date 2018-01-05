package com.example.dell.final3;
//Written by Jingxuan Chen
//Debugged by Jingxuan Chen
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LogIn extends AppCompatActivity {

    EditText name ;
    EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        FirebaseApp.initializeApp(this);
        Button login = (Button)findViewById(R.id.login);
        Button signup = (Button) findViewById(R.id.signUp);
        name=(EditText) findViewById(R.id.account);
        pass=(EditText) findViewById(R.id.password);
        final String[] passw = {""};

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference  data = FirebaseDatabase.getInstance().getReference();
                DatabaseReference  user = data.child("user");
                Query query = user.orderByChild("account").startAt(name.getText().toString()).endAt(name.getText().toString());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChildren())
                        {

                            for(DataSnapshot child: dataSnapshot.getChildren())
                            {
                               for(DataSnapshot a:child.getChildren())
                               {
                                   if(a.getKey().equals("password"))
                                   {
                                       passw[0] =a.getValue().toString();
                                       //Toast.makeText(LogIn.this,passw[0],Toast.LENGTH_SHORT).show();
                                       if(passw[0].equals(pass.getText().toString())) {
                                           Intent intent = new Intent(LogIn.this, HomePage.class);
                                           intent.putExtra("name",name.getText().toString());
                                           LogIn.this.finish();
                                           startActivity(intent);
                                       }
                                       else
                                       {
                                           name.setText("");
                                           pass.setText("");
                                           Toast.makeText(LogIn.this,"Wrong password",Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               }
                            }
                        }
                        else
                        {
                            name.setText("");
                            pass.setText("");
                            Toast.makeText(LogIn.this,"Wrong username",Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogIn.this, SignUp.class);
                startActivity(intent);
            }
        });
    }
}
