package com.example.a18200.project3;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private GoogleMap map = null;
    private double lng;
    private double lat;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastKnownLocation;
    private List<Marker> markerList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i=getIntent();
        lng=i.getDoubleExtra("long",0);
        lat=i.getDoubleExtra("lati",0);

        setContentView(R.layout.activity_main2);
        MapFragment mf = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mf.getMapAsync(this);
        //map.getUiSettings().setMyLocationButtonEnabled(true);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
        //map.setMyLocationEnabled(true);
    }

    @Override
    public void onMapLoaded() {
        map.setOnMapLoadedCallback(this);

    }

    @Override
    public void onMapReady(GoogleMap Mmap) {
        map = Mmap;

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
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        final SQLiteDatabase db = openOrCreateDatabase(
                "location", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT long, lati FROM locations", null);
        //final ArrayList<item> preData=new ArrayList<>();
        cursor.moveToFirst();
        if (cursor.moveToFirst() != false) {
            do {
                double longtemp = cursor.getDouble(cursor.getColumnIndex("long"));
                double latitemp = cursor.getDouble(cursor.getColumnIndex("lati"));

                LatLng ms = new LatLng(latitemp,longtemp);
//                String addtemp = cursor.getString(cursor.getColumnIndex("address"));
//                long datetemp = cursor.getLong(cursor.getColumnIndex("time"));
//                SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String datetemp2 = dformat.format(datetemp);
//                String nametmp = cursor.getString(cursor.getColumnIndex("name"));
//                item temp = new item(longtemp, latitemp, datetemp2, addtemp, nametmp);
//                preData.add(temp);
                //titemp, longtemp));
                Marker m=map.addMarker(new MarkerOptions()
                        .position(ms).visible(true));
                markerList.add(m);
                //Toast.makeText(this,"good"+latitemp,Toast.LENGTH_LONG).show();
                //test.setText(datetemp2);
                //Toast.makeText(this,"111",Toast.LENGTH_LONG).show();
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        IntentFilter filter = new IntentFilter();
        filter.addAction("refresh");
        registerReceiver(new Main2Activity.refreshReceiver(), filter);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            mLastKnownLocation = location;
                            // Logic to handle location object
                        }
                    }
                });
        //mLastKnownLocation=
        if (mLastKnownLocation != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), 13));
        }
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng point) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 13));

                final Geocoder gc = new Geocoder(getApplicationContext());
                List<Address> adds = new ArrayList<>();
                try {
                    adds = gc.getFromLocation(point.latitude, point.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                final LatLng use = point;
                AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                //builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("Input the name");
                View view = LayoutInflater.from(Main2Activity.this).inflate(R.layout.nameinputformap, null);
                builder.setView(view);

                final EditText name = (EditText) view.findViewById(R.id.nameMap);
                //final String[] nameGet = new String[1];
                //final EditText password = (EditText)view.findViewById(R.id.password);

                final List<Address> finalAdds = adds;
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Marker m=map.addMarker(new MarkerOptions()
                                .position(use)
                                .draggable(true));
                        markerList.add(m);
                        String a=" ";
                        if(name.getText().toString().trim()!=null)
                            a = name.getText().toString().trim();
                        String addMap;
                        if (finalAdds.size() > 0) {
                            Address curAdd = finalAdds.get(0);
                            addMap = curAdd.getAddressLine(0) + " " + curAdd.getAddressLine(1);
                        } else {
                            addMap = "not available";
                        }
                        BigDecimal bd = new BigDecimal(use.longitude);
                        double lngemp = bd.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
                        BigDecimal bd2 = new BigDecimal(use.latitude);
                        double latemp = bd2.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();

                        db.execSQL("INSERT INTO locations (long, lati, address, name) VALUES (" + lngemp + "," + latemp + ",'" + addMap + "','" + a + "')");

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.infowindow, null);
                LatLng loc = marker.getPosition();
//                BigDecimal bd = new BigDecimal(loc.longitude);
//                double lngemp = bd.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
//                BigDecimal bd2 = new BigDecimal(loc.latitude);
//                double latemp = bd2.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
                double lngemp=loc.longitude;
                double latemp=loc.latitude;
                Cursor cur2 = db.rawQuery("SELECT id, name FROM locations WHERE long=" + lngemp + " AND lati=" + latemp + "", null);
                String nameMark;
                String timeMark;
                int i = cur2.getCount();
                if (i == 0)
                    return null;
                else {
                    cur2.moveToFirst();
                    int id = cur2.getInt(cur2.getColumnIndex("id"));
                    nameMark = cur2.getString(cur2.getColumnIndex("name"));
                    cur2 = db.rawQuery("SELECT time FROM checkins WHERE locationid=" + id + " ORDER BY time DESC", null);
                    int i2 = cur2.getCount();
                    //timeMark;
                    if (i2 == 0) {
                        timeMark = "Time not Available.";
                    } else {
                        cur2.moveToFirst();
                        long datetemp = cur2.getLong(cur2.getColumnIndex("time"));
                        SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        timeMark = dformat.format(datetemp);
                        //timeMark = cur2.getString(cur2.getColumnIndex("time"));
                    }
                }
                TextView nameInfo =(TextView) v.findViewById(R.id.nameInfo);
                TextView timeInfo =(TextView) v.findViewById(R.id.timeInfoWindow);
                if(nameMark=="")
                    nameMark="Name not available.";
                nameInfo.setText(nameMark);
                timeInfo.setText(timeMark);
                cur2.close();
                return v;
            }
        });
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return false;
            }
        });
    }


    private class refreshReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // handle the received broadcast message
//            final TextView Long = (TextView) findViewById(R.id.longi);
//            final TextView Lati = (TextView) findViewById(R.id.lati);
//            final TextView Address = (TextView) findViewById(R.id.address);
//              Toast.makeText(getApplicationContext(),"refresh2",Toast.LENGTH_SHORT).show();
//            Bundle loc=intent.getExtras("location");
            lng = intent.getDoubleExtra("long", 0);
            lat = intent.getDoubleExtra("lati", 0);
//            String addGet=intent.getStringExtra("add");
//            Lati.setText("Latitude: " + lngGet);
//            Long.setText("Longitude: " + latGet);
//            Address.setText("Address: " + addGet);
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                    new LatLng(lat,
//                            lng), 13));
            double minDis = 30;
            Marker mGet = null;
            if(!markerList.isEmpty()) {
                for (int i = 0; i < markerList.size(); i++) {
                    Marker m = markerList.get(i);
                    double dis = getDistance(lat, lng, m.getPosition().latitude, m.getPosition().longitude);
                    if (dis < minDis) {
                        minDis = dis;
                        mGet = m;
                    }
                }
                if (mGet != null)
                    mGet.showInfoWindow();
            }
        }
    }

    public double getDistance(double lat1, double lon1, double lat2, double lon2)
    {
        float[] results=new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return results[0];
    }
}




