package com.example.habitpadapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class DoctorLoginActivity extends AppCompatActivity {
    TextInputLayout dEmail, dPassword;
    MaterialButton dSignIn;

    SharedPreferences sharedPreferences;

    public static final String DfileName = "doctorLogin";
    public static final String DEmail = "doctorEmail";
    public static final String DPassword = "doctorPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor_login);

        // Getting UI views from our xml file
        dEmail = findViewById(R.id.doctoremaillayout);
        dPassword = findViewById(R.id.doctorpasswordlayout);
        dSignIn = findViewById(R.id.doctor_signinbtn);

        sharedPreferences = getSharedPreferences(DfileName, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(DEmail)){
            Toast.makeText(DoctorLoginActivity.this,"Doctor login successfully",Toast.LENGTH_SHORT).show();
            // Finish
            finish();
            // Start activity dashboard
            startActivity(new Intent(DoctorLoginActivity.this,AdminTipsActivity.class));
        }

        dSignIn.setOnClickListener(v -> {

            if(!validateEmail() | !validatePassword()){
                return;
            }

            if (dEmail.getEditText().getText().toString().equals("doctor@doctor.com") &&  dPassword.getEditText().getText().toString().equals("doctor456")) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(DEmail,dEmail.getEditText().getText().toString());
                editor.putString(DPassword,dPassword.getEditText().getText().toString());
                editor.commit();

                Toast.makeText(DoctorLoginActivity.this,"Doctor login successfully",Toast.LENGTH_SHORT).show();
                // Finish
                finish();
                // Start activity dashboard
                startActivity(new Intent(DoctorLoginActivity.this,AdminTipsActivity.class));
            }

        });

    }

    private  boolean validateEmail(){
        String val = dEmail.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()){
            dEmail.setError("Field cannot be empty");
            return false;
        } else if(!val.matches(checkEmail)){
            dEmail.setError("Invalid Email!");
            return false;
        } else{
            dEmail.setError(null);
            dEmail.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validatePassword(){
        String val = dPassword.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            dPassword.setError("Field cannot be empty");
            return false;
        } else{
            dPassword.setError(null);
            dPassword.setErrorEnabled(false);
            return true;
        }
    }

    public void backUserOptionPage(View view) {
        startActivity(new Intent(DoctorLoginActivity.this,UserOptionActivity.class));
    }
}