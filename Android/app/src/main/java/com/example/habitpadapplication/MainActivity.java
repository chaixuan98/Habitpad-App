package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    // Variable declarations
    private TextInputLayout mEmail, mPassword;
    private TextView mSignUp;
    private MaterialButton mSignIn;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);

        // Getting UI views from our xml file
        mEmail = findViewById(R.id.emaillayout);
        mPassword = findViewById(R.id.passwordlayout);
        mSignIn = findViewById(R.id.signinbtn);
        mSignUp = findViewById(R.id.signup_textView);


        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,SignUp.class));

            }
        });


        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validateEmail() | !validatePassword()){
                    return;
                }

                signIn(mEmail.getEditText().getText().toString(), mPassword.getEditText().getText().toString());

            }
        });

    }


    private void signIn( final String email, final String password) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.SIGN_IN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.i("tagconvertstr", "["+response+"]");
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    JSONArray jsonArray = jsonObject.getJSONArray("login");

                    if (success.equals("1")) {
                        for (int i =0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            String userID = object.getString("userID").trim();
                            String userPhoto = object.getString("userPhoto").trim();
                            String username = object.getString("username").trim();
                            String email = object.getString("email").trim();
                            String phone = object.getString("phone").trim();
                            String password = object.getString("password").trim();
                            String gender = object.getString("gender").trim();
                            String birthday = object.getString("age").trim();
                            String weight = object.getString("weight").trim();
                            String height = object.getString("height").trim();
                            String familySuffered = object.getString("familySuffered").trim();
                            String lifestyle = object.getString("lifestyle").trim();
                            String bmi = object.getString("bmi").trim();
                            String smoked = object.getString("smoked").trim();
                            String alcohol = object.getString("alcohol").trim();
                            String medical = object.getString("medical").trim();

                            SessionManager sessionManager = new SessionManager(MainActivity.this);
                            sessionManager.createLoginSession(userID,userPhoto,username,email,phone,password,gender,birthday,weight,height,familySuffered,lifestyle,bmi,smoked,alcohol,medical);

                            Toast.makeText(MainActivity.this,"Success Login!! \n Welcome ",Toast.LENGTH_SHORT).show();
                            // Start activity dashboard
                            startActivity(new Intent(MainActivity.this,HomeActivity.class));
                        }

                    }
                    if (success.equals("0")) {

                        Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {


                    Toast.makeText(MainActivity.this,"Error: Sign In Unsuccessfully",Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this,"Error: Sign In Unsuccessfully",Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("email",email);
                params.put("password",password);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private  boolean validateEmail(){
        String val = mEmail.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()){
            mEmail.setError("Field cannot be empty");
            return false;
        } else if(!val.matches(checkEmail)){
            mEmail.setError("Invalid Email!");
            return false;
        } else{
            mEmail.setError(null);
            mEmail.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validatePassword(){
        String val = mPassword.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            mPassword.setError("Field cannot be empty");
            return false;
        } else{
            mPassword.setError(null);
            mPassword.setErrorEnabled(false);
            return true;
        }
    }

    public void backUserOptionPage(View view) {
        startActivity(new Intent(MainActivity.this,UserOptionActivity.class));
    }
}