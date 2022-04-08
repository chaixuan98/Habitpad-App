package com.example.habitpadapplication.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.example.habitpadapplication.HomeActivity;

import com.example.habitpadapplication.R;
import com.example.habitpadapplication.SessionManager;
import com.example.habitpadapplication.Urls;
import com.example.habitpadapplication.VolleySingleton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;

public class UserSettingsActivity extends AppCompatActivity  {

    private Spinner genderSpinner, alcoholSpinner, lifestyleSpinner, smokedSpinner, familySufferedSpinner,medicalSpinner ;
    private static final String[] genders = {" ", "Male", "Female"};
    private static final String[] alcohol = {" ", "No", "Sometimes", "Frequetly"};
    private static final String[] lifestyles = {" ", "No Active", "Lightly Active", "Moderately Active", "Very Active"};
    private static final String[] smoked = {" ", "Yes", "No"};
    private static final String[] familySuffered = {" ", "Yes", "No"};
    private static final String[] medicals = {" ", "None", "Diabetes", "Cholesterol", "Hypertension", "Others"};

    private TextInputLayout mUsername,mEmail,mPhone,mAge;
    private TextInputEditText mWeight,mHeight;
    private TextView mGender,mAlcohol, mBmi, mLifestyle, mSmoked,mFamilySuffered, mMedical;
    private MaterialButton editUserBtn;


