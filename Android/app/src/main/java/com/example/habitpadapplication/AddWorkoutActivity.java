package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddWorkoutActivity extends AppCompatActivity {

    private String intentFrom, workoutID, intentUserID,intentUserGender,intentUserAge,intentUserLifestyle,intentUserWeight,intentUserHeight;

    private ProgressDialog loadingbar;

    private TextView workoutName, workoutMET, workoutDuration, workoutCalories, durationError;
    private Button addbtn;
    private LinearLayout workoutDurationContainer;

    private String workoutDurationValue="0", workoutNameValue, workoutMETValue, workoutCaloriesValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Add Workout");
        setContentView(R.layout.activity_add_workout);

        Intent intent = getIntent();
        workoutID = intent.getExtras().getString("workoutID");
        intentUserID = intent.getExtras().getString("intentUserID");
        intentUserGender = intent.getExtras().getString("intentUserGender");
        intentUserAge = intent.getExtras().getString("intentUserAge");
        intentUserLifestyle = intent.getExtras().getString("intentUserLifestyle");
        intentUserWeight = intent.getExtras().getString("intentUserWeight");
        intentUserHeight = intent.getExtras().getString("intentUserHeight");


        workoutName = (TextView) findViewById(R.id.addworkout_workoutname);
        workoutMET = (TextView) findViewById(R.id.addworkout_MET);
        workoutDuration = (TextView) findViewById(R.id.add_workout_duration);
        workoutCalories = (TextView) findViewById(R.id.addworkout_calories);
        workoutDurationContainer = findViewById(R.id.add_food_workout_duration_container);
        durationError = findViewById(R.id.duration_error);

        addbtn = (Button) findViewById(R.id.addworkout_addbutton);

        loadingbar = new ProgressDialog(this);

        if(!TextUtils.isEmpty(workoutID))
        {
            GetAndSetWorkoutDetails();
        }

        addbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(TextUtils.isEmpty(workoutDuration.getText().toString()) || Double.parseDouble(workoutDuration.getText().toString()) <= 0)
                {
                    durationError.setVisibility(View.VISIBLE);
                }
                if(!TextUtils.isEmpty(workoutDuration.getText().toString()) && Double.parseDouble(workoutDuration.getText().toString()) > 0){
                    durationError.setVisibility(View.GONE);
                    AddWorkoutoUserDiary(DateHandler.getCurrentFormedDate(),DateHandler.getCurrentTime());
                }
            }
        });

        workoutDurationContainer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PopupWorkoutDurationEditDialog();
            }
        });

    }

    /* toolbar back button click action */
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }


    private void PopupWorkoutDurationEditDialog()
    {
        final Dialog workoutDurationEditDialog = new Dialog(this);
        workoutDurationEditDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        workoutDurationEditDialog.setContentView(R.layout.workout_duration_edit_layout);
        workoutDurationEditDialog.setTitle("Workout Duration Edit Window");
        workoutDurationEditDialog.show();
        Window workoutDurationEditDialogWindow = workoutDurationEditDialog.getWindow();
        workoutDurationEditDialogWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        final EditText numberOfDuraionInput = (EditText) workoutDurationEditDialog.findViewById(R.id.number_of_duraton_dialog_input);
        final TextView errorMsg = (TextView) workoutDurationEditDialog.findViewById(R.id.number_of_duration_dialog_error);
        errorMsg.setVisibility(View.GONE);


        /* cancel button click action */
        Button cancelBtn = (Button) workoutDurationEditDialog.findViewById(R.id.number_of_duration_dialog_cancel_button);
        cancelBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                workoutDurationEditDialog.cancel();
            }
        });


        /* submit button click action */
        Button submitBtn = (Button)workoutDurationEditDialog.findViewById(R.id.number_of_duration_dialog_submit_button);
        submitBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(TextUtils.isEmpty(numberOfDuraionInput.getText().toString()) || Double.parseDouble(numberOfDuraionInput.getText().toString()) <= 0)
                {
                    errorMsg.setVisibility(View.VISIBLE);
                }
                else
                {
                    workoutDurationValue = numberOfDuraionInput.getText().toString().trim();
                    workoutDurationEditDialog.cancel();
                    NewValueCalculation();
                }
            }
        });
    }

    private void NewValueCalculation()
    {
        workoutDuration.setText(workoutDurationValue);

        String newCaloriesValue = String.format("%.0f",(Double.parseDouble(workoutDurationValue) * 3.5 * Double.parseDouble(workoutMETValue)* Double.parseDouble(intentUserWeight))/200 );
        workoutCalories.setText(newCaloriesValue);

    }

    private void GetAndSetWorkoutDetails(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_WORKOUT_DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("workoutdata");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);


                                    workoutNameValue = object.getString("workoutType").trim();
                                    workoutMETValue = object.getString("workoutMET").trim();

                                    workoutName.setText(workoutNameValue);
                                    workoutMET.setText("MET: " + workoutMETValue);

                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddWorkoutActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("workoutID",workoutID);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }

    private void AddWorkoutoUserDiary(String date, String time) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.ADD_USER_WORKOUT_DETAIL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equals("OK")) {
                                Toast.makeText(AddWorkoutActivity.this,message,Toast.LENGTH_SHORT).show();
                                UserSendToDiaryPage();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(AddWorkoutActivity.this,"Save Error!" + e.toString(),Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddWorkoutActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userID", intentUserID);
                params.put("workoutID",workoutID);
                params.put("numberOfDuration", workoutDurationValue);
                params.put("addWorkoutDate", date);
                params.put("addWorkouTime", time);


                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void UserSendToDiaryPage()
    {
        Intent diaryIntent = new Intent(AddWorkoutActivity.this, DiaryActivity.class);
        diaryIntent.putExtra("intentFrom", "AddWorkoutActivity");
        diaryIntent.putExtra("intentUserID", intentUserID);
        diaryIntent.putExtra("intentUserGender", intentUserGender);
        diaryIntent.putExtra("intentUserAge", intentUserAge);
        diaryIntent.putExtra("intentUserLifestyle", intentUserLifestyle);
        diaryIntent.putExtra("intentUserWeight", intentUserWeight);
        diaryIntent.putExtra("intentUserHeight", intentUserHeight);
        startActivity(diaryIntent);
        finish();
    }
}