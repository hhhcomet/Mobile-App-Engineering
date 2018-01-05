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
//written by Rong Zhang

public class MemberListAdapter extends ArrayAdapter<Member> {
    //private List<Member> memberlist=new ArrayList<Member>();
    private int resourceId;

    public MemberListAdapter(@NonNull Context context, int resource, @NonNull List<Member> objects) {
        super(context, resource, objects);
        resourceId=resource;
        //memberlist=objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        convertView= LayoutInflater.from(getContext()).inflate(resourceId, null);
        final Member member=getItem(position);
        TextView memberT=(TextView) convertView.findViewById(R.id.MemberText);
        final CheckBox checkM=(CheckBox) convertView.findViewById(R.id.MemberCheck);
        memberT.setText(member.name.toString());
        checkM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkM.isChecked()==true)
                    if(member.checked==false)
                        getItem(position).checked=true;
                if(checkM.isChecked()==false)
                    if(member.checked==true)
                        getItem(position).checked=false;
            }
        });
        return convertView;
    }

}