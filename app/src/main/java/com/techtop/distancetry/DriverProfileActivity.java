package com.techtop.distancetry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class DriverProfileActivity extends AppCompatActivity {
    AdView mAdView;
    public static final String SHARED_PREFS = "sharedPrefs";
    Button btn_update;
    TextView name, tel, vehicleno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);
        getSupportActionBar().hide(); //hide the title bar

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        name = (TextView) findViewById(R.id.DriverName);
        tel = (TextView) findViewById(R.id.PhoneNumber);
        vehicleno = (TextView) findViewById(R.id.Vechicle_Name);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        if (sharedPreferences.contains("name")||sharedPreferences.contains("tel")||sharedPreferences.contains("vehicle")) {
            loaddata();
        }



        btn_update = (Button) findViewById(R.id.btn_update);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DriverProfileActivity.this, UpdateDriverProfile.class));
            }
        });
//


    }

    public void loaddata(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        name.setText(sharedPreferences.getString("name","~Not set~"));
        tel.setText(sharedPreferences.getString("tel","~Not set~"));
        vehicleno.setText(sharedPreferences.getString("vehicle","~Not set~"));


    }
}