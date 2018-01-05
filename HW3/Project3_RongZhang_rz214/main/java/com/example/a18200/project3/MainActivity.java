package com.example.a18200.project3;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {
//        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
//    GoogleApiClient mGoogleApiClient=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ////////////////////////////////////Graduate part 1) test/////////////////////////////////////
        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final long start = System.currentTimeMillis();
        Log.v("start", "" + System.currentTimeMillis());
        final long[] end = {0};
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //Provider can be changed due to different use on the test. While testing on one provider, another provider needs to be disabled in the service.
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                end[0] = System.currentTimeMillis();
                Log.v("end", "" + end[0]);
                Log.v("total1", "" + (end[0] - start));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        }, null);
        /////////////////////////////////////////test///////////////////////////////////////////






        Intent serviceIntent=new Intent(this,UpdateService.class);
        startService(serviceIntent);
        final TextView Long = (TextView) findViewById(R.id.longi);
        final TextView Lati = (TextView) findViewById(R.id.lati);
        final TextView Address = (TextView) findViewById(R.id.address);
        final EditText Name=(EditText) findViewById(R.id.customName);
        Button test=(Button) findViewById(R.id.test);
        ListView list=(ListView) findViewById(R.id.list);
        //currentLocation.setText("1");
//        if(mGoogleApiClient==null) {
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(LocationServices.API)
//                    .build();
//        }
        final SQLiteDatabase db = openOrCreateDatabase(
                "location", MODE_PRIVATE, null);
        //db.beginTransaction();
//        db.execSQL("");
        //showDebugDBAddressLogToast(this);
