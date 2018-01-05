package com.example.dell.final3;
//Written by Xiuqi Ye
//Debugged by Jingxuan Chen
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyService extends Service {
    private String user;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void Create() {
        Log.v("MyService","executed at"+111);


        return;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        user=intent.getStringExtra("name");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Create();
                Log.v("MyService","executed at"+user);
            }
        }).start();

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 10000;

        long triggerAtTime = SystemClock.elapsedRealtime()+anHour;
        Intent i = new Intent(this,AlarmReceiver.class);
        i.putExtra("name",user);
        // i.putExtra("title","Trip Alert");
        //  i.putExtra("content","rtfg");
        Log.v("MyService","LAST"+user);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,i,0);

        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pendingIntent);

        return super.onStartCommand(intent, flags, startId);
    }



}