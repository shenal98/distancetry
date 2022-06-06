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

public class AmountActivity extends AppCompatActivity {
    AdView mAdView;
    Button btn_update, btn_cancle;
    EditText day_sFee, day_twokmFee, day_waitFee, night_sFee, night_twokmFee, night_waitFee;
    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount);
        getSupportActionBar().hide(); //hide the title bar

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        btn_update = findViewById(R.id.updateTripDetails_btn);
        btn_cancle = findViewById(R.id.cancelTripDetails_btn);

        day_sFee = findViewById(R.id.day_sFee_txt);
        day_twokmFee = findViewById(R.id.d_twokm_fee);
        day_waitFee = findViewById(R.id.d_wait_fee);

        night_sFee = findViewById(R.id.n_starting_fee);
        night_twokmFee = findViewById(R.id.n_twokm_fee);
        night_waitFee = findViewById(R.id.n_waiting_fee);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        if (sharedPreferences.contains("day_sFee")) {
            loaddata();
        }



        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(day_sFee.getText().toString().trim())
                        || TextUtils.isEmpty(day_twokmFee.getText().toString().trim())
                        || TextUtils.isEmpty(day_waitFee.getText().toString().trim())

                        || TextUtils.isEmpty(night_sFee.getText().toString().trim())
                        || TextUtils.isEmpty(night_twokmFee.getText().toString().trim())
                        || TextUtils.isEmpty(night_waitFee.getText().toString().trim())
                ){
                    Toast.makeText(AmountActivity.this, "Please fill all fields! ", Toast.LENGTH_SHORT).show();
                }else {
                    saveData();
                    Toast.makeText(AmountActivity.this, "Trip details has been updated! ✔️", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AmountActivity.this, MainActivity.class));
                }
            }
        });

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AmountActivity.this, MainActivity.class));
            }
        });
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("day_sFee", String.valueOf(day_sFee.getText()));
        editor.putString("day_twokmFee", String.valueOf(day_twokmFee.getText()));
        editor.putString("day_waitFee", String.valueOf(day_waitFee.getText()));

        editor.putString("night_sFee", String.valueOf(night_sFee.getText()));
        editor.putString("night_twokmFee", String.valueOf(night_twokmFee.getText()));
        editor.putString("night_waitFee", String.valueOf(night_waitFee.getText()));

        editor.apply();
    }

    public void loaddata(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        day_sFee.setText(sharedPreferences.getString("day_sFee",""));
        day_twokmFee.setText(sharedPreferences.getString("day_twokmFee",""));
        day_waitFee.setText(sharedPreferences.getString("day_waitFee",""));

        night_sFee.setText(sharedPreferences.getString("night_sFee",""));
        night_twokmFee.setText(sharedPreferences.getString("night_twokmFee",""));
        night_waitFee.setText(sharedPreferences.getString("night_waitFee",""));

    }

}