//        db.execSQL("DROP TABLE checkins");
//        db.execSQL("DROP TABLE locations");
        db.execSQL("CREATE TABLE IF NOT EXISTS locations (long DOUBLE NOT NULL, lati DOUBLE NOT NULL, address VARCHAR(100), name VARCHAR(30), id INTEGER PRIMARY KEY AUTOINCREMENT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS checkins (locationid INTEGER, time INTEGER, id INTEGER PRIMARY KEY AUTOINCREMENT)");
        Button checkin=(Button)findViewById(R.id.CheckIn);
        final ArrayList<item> preData=new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT long, lati, address, name,id FROM locations",null);
        cursor.moveToFirst();
        if (cursor.moveToFirst()!=false) {
            do{
                double longtemp = cursor.getDouble(cursor.getColumnIndex("long"));
                double latitemp = cursor.getDouble(cursor.getColumnIndex("lati"));
                String addtemp = cursor.getString(cursor.getColumnIndex("address"));
                String nametmp = cursor.getString(cursor.getColumnIndex("name"));
                int idtemp=cursor.getInt(cursor.getColumnIndex("id"));
                Cursor cursor2=db.rawQuery("SELECT time FROM checkins WHERE locationid="+idtemp+"",null);
                if(cursor2.moveToFirst()!=false) {
                    cursor2.moveToFirst();
                    do {
                        long datetemp = cursor2.getLong(cursor2.getColumnIndex("time"));
                        SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String datetemp2 = dformat.format(datetemp);
//                        String nametmp = cursor.getString(cursor.getColumnIndex("name"));
                        item temp = new item(longtemp, latitemp, datetemp2, addtemp, nametmp);
                        preData.add(temp);
                    } while (cursor2.moveToNext());
                }
                cursor2.close();
                //test.setText(datetemp2);
                //Toast.makeText(this,"111",Toast.LENGTH_LONG).show();
            }
            while (cursor.moveToNext()) ;
        }
        cursor.close();
        //SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        final ArrayAdapter<item> listadapter=new ArrayAdapter<item>(this,R.layout.item,preData)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                convertView=View.inflate(getContext(),R.layout.item,null);
                //TextView test=(TextView)findViewById(R.id.test);
                //test.setText(preData.get(0).addr);
                TextView listlong = (TextView) convertView.findViewById(R.id.longtmp);
                TextView listlati = (TextView) convertView.findViewById(R.id.latitmp);
                TextView listtime = (TextView) convertView.findViewById(R.id.timetmp);
                TextView listadd = (TextView) convertView.findViewById(R.id.addtmp);
                //TextView listname=(TextView) convertView.findViewById(R.id.nametmp);
                listlong.setText("Longitude: "+String.valueOf(preData.get(position).longi));
                listlati.setText("Latitude: "+String.valueOf(preData.get(position).lati));
                listadd.setText(preData.get(position).addr);
                listtime.setText(preData.get(position).time);
                //listname.setText("Name: "+preData.get(position).name);
                return convertView;
            }
        };
        list.setAdapter(listadapter);
        double rutgers[][]={{40.527799,-74.465344},{40.526692,-74.462295},{40.519222, -74.461608},{40.522452, -74.457660},{40.525381, -74.459843}};
        Geocoder gc = new Geocoder(this);
        for(int k=0;k<5;k++)
        {
            double longIn=rutgers[k][1];
            double latiIn=rutgers[k][0];
            String addIn="";
            String nameIn="Rutgers place "+k;

            List<Address> add = new ArrayList<>();
            try {
                add = gc.getFromLocation(latiIn, longIn, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (add.size() > 0) {
                Address newadd = add.get(0);
                addIn = newadd.getAddressLine(0);
            }
            //values.put("long",Double.parseDouble(Long.getText().toString().substring(11)));
            //values.put("lati",Double.parseDouble(Lati.getText().toString().substring(10)));
            //values.put("address", Address.getText().toString().substring(9));
            //values.put("name",Name.getText().toString());
            Date date=new Date();
            long time=date.getTime();
            //values.put("time",time);
            SimpleDateFormat dformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time2=dformat.format(time);
            //db.insert("checkins", null, values);
            Cursor curIn=db.rawQuery("SELECT id FROM locations WHERE long="+longIn+" AND lati="+latiIn+"",null);
            int i=curIn.getCount();
            int id;
            if(i<=0) {
                //Toast.makeText(getApplicationContext(),"test",Toast.LENGTH_LONG).show();
                curIn=db.rawQuery("SELECT long, lati, name FROM locations",null);
                double minDis=30;
                //String nameDis;
                if(curIn.getCount()>0) {
                    curIn.moveToFirst();
                    do {
                        double longGet = curIn.getDouble(curIn.getColumnIndex("long"));
                        double latiGet = curIn.getDouble(curIn.getColumnIndex("lati"));
                        String nameGet = curIn.getString(curIn.getColumnIndex("name"));
                        double temp = getDistance(latiIn, longIn, latiGet, longGet);
                        if (temp <= minDis) {
                            minDis = temp;
                            if(nameGet!=" ") {
                                nameIn = nameGet;
                            }
                        }
                    } while (curIn.moveToNext());
                }
                db.execSQL("INSERT INTO locations (long, lati, address, name) VALUES (" + longIn + "," + latiIn + ",'" + addIn + "','" + nameIn + "')");
                curIn=db.rawQuery("SELECT id FROM locations ORDER BY id DESC",null);
                curIn.moveToFirst();
                id=curIn.getInt(curIn.getColumnIndex("id"));
            }
            else
            {
                curIn.moveToFirst();
                id=curIn.getInt(curIn.getColumnIndex("id"));
            }
            db.execSQL("INSERT INTO checkins (time, locationid) VALUES ("+time+","+id+")");
            curIn=db.rawQuery("SELECT name FROM locations WHERE id="+id+"",null);
            curIn.moveToFirst();
            nameIn=curIn.getString(curIn.getColumnIndex("name"));
            item newi=new item(longIn,
                    latiIn,time2,addIn,nameIn);
            preData.add(newi);
            listadapter.notifyDataSetChanged();
            curIn.close();
        }
        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ContentValues values=new ContentValues();
                double longIn=Double.parseDouble(Long.getText().toString().substring(11));
                double latiIn=Double.parseDouble(Lati.getText().toString().substring(10));
                String addIn=Address.getText().toString().substring(9);
                String nameIn=Name.getText().toString();
                //values.put("long",Double.parseDouble(Long.getText().toString().substring(11)));
                //values.put("lati",Double.parseDouble(Lati.getText().toString().substring(10)));
                //values.put("address", Address.getText().toString().substring(9));
                //values.put("name",Name.getText().toString());
                Date date=new Date();
                long time=date.getTime();
                //values.put("time",time);
                SimpleDateFormat dformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time2=dformat.format(time);
                //db.insert("checkins", null, values);
                Cursor curIn=db.rawQuery("SELECT id FROM locations WHERE long="+longIn+" AND lati="+latiIn+"",null);
                int i=curIn.getCount();
                int id;
                if(i<=0) {
                    //Toast.makeText(getApplicationContext(),"test",Toast.LENGTH_LONG).show();
                    curIn=db.rawQuery("SELECT long, lati, name FROM locations",null);
                    double minDis=30;
                    //String nameDis;
                    if(curIn.getCount()>0) {
                        curIn.moveToFirst();
                        do {
                            double longGet = curIn.getDouble(curIn.getColumnIndex("long"));
                            double latiGet = curIn.getDouble(curIn.getColumnIndex("lati"));
                            String nameGet = curIn.getString(curIn.getColumnIndex("name"));
                            double temp = getDistance(latiIn, longIn, latiGet, longGet);
                            if (temp <= minDis) {
                                minDis = temp;
                                if(nameGet!=" ") {
                                    nameIn = nameGet;
                                }
                            }
                        } while (curIn.moveToNext());
                    }
                    db.execSQL("INSERT INTO locations (long, lati, address, name) VALUES (" + longIn + "," + latiIn + ",'" + addIn + "','" + nameIn + "')");
                    curIn=db.rawQuery("SELECT id FROM locations ORDER BY id DESC",null);
                    curIn.moveToFirst();
                    id=curIn.getInt(curIn.getColumnIndex("id"));
                }
                else
                {
                    curIn.moveToFirst();
                    id=curIn.getInt(curIn.getColumnIndex("id"));
                }
                db.execSQL("INSERT INTO checkins (time, locationid) VALUES ("+time+","+id+")");
                curIn=db.rawQuery("SELECT name FROM locations WHERE id="+id+"",null);
                curIn.moveToFirst();
                nameIn=curIn.getString(curIn.getColumnIndex("name"));
                item newi=new item(Double.parseDouble(Long.getText().toString().substring(11)),
                        Double.parseDouble(Lati.getText().toString().substring(10)),time2,Address.getText().toString().substring(9),nameIn);
                preData.add(newi);
                listadapter.notifyDataSetChanged();
                curIn.close();
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,Main2Activity.class);
                i.putExtra("long",Double.parseDouble(Long.getText().toString().substring(11)));
                i.putExtra("lati",Double.parseDouble(Lati.getText().toString().substring(10)));
                startActivity(i);
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction("refresh");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final TextView Long = (TextView) findViewById(R.id.longi);
                final TextView Lati = (TextView) findViewById(R.id.lati);
                final TextView Address = (TextView) findViewById(R.id.address);
                //Toast.makeText(getApplicationContext(),"refresh1",Toast.LENGTH_SHORT).show();
                double lngGet = intent.getDoubleExtra("long", 0);
                double latGet = intent.getDoubleExtra("lati", 0);
                String addGet = intent.getStringExtra("add");
                Lati.setText("Latitude: " + latGet);
                Long.setText("Longitude: " + lngGet);
                Address.setText("Address: " + addGet);
                boolean auto = intent.getBooleanExtra("auto", false);
                if (auto == true) {
                    //Toast.makeText(getApplicationContext(),"auto!",Toast.LENGTH_SHORT).show();
                    String nameGet = intent.getStringExtra("name");
                    Date date1 = new Date();
                    long time1 = date1.getTime();
                    //values.put("time",time);
                    SimpleDateFormat dformat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time21 = dformat1.format(time1);
                    item newi = new item(lngGet,
                            latGet, time21, addGet, nameGet);
                    preData.add(newi);
                    listadapter.notifyDataSetChanged();
                }
            }
        }, filter);

        Button swi=(Button)findViewById(R.id.swi);
        swi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //not completed
                Toast.makeText(getApplicationContext(),"switch",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static double getDistance(double lat1, double lon1, double lat2, double lon2)
    {
        float[] results=new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return results[0];
    }
}
