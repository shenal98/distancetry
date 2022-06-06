package com.techtop.distancetry;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.text.DecimalFormat;

public class DriverMainActivity extends AppCompatActivity {
    AdView mAdView;

    private TextView textViewLatitude, textViewLongitude,textViewspeed, textViewDistance,textViewFee;
    private LocationManager locationManager;
    Button btn_start,btn_stop;

    public static final String SHARED_PREFS = "sharedPrefs";

    ImageView imageViewWait_start,imageViewWait_pause, ImageViewTrip_details, ImageViewHireHisory;
    long start_time, end_time, start_short_time, end_short_time, wait_time_start, wait_time_end, wait_difference;
    long wait_tot;
    long time_difference;

    String end_time_send;
    String start_time_send;

    float starting_fee = 0;
    float two_km_fee = 0;
    float wait_min_fee = 0;

    //stop watch
    private long pauseOffset;
    private boolean running;

    private long p_pauseOffset;
    private boolean p_running;

    DateFormat dateFormat = new SimpleDateFormat("hh.mm aa");

    Chronometer chorono_wait_time,chrono_hire_time;


    int count = 1;

    double start_latitude = 0.0;
    double start_longitude = 0.0;

    double end_latitude = 0.0;
    double end_longitude = 0.0;

    double tot_distance;
    double dis;

    double wait_time;
    double fee;

    float speed = 0;

    private static final DecimalFormat df = new DecimalFormat("0.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);
        //getSupportActionBar().hide(); //hide the title bar

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        textViewspeed = (TextView) findViewById(R.id.speedtxt);
        textViewDistance = (TextView) findViewById(R.id.distancetxt);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        btn_start = (Button) findViewById(R.id.btn_update);
        textViewFee = (TextView) findViewById(R.id.feetxt);
        btn_stop = (Button) findViewById(R.id.stopbtn);

        chrono_hire_time = (Chronometer) findViewById(R.id.chrono_hire_time);
        chorono_wait_time = (Chronometer) findViewById(R.id.chrono_wait_time);

        imageViewWait_start = (ImageView) findViewById(R.id.p_start);
        imageViewWait_pause = (ImageView) findViewById(R.id.p_pause);
        ImageViewTrip_details = (ImageView) findViewById(R.id.trip_details);
        ImageViewHireHisory = (ImageView) findViewById(R.id.tripHistory);


        //Hire History
        ImageViewHireHisory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverMainActivity.this, TripDetailActivity.class);
                startActivity(intent);
            }
        });




        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        if (!(sharedPreferences.contains("day_sFee"))){
            //default trip details values

            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("day_sFee","30");
            editor.putString("day_twokmFee", "35");
            editor.putString("day_waitFee", "5");

            editor.putString("night_sFee", "40");
            editor.putString("night_twokmFee", "45");
            editor.putString("night_waitFee", "10");

            editor.apply();
        }




