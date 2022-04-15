package com.example.habitpadapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DoctorLoginActivity extends AppCompatActivity {

    private TextInputLayout dEmail, dPassword;
    private MaterialButton dSignIn;
    private TextView dSignUp;

    SharedPreferences sharedPreferences;

    public static final String DfileName = "doctorLogin";
    public static final String DEmail = "doctorEmail";
    public static final String DPassword = "doctorPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Doctor Login");
        setContentView(R.layout.activity_doctor_login);

        // Getting UI views from our xml file
        dEmail = findViewById(R.id.doctoremaillayout);
        dPassword = findViewById(R.id.doctorpasswordlayout);
        dSignIn = findViewById(R.id.doctor_signinbtn);
        dSignUp = findViewById(R.id.doctor_signup_tv);

        sharedPreferences = getSharedPreferences(DfileName, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(DEmail)){
            Toast.makeText(DoctorLoginActivity.this,"Doctor login successfully",Toast.LENGTH_SHORT).show();
            // Finish
            finish();
            // Start activity dashboard
            startActivity(new Intent(DoctorLoginActivity.this,DoctorMainActivity.class));
        }

        dSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DoctorLoginActivity.this,DoctorSignUpActivity.class));

            }
        });

        dSignIn.setOnClickListener(v -> {

            if(!validateEmail() | !validatePassword()){
                return;
            }

            signIn(dEmail.getEditText().getText().toString(), dPassword.getEditText().getText().toString());

//            if (dEmail.getEditText().getText().toString().equals("doctor@doctor.com") &&  dPassword.getEditText().getText().toString().equals("doctor456")) {
//
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString(DEmail,dEmail.getEditText().getText().toString());
//                editor.putString(DPassword,dPassword.getEditText().getText().toString());
//                editor.commit();
//
//            }



        });

    }

    private void signIn( final String doctorEmail, final String doctorPassword) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.DOCTOR_SIGN_IN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.i("tagconvertstr", "["+response+"]");
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    JSONArray jsonArray = jsonObject.getJSONArray("doctorlogin");

                    if (success.equals("1")) {
                        for (int i =0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            String doctorID = object.getString("doctorID").trim();
                            String doctorPhoto = object.getString("doctorPhoto").trim();
                            String doctorName = object.getString("doctorName").trim();
                            String doctorPassword = object.getString("doctorPassword").trim();
                            String doctorEmail = object.getString("doctorEmail").trim();
                            String doctorPhone = object.getString("doctorPhone").trim();
                            String doctorEducation = object.getString("doctorEducation").trim();
                            String doctorExp = object.getString("doctorExp").trim();
                            String doctorHospital = object.getString("doctorHospital").trim();

//                            SessionManager sessionManager = new SessionManager(MainActivity.this);
//                            sessionManager.createLoginSession(userID,userPhoto,username,email,phone,password,gender,birthday,weight,height,familySuffered,lifestyle,bmi,smoked,alcohol,medical);

                            Toast.makeText(DoctorLoginActivity.this,message,Toast.LENGTH_SHORT).show();
                            // Start activity dashboard
                            startActivity(new Intent(DoctorLoginActivity.this,DoctorMainActivity.class));
                        }

                    }
                    if (success.equals("0")) {

                        Toast.makeText(DoctorLoginActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {


                    Toast.makeText(DoctorLoginActivity.this,"Error: Sign In Unsuccessfully",Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(DoctorLoginActivity.this,"Error: Sign In Unsuccessfully",Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("doctorEmail",doctorEmail);
                params.put("doctorPassword",doctorPassword);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent(DoctorLoginActivity.this, UserOptionActivity.class);
        startActivity(intent);
        finish();
    }
}