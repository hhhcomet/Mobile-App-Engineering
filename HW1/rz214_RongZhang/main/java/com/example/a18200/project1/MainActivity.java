package com.example.a18200.project1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    EditText Title_edit;
    EditText Des_edit;
    ListView content_list;
    ArrayList<HashMap<String, String>> list;
    SimpleAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Title_edit = (EditText) findViewById(R.id.TitleText);
        Des_edit = (EditText) findViewById(R.id.DesText);
        content_list = (ListView) findViewById(R.id.mainlist);
        list = new ArrayList<>();
        list = getData();

        adapter = new SimpleAdapter(this, list, R.layout.listitem, new String[]{"title", "des"}, new int[]{R.id.itemTitle, R.id.itemDes})
        {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent)
            {
                if(convertView == null)
                    convertView = View.inflate(MainActivity.this, R.layout.listitem, null);
                CheckBox check = (CheckBox)convertView.findViewById(R.id.deleteCheck);
                check.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                        writeFile();
                    }
                });
            return super.getView(position, convertView, parent);
            }
        };
        content_list.setAdapter(adapter);
        content_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                list.remove(position);
                adapter.notifyDataSetChanged();
                writeFile();
                return true;
            }
        });
    }

    public ArrayList<HashMap<String, String>> getData() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(openFileInput("Todo_list.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (scanner == null) {
            Toast.makeText(this, "Your list is empty!", Toast.LENGTH_SHORT).show();
            return list;
        }

        String line;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            String[] tem = line.split("\t");
            HashMap<String, String> item = new HashMap<>();
            item.put("title", tem[0]);
            item.put("des", tem[1]);
            list.add(item);
        }
        scanner.close();
        return list;
    }


    public void addClick(View view) {
        HashMap<String, String> item = new HashMap<>();
        item.put("title", Title_edit.getText().toString());
        item.put("des", Des_edit.getText().toString());
        list.add(item);
        adapter.notifyDataSetChanged();
        Title_edit.setText("");
        Des_edit.setText("");
        writeFile();
    }

    public void writeFile() {
        PrintStream output=null;
        try
        {
            output=new PrintStream(openFileOutput("Todo_list.txt", MODE_PRIVATE));
            for(HashMap<String,String> item:list)
            {
                output.println(item.get("title")+"\t"+item.get("des"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}


