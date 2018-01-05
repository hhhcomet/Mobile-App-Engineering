package com.example.dell.final3;

//written by Rong Zhang
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.view.LayoutInflater;

public class DetailsAdapter1 extends ArrayAdapter<String> {
    private List<String> memberList=new ArrayList<String>();
    private int resourceId;
    //  private LayoutInflater mInflater;
    public DetailsAdapter1(Context context, int textViewResourceId,
                           List<String> objects) {
        super(context, textViewResourceId, objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String member=getItem(position);
        View view=LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView membername=(TextView)view.findViewById(R.id.member1);
        membername.setText(member);
        return view;
    }
}