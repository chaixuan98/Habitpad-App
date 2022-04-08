package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RetryPolicy;
import com.google.android.material.button.MaterialButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUp4ndClass extends AppCompatActivity {

    private ImageView backBtn;
    private MaterialButton finish;
    private TextView loginText;
    private RadioGroup rgSmoked, rgAlcohol ,rgMedical;
    private RadioButton smoked, alcohol, medical;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up4nd_class);

        Intent intent = getIntent();

        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phone");
        String password = intent.getStringExtra("password");
        String gender = intent.getStringExtra("gender");
        String age = intent.getStringExtra("age");
        String weight = intent.getStringExtra("weight");
        String height = intent.getStringExtra("height");
        String familySuffered = intent.getStringExtra("familySuffered");
        String lifestyle = intent.getStringExtra("lifestyle");
        String bmi = intent.getStringExtra("bmi");

        rgMedical = findViewById(R.id.radioGroupMedical);
        rgSmoked = findViewById(R.id.radioGroupSmoked);
        rgAlcohol = findViewById(R.id.radioGroupAlcohol);
        finish = findViewById(R.id.finishbtn);
        loginText = findViewById(R.id.signin_textView);


        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SignUp4ndClass.this,MainActivity.class));

            }
        });
        rgSmoked.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.smoked_Yes:
                        smoked = findViewById(checkedId);

                    case R.id.smoked_No:
                        smoked = findViewById(checkedId);

                }
            }
        });
        rgAlcohol.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.alcohol_No:
                        alcohol = findViewById(checkedId);

                    case R.id.alcohol_sometime:
                        alcohol = findViewById(checkedId);

                    case R.id.alcohol_frequently:
                        alcohol = findViewById(checkedId);
                }
            }
        });

        rgMedical.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_None:
                        medical = findViewById(checkedId);

                    case R.id.rb_Diabetes:
                        medical = findViewById(checkedId);

                    case R.id.rb_Cholesterol:
                        medical = findViewById(checkedId);

                    case R.id.rb_Hypertension:
                        medical = findViewById(checkedId);

                    case R.id.rb_Other:
                        medical = findViewById(checkedId);

                }
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!validateSmoked()|!validateFamilySuffered()|!validateMedical()){
                    return;
                }

                createUser(username,email,phone,password,gender,age,weight,height,familySuffered,lifestyle,bmi,smoked.getText().toString(),alcohol.getText().toString(),medical.getText().toString() );
                startActivity(new Intent(SignUp4ndClass.this,MainActivity.class));
            }
        });
    }

    private void createUser(final String username,
                            final String email,
                            final String phone,
                            final String password,
                            final String gender,
                            final String age,
                            final String weight,
                            final String height,
                            final String familySuffered,
                            final String lifestyle,
                            final String bmi,
                            final String smoked,
                            final String alcohol,
                            final String medical){

        // Progress
        finish.setText("Creating User...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.SIGN_UP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.i("tagconvertstr", "["+response+"]");
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");

                    if (success.equals("1")) {

                        Toast.makeText(getApplicationContext(),"Sign Up successffully! Please login again",Toast.LENGTH_SHORT).show();
                        finish.setText("Sign Up");

                    }

                } catch (JSONException e) {

                    Toast.makeText(getApplicationContext(),"Register Error!" + e.toString(),Toast.LENGTH_LONG).show();
                    finish.setText("Sign Up");

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                finish.setText("Sign Up");

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("username",username);
                params.put("email",email);
                params.put("phone",phone);
                params.put("password",password);
                params.put("gender",gender);
                params.put("age",age);
                params.put("weight",weight);
                params.put("height",height);
                params.put("familySuffered",familySuffered);
                params.put("lifestyle",lifestyle);
                params.put("bmi",bmi);
                params.put("smoked",smoked);
                params.put("alcohol",alcohol);
                params.put("medical",medical);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }



    private  boolean validateSmoked(){
        if(rgSmoked.getCheckedRadioButtonId() == -1){
            Toast.makeText(SignUp4ndClass.this, "Please Select Smoked", Toast.LENGTH_SHORT).show();
            return false;
        }  else{
            return true;
        }
    }

    private  boolean validateFamilySuffered(){
        if(rgAlcohol.getCheckedRadioButtonId() == -1){
            Toast.makeText(SignUp4ndClass.this, "Please Select Alcohol", Toast.LENGTH_SHORT).show();
            return false;
        }  else{
            return true;
        }
    }

    private  boolean validateMedical(){
        if(rgMedical.getCheckedRadioButtonId() == -1){
            Toast.makeText(SignUp4ndClass.this, "Please Select Medical Condition", Toast.LENGTH_SHORT).show();
            return false;
        }  else{
            return true;
        }
    }

    public void backSignupPage(View view) {
        Intent intent = new Intent(getApplicationContext(),SignUp3rdClass.class);
        startActivity(intent);
    }



}