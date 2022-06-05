package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class UserOptionActivity extends AppCompatActivity {

    private MaterialButton patientBtn,doctorBtn,adminBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_option);

        patientBtn = findViewById(R.id.patientLogin);
        doctorBtn = findViewById(R.id.doctorLogin);
        adminBtn = findViewById(R.id.adminLogin);

        patientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(UserOptionActivity.this, LoginActivity.class));

            }
        });

        doctorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(UserOptionActivity.this,DoctorLoginActivity.class));

            }
        });

        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(UserOptionActivity.this,AdminLoginActivity.class));

            }
        });

    }

}