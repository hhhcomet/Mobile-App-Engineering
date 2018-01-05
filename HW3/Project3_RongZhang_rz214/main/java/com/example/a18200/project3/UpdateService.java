package com.example.a18200.project3;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;


import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;

public class UpdateService extends Service {
    //private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastKnownLocation;

    public UpdateService() {
    }

    @Override
    public int onStartCommand(Intent tent, int flags, int id) {

        LocationManager lm = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        final Geocoder gc = new Geocoder(getApplicationContext());
        //final Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        final double[] myLat = {0};
        final double[] myLong = {0};

        Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (loc == null) {
            // currentLocation.setText("2");
            loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (loc != null) {
            myLat[0] = loc.getLatitude();
            myLong[0] = loc.getLongitude();
        }
        List<Address> add = new ArrayList<>();
        try {
            add = gc.getFromLocation(myLat[0], myLong[0], 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (add.size() > 0) {
            Address newadd = add.get(0);
            BigDecimal bd = new BigDecimal(myLat[0]);
            myLat[0] = bd.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
            BigDecimal bd2 = new BigDecimal(myLong[0]);
            myLong[0] = bd2.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
            Intent refresh = new Intent();
            refresh.setAction("refresh");
            //String key = getResources().getString(key);
            refresh.putExtra("long", myLong[0]);
            refresh.putExtra("lati", myLat[0]);
            refresh.putExtra("add", newadd.getAddressLine(0) + " " + newadd.getAddressLine(1));
            sendBroadcast(refresh);
        }

        final SQLiteDatabase db = openOrCreateDatabase(
                "location", MODE_PRIVATE, null);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                //Toast.makeText(getApplicationContext(),"time up",Toast.LENGTH_SHORT).show();
                LocationManager lm = (LocationManager)
                        getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location loc1 = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                //Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (loc1 == null) {
                    // currentLocation.setText("2");
                    loc1 = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                if (loc1 != null) {
                    myLat[0] = loc1.getLatitude();
                    myLong[0] = loc1.getLongitude();
                }
                List<Address> adds = new ArrayList<>();
                try {
                    adds = gc.getFromLocation(myLat[0], myLong[0], 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (adds.size() > 0) {
                    Address addUp = adds.get(0);
                    BigDecimal bd = new BigDecimal(loc1.getLatitude());
                    double latUp = bd.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
                    BigDecimal bd2 = new BigDecimal(loc1.getLongitude());
                    double longUp = bd2.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
                    Date date = new Date();
                    long time = date.getTime();
                    //values.put("time",time);
                    String name = "";
                    SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time2 = dformat.format(time);
                    //db.insert("checkins", null, values);
                    Cursor curIn = db.rawQuery("SELECT id FROM locations WHERE long=" + longUp + " AND lati=" + latUp + "", null);
                    int i = curIn.getCount();
                    int id;
                    if (i <= 0) {
//                            Toast.makeText(getApplicationContext(), "autocheck", Toast.LENGTH_LONG).show();
                        curIn = db.rawQuery("SELECT long, lati, name FROM locations", null);
                        double minDis = 30;
                        //String nameDis;
                        if (curIn.getCount() > 0) {
                            curIn.moveToFirst();
                            do {
                                double longGet = curIn.getDouble(curIn.getColumnIndex("long"));
                                double latiGet = curIn.getDouble(curIn.getColumnIndex("lati"));
                                String nameGet = curIn.getString(curIn.getColumnIndex("name"));
                                double temp = getDistance(latUp, longUp, latiGet, longGet);
                                if (temp <= minDis) {
                                    minDis = temp;
                                    if (nameGet != "") {
                                        name = nameGet;
                                    }
                                }
                            } while (curIn.moveToNext());
                        }
                        db.execSQL("INSERT INTO locations (long, lati, address, name) VALUES (" + longUp + "," + latUp + ",'" + addUp.getAddressLine(0)+" "+addUp.getAddressLine(1) + "','" + name + "')");
                        curIn = db.rawQuery("SELECT id FROM locations ORDER BY id DESC", null);
                        curIn.moveToFirst();
                        id = curIn.getInt(curIn.getColumnIndex("id"));
                    } else {
                        curIn.moveToFirst();
                        id = curIn.getInt(curIn.getColumnIndex("id"));
                    }
                    db.execSQL("INSERT INTO checkins (time, locationid) VALUES (" + time + "," + id + ")");
                    curIn = db.rawQuery("SELECT name FROM locations WHERE id=" + id + "", null);
                    curIn.moveToFirst();
                    name = curIn.getString(curIn.getColumnIndex("name"));
                    curIn.close();

                    Intent refresh = new Intent();
                    refresh.setAction("refresh");
                    //refresh.putExtra("location",loc);
                    refresh.putExtra("long", longUp);
                    refresh.putExtra("lati", latUp);
                    refresh.putExtra("name", name);
                    refresh.putExtra("add", addUp.getAddressLine(0) + " " + addUp.getAddressLine(1));
                    refresh.putExtra("auto", true);
                    sendBroadcast(refresh);
                    //Log.v("1", "nihao");
                }
            }
        },300000, 300000);


        if (loc != null) {
            myLat[0] = loc.getLatitude();
            myLong[0] = loc.getLongitude();
        }
        final double[] lat1 = {myLat[0]};
        final double[] lng1 = {myLong[0]};
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
            return START_NOT_STICKY;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                myLat[0] = location.getLatitude();
                myLong[0] = location.getLongitude();
                List<Address> add = new ArrayList<>();
                try {
                    add = gc.getFromLocation(myLat[0], myLong[0], 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (add.size() > 0) {
                    Address newadd = add.get(0);
                    BigDecimal bd = new BigDecimal(myLat[0]);
                    myLat[0] = bd.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
                    BigDecimal bd2 = new BigDecimal(myLong[0]);
                    myLong[0] = bd2.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
                    Intent refresh = new Intent();
                    refresh.setAction("refresh");
                    //String key = getResources().getString(key);
                    Bundle locBun = location.getExtras();
                    refresh.putExtra("location", locBun);
                    refresh.putExtra("long", myLong[0]);
                    refresh.putExtra("lati", myLat[0]);
                    refresh.putExtra("add", newadd.getAddressLine(0) + " " + newadd.getAddressLine(1));
                    //refresh.putExtra("auto",false);
                    //sendBroadcast(refresh);
                    double dis = getDistance(lat1[0], lng1[0], myLat[0], myLong[0]);
                    if (dis > 100) {
                            BigDecimal bd3 = new BigDecimal(myLat[0]);
                            double latUp2 = bd3.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
                            BigDecimal bd4 = new BigDecimal(myLong[0]);
                            double longUp2 = bd4.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
                            Date date = new Date();
                            long time = date.getTime();
                            //values.put("time",time);
                            String name = "";
                            SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String time2 = dformat.format(time);
                            //db.insert("checkins", null, values);
                            Cursor curIn = db.rawQuery("SELECT id FROM locations WHERE long=" + longUp2 + " AND lati=" + latUp2 + "", null);
                            int i = curIn.getCount();
                            int id;
                            if (i <= 0) {
                                //Toast.makeText(getApplicationContext(), "autocheck", Toast.LENGTH_LONG).show();
                                curIn = db.rawQuery("SELECT long, lati, name FROM locations", null);
                                double minDis = 30;
                                //String nameDis;
                                if (curIn.getCount() > 0) {
                                    curIn.moveToFirst();
                                    do {
                                        double longGet = curIn.getDouble(curIn.getColumnIndex("long"));
                                        double latiGet = curIn.getDouble(curIn.getColumnIndex("lati"));
                                        String nameGet = curIn.getString(curIn.getColumnIndex("name"));
                                        double temp = getDistance(latUp2, longUp2, latiGet, longGet);
                                        if (temp <= minDis) {
                                            minDis = temp;
                                            if (nameGet != "") {
                                                name = nameGet;
                                            }
                                        }
                                    } while (curIn.moveToNext());
                                }
                                db.execSQL("INSERT INTO locations (long, lati, address, name) VALUES (" + longUp2 + "," + latUp2 + ",'" + newadd.getAddressLine(0) + " " + newadd.getAddressLine(1) + "','" + name + "')");
                                        curIn = db.rawQuery("SELECT id FROM locations ORDER BY id DESC", null);
                                curIn.moveToFirst();
                                id = curIn.getInt(curIn.getColumnIndex("id"));
                            } else {
                                curIn.moveToFirst();
                                id = curIn.getInt(curIn.getColumnIndex("id"));
                            }
                            db.execSQL("INSERT INTO checkins (time, locationid) VALUES (" + time + "," + id + ")");
                            curIn = db.rawQuery("SELECT name FROM locations WHERE id=" + id + "", null);
                            curIn.moveToFirst();
                            name = curIn.getString(curIn.getColumnIndex("name"));
                            curIn.close();
                            // Intent refresh = new Intent();
                            //refresh.setAction("refresh");
                            //refresh.putExtra("location",loc);
                            //refresh.putExtra("long", longUp);
                            //refresh.putExtra("lati", latUp);
                            refresh.putExtra("name", name);
                            //refresh.putExtra("add", addUp.getAddressLine(0) + " " + addUp.getAddressLine(1));
                            refresh.putExtra("auto", true);
                            //sendBroadcast(refresh);
                            lat1[0] =myLat[0];
                            lng1[0] =myLong[0];
                    }
                    sendBroadcast(refresh);
                }
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
                });
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        myLat[0] = location.getLatitude();
                        myLong[0] = location.getLongitude();
                        List<Address> add = new ArrayList<>();
                        try {
                            add = gc.getFromLocation(myLat[0], myLong[0], 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (add.size() > 0) {
                            Address newadd = add.get(0);
                            BigDecimal bd = new BigDecimal(myLat[0]);
                            myLat[0] = bd.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
                            BigDecimal bd2 = new BigDecimal(myLong[0]);
                            myLong[0] = bd2.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
                            Intent refresh = new Intent();
                            refresh.setAction("refresh");
                            //String key = getResources().getString(key);
                            Bundle locBun = location.getExtras();
                            refresh.putExtra("location", locBun);
                            refresh.putExtra("long", myLong[0]);
                            refresh.putExtra("lati", myLat[0]);
                            refresh.putExtra("add", newadd.getAddressLine(0) + " " + newadd.getAddressLine(1));
                            //refresh.putExtra("auto",false);
                            //sendBroadcast(refresh);
                            double dis = getDistance(lat1[0], lng1[0], myLat[0], myLong[0]);
                            if (dis > 100) {
                                BigDecimal bd3 = new BigDecimal(myLat[0]);
                                double latUp2 = bd3.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
                                BigDecimal bd4 = new BigDecimal(myLong[0]);
                                double longUp2 = bd4.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
                                Date date = new Date();
                                long time = date.getTime();
                                //values.put("time",time);
                                String name = "";
                                SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String time2 = dformat.format(time);
                                //db.insert("checkins", null, values);
                                Cursor curIn = db.rawQuery("SELECT id FROM locations WHERE long=" + longUp2 + " AND lati=" + latUp2 + "", null);
                                int i = curIn.getCount();
                                int id;
                                if (i <= 0) {
                                   // Toast.makeText(getApplicationContext(), "autocheck", Toast.LENGTH_LONG).show();
                                    curIn = db.rawQuery("SELECT long, lati, name FROM locations", null);
                                    double minDis = 30;
                                    //String nameDis;
                                    if (curIn.getCount() > 0) {
                                        curIn.moveToFirst();
                                        do {
                                            double longGet = curIn.getDouble(curIn.getColumnIndex("long"));
                                            double latiGet = curIn.getDouble(curIn.getColumnIndex("lati"));
                                            String nameGet = curIn.getString(curIn.getColumnIndex("name"));
                                            double temp = getDistance(latUp2, longUp2, latiGet, longGet);
                                            if (temp <= minDis) {
                                                minDis = temp;
                                                if (nameGet != "") {
                                                    name = nameGet;
                                                }
                                            }
                                        } while (curIn.moveToNext());
                                    }
                                    db.execSQL("INSERT INTO locations (long, lati, address, name) VALUES (" + longUp2 + "," + latUp2 + ",'" + newadd.getAddressLine(0) + " " + newadd.getAddressLine(1) + "','" + name + "')");
                                    curIn = db.rawQuery("SELECT id FROM locations ORDER BY id DESC", null);
                                    curIn.moveToFirst();
                                    id = curIn.getInt(curIn.getColumnIndex("id"));
                                } else {
                                    curIn.moveToFirst();
                                    id = curIn.getInt(curIn.getColumnIndex("id"));
                                }
                                db.execSQL("INSERT INTO checkins (time, locationid) VALUES (" + time + "," + id + ")");
                                curIn = db.rawQuery("SELECT name FROM locations WHERE id=" + id + "", null);
                                curIn.moveToFirst();
                                name = curIn.getString(curIn.getColumnIndex("name"));
                                curIn.close();
                                // Intent refresh = new Intent();
                                //refresh.setAction("refresh");
                                //refresh.putExtra("location",loc);
                                //refresh.putExtra("long", longUp);
                                //refresh.putExtra("lati", latUp);
                                refresh.putExtra("name", name);
                                //refresh.putExtra("add", addUp.getAddressLine(0) + " " + addUp.getAddressLine(1));
                                refresh.putExtra("auto", true);
                                //sendBroadcast(refresh);
                                lat1[0] =myLat[0];
                                lng1[0] =myLong[0];
                            }
                            sendBroadcast(refresh);
                        }
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

                });

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static double getDistance(double lat1, double lon1, double lat2, double lon2)
    {
        float[] results=new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return results[0];
    }

}
