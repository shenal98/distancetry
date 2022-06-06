package com.techtop.distancetry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.techtop.distancetry.databinding.ActivityMainBinding;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {




    private ActivityMainBinding binding;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    private String mVerificationId;
    private static String TAG = "Main_TAG";

    private FirebaseAuth firebaseAuth;

    private ProgressDialog pd;

    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);





        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        if (sharedPreferences.contains("txt")) {

            startActivity(new Intent(MainActivity.this, DriverMainActivity.class));
        }

        binding.regli.setVisibility(View.VISIBLE); //visible main
        binding.codeLi.setVisibility(View.GONE); //hide code layout


        firebaseAuth =FirebaseAuth.getInstance();

        pd = new ProgressDialog(this);
        pd.setTitle("Please wait...");
        pd.setCanceledOnTouchOutside(false);

        //test UIs here

//        Button test_btn = (Button) findViewById(R.id.btn_test);
//        test_btn.setOnClickListener(v->{
//            startActivity(new Intent(MainActivity.this, WelcomexActivity.class));
//        });


        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                pd.dismiss();
                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, forceResendingToken);

                Log.d(TAG, "onCodeSent: "+ verificationId);

                mVerificationId = verificationId;
                forceResendingToken = token;
                pd.dismiss();

                binding.codeLi.setVisibility(View.VISIBLE);
                binding.regli.setVisibility(View.GONE); //hide main

                Toast.makeText(MainActivity.this, "Verification code sent...", Toast.LENGTH_SHORT).show();
                binding.codeSentDescription.setText("Verification code sent to +94 "+binding.phoneEt.getText().toString().trim());

            }
        };

        binding.phoneContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = binding.phoneEt.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(MainActivity.this, "Please Enter the phone number", Toast.LENGTH_SHORT).show();
                }
                else {
                    startPhoneNumberVerification("+94"+phone);
                }
            }
        });

        binding.resendCodeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = binding.phoneEt.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(MainActivity.this, "Please Enter the phone number", Toast.LENGTH_SHORT).show();
                }
                else {
                    resendVerificationCode("+94"+phone, forceResendingToken);
                }
            }
        });

        binding.codeSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = binding.codeEt.getText().toString().trim();
                if(TextUtils.isEmpty(code)){
                    Toast.makeText(MainActivity.this, "Please Enter verification code", Toast.LENGTH_SHORT).show();
                } else{
                    verifyPhoneNumberWithCode(mVerificationId, code);
                }
            }
        });

    }

    private void startPhoneNumberVerification(String phone) {
        pd.setMessage("Verifying Phone Number");
        pd.show();

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallback)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void resendVerificationCode(String phone,PhoneAuthProvider.ForceResendingToken token) {
        pd.setMessage("Resending Code");
        pd.show();

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallback)
                .setForceResendingToken(token)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void verifyPhoneNumberWithCode(String mVerificationId, String code) {
        pd.setMessage("Verifying Code");
        pd.show();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithAuthCredential(credential);
    }

    private void signInWithAuthCredential(PhoneAuthCredential credential) {
        pd.setMessage("Logging in");

        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        pd.dismiss();
                        String phone = firebaseAuth.getCurrentUser().getPhoneNumber();
                        Toast.makeText(MainActivity.this, "Logged in!", Toast.LENGTH_SHORT).show();

                        //change here to test intent
                        startActivity(new Intent(MainActivity.this, DetailsActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}