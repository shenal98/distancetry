package com.techtop.distancetry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Date;

public class BillActivity extends AppCompatActivity {
    AdView mAdView;
    TextView dateTxt,vnoTxt,phoneTxt,oneKmRateTxt,twoKmRate,startTime,waitTime,waitRate,distance,totFare,endTime,totHireTime;
    Button btnCancle, btnSave;
    public static final String SHARED_PREFS = "sharedPrefs";
    String vno,pno = "~Not set~";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        dateTxt = (TextView) findViewById(R.id.datetxt);
        vnoTxt = (TextView) findViewById(R.id.v_numtxt);
        phoneTxt = (TextView) findViewById(R.id.pnotxt);
        oneKmRateTxt = (TextView) findViewById(R.id.onekmtxt);
        twoKmRate = (TextView) findViewById(R.id.twokmtxt);
        startTime = (TextView) findViewById(R.id.stimetxt);
        waitTime = (TextView) findViewById(R.id.wtimetxt);
        waitRate = (TextView) findViewById(R.id.wratetxt);
        distance = (TextView) findViewById(R.id.distnacetxt);
        totFare = (TextView) findViewById(R.id.tothiretxt);
        endTime = (TextView) findViewById(R.id.end_time);
        totHireTime = (TextView) findViewById(R.id.tot_hire_time);
        btnCancle = (Button) findViewById(R.id.canclebtn);
        btnSave = (Button) findViewById(R.id.savebtn);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        if (sharedPreferences.contains("name")||sharedPreferences.contains("tel")||sharedPreferences.contains("vehicle")) {
            loaddata();
        }

        dateTxt.setText(getIntent().getStringExtra("date"));
        vnoTxt.setText(vno);
        phoneTxt.setText(pno);
        oneKmRateTxt.setText(getIntent().getStringExtra("kmone_fee"));
        twoKmRate.setText(getIntent().getStringExtra("kmtwo_fee"));
        startTime.setText(getIntent().getStringExtra("started_time"));
        waitTime.setText(getIntent().getStringExtra("total_wait_time"));
        waitRate.setText(getIntent().getStringExtra("wait_fee"));
        distance.setText(getIntent().getStringExtra("distance"));
        totFare.setText(getIntent().getStringExtra("total_fare"));
        totHireTime.setText(getIntent().getStringExtra("hire_time"));
        endTime.setText(getIntent().getStringExtra("end_time"));

        DAOTripData dao = new DAOTripData();


        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BillActivity.this, MainActivity.class));
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            Date date = new Date();
            long timeMill = date.getTime();
            @Override
            public void onClick(View view) {

                TripData tripData = new TripData(getIntent().getStringExtra("date"),
                        pno,
                        vno,
                        getIntent().getStringExtra("kmone_fee"),
                        getIntent().getStringExtra("kmtwo_fee"),
                        getIntent().getStringExtra("started_time"),
                        getIntent().getStringExtra("total_wait_time"),
                        getIntent().getStringExtra("wait_fee"),
                        getIntent().getStringExtra("distance"),
                        getIntent().getStringExtra("total_fare"),
                        getIntent().getStringExtra("hire_time"),
                        getIntent().getStringExtra("end_time"),
                        String.valueOf(timeMill)
                        );

                dao.add(tripData).addOnSuccessListener(suc->{
                    Toast.makeText(BillActivity.this, "Inserted to Hire Details!", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(er->{
                    Toast.makeText(BillActivity.this, "Error occured: "+er.getMessage(), Toast.LENGTH_SHORT).show();
                });

                startActivity(new Intent(BillActivity.this, MainActivity.class));
//                Intent intent = getIntent();
                finish();

//                startActivity(intent);
            }
        });
    }
    public void loaddata(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        pno = sharedPreferences.getString("tel","~Not set~");
        vno = sharedPreferences.getString("vehicle","~Not set~");

    }
}