package com.example.dell.final3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//Written by Feng Rong
public class TodoAdapter extends ArrayAdapter<Todo>{
    private List<Todo> todoList=new ArrayList<Todo>();
    private int resourceId;
    public TodoAdapter(Context context, int id,
                       List<Todo> objects){
        super(context,id,objects);
        resourceId=id;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        Todo todo=getItem(position);
        convertView= LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView title=(TextView)convertView.findViewById(R.id.title_todo);
        TextView time=(TextView)convertView.findViewById(R.id.time_todo);
        title.setText(todo.getName());
        time.setText(todo.getTime());
        final CheckBox checkBox=(CheckBox)convertView.findViewById(R.id.checkbox_todo);
        checkBox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                remove(getItem(position));
                checkBox.setChecked(false);
            }
        });
        return convertView;
    }

}
