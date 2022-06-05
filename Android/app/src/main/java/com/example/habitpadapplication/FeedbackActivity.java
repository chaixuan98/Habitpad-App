package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.habitpadapplication.Settings.AccountSettingActivity;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FeedbackActivity extends AppCompatActivity {

    private TextInputLayout feedbackLayout;
    private Button feedbackSubmitBtn;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Feedback");
        setContentView(R.layout.activity_feedback);

        userID = getIntent().getExtras().getString("intentUserID");

        feedbackLayout = findViewById(R.id.feedbacklayout);
        feedbackSubmitBtn = findViewById(R.id.feedback_submit_button);

        feedbackSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateFeedback()){
                    return;
                }

                AddFeedback(userID, feedbackLayout.getEditText().getText().toString(), DateHandler.getCurrentFormedDate());

            }
        });

    }

    private void AddFeedback(final String userID, final String feedback, final String date)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.ADD_FEEDBACK_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {
                                Toast.makeText(FeedbackActivity.this,"Your feedback is submitted. Thank You!",Toast.LENGTH_SHORT).show();
                                //Log.i("tagtoast", "["+message+"]");
                            }

                            if (success.equals("0")) {
                                //Toast.makeText(HomeActivity.this,message,Toast.LENGTH_SHORT).show();
                                Log.i("tagtoast", "["+message+"]");
                            }

                        } catch (JSONException e) {
                            Toast.makeText(FeedbackActivity.this,"Insert user feedback Error!" + e.toString(),Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("tagerror", "["+error+"]");
                        //Toast.makeText(HomeActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userID", userID);
                params.put("feedbackDetail",feedback);
                params.put("feedbackDate",date);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private  boolean validateFeedback(){
        String val = feedbackLayout.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            feedbackLayout.setError("Field cannot be empty");
            return false;
        } else{
            feedbackLayout.setError(null);
            feedbackLayout.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent(FeedbackActivity.this, AccountSettingActivity.class);
        intent.putExtra("intentUserID", userID);
        startActivity(intent);
        finish();
    }
}