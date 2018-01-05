package com.example.dell.final3;
//Written by Feng Rong
//Debugged by Feng Rong
import android.app.ActivityManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class newTripMap extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback,GoogleMap.OnMapClickListener,GoogleMap.OnPoiClickListener{
    private int s_y,s_m,s_d,e_y,e_m,e_d;
    private GoogleMap map;
    private static String CALENDER_URL = "content://com.android.calendar/calendars";
    private static String CALENDER_EVENT_URL = "content://com.android.calendar/events";
    private static String CALENDER_REMINDER_URL = "content://com.android.calendar/reminders";

    //private LatLng sydney = new LatLng(27.746974, 85.301582);
    //private Marker marker;

       // map.addMarker(new MarkerOptions().position(sydney).title("Kathmandu, Nepal"));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip_map);
        MapFragment mapFragment=(MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setClickable(true);

       // Intent intent = getIntent();
        //map.setOnMapClickListener();
        final String travelName = getIntent().getStringExtra("travelName");
        final String userName = getIntent().getStringExtra("name");
        final String start1 = getIntent().getStringExtra("start");
        final String end1 = getIntent().getStringExtra("end");


        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_new_dest);
        Button addNewdest=(Button)findViewById(R.id.addNewDest);
        final EditText destName=(EditText)findViewById(R.id.destNameIn);
        DatePicker start=(DatePicker)findViewById(R.id.DestdpStart);
        DatePicker end=(DatePicker)findViewById(R.id.DestdpEnd);
        final String[] dateStart={"2017-12-15"};
        final String[] dateEnd={"2018-1-1"};
        s_y=2017;
        s_m=12;
        s_d=10;
        e_y=2018;
        e_m=1;
        e_d=1;

        start.init(2017, 11, 15, new DatePicker.OnDateChangedListener() {
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

        addNewdest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(destName.getText().toString().equals(""))
                {
                    Toast.makeText(newTripMap.this,"Please fill in the information.",Toast.LENGTH_SHORT).show();
                }
                else {
                    int y = s_y - e_y;
                    int m = s_m - e_m;
                    int d = s_d - e_d;
                    int check = 0;
                    Log.v("timedata", "compare:" + y + " " + m + " " + d);
                    if (y < 0) {
                        check = 1;
                    } else if (y == 0) {
                        if (m < 0) {
                            check = 1;
                            if (d <= 0) {
                                check = 1;
                            }
                        } else if (m == 0) {
                            if (d <= 0) {
                                check = 1;
                            }
                        }
                    }
                    if (check == 0) {
                        Toast.makeText(newTripMap.this, "Wrong date", Toast.LENGTH_SHORT).show();
                    } else if (check == 1) {
                        Todo newDest = new Todo(destName.getText().toString(), dateStart[0] + "----" + dateEnd[0]);
                        DatabaseReference table = FirebaseDatabase.getInstance().getReference("travel/" + userName+"/"+travelName);
                        DatabaseReference nDest = table.child("destination").child(destName.getText().toString());
                        nDest.child("name").setValue(destName.getText().toString());
                        nDest.child("time").setValue(dateStart[0] + "----" + dateEnd[0]);

                        DatabaseReference other =  FirebaseDatabase.getInstance().getReference("travel/"+userName+"/"+travelName+"/member");
                        Query otherme = other.orderByKey();
                        otherme.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                               for(DataSnapshot m :dataSnapshot.getChildren())
                               {
                                  DatabaseReference others = FirebaseDatabase.getInstance().getReference("travel/"+m.getValue()+"/"+travelName);
                                   DatabaseReference nDest_t = others.child("destination").child(destName.getText().toString());
                                   nDest_t.child("name").setValue(destName.getText().toString());
                                   nDest_t.child("time").setValue(dateStart[0] + "----" + dateEnd[0]);
                               }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        newTripMap.this.finish();
                        Intent intent = new Intent(newTripMap.this, Details.class);
                        intent.putExtra("name", userName);
                        intent.putExtra("travelName",travelName);
                        intent.putExtra("start",start1);
                        intent.putExtra("end",end1);
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
                        addCalendarEvent(getApplicationContext(),"Your travel to "+destName.getText().toString()+" is at today!","You have a new destination today!",time1,time2);
                        startActivity(intent);
                    }
                }
            }
        });
    }
    public void onMapSearch(View view) {
        map.clear();
        EditText locationSearch = (EditText) findViewById(R.id.destNameIn);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            //marker.setPosition(latLng);

            map.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        //Toast.makeText(newTripMap.this,"saf",Toast.LENGTH_SHORT).show();
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(40.711626, -74.001582);
        //marker.setPosition()
       // map.addMarker(new MarkerOptions().position(sydney).title("Kathmandu, Nepal"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        map.setOnMapClickListener(this);
        map.setOnPoiClickListener(this);
       // if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           // return;
        //}
        //map.setMyLocationEnabled(true);
    }



    @Override
    public void onMapLoaded() {

    }


    @Override
    public void onMapClick(LatLng latLng)
    {
        //marker.setVisible(false);
        map.clear();
        double lat=latLng.latitude;
        //Toast.makeText(newTripMap.this,"saf",Toast.LENGTH_SHORT).show();
        BigDecimal bd=new BigDecimal(lat);
        lat=bd.setScale(6,BigDecimal.ROUND_HALF_UP).doubleValue();
        double lon=latLng.longitude;
        bd=new BigDecimal(lon);
        lon=bd.setScale(6,BigDecimal.ROUND_HALF_UP).doubleValue();
        Geocoder geocoder=new Geocoder(this);
       // marker.position(latLng);
        //map.addMarker(marker.position(latLng));
        map.addMarker(new MarkerOptions().position(latLng));
        //marker.setVisible(true);
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        //String addr="";
        try
        {
            List<Address> list = geocoder.getFromLocation(lat, lon, 1);
            Address address=list.get(0);
            //addr=address.getAddressLine(0)+address.getAddressLine(1);
            EditText destName=(EditText)findViewById(R.id.destNameIn);
            destName.setText(address.getAddressLine(0)+address.getAddressLine(1));
            //addr.setText("Address:"+address.getAddressLine(0)+address.getAddressLine(1));

        }catch(Exception e)
        {
            Log.e("WEI","Error:"+e.toString());
        }

    }

    @Override
    public void onPoiClick(PointOfInterest pointOfInterest) {
        map.clear();
        EditText destName=(EditText)findViewById(R.id.destNameIn);
        destName.setText(pointOfInterest.name);
        //marker.setPosition(pointOfInterest.latLng);
        map.addMarker(new MarkerOptions().position(pointOfInterest.latLng));


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
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.RED);
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
