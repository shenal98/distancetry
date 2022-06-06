package com.techtop.distancetry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.ads.AdView;

public class WelcomexActivity extends AppCompatActivity {

    Button btn_start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomex);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        btn_start = (Button)findViewById(R.id.btn_start);
        btn_start.setOnClickListener(v->{
            startActivity(new Intent(WelcomexActivity.this, DriverMainActivity.class));
        });

    }
}