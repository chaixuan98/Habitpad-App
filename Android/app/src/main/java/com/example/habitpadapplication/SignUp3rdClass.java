package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;

public class SignUp3rdClass extends AppCompatActivity implements View.OnClickListener{

    private Button no_active,lightly_active,moderately_active, very_active;
    private ImageView backBtn;
    private TextView loginText;
    private MaterialButton next;
    private String lifestyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up3rd_class);

        loginText = findViewById(R.id.signin_textView);
        backBtn = findViewById(R.id.signup_back_btn);
        next = findViewById(R.id.nextbtn);
        no_active = findViewById(R.id.noactive_btn);
        lightly_active = findViewById(R.id.lightly_btn);
        moderately_active = findViewById(R.id.moderately_btn);
        very_active = findViewById(R.id.active_btn);

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SignUp3rdClass.this,MainActivity.class));

            }
        });



        no_active.setOnClickListener(this);
        lightly_active.setOnClickListener(this);
        moderately_active.setOnClickListener(this);
        very_active.setOnClickListener(this);

    }
//
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.noactive_btn:
                no_active.setBackgroundColor(Color.BLUE);
                lightly_active.setBackgroundColor(Color.GRAY);
                moderately_active.setBackgroundColor(Color.GRAY);
                very_active.setBackgroundColor(Color.GRAY);
                //lifestyle = no_active.getText().toString().trim();

                lifestyle = "No Active";
                Toast.makeText(this,lifestyle,Toast.LENGTH_SHORT).show();

                break;

            case R.id.lightly_btn:
                no_active.setBackgroundColor(Color.GRAY);
                lightly_active.setBackgroundColor(Color.BLUE);
                moderately_active.setBackgroundColor(Color.GRAY);
                very_active.setBackgroundColor(Color.GRAY);
                //lifestyle = lightly_active.getText().toString().trim();

                lifestyle = "Lightly Active";
                Toast.makeText(this,lifestyle,Toast.LENGTH_SHORT).show();
                break;

            case R.id.moderately_btn:
                no_active.setBackgroundColor(Color.GRAY);
                lightly_active.setBackgroundColor(Color.GRAY);
                moderately_active.setBackgroundColor(Color.BLUE);
                very_active.setBackgroundColor(Color.GRAY);
                //lifestyle = lightly_active.getText().toString().trim();

                lifestyle = "Moderately Active";
                Toast.makeText(this,lifestyle,Toast.LENGTH_SHORT).show();
                break;

            case R.id.active_btn:
                no_active.setBackgroundColor(Color.GRAY);
                lightly_active.setBackgroundColor(Color.GRAY);
                moderately_active.setBackgroundColor(Color.GRAY);
                very_active.setBackgroundColor(Color.BLUE);
                //lifestyle = active.getText().toString().trim();

                lifestyle = "Very Active";
                Toast.makeText(this,lifestyle,Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void callNextSignupScreen(View view) {

        if(!validateLifestyle()){
            return;
        }

        Intent intent = new Intent(getApplicationContext(),SignUp4ndClass.class);

        String username = getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("email");
        String phone = getIntent().getStringExtra("phone");
        String password = getIntent().getStringExtra("password");
        String gender = getIntent().getStringExtra("gender");
        String age = getIntent().getStringExtra("age");
        String startWeight = getIntent().getStringExtra("startWeight");
        String weight = getIntent().getStringExtra("weight");
        String height = getIntent().getStringExtra("height");
        String goalWeight = getIntent().getStringExtra("goalWeight");
        String weeklyGoalWeight = getIntent().getStringExtra("weeklyGoalWeight");
        String goalStartDate = getIntent().getStringExtra("goalStartDate");
        String bmi = getIntent().getStringExtra("bmi");

        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("phone", phone);
        intent.putExtra("password", password);
        intent.putExtra("gender", gender);
        intent.putExtra("age", age);
        intent.putExtra("startWeight", startWeight);
        intent.putExtra("weight", weight);
        intent.putExtra("height", height);
        intent.putExtra("goalWeight", goalWeight);
        intent.putExtra("weeklyGoalWeight", weeklyGoalWeight);
        intent.putExtra("goalStartDate", goalStartDate);
        intent.putExtra("bmi", bmi);
        intent.putExtra("lifestyle", lifestyle);


        //Add Transition
        Pair[] pairs = new Pair[3];

        pairs[0] = new Pair<View,String>(backBtn,"transition_back_arrow_btn");
        pairs[1] = new Pair<View,String>(next,"transition_next_btn");
        pairs[2] = new Pair<View,String>(loginText,"transition_login_text");


        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp3rdClass.this,pairs);
        startActivity(intent, options.toBundle());

    }

    private  boolean validateLifestyle(){
        if(lifestyle.isEmpty()){
            Toast.makeText(SignUp3rdClass.this, "Please Select Lifestyle", Toast.LENGTH_SHORT).show();
            return false;
        }  else{
            return true;
        }
    }

    public void backSignupPage(View view) {
        Intent intent = new Intent(getApplicationContext(),SignUp2ndClass.class);
        startActivity(intent);
    }


}