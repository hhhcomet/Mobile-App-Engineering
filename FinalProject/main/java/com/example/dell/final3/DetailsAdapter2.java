package com.example.dell.final3;

//Written by Xiuqi Ye

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.view.LayoutInflater;

public class DetailsAdapter2 extends ArrayAdapter<Todo> {
    private List<Todo> travelList=new ArrayList<Todo>();
    private int resourceId;
    //  private LayoutInflater mInflater;
    public DetailsAdapter2(Context context, int textViewResourceId,
                           List<Todo> objects) {
        super(context, textViewResourceId, objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Todo todo=getItem(position);
        View view=LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView time=(TextView)view.findViewById(R.id.time2);
        TextView travelname=(TextView) view.findViewById(R.id.travelName2);
        travelname.setText(todo.getTime());
        time.setText(todo.getName());
        return view;
    }
}