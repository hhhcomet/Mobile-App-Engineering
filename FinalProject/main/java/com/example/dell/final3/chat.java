package com.example.dell.final3;
// written by Jingxuan Chen  Feng Rong
//Debugged by Jingxuan Chen
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class chat extends AppCompatActivity {

    EditText input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        input =(EditText) findViewById(R.id.message);
        final String user = getIntent().getStringExtra("name");
        final String travel = getIntent().getStringExtra("travelName");
        Button send =(Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("chat/"+travel).push().setValue(new ChatMessage(input.getText().toString(),user));
                input.setText("");

            }
        });

        FirebaseListAdapter<ChatMessage> adapter = new FirebaseListAdapter<ChatMessage>(this,ChatMessage.class,R.layout.message_list,FirebaseDatabase.getInstance().getReference("chat/"+travel)) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {


                TextView mess =(TextView) v.findViewById(R.id.message_message);
                TextView messU=(TextView) v.findViewById(R.id.user_message);
                TextView messT=(TextView) v.findViewById(R.id.time_message);
                mess.setText(model.getMessageText());
                messU.setText(model.getMessageUser());
                SimpleDateFormat formatter1   =   new   SimpleDateFormat   ("yyyy/MM/dd HH:mm:ss");
                messT.setText(formatter1.format(model.getMessageTime()));
            }
        };

        ListView listView=(ListView) findViewById(R.id.chat_list);
        listView.setAdapter(adapter);
    }
}