    SessionManager sessionManager;
    String userID, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_user_settings);
        setTitle("Settings");

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> usersDetails = sessionManager.getUsersDetailFromSession();

        userID = usersDetails.get(SessionManager.KEY_USERID);
        password = usersDetails.get(SessionManager.KEY_PASSWORD);

        mUsername = findViewById(R.id.usernamelayout);
        mEmail = findViewById(R.id.useremaillayout);
        mPhone = findViewById(R.id.userphonelayout);
        mGender = findViewById(R.id.genderView);
        mAge = findViewById(R.id.useragelayout);
        mWeight = findViewById(R.id.userweight);
        mHeight = findViewById(R.id.userheight);
        mFamilySuffered = findViewById(R.id.userfamilysuffered);
        mBmi = findViewById(R.id.userbmi);
        mLifestyle = findViewById(R.id.userlifestyle);
        mSmoked = findViewById(R.id.usersmoked);
        mAlcohol = findViewById(R.id.useralcohol);
        mMedical = findViewById(R.id.usermedical);

        editUserBtn = findViewById(R.id.editUserButton);


        TextWatcher textWatcher = new TextWatcher(){
            public void afterTextChanged(Editable s){
                calcResult();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}

        };

        mWeight.addTextChangedListener(textWatcher);
        mHeight.addTextChangedListener(textWatcher);


        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserSettingsActivity.this,
                android.R.layout.simple_spinner_item, genders);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals(" ")){
                }else {
                    String itemGender = parent.getItemAtPosition(position).toString();

                    Toast.makeText(parent.getContext(),"Selected: " +itemGender, Toast.LENGTH_SHORT).show();
                    mGender.setText(itemGender);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        alcoholSpinner = (Spinner) findViewById(R.id.alcoholSpinner);
        ArrayAdapter<String> Aadapter = new ArrayAdapter<String>(UserSettingsActivity.this,
                android.R.layout.simple_spinner_item, alcohol);

        Aadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        alcoholSpinner.setAdapter(Aadapter);
        alcoholSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals(" ")){
                }else {
                    String itemAlcohol = parent.getItemAtPosition(position).toString();

                    Toast.makeText(parent.getContext(),"Selected: " +itemAlcohol, Toast.LENGTH_SHORT).show();
                    mAlcohol.setText(itemAlcohol);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lifestyleSpinner = (Spinner) findViewById(R.id.lifestyleSpinner);
        ArrayAdapter<String> ladapter = new ArrayAdapter<String>(UserSettingsActivity.this,
                android.R.layout.simple_spinner_item, lifestyles);

        ladapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lifestyleSpinner.setAdapter(ladapter);
        lifestyleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals(" ")){
                }else {
                    String itemLifestyle = parent.getItemAtPosition(position).toString();

                    Toast.makeText(parent.getContext(),"Selected: " +itemLifestyle, Toast.LENGTH_SHORT).show();
                    mLifestyle.setText(itemLifestyle);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        smokedSpinner = (Spinner) findViewById(R.id.smokeSpinner);
        ArrayAdapter<String> sadapter = new ArrayAdapter<String>(UserSettingsActivity.this,
                android.R.layout.simple_spinner_item, smoked);

        sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        smokedSpinner.setAdapter(sadapter);
        smokedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals(" ")){
                }else {
                    String itemSmoked = parent.getItemAtPosition(position).toString();

                    Toast.makeText(parent.getContext(),"Selected: " +itemSmoked, Toast.LENGTH_SHORT).show();
                    mSmoked.setText(itemSmoked);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        familySufferedSpinner = (Spinner) findViewById(R.id.familySufferedSpinner);
        ArrayAdapter<String> fadapter = new ArrayAdapter<String>(UserSettingsActivity.this,
                android.R.layout.simple_spinner_item, familySuffered);

        fadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        familySufferedSpinner.setAdapter(fadapter);
        familySufferedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals(" ")){
                }else {
                    String itemFamilySuffered = parent.getItemAtPosition(position).toString();

                    Toast.makeText(parent.getContext(),"Selected: " +itemFamilySuffered, Toast.LENGTH_SHORT).show();
                    mFamilySuffered.setText(itemFamilySuffered);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        medicalSpinner = (Spinner) findViewById(R.id.medicalSpinner);
        ArrayAdapter<String> madapter = new ArrayAdapter<String>(UserSettingsActivity.this,
                android.R.layout.simple_spinner_item, medicals);

        madapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medicalSpinner.setAdapter(madapter);
        medicalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals(" ")){
                }else {
                    String itemMedical = parent.getItemAtPosition(position).toString();

                    Toast.makeText(parent.getContext(),"Selected: " +itemMedical, Toast.LENGTH_SHORT).show();
                    mMedical.setText(itemMedical);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        editUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editUserDetails(userID,
                        mUsername.getEditText().getText().toString(),
                        mEmail.getEditText().getText().toString(),
                        mPhone.getEditText().getText().toString(),
                        password,
                        mGender.getText().toString(),
                        mAge.getEditText().getText().toString(),
                        mWeight.getText().toString(),
                        mHeight.getText().toString(),
                        mFamilySuffered.getText().toString(),
                        mLifestyle.getText().toString(),
                        mBmi.getText().toString(),
                        mSmoked.getText().toString(),
                        mAlcohol.getText().toString(),
                        mMedical.getText().toString());

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed(){

        Intent intent = new Intent(UserSettingsActivity.this, HomeActivity.class);
        startActivity(intent);

        finish();

    }


    private void calcResult() throws NumberFormatException {
        Editable editableText1 = mWeight.getText(), editableText2 = mHeight.getText();

        double  weightValue =0.0 , heightValue =0.0 , result;

        // NOTE: “&& editableText1.length() >= 1” prevents possible “invalid Double” errors!

        if (editableText1 != null && editableText1.length() >= 1)
            weightValue = Double.parseDouble(editableText1.toString());

        if (editableText2 != null && editableText2.length() >= 1)
            heightValue = Double.parseDouble(editableText2.toString())/100;

        // Whatever your magic formula is
        result = weightValue / (heightValue * heightValue);

        mBmi.setText(Double.toString(result));
        {// Displays result to 1 decimal places
            mBmi.setText(String.format("%.1f", result));
        }
    }

    private void getUserDetails() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.READ_USER_DETAILS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    Log.i("tagconvertstr", "[" + response + "]");
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    JSONArray jsonArray = jsonObject.getJSONArray("read");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String username = object.getString("username").trim();
                            String email = object.getString("email").trim();
                            String phone = object.getString("phone").trim();
                            String password = object.getString("password").trim();
                            String gender = object.getString("gender").trim();
                            String age = object.getString("age").trim();
                            String weight = object.getString("weight").trim();
                            String height = object.getString("height").trim();
                            String familySuffered = object.getString("familySuffered").trim();
                            String bmi = object.getString("bmi").trim();
                            String lifestyle = object.getString("lifestyle").trim();
                            String smoked = object.getString("smoked").trim();
                            String alcohol = object.getString("alcohol").trim();
                            String medical = object.getString("medical").trim();

                            mUsername.getEditText().setText(username);
                            mEmail.getEditText().setText(email);
                            mPhone.getEditText().setText(phone);
                            mGender.setText(gender);
                            mAge.getEditText().setText(age);
                            mWeight.setText(weight);
                            mHeight.setText(height);
                            mFamilySuffered.setText(familySuffered);
                            mBmi.setText(bmi);
                            mLifestyle.setText(lifestyle);
                            mSmoked.setText(smoked);
                            mAlcohol.setText(alcohol);
                            mMedical.setText(medical);


                        }
                    }
                    if (success.equals("0")) {

                        Toast.makeText(UserSettingsActivity.this, "Read error", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(UserSettingsActivity.this, "Read error" + e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(UserSettingsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID", userID);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void editUserDetails(final String userID,
                                 final String username,
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

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.UPDATE_USER_DETAILS_URL, new Response.Listener<String>()  {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    Log.i("tagconvertstr", "["+response+"]");
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");

                    if (success.equals("1")) {
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                        sessionManager.createLoginSession(userID,username,email,phone,password,gender,age,weight,height,familySuffered,lifestyle,bmi,smoked,alcohol,medical);
                    }

                    if (success.equals("0")) {
                        Toast.makeText(UserSettingsActivity.this, "Edit error", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Edit Error!",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(UserSettingsActivity.this, error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("userID",userID);
                params.put("username",username);
                params.put("email",email);
                params.put("phone",phone);
                params.put("password",password);
                params.put("gender",gender);
                params.put("age",age);
                params.put("weight",weight);
                params.put("height",height);
                params.put("lifestyle",lifestyle);
                params.put("bmi",bmi);
                params.put("smoked",smoked);
                params.put("alcohol",alcohol);
                params.put("familySuffered",familySuffered);
                params.put("medical",medical);

                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }




    @Override
    protected void onResume(){
        super.onResume();
        getUserDetails();

    }

}

