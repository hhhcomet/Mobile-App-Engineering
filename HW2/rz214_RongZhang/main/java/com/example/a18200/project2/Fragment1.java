package com.example.a18200.project2;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class Fragment1 extends Fragment {

    Button addBut, delBut;
    ListView listview;
    ArrayAdapter<item> listAdapter;
    ArrayList<item> preData;
    HashMap<String,Boolean> deleteCheck;

    public Fragment1() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        preData.clear();;
        SharedPreferences preferences=getActivity().getSharedPreferences("profile",MODE_PRIVATE);
        Gson gson=new Gson();
        Map<String,?> getPre=preferences.getAll();
        for (Map.Entry<String, ?> entry : getPre.entrySet()) {
            String json = (String) entry.getValue();
            item newI = gson.fromJson(json, item.class);
            preData.add(newI);
        }
        deleteCheck=new HashMap<>();
        for(item n:preData)
        {
            deleteCheck.put(n.name,false);
        }
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_fragment1, container, false);
        final Intent intent = getActivity().getIntent();
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            Intent intent2= getActivity().getIntent();
            intent2.putExtra("from",1);
        }
        addBut=(Button)view.findViewById(R.id.addBut);
        delBut=(Button)view.findViewById(R.id.delBut);
        listview=(ListView)view.findViewById(R.id.conList);
        preData=new ArrayList<>();
        //final HashMap<String,Boolean> deleteCheck=new HashMap<>();

        listAdapter=new ArrayAdapter<item>(getContext(),R.layout.listitem,preData) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                convertView = View.inflate(getContext(), R.layout.listitem, null);
                TextView name = (TextView) convertView.findViewById(R.id.textView);
                name.setText(preData.get(position).name);
                final CheckBox check = (CheckBox) convertView.findViewById(R.id.CB);
                check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (check.isChecked()==true)
                            deleteCheck.put(preData.get(position).name, true);
                        else
                            deleteCheck.put(preData.get(position).name, false);
                    }
                });
                return convertView;
            }
        };
        listview.setAdapter(listAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterview, View v, int position, long l)
            {
                if (getActivity().getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT) {
                    Intent intent = new Intent(adapterview.getContext(), Main3Activity.class);
                    intent.putExtra("name", preData.get(position).name);
                    startActivity(intent);
                }
                else if(getActivity().getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
                    //Intent i3 = new Intent(adapterview.getContext(), MainActivity.class);
                    intent.putExtra("name", preData.get(position).name);
                    FragmentManager manager=getActivity().getFragmentManager();
                    FragmentTransaction trans=manager.beginTransaction();
                    trans.replace(R.id.fra2,new Fragment3(),"f3");
                    trans.commit();
                }
            }
        });

        delBut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getActivity().getSharedPreferences("profile",MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor=prefs.edit();
                Iterator<item> n=preData.iterator();
                ArrayList<item> data=preData;
                while(n.hasNext())
                {
                    item m=n.next();
                    if(deleteCheck.get(m.name).equals(true)) {
                        deleteFile(m.name);
                        n.remove();
                        prefsEditor.remove(m.name);
                        prefsEditor.apply();
                        for(int k=0;k<m.relation.size();k++) {
                            String s=m.relation.get(k);
                            //Toast.makeText(getContext(),"1 "+ s,Toast.LENGTH_SHORT).show();
                            for (int i=0;i<data.size();i++) {
                                //Toast.makeText(getContext()," "+ m.relation.size(),Toast.LENGTH_SHORT).show();
                                //String s=m.relation.get(k);
                                item in=data.get(i);
                                if (in.name.equals(s)) {
                                    in.relation.remove(m.name);
                                    prefsEditor.remove(in.name);
                                    Gson gson = new Gson();
                                    String js = gson.toJson(in);
                                    prefsEditor.putString(in.name, js);
                                    prefsEditor.apply();
                                }
                            }
                        }
                    }
                }
                listAdapter.notifyDataSetChanged();
                deleteCheck.clear();
                for(item i:preData)
                {
                    deleteCheck.put(i.name,false);
                }
                //data.clear();
            }
        });

        addBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //切换
                if (getActivity().getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT) {
                    Intent i = new Intent(v.getContext(), Main2Activity.class);
                    //i.putExtra("data",data);
                    startActivity(i);
                }
                else if(getActivity().getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE)
                {
                    FragmentManager manager=getActivity().getFragmentManager();
                    FragmentTransaction trans=manager.beginTransaction();
                    trans.replace(R.id.fra2,new Fragment2(),"f2");
                    trans.commit();
                }
            }
        });


        // Inflate the layout for this fragment
        return view;
    }



     @Override
    public void onActivityCreated (Bundle savedState) {
         super.onActivityCreated(savedState);
    }

    public boolean deleteFile(String name) {
        ContextWrapper cw = new ContextWrapper(getContext());
        File directory = cw.getDir("UserImage", Context.MODE_PRIVATE);
        File file=new File(directory,name+".png");
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
