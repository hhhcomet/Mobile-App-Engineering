package com.example.a18200.project2;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link //Fragment2.//OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText name;
    private EditText tel;
    private Button addBut;
    private ListView relation;
    private ArrayAdapter listadapter;
    private ImageView photo;
    private static final int REQ_CODE_TAKE_PICTURE = 90210;
    private Bitmap image;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragment2() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_fragment2, container, false);
        final Intent i=getActivity().getIntent();
        name=(EditText)view.findViewById(R.id.nameIn);
        tel=(EditText)view.findViewById(R.id.telIn);
//        String t1=(String) i.getSerializableExtra("passvalue1");
//        String t2=(String) i.getSerializableExtra("passvalue2");
//        name.setText(t1);
//        tel.setText(t2);
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            Intent intent = getActivity().getIntent();
            intent.putExtra("change",2);
//            String get1=name.getText().toString();
//            String get2=tel.getText().toString();
//            intent.putExtra("passvalue1",get1);
//            intent.putExtra("passvalue2",get2);
        }


        relation=(ListView)view.findViewById(R.id.detailList);
        addBut=(Button)view.findViewById(R.id.addpBut);
        photo=(ImageView)view.findViewById(R.id.photo1);
        //final ArrayList<item> data= (ArrayList<item>)i.getSerializableExtra("data");
        ArrayList<item> preData=new ArrayList<>();
        SharedPreferences preferences=getActivity().getSharedPreferences("profile",MODE_PRIVATE);
        Gson gson=new Gson();
        final Map<String,?> getPre=preferences.getAll();
        for (Map.Entry<String, ?> entry : getPre.entrySet()) {
            String json = (String) entry.getValue();
            item newI = gson.fromJson(json, item.class);
            preData.add(newI);
        }
        final ArrayList<item> data=preData;
        final Map<String,Boolean> relationCheck=new HashMap<>();
        for(int k=0;k<preData.size();k++)
        {
            relationCheck.put(preData.get(k).name,false);
        }
        listadapter=new ArrayAdapter<item>(getContext(),R.layout.listitem, data)
        {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent)
            {
                convertView = View.inflate(getContext(), R.layout.listitem, null);
                TextView name = (TextView) convertView.findViewById(R.id.textView);
                name.setText(data.get(position).name);
                final CheckBox check = (CheckBox) convertView.findViewById(R.id.CB);
                check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(check.isChecked()==true)
                        {
                            relationCheck.put(data.get(position).name,true);
                            item Temp=data.get(position);
                            data.add(0,Temp);
                            data.remove(position+1);
                            notifyDataSetChanged();
                        }
                        else {
                            relationCheck.put(data.get(position).name, false);
                            item Temp = data.get(position);
                            data.remove(position);
                            data.add(Temp);
                            //data.remove(position);
                            notifyDataSetChanged();
                        }

                    }
                });
                if(relationCheck.get(data.get(position).name).equals(true))
                    check.setChecked(true);
                return convertView;
            }
        };
        relation.setAdapter(listadapter);
        photo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                takePhoto();
            }
        });
        addBut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                SharedPreferences prefs = getActivity().getSharedPreferences("profile",MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor=prefs.edit();
                String Name=name.getText().toString();
                String Tel=tel.getText().toString();
                List<String> relationList=new ArrayList<String>();
                for(Map.Entry<String, Boolean> entry:relationCheck.entrySet())
                {
                    if(entry.getValue().equals(true)) {
                        relationList.add(entry.getKey());
                        for (item i : data) {
                            if (i.name.equals(entry.getKey())) {
                                i.relation.add(Name);
                                prefsEditor.remove(i.name);
                                Gson gson = new Gson();
                                String json = gson.toJson(i);
                                prefsEditor.putString(i.name, json);
                                prefsEditor.apply();
                            }
                        }
                    }
                }
                String path;
                if(image==null)
                    path="default";
                else
                    path=saveBitmap(image,Name);
                item Info=new item(Name, Tel, relationList,path);
                data.add(Info);
                Gson gson=new Gson();
                String json=gson.toJson(Info);
                prefsEditor.putString(Name,json);
                prefsEditor.apply();
                if(getActivity().getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT) {
                    getActivity().finish();
                }
                else if(getActivity().getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
                    FragmentManager manager=getActivity().getFragmentManager();
                    FragmentTransaction trans=manager.beginTransaction();
                    Fragment fm2= manager.findFragmentByTag("f2");
                    Fragment fm1=manager.findFragmentByTag("f1");
                    if(fm1!=null)
                        fm1.onResume();
                    trans.remove(fm2);
                    trans.commit();
                    Intent i=getActivity().getIntent();
                    i.putExtra("change",1);
                }
            }
        });
//        OrientationEventListener o = new OrientationEventListener(getContext()) {
//            @Override
//            public void onOrientationChanged(int i) {
//                Intent intent = getActivity().getIntent();
//                EditText text1 = (EditText) getActivity().findViewById(R.id.nameIn);
//                //if (!TextUtils.isEmpty(text1.getText())) {
//                    String t1 = text1.getText().toString();
//                    intent.putExtra("passvalue1", t1);
//                //}
//                EditText text2 = (EditText) getActivity().findViewById(R.id.telIn);
//                //if (!TextUtils.isEmpty(text2.getText())) {
//                    String t2 = text2.getText().toString();
//                    intent.putExtra("passvalue2", t2);
//               //}
//            }
//        };
        // Inflate the layout for this fragment
        return view;
    }

    public void onActivityCreated(Bundle savedInstance)
    {
        super.onActivityCreated(savedInstance);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //super.onActivityResult(requestCode,resultCode,intent);
        if (requestCode == REQ_CODE_TAKE_PICTURE
                && resultCode == RESULT_OK) {
            image = (Bitmap) intent.getExtras().get("data");
            photo = (ImageView) getActivity().findViewById(R.id.photo1);
            photo.setImageBitmap(image);
        }
    }

    public void takePhoto()
    {
        Intent takePicture=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture,REQ_CODE_TAKE_PICTURE);
    }

    public String saveBitmap(Bitmap bitmap, String bitName){
        ContextWrapper cw = new ContextWrapper(getContext());
        File directory = cw.getDir("UserImage", Context.MODE_PRIVATE);
        File mypath=new File(directory,bitName+".png");
        FileOutputStream f = null;
        try {
            f = new FileOutputStream(mypath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, f);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                f.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath()+"/"+bitName+".png";
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState)
//    {
//        super.onSaveInstanceState(outState);
//        saveState();
//    }
//
//    @Override
//    public void onDestroyView()
//    {
//        super.onDestroyView();
//        saveState();
//    }
//
//    public void saveState()
//    {
//        Bundle state=new Bundle();
//        name=(EditText)getActivity().findViewById(R.id.nameIn);
//        tel=(EditText)getActivity().findViewById(R.id.telIn);
//        String get1=name.getText().toString();
//        String get2=tel.getText().toString();
//        state.putString("passvalue1",get1);
//        state.putString("passvalue2",get2);
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
