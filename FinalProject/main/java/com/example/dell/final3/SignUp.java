package com.example.dell.final3;
//Written by Rong  Zhang
//Debugged by Jingxuan Chen
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {
    EditText name;
    EditText pass;
    EditText con;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button signup2 = (Button) findViewById(R.id.signUp2);
         name =(EditText) findViewById(R.id.username_sign);
         pass=(EditText) findViewById(R.id.password_sign);
         con=(EditText) findViewById(R.id.confirm_sign);
         email=(EditText) findViewById(R.id.email_sign);

        signup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().equals(""))
                {
                    Toast.makeText(SignUp.this,"Empty name",Toast.LENGTH_SHORT).show();
                }
                else if(!pass.getText().toString().equals(con.getText().toString()))
                {
                    Toast.makeText(SignUp.this,"Password Wrong!",Toast.LENGTH_SHORT).show();
                }
                else {
                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference user =databaseReference.child("user");
                    Query query = user.orderByChild("account").equalTo(name.getText().toString());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChildren())
                            {
                                Toast.makeText(SignUp.this,"User name exists",Toast.LENGTH_SHORT).show();
                                name.setText("");
                                pass.setText("");
                                con.setText("");
                                email.setText("");
                            }
                            else
                            {
                                DatabaseReference table = databaseReference.child("user");
                                //DatabaseReference newUser = table.push();
                                DatabaseReference newUser = table.child(name.getText().toString());
                                newUser.child("id").setValue(newUser.getKey());
                                newUser.child("account").setValue(name.getText().toString());
                                newUser.child("password").setValue(pass.getText().toString());
                                newUser.child("email").setValue(email.getText().toString());
                                Intent intent = new Intent(SignUp.this, LogIn.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }
        });

    }
}
