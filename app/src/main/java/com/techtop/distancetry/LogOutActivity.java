package com.techtop.distancetry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.techtop.distancetry.databinding.ActivityLogOutBinding;
public class LogOutActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private ActivityLogOutBinding binding;
    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hide the title bar



        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        binding = ActivityLogOutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        checkUserStatus();

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                editor.commit();
                firebaseAuth.signOut();

                checkUserStatus();
                startActivity(new Intent(LogOutActivity.this, MainActivity.class));
            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogOutActivity.this, DriverMainActivity.class));
            }
        });
    }



    private void checkUserStatus() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null){
            //user logged in
            String phone = firebaseUser.getPhoneNumber();
            binding.phoneTv.setText(phone);
        }else {
            //user not logged in
            finish();
        }

    }
}