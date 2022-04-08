package com.example.habitpadapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class SignUp extends AppCompatActivity {

    private TextInputLayout username, email, phone, password;
    private ImageView backBtn;
    private MaterialButton next;
    private TextView  loginText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        //Hooks for getting data

        username = findViewById(R.id.usernamelayout);
        email = findViewById(R.id.useremaillayout);
        phone = findViewById(R.id.userphonelayout);
        password = findViewById(R.id.userpasswordlayout);

        //Hooks for animation
        backBtn = findViewById(R.id.signup_back_btn);
        next = findViewById(R.id.nextbtn);
        loginText = findViewById(R.id.signin_textView);

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SignUp.this,MainActivity.class));

            }
        });

    }


    public void callNextSignupScreen(View view) {

        if(!validateUsername() | !validateEmail() | !validatePhone() | !validatePassword()){
            return;
        }

        Intent intent = new Intent(getApplicationContext(),SignUp2ndClass.class);

        intent.putExtra("username", username.getEditText().getText().toString().trim());
        intent.putExtra("email", email.getEditText().getText().toString().trim());
        intent.putExtra("phone", phone.getEditText().getText().toString().trim());
        intent.putExtra("password", password.getEditText().getText().toString().trim());


        //Add Transition
        Pair[] pairs = new Pair[3];

        pairs[0] = new Pair<View,String>(backBtn,"transition_back_arrow_btn");
        pairs[1] = new Pair<View,String>(next,"transition_next_btn");
        pairs[2] = new Pair<View,String>(loginText,"transition_login_text");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this,pairs);
        startActivity(intent, options.toBundle());


    }

    //Validation Functions

    private  boolean validateUsername(){
        String val = username.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";

        if(val.isEmpty()){
            username.setError("Field cannot be empty");
            return false;
        } else if(val.length()>20){
            username.setError("Username is too large");
            return false;
        } else if(!val.matches(checkspaces)){
            username.setError("No White spaces are allowed");
            return false;
        } else{
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validateEmail(){
        String val = email.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()){
            email.setError("Field cannot be empty");
            return false;
        } else if(!val.matches(checkEmail)){
            email.setError("Invalid Email!");
            return false;
        } else{
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validatePhone() {
        String val = phone.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            phone.setError("Field cannot be empty");
            return false;
        } else {
            phone.setError(null);
            phone.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validatePassword(){
        String val = password.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            password.setError("Field cannot be empty");
            return false;
        } else{
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    public void backSignupPage(View view) {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
}