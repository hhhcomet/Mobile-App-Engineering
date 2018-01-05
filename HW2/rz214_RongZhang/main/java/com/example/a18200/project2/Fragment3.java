package com.example.a18200.project2;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.R.layout.simple_expandable_list_item_1;
import static android.content.Context.MODE_PRIVATE;
import static com.example.a18200.project2.R.layout.listitem;



public class Fragment3 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView name;
    private TextView tel;
    private ListView list;
    private ArrayAdapter<String> listadapter;
    private ImageView photo;

    private OnFragmentInteractionListener mListener;

    public Fragment3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment3.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment3 newInstance(String param1, String param2) {
        Fragment3 fragment = new Fragment3();
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
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_fragment3, container, false);
        name=(TextView)view.findViewById(R.id.nameOut);
        tel=(TextView)view.findViewById(R.id.telOut);
        list=(ListView)view.findViewById(R.id.detailList2);
        photo=(ImageView)view.findViewById(R.id.photo2) ;
        Intent i=getActivity().getIntent();
        String string=(String)i.getSerializableExtra("name");
        //ArrayList<item> preData=new ArrayList<>();
        final Intent intent=getActivity().getIntent();
        if(getActivity().getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE)
        {
            Intent i2=getActivity().getIntent();
            i2.putExtra("change",3);
        }
        SharedPreferences preferences=getActivity().getSharedPreferences("profile",MODE_PRIVATE);
        Gson gson=new Gson();
        String json=preferences.getString(string,"");
        item Item = gson.fromJson(json, item.class);
        if(Item.path.equals("default"))
        {
            photo.setImageResource(R.drawable.a);
        }
        else{
            Bitmap image = getBitmap(Item.path);
            photo.setImageBitmap(image);
        }
        name.setText(Item.name);
        //String t=String.valueOf(item.tel);
        tel.setText(Item.tel);

        final List<String> relation=Item.relation;
        listadapter=new ArrayAdapter<String>(getContext(),R.layout.showitem, relation)
        {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent)
            {
                convertView = View.inflate(getContext(), R.layout.showitem, null);
                TextView name= (TextView) convertView.findViewById(R.id.showbox);
                name.setText(relation.get(position));
                return convertView;
            }
        };
        list.setAdapter(listadapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterview, View v, int position, long l) {
                if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    Intent i3 = new Intent(adapterview.getContext(), Main3Activity.class);
                    i3.putExtra("name", relation.get(position));
                    startActivity(i3);
                    getActivity().finish();
                } else if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    intent.putExtra("name", relation.get(position));
                    FragmentManager manager=getActivity().getFragmentManager();
                    FragmentTransaction trans=manager.beginTransaction();
                    trans.replace(R.id.fra2,new Fragment3(),"f3");
                    trans.commit();
                }

            }

        });
        final String P=Item.path;
        photo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               Intent intent=new Intent(getContext(), ZoomActivity.class);
                intent.putExtra("path",P);
                startActivity(intent);
            }
        });
        return view;
    }

    public void onActivityCreated(Bundle savedInstance)
    {
        super.onActivityCreated(savedInstance);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private Bitmap getBitmap(String pathString)
    {
        Bitmap bitmap = null;
        try
        {
            File file = new File(pathString);
            if(file.exists())
            {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        } catch (Exception e)
        {
        }
        return bitmap;
    }

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
