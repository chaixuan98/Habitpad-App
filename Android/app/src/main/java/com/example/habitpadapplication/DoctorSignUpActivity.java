package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DoctorSignUpActivity extends AppCompatActivity {

    private TextInputLayout dName, dEmail, dPassword, dPhone, dEducation, dExp, dHospital;
    private MaterialButton dConfirm;
    private TextView dSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Doctor Sign Up");
        setContentView(R.layout.activity_doctor_sign_up);

        dName = findViewById(R.id.doctor_full_name_layout);
        dPassword = findViewById(R.id.doctor_password_layout);
        dEmail = findViewById(R.id.doctor_email_layout);
        dPhone = findViewById(R.id.doctor_mobileNo_layout);
        dEducation  = findViewById(R.id.doctor_education_layout);
        dExp = findViewById(R.id.doctor_exp_layout);
        dHospital= findViewById(R.id.doctor_hospital_layout);
        dConfirm = findViewById(R.id.confirm);
        dSignIn = findViewById(R.id.doctor_signin_tv);

        dSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DoctorSignUpActivity.this,DoctorLoginActivity.class));

            }
        });

        dConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validateFullname() | !validateEmail() | !validatePhone() | !validatePassword() | !validateEducation() | !validateExp() | !validateHospital()){
                    return;
                }

                createDoctor( dName.getEditText().getText().toString().trim(),
                        dPassword.getEditText().getText().toString().trim(),
                        dEmail.getEditText().getText().toString().trim(),
                        dPhone.getEditText().getText().toString().trim(),
                        dEducation.getEditText().getText().toString().trim(),
                        dExp.getEditText().getText().toString().trim(),
                        dHospital.getEditText().getText().toString().trim());

                Intent intent = new Intent(getApplicationContext(),DoctorMainActivity.class);
                intent.putExtra("dName", dName.getEditText().getText().toString().trim());
                startActivity(intent);

            }
        });
    }

    private void createDoctor(final String doctorName,
                            final String doctorPassword,
                            final String doctorEmail,
                            final String doctorPhone,
                            final String doctorEducation,
                            final String doctorExp,
                            final String doctorHospital){

        // Progress
        dConfirm.setText("Creating...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.DOCTOR_SIGN_UP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.i("tagconvertstr", "["+response+"]");
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");

                    if (success.equals("1")) {

                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                        dConfirm.setText("Confirm");

                    }

                    if (success.equals("0")) {

                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                        dConfirm.setText("Confirm");

                    }

                } catch (JSONException e) {

                    Toast.makeText(getApplicationContext(),"Register Error!" + e.toString(),Toast.LENGTH_LONG).show();
                    dConfirm.setText("Confirm");

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                dConfirm.setText("Confirm");

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("doctorName",doctorName);
                params.put("doctorPassword",doctorPassword);
                params.put("doctorEmail",doctorEmail);
                params.put("doctorPhone",doctorPhone);
                params.put("doctorEducation",doctorEducation);
                params.put("doctorExp",doctorExp);
                params.put("doctorHospital",doctorHospital);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private  boolean validateFullname(){
        String val = dName.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            dName.setError("Field cannot be empty");
            return false;
        } else if(val.length()>30){
            dName.setError("Username is too large");
            return false;
        } else{
            dName.setError(null);
            dName.setErrorEnabled(false);
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

    private  boolean validatePhone() {
        String val = dPhone.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            dPhone.setError("Field cannot be empty");
            return false;
        } else {
            dPhone.setError(null);
            dPhone.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validateEducation() {
        String val = dEducation.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            dEducation.setError("Field cannot be empty");
            return false;
        } else {
            dEducation.setError(null);
            dEducation.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validateExp() {
        String val = dExp.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            dExp.setError("Field cannot be empty");
            return false;
        } else {
            dExp.setError(null);
            dExp.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validateHospital() {
        String val = dHospital.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            dHospital.setError("Field cannot be empty");
            return false;
        } else {
            dHospital.setError(null);
            dHospital.setErrorEnabled(false);
            return true;
        }
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent(DoctorSignUpActivity.this, DoctorLoginActivity.class);
        startActivity(intent);
        finish();
    }


}