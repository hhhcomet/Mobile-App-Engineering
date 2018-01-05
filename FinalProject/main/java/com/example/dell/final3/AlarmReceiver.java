package com.example.dell.final3;

//written by xiuqi ye
//Debugged by Feng Rong
//Assisted by Rong Zhang

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.text.ParseException;
import java.util.Date;
import java.util.ArrayList;

import java.util.List;
import    java.text.SimpleDateFormat;

import static android.R.attr.y;
import static com.example.dell.final3.R.id.travelName;

public class AlarmReceiver extends BroadcastReceiver {
    private NotificationManager manager;
    private static final int NOTIFICATION_ID_1 = 0x00113;
    private String title;
    private String user;
    private String str;
    private long time2;
    private long time3;
    private long t;
    private String temp;
    private String cstr;
    private String content = "";
    // private List<Travel> travelList=new ArrayList<Travel>();
    @Override
    public void onReceive(final Context context, final Intent intent) {
        user = intent.getStringExtra("name");
        //  title = intent.getStringExtra("title");
        //  content = intent.getStringExtra("content");
        Log.v("MyRE","1"+user);
        Log.v("MyRE","2"+title);

        final DatabaseReference travel = FirebaseDatabase.getInstance().getReference("travel/"+user);
        Query tra = travel.orderByKey();
        tra.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot da : dataSnapshot.getChildren()) {
                    content = da.child("name").getValue().toString();
                    str = da.child("start").getValue().toString();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                    cstr = formatter.format(curDate);

                    str = str+" 23-59-59";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");//24小时制
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");//12小时制
                    time2 = 0;
                    try {
                        time2 = simpleDateFormat.parse(str).getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    time3 = 0;
                    try {
                        time3 = simpleDateFormat.parse(cstr).getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Log.v("timedata", "t2:" +str+time2);
                    Log.v("timedata", "t3:" +cstr+time3);
                    t=time2-time3;
                    Log.v("timedata", "t2-t3:" +t);

                    if (time2-time3<48*60*60*1000&&time2-time3>0) {
                        if (!da.child("clock").exists()) {
                            da.getRef().child("clock").setValue("1");
                            str = da.child("start").getValue().toString();
                            showNormal(context);
                        }

                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent i = new Intent(context,MyService.class);
        context.startService(i);

    }
    private void showNormal(Context context) {
        Intent intent = new Intent(context, HomePage.class);//这里是点击Notification 跳转的界面，可以自己选择
        intent.putExtra("name",user);
        Log.v("MyRE","executed at"+user);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(content)
                .setContentInfo("Trip Reminder")
                .setContentTitle("Trip Reminder")
                .setContentText("You will have a trip:"+content+"on "+str)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pi)
                .build();
        manager.notify(NOTIFICATION_ID_1, notification);
    }


}
