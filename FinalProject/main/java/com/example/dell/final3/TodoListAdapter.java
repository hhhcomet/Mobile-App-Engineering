package com.example.dell.final3;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import static com.example.dell.final3.R.id.member;

//Written by Jingxuan Chen

public class TodoListAdapter extends ArrayAdapter<Todo> {
    //private List<Member> memberlist=new ArrayList<Member>();
    private int resourceId;

    public TodoListAdapter(@NonNull Context context, int resource, @NonNull List<Todo> objects) {
        super(context, resource, objects);
        resourceId=resource;
        //memberlist=objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        convertView= LayoutInflater.from(getContext()).inflate(resourceId, null);
        final Todo todo=getItem(position);
        TextView todoTitle=(TextView) convertView.findViewById(R.id.title_todo);
        TextView todoTime=(TextView) convertView.findViewById(R.id.time_todo);
        final CheckBox checkT=(CheckBox) convertView.findViewById(R.id.checkbox_todo);
        todoTitle.setText(todo.name.toString());
        todoTime.setText(todo.time.toString());
        checkT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkT.isChecked()==true)
                    if(todo.pic==false)
                        getItem(position).pic=true;
                if(checkT.isChecked()==false)
                    if(todo.pic==true)
                        getItem(position).pic=false;
            }
        });
        return convertView;
    }

}