package com.example.dell.final3;

//Written by Jingxuan Chen

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.view.LayoutInflater;

public class TravelAdapter extends ArrayAdapter<Travel> {
    private List<Travel> travelList=new ArrayList<Travel>();
    private int resourceId;
    //  private LayoutInflater mInflater;
    public TravelAdapter(Context context, int textViewResourceId,
                        List<Travel> objects) {
        super(context, textViewResourceId, objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Travel travel=getItem(position);
        View view=LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView time=(TextView)view.findViewById(R.id.time);
        TextView travelname=(TextView) view.findViewById(R.id.travelName);
        time.setText(travel.getStart()+"------"+travel.getEnd());
        travelname.setText(travel.getName());
        return view;
    }
}