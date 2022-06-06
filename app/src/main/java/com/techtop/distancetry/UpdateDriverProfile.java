package com.techtop.distancetry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class UpdateDriverProfile extends AppCompatActivity {
    AdView mAdView;
    EditText edit_name, edit_tel, edit_vehicle;
    public static final String SHARED_PREFS = "sharedPrefs";

    Button btn_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_driver_profile);
        getSupportActionBar().hide(); //hide the title bar

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        edit_name = findViewById(R.id.edit_name);

        edit_vehicle = findViewById(R.id.edit_vehicle);
        btn_update = findViewById(R.id.update_btn);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        if (sharedPreferences.contains("name")||sharedPreferences.contains("tel")||sharedPreferences.contains("vehicle")) {
            loaddata();
        }


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(edit_name.getText().toString().trim())
                        || TextUtils.isEmpty(edit_vehicle.getText().toString().trim())
                ){
                    Toast.makeText(UpdateDriverProfile.this, "Please fill all fields! ", Toast.LENGTH_SHORT).show();
                }else {
                    saveData();
                    Toast.makeText(UpdateDriverProfile.this, "Driver profile has been updated! ✔️", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UpdateDriverProfile.this, MainActivity.class));
                }
            }
        });


    }
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("name", String.valueOf(edit_name.getText()));
        editor.putString("vehicle", String.valueOf(edit_vehicle.getText()));


        editor.apply();
    }

    public void loaddata(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        edit_name.setText(sharedPreferences.getString("name",""));
        edit_vehicle.setText(sharedPreferences.getString("vehicle",""));


    }
}