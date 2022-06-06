package com.techtop.distancetry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DetailsActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    TextView edit_phone,sign_in;
    String authPhone;
    EditText edit_name;
    EditText edit_vehicle;
    public static final String SHARED_PREFS = "sharedPrefs";



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        firebaseAuth = FirebaseAuth.getInstance();
        checkUserStatus();

        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_phone = (TextView) findViewById(R.id.edit_tel);
        sign_in = (TextView) findViewById(R.id.sign_in);
        edit_vehicle = (EditText) findViewById(R.id.edit_vehicle);
        Button btn = (Button) findViewById(R.id.register);

        edit_phone.setText(authPhone);


        DAOEmployee dao = new DAOEmployee();

        sign_in.setOnClickListener(v->{
            startActivity(new Intent(DetailsActivity.this, MainActivity.class));
        });

        btn.setOnClickListener(v->{

            if(TextUtils.isEmpty(edit_name.getText().toString().trim())){
                Toast.makeText(DetailsActivity.this, "Please Enter the Name", Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(edit_vehicle.getText().toString().trim())){
                Toast.makeText(DetailsActivity.this, "Please Enter the Vehicle Reg.no", Toast.LENGTH_SHORT).show();
            }else{
                //insert action
                Employee emp = new Employee(edit_name.getText().toString(), authPhone, edit_vehicle.getText().toString());
                dao.add(emp).addOnSuccessListener(suc->
                {
                    saveData();
                    Toast.makeText(this, "Registered Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DetailsActivity.this, WelcomexActivity.class));
                }).addOnFailureListener(er->{
                    Toast.makeText(this, "Error! "+er.getMessage(), Toast.LENGTH_SHORT).show();
                });
                //update action
//            HashMap<String, Object> hashMap = new HashMap<>();
//            hashMap.put("name", edit_name.getText().toString());
//            hashMap.put("position",edit_position.getText().toString());
//
//            dao.update("-MutBfDoOBhbkh2puY9Y",hashMap).addOnSuccessListener(suc->
//            {
//                Toast.makeText(this, "Record is updated!", Toast.LENGTH_SHORT).show();
//            }).addOnFailureListener(er->{
//                Toast.makeText(this, "Error! "+er.getMessage(), Toast.LENGTH_SHORT).show();
//            });

            }



        });

    }
    private void checkUserStatus() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null){
            //user logged in
            String phone = firebaseUser.getPhoneNumber();
            //edit_phone.setText(firebaseUser.getPhoneNumber().toString());
            authPhone = phone;
        }else {
            //user not logged in
            finish();
        }

    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", String.valueOf(edit_name.getText()));
        editor.putString("tel", String.valueOf(authPhone));
        editor.putString("vehicle", String.valueOf(edit_vehicle.getText()));
        editor.putInt("txt", 1);
        editor.apply();
    }

}