//        textViewLatitude = (TextView) findViewById(R.id.latitudetxt);
//        textViewLongitude = (TextView) findViewById(R.id.longitudetxt);


        //trip details

        ImageViewTrip_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DriverMainActivity.this, AmountActivity.class));
            }
        });


        //wait start action
        imageViewWait_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!p_running && running){
                    chorono_wait_time.setBase(SystemClock.elapsedRealtime() - p_pauseOffset);
                    chorono_wait_time.start();
                    p_running = true;
                    imageViewWait_start.setVisibility(View.GONE);
                    wait_time_start = (new Date().getTime()) / 1000;
                }else{
                    Toast.makeText(DriverMainActivity.this, "You can't use wait before you starts the trip 👨‍🔧", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //wait pause action
        imageViewWait_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(p_running) {
                    wait_time_end = (new Date().getTime()) / 1000;
                    wait_difference = (wait_time_end - wait_time_start)/60;

                    wait_tot = wait_tot + wait_difference;
                    chorono_wait_time.stop();
                    p_pauseOffset = SystemClock.elapsedRealtime() - chorono_wait_time.getBase();
                    p_running = false;
                    imageViewWait_start.setVisibility(View.VISIBLE);
                }
            }
        });




        //action of the stop button
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //chrono stop
                chrono_hire_time.setBase(SystemClock.elapsedRealtime());
                pauseOffset = 0;
                end_time = (new Date().getTime()) / 1000;

                time_difference = (end_time - start_time)/60; //in minutes


                end_time_send = String.valueOf(dateFormat.format(new Date()));

                DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
                String dateString2 = dateFormat2.format(new Date()).toString();

                Intent intent_send = new Intent(getBaseContext(), BillActivity.class);
                intent_send.putExtra("distance", String.valueOf(df.format(tot_distance/1000))+"km");
                intent_send.putExtra("hire_time", String.valueOf(time_difference)+"min");
                intent_send.putExtra("kmone_fee", String.valueOf(starting_fee)+"/=");
                intent_send.putExtra("kmtwo_fee", String.valueOf(two_km_fee)+"/=");
                intent_send.putExtra("wait_fee", String.valueOf(wait_min_fee)+"/=");
                intent_send.putExtra("started_time", start_time_send);
                intent_send.putExtra("end_time", end_time_send);
                intent_send.putExtra("date", dateString2);
                intent_send.putExtra("total_wait_time",String.valueOf(wait_tot)+"min");
                intent_send.putExtra("total_fare","Rs."+String.valueOf(Math.ceil(fee))+"/=");

                //restart intent to fix the bug of refreshing
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                //*******************************************

                startActivity(intent_send);
                finish();
                //startActivity(new Intent(MainActivity.this, BillActivity.class));
                Toast.makeText(DriverMainActivity.this, "Trip ended!✋", Toast.LENGTH_SHORT).show();
            }
        });




        //action of the start button
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!running){
                    chrono_hire_time.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                    chrono_hire_time.start();
                    running = true;
                }

                Toast.makeText(DriverMainActivity.this, "Trip Started!\uD83C\uDFC1", Toast.LENGTH_SHORT).show();
                if(ContextCompat.checkSelfPermission(DriverMainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(DriverMainActivity.this, ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(DriverMainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION},1);
                }

                btn_start.setVisibility(View.GONE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
//                        textViewLongitude.setText(String.valueOf(Math.round(location.getLongitude()*1000000.0)/1000000.0));
//                        textViewLatitude.setText(String.valueOf(Math.round(location.getLatitude()*1000000.0)/1000000.0));

                        //textViewCount.setText(String.valueOf(location.hasAccuracy()));

                        if(count == 1){
                            start_latitude = location.getLatitude();
                            start_longitude = location.getLongitude();
                            end_latitude = location.getLatitude();
                            end_longitude = location.getLongitude();

                            start_time = (new Date().getTime()) / 1000;
                            start_short_time = (new Date().getTime()) / 1000;
                            start_time_send = String.valueOf(dateFormat.format(new Date()));

                        }else if (count % 2 == 0){
                            end_latitude = location.getLatitude();
                            end_longitude = location.getLongitude();

                            end_short_time = (new Date().getTime()) / 1000;

                        }else{
                            start_latitude = location.getLatitude();
                            start_longitude = location.getLongitude();

                            start_short_time = (new Date().getTime()) / 1000;
                        }

                        float[] results = new float[1];
                        Location.distanceBetween(start_latitude, start_longitude,end_latitude,end_longitude, results);
                        dis = results[0];
                        tot_distance =  tot_distance + dis;

                        speed = (Float.parseFloat(String.valueOf(dis))/Float.parseFloat(String.valueOf(Math.abs(end_short_time-start_short_time))));
                        speed = (speed* Float.parseFloat(String.valueOf(3.6))) ;// ms-1 to kmh-1

                        if(String.valueOf(Math.ceil(speed)) != "Infinity"){
                            textViewspeed.setText(String.valueOf(df.format(Double.parseDouble(String.valueOf(speed)))));
                        }


//                        if ((tot_distance/1000) == 1/0){
//
//                        }
                        textViewDistance.setText(String.valueOf(df.format(tot_distance/1000)));

                        fee = feeCal(Double.parseDouble(String.valueOf(df.format(tot_distance/1000))),wait_tot);

                        textViewFee.setText("Rs."+String.valueOf(fee));

                        count ++;
                    }

                });
            }
        });




    }

    public double feeCal (double distance, double wait_min){

        double tot_fee = 0;

        Formatter format = new Formatter();

        Calendar gfg_calender = Calendar.getInstance();

        format = new Formatter();
        format.format("%tH", gfg_calender, gfg_calender);

        System.out.println(format);
        int time = Integer.parseInt(String.valueOf(format));

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        if((time>6 && time<=20)){
            //morning 6pm-8.59pm

            //take different 6 shared preference values night and day times

            starting_fee = Float.parseFloat(sharedPreferences.getString("day_sFee",""));
            two_km_fee =  Float.parseFloat(sharedPreferences.getString("day_twokmFee",""));
            wait_min_fee =  Float.parseFloat(sharedPreferences.getString("day_waitFee",""));
        }else{
            //night 9.00pm-5.59am

            starting_fee = Float.parseFloat(sharedPreferences.getString("night_sFee",""));
            two_km_fee =  Float.parseFloat(sharedPreferences.getString("night_twokmFee",""));
            wait_min_fee =  Float.parseFloat(sharedPreferences.getString("night_waitFee",""));
        }

        if (distance <= 1){
            tot_fee = starting_fee + (wait_min_fee * wait_min); //tot_fee = starting_fee * distance + (wait_min_fee * wait_min);
        }else{
            tot_fee = (starting_fee * 1)+ (two_km_fee * (distance -1)) + (wait_min_fee * wait_min);
        }
        return tot_fee;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.hire_history_itm:

                Toast.makeText(this, "Hire history", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DriverMainActivity.this, TripDetailActivity.class));
                break;
            case R.id.driver_details_itm:
                startActivity(new Intent(DriverMainActivity.this, DriverProfileActivity.class));
                Toast.makeText(this, "Driver Profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.change_hire_details_itm:
                startActivity(new Intent(DriverMainActivity.this, AmountActivity.class));
                Toast.makeText(this, "Change hire rates", Toast.LENGTH_SHORT).show();
                break;
            case R.id.driver_logout:
                startActivity(new Intent(DriverMainActivity.this, LogOutActivity.class));
                Toast.makeText(this, "Go to Logout", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}