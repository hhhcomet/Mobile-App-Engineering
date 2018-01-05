package com.example.dell.final3;
//Written by Jingxuan Chen  Feng Rong
//Debugged by Feng Rong
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class NewTrip extends AppCompatActivity {
    ListView friendDis;
    List<Member> friendname;
    MemberListAdapter listAdapter;
    String userName;
    EditText travelName;
    DatePicker start;
    DatePicker end;
    private int s_y,s_m,s_d,e_y,e_m,e_d;
    private static String CALENDER_URL = "content://com.android.calendar/calendars";
    private static String CALENDER_EVENT_URL = "content://com.android.calendar/events";
    private static String CALENDER_REMINDER_URL = "content://com.android.calendar/reminders";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);

        userName=getIntent().getStringExtra("name");
        travelName=(EditText) findViewById(R.id.nameEdit);
//        start=(EditText) findViewById(R.id.startEdit);
//        end=(EditText) findViewById(R.id.endEdit);
        start=(DatePicker)findViewById(R.id.TripdpStart);
        end=(DatePicker)findViewById(R.id.TripdpEnd);
        final String[] dateStart={"2017-12-10"};
        final String[] dateEnd={"2018-1-1"};
        s_y=2017;
        s_m=12;
        s_d=10;
        e_y=2018;
        e_m=1;
        e_d=1;

        start.init(2017, 11, 10, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateStart[0] =year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                s_y=year;
                s_m=monthOfYear+1;
                s_d=dayOfMonth;
            }
        });
        end.init(2018, 0, 1, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateEnd[0] =year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                e_y=year;
                e_m=monthOfYear+1;
                e_d=dayOfMonth;
            }
        });


        friendDis =(ListView) findViewById(R.id.member_list);
        friendname=new  ArrayList<Member>();
        DatabaseReference friend= FirebaseDatabase.getInstance().getReference("friend/"+userName);
        Query friendD = friend.orderByKey();
        friendD.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot a:dataSnapshot.getChildren())
                {
                    friendname.add(new Member(a.getValue().toString()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listAdapter= new MemberListAdapter(NewTrip.this,R.layout.member_list,friendname);
        friendDis.setAdapter(listAdapter);




        Button add = (Button)findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(travelName.getText().toString().equals(""))
                {
                    Toast.makeText(NewTrip.this,"Please fill in the information.",Toast.LENGTH_SHORT).show();
                }
                else {
                    int y = s_y - e_y;
                    int mo = s_m - e_m;
                    int d = s_d - e_d;
                    int check = 0;
                    Log.v("timedata", "compare:" + y + " " + mo + " " + d);
                    if (y < 0) {
                        check = 1;
                    } else if (y == 0) {
                        if (mo < 0) {
                            check = 1;
                            if (d <= 0) {
                                check = 1;
                            }
                        } else if (mo == 0) {
                            if (d <= 0) {
                                check = 1;
                            }
                        }
                    }
                    if (check == 0) {
                        Toast.makeText(NewTrip.this, "Wrong date", Toast.LENGTH_SHORT).show();
                    } else if (check == 1) {
                        DatabaseReference table = FirebaseDatabase.getInstance().getReference("travel/" + userName);
                        DatabaseReference newTrip = table.child(travelName.getText().toString());
                        newTrip.child("name").setValue(travelName.getText().toString());
                        newTrip.child("start").setValue(dateStart[0]);
                        newTrip.child("end").setValue(dateEnd[0]);
                        for (Member m : friendname) {
                            if (m.checked) {
                                newTrip.child("member").push().setValue(m.name);
                                DatabaseReference otheruser = FirebaseDatabase.getInstance().getReference("travel/" + m.name);
                                //DatabaseReference otherNew = otheruser.push();
                                DatabaseReference otherNew = otheruser.child(travelName.getText().toString());
                                otherNew.child("name").setValue(travelName.getText().toString());
                                otherNew.child("start").setValue(dateStart[0]);
                                otherNew.child("end").setValue(dateEnd[0]);
                                otherNew.child("member").push().setValue(userName);
                                for (Member me : friendname) {
                                    if (me.checked && !me.name.equals(m.name)) {
                                        otherNew.child("member").push().setValue(me.name);
                                    }
                                }
                            }
                        }
                        //newTrip.child("member").push().setValue(userName);
                        //newTrip.child("member").push().setValue("zr");
                        String Sdate = s_y+"-"+s_m+"-"+s_d+" 08-00-00";
                        String Edate= e_y+"-"+e_m+"-"+e_d+" 08-00-00";
                        long time1=0;
                        long time2=0;
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                        try {
                            time1 = simpleDateFormat.parse(Sdate).getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        try {
                            time2=simpleDateFormat.parse(Edate).getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        addCalendarEvent(getApplicationContext(),"Your trip "+travelName.getText().toString()+" is at today!","You have trip today!",time1,time2);
                        NewTrip.this.finish();
                        Intent intent = new Intent(NewTrip.this, HomePage.class);
                        intent.putExtra("name", userName);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private static int checkCalendarAccount(Context context) {
        Cursor userCursor = context.getContentResolver().query(Uri.parse(CALENDER_URL), null, null, null, null);
        try {
            if (userCursor == null)
                return -1;
            int count = userCursor.getCount();
            if (count > 0) {
                userCursor.moveToFirst();
                return userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID));
            } else {
                return -1;
            }
        } finally {
            if (userCursor != null) {
                userCursor.close();
            }
        }
    }
    private static String CALENDARS_NAME = "test";
    private static String CALENDARS_ACCOUNT_NAME = "test@gmail.com";
    private static String CALENDARS_ACCOUNT_TYPE = "com.android.exchange";
    private static String CALENDARS_DISPLAY_NAME = "TEST ACCOUNT";

    private static long addCalendarAccount(Context context) {
        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(CalendarContract.Calendars.NAME, CALENDARS_NAME);

        value.put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE);
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDARS_DISPLAY_NAME);
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = Uri.parse(CALENDER_URL);
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE)
                .build();

        Uri result = context.getContentResolver().insert(calendarUri, value);
        long id = result == null ? -1 : ContentUris.parseId(result);
        return id;
    }

    private static int checkAndAddCalendarAccount(Context context){
        int oldId = checkCalendarAccount(context);
        if( oldId >= 0 ){
            return oldId;
        }else{
            long addId = addCalendarAccount(context);
            if (addId >= 0) {
                return checkCalendarAccount(context);
            } else {
                return -1;
            }
        }
    }

    public static void addCalendarEvent(Context context,String title, String description, long beginTime, long endTime){
        int calId = checkAndAddCalendarAccount(context);
        if (calId < 0) {
            return;
        }

        ContentValues event = new ContentValues();
        event.put("title", title);
        event.put("description", description);
        event.put("calendar_id", calId);

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(beginTime);
        long start = mCalendar.getTime().getTime();
        mCalendar.setTimeInMillis(endTime);
        long end = mCalendar.getTime().getTime();

        event.put(CalendarContract.Events.DTSTART, start);
        event.put(CalendarContract.Events.DTEND, end);
        event.put(CalendarContract.Events.HAS_ALARM, 1);
        event.put(CalendarContract.Events.EVENT_TIMEZONE, "America/New_York");
        Uri newEvent = context.getContentResolver().insert(Uri.parse(CALENDER_EVENT_URL), event);
        if (newEvent == null) {

            return;
        }
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(newEvent));
        values.put(CalendarContract.Reminders.MINUTES, 1320);
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        Uri uri = context.getContentResolver().insert(Uri.parse(CALENDER_REMINDER_URL), values);
        if(uri == null) {
            return;
        }
    }
}
