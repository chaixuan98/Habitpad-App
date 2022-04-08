package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.provider.CalendarContract;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SignUp2ndClass extends AppCompatActivity {

    private ImageView backBtn;
    private MaterialButton next;
    private TextView  loginText, age, bmi_tv;
    private RadioGroup rgGender;
    private RadioButton gender;
    private EditText birth;
    private TextInputLayout weight,height;
    private DatePickerDialog.OnDateSetListener setListener;

    private RadioGroup rgFamilySuffered;
    private RadioButton familySuffered;

    private String calculation, ageS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up2nd_class);

        rgGender = findViewById(R.id.radioGroupGender);
        backBtn = findViewById(R.id.signup_back_btn);
        next = findViewById(R.id.nextbtn);
        loginText = findViewById(R.id.signin_textView);
        birth = findViewById(R.id.userbirthday);

        weight = findViewById(R.id.weightlayout);
        height = findViewById(R.id.heightlayout);
       //bmi_tv = findViewById(R.id.userBMI);

        rgFamilySuffered = findViewById(R.id.radioGroupFamily);


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SignUp2ndClass.this,MainActivity.class));

            }
        });




        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SignUp2ndClass.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,
                        year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                Calendar today = Calendar.getInstance();

                month = month + 1;
                String date = day + "-" + month + "-" + year;
                birth.setText(date);


                int calage = today.get(Calendar.YEAR) -year;

                if (today.get(Calendar.DAY_OF_YEAR) < day){
                    calage--;
                }

                Integer ageInt = new Integer(calage);
                ageS = ageInt.toString();

                //age.setText(ageS);

            }
        };

        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SignUp2ndClass.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        Calendar today = Calendar.getInstance();

                        month = month + 1;
                        String date = day + "-" + month + "-" + year;
                        birth.setText(date);


                        int calage = today.get(Calendar.YEAR) -year;

                        if (today.get(Calendar.DAY_OF_YEAR) < day){
                            calage--;
                        }

                        Integer ageInt = new Integer(calage);
                        ageS = ageInt.toString();

                        //age.setText(ageS);

                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });



        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //Intent intent1 = new Intent(getApplicationContext(),SignUp3rdClass.class);
                switch (checkedId){
                    case R.id.rb_Male:
                        gender = findViewById(checkedId);

                    case R.id.rb_Female:
                        gender = findViewById(checkedId);


                }
            }
        });

        rgFamilySuffered.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.family_Yes:
                        familySuffered = findViewById(checkedId);

                    case R.id.family_No:
                        familySuffered = findViewById(checkedId);
                }
            }
        });

    }

    public void callNextSignupScreen(View view) {

        if(!validateGender() | !validateBirthday() | !validateWeight() | !validateHeight()){
            return;
        }

        Intent intent = new Intent(getApplicationContext(),SignUp3rdClass.class);


        String username = getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("email");
        String phone = getIntent().getStringExtra("phone");
        String password = getIntent().getStringExtra("password");


        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("phone", phone);
        intent.putExtra("password", password);
        intent.putExtra("gender", gender.getText().toString().trim());
        intent.putExtra("age", ageS);
        intent.putExtra("weight", weight.getEditText().getText().toString().trim());
        intent.putExtra("height", height.getEditText().getText().toString().trim());
        intent.putExtra("familySuffered", familySuffered.getText().toString().trim());


        String S1 = weight.getEditText().getText().toString();
        String S2 = height.getEditText().getText().toString();

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);

        float weightValue = Float.parseFloat(S1);
        float heightValue = Float.parseFloat(S2)/100;

        float bmi = weightValue / (heightValue * heightValue);
        calculation = df.format(bmi);
        //bmi_tv.setText(calculation);
        intent.putExtra("bmi", calculation);


        //Add Transition
        Pair[] pairs = new Pair[3];

        pairs[0] = new Pair<View,String>(backBtn,"transition_back_arrow_btn");
        pairs[1] = new Pair<View,String>(next,"transition_next_btn");
        pairs[2] = new Pair<View,String>(loginText,"transition_login_text");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp2ndClass.this,pairs);
        startActivity(intent, options.toBundle());

    }

    private  boolean validateGender(){
        if(rgGender.getCheckedRadioButtonId() == -1){
            Toast.makeText(SignUp2ndClass.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
            return false;
        }  else{
            return true;
        }
    }

    private  boolean validateBirthday(){
        String val = birth.getText().toString().trim();

        if(val.isEmpty()){
            birth.setError("Field cannot be empty");
            return false;
        } else{
            birth.setError(null);
           // birth.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validateWeight(){
        String val = weight.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            weight.setError("Field cannot be empty");
            return false;
        } else{
            weight.setError(null);
            weight.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validateHeight(){
        String val = height.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            height.setError("Field cannot be empty");
            return false;
        } else{
            height.setError(null);
            height.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validateFamilySuffered(){
        if(rgFamilySuffered.getCheckedRadioButtonId() == -1){
            Toast.makeText(SignUp2ndClass.this, "Please Select Family Suffered Obesity", Toast.LENGTH_SHORT).show();
            return false;
        }  else{
            return true;
        }
    }


    public void backSignupPage(View view) {
        Intent intent = new Intent(getApplicationContext(),SignUp.class);
        startActivity(intent);
    }
}