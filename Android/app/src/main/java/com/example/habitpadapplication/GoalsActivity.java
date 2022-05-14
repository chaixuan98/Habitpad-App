package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GoalsActivity extends AppCompatActivity {

    private ProgressDialog loadingBar;

    private Button GoalSetButton;
    private TextView GoalCurrentWeight, GoalGoalWeight, GoalWeeklyGoalWeight, GoalStartWeight, GoalStartDate, GoalRemainingWeight,
            GoalCutCalories, GoalCompleteDetails, GoalTitle;
    private LinearLayout GoalCompleteContainer, GoalDetailsContainer;

    private String currentWeight="", goalWeight="", weeklyGoalWeight="", startWeight, startDate, remainingWeight, cutCalories;
    private String goalStatus="", achievementStatus="";
    private String intentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Goal Setting");

        intentUserID = getIntent().getExtras().getString("intentUserID");

        GoalCurrentWeight = findViewById(R.id.goal_current_weight);
        GoalGoalWeight = findViewById(R.id.goal_goal_weight);
        GoalWeeklyGoalWeight = findViewById(R.id.goal_goal_weight_per_week);
        GoalStartWeight = findViewById(R.id.goal_goal_start_weight);
        GoalStartDate = findViewById(R.id.goal_goal_start_date);
        GoalRemainingWeight = findViewById(R.id.goal_remaining_weight);
        GoalCutCalories = findViewById(R.id.goal_cut_calories);
        GoalSetButton = findViewById(R.id.goal_goal_set_button);
        GoalDetailsContainer = findViewById(R.id.goal_goal_details_container);
        GoalCompleteContainer = findViewById(R.id.goal_goal_complete_container);
        GoalCompleteContainer.setVisibility(View.GONE);
        GoalCompleteDetails = findViewById(R.id.goal_goal_complete_details);
        GoalTitle = findViewById(R.id.goal_goal_title);


        GoalSetButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(currentWeight.isEmpty())
                {
                    Toast.makeText(GoalsActivity.this, "Please setup your profile for set goal", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    PopupSetGoalDialog();
                }
            }
        });

        getUserDetails();

    }

    private void PopupSetGoalDialog()
    {
        final Dialog setGoalDialog = new Dialog(this);
        setGoalDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setGoalDialog.setContentView(R.layout.set_goal_layout);
        setGoalDialog.setTitle("Set Goal Dialog");
        setGoalDialog.show();
        Window window = setGoalDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        final EditText weightInput = setGoalDialog.findViewById(R.id.set_goal_goal_weight_input);
        final EditText weeklyWeightInput = setGoalDialog.findViewById(R.id.set_goal_goal_weight_per_week_input);
        final TextView error = setGoalDialog.findViewById(R.id.set_goal_dialog_error);
        error.setVisibility(View.GONE);

        weeklyWeightInput.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog weightdialog = new Dialog(GoalsActivity.this);
                weightdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                weightdialog.setContentView(R.layout.weekly_number_picker_dialog);
                weightdialog.setTitle("Weekly goal weight");
                weightdialog.show();
                Window weightWindow = weightdialog.getWindow();
                weightWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                final NumberPicker weeklyWeightNumberPicker= (NumberPicker) weightdialog.findViewById(R.id.weekly_weight_numberPicker);
                TextView weeklyGoalTV = weightdialog.findViewById(R.id.weekly_goal_weight_tv);
                final int weeklyWeightNumberPickerValue;
                final String[] weeklyWeightValue;


                weeklyWeightValue= getResources().getStringArray(R.array.weekly_goal_weight);
                weeklyWeightNumberPicker.setMaxValue(2);
                weeklyWeightNumberPicker.setMinValue(0);
                weeklyWeightNumberPicker.setWrapSelectorWheel(false);
                weeklyWeightNumberPicker.setDisplayedValues(weeklyWeightValue);
                //weeklyGoalTV.setText(String.format("Weekly Goal Weight: %s",weeklyWeightNumberPicker.getValue()));

                weeklyWeightNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        weeklyGoalTV.setText(String.format("Weekly Goal Weight: %s",weeklyWeightValue[i1]));
                        weeklyWeightInput.setText(weeklyWeightValue[i1]);
                    }
                });

                /* cancel button click action */
                Button cancelbtn = (Button)weightdialog.findViewById(R.id.weekly_weight_cancel_button);
                cancelbtn.setEnabled(true);
                cancelbtn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        weightdialog.cancel();
                    }
                });


                /* ok button click action */
                Button confirmBtn = (Button)weightdialog.findViewById(R.id.weekly_weight_confirm_button);
                confirmBtn.setEnabled(true);
                confirmBtn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                        //weeklyGoalWeight = String.valueOf(weeklyWeightNumberPicker.getValue());
                        //userWeeklyGoalWeight.setText(weeklyGoalWeight);
                        weightdialog.cancel();
                    }
                });

            }
        });

        if(!goalWeight.isEmpty())
        {
            weightInput.setText(goalWeight);
        }

        if(!weeklyGoalWeight.isEmpty())
        {
            weeklyWeightInput.setText(weeklyGoalWeight);
        }

        Button cancelBtn = setGoalDialog.findViewById(R.id.set_goal_dialog_cancel_button);
        cancelBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setGoalDialog.dismiss();
            }
        });

        Button submitBtn = setGoalDialog.findViewById(R.id.set_goal_dialog_submit_button);
        submitBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String DialogWeight, DialogWeeklyWeight;

                DialogWeight = weightInput.getText().toString();
                DialogWeeklyWeight = weeklyWeightInput.getText().toString();

                if(DialogWeight.isEmpty() || DialogWeeklyWeight.isEmpty())
                {
                    error.setText("Above fields can not be empty.");
                    error.setVisibility(View.VISIBLE);
                }

                else if(Double.parseDouble(DialogWeight) >= (Double.parseDouble(currentWeight)))
                {
                    error.setText("Goal weight should be smaller than current weight.");
                    error.setVisibility(View.VISIBLE);
                }
                else if(Double.parseDouble(DialogWeeklyWeight) > 1)
                {
                    error.setText("Weekly Goal weight should be equal to 1 KG or smaller than 1 KG.");
                    error.setVisibility(View.VISIBLE);
                }
                else
                {
                    setGoalDialog.dismiss();

                    /* adding Loading bar  */
                    loadingBar = new ProgressDialog(GoalsActivity.this);
                    String ProgressDialogMessage="Setting Goal...";
                    SpannableString spannableMessage=  new SpannableString(ProgressDialogMessage);
                    spannableMessage.setSpan(new RelativeSizeSpan(1.3f), 0, spannableMessage.length(), 0);
                    loadingBar.setMessage(spannableMessage);
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.setCancelable(false);

                    UpdateUserGoalWeight(intentUserID,currentWeight,DialogWeight,DialogWeeklyWeight,DateHandler.getCurrentFormedDate());
                    UpdateUserWeightAchievement(intentUserID,"false");
                    getUserDetails();

                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            loadingBar.cancel();
                        }
                    },500);
                }
            }
        });
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


                            startWeight = object.getString("startWeight").trim();
                            currentWeight = object.getString("weight").trim();
                            goalWeight = object.getString("goalWeight").trim();
                            weeklyGoalWeight = object.getString("weeklyGoalWeight").trim();
                            startDate = object.getString("goalStartDate").trim();

                            GoalStartWeight.setText(startWeight+" KG");
                            GoalCurrentWeight.setText(currentWeight+" KG");
                            GoalGoalWeight.setText(goalWeight+" KG");
                            GoalWeeklyGoalWeight.setText(weeklyGoalWeight+" KG Per Week");
                            GoalStartDate.setText(startDate);

                            if(!goalWeight.isEmpty())
                            {
                                remainingWeight = String.format("%.1f",(Double.parseDouble(currentWeight) - Double.parseDouble(goalWeight)));
                                GoalRemainingWeight.setText(remainingWeight+" KG");

                                cutCalories = String.format("%.0f",((Double.parseDouble(weeklyGoalWeight) * 3500) / 0.45));
                                GoalCutCalories.setText(cutCalories+" Per Week");
                            }
                            else
                            {
                                GoalDetailsContainer.setVisibility(View.GONE);
                                GoalCompleteContainer.setVisibility(View.VISIBLE);
                                GoalTitle.setText("Weight Loss Goal");
                                GoalCompleteDetails.setText("Set a goal");
                            }

                            getUserWeightAchievemnt();

                        }
                    }
                    if (success.equals("0")) {

                        Toast.makeText(GoalsActivity.this, "Read error", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(GoalsActivity.this, "Read error" + e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(GoalsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID", intentUserID);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void getUserWeightAchievemnt()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_WEIGHT_ACHIEVEMENT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.i("tagconvertstr", "[" + response + "]");
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    JSONArray jsonArray = jsonObject.getJSONArray("weightAchieve");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            goalStatus = object.getString("weightLossGoalStatus").trim();
                            achievementStatus = object.getString("weightLossAchievementStatus").trim();


                            if(goalStatus.equals("true") && achievementStatus.equals("true"))
                            {
                                GoalTitle.setText("Achievement");
                                GoalDetailsContainer.setVisibility(View.GONE);
                                GoalCompleteContainer.setVisibility(View.VISIBLE);
                                GoalCompleteDetails.setText("Congratulations, You have achieved your previous weight loss goal." +
                                        "\n\n Start Weight : "+startWeight+" KG " +
                                        "\n " + "Start Date : "+startDate+"" +
                                        "\n Goal Weight : "+goalWeight+" KG");

                                GoalSetButton.setText("Set New Goal");
                            }
                            else
                            {
                                GoalDetailsContainer.setVisibility(View.VISIBLE);
                                GoalCompleteContainer.setVisibility(View.GONE);
                                GoalSetButton.setText("Change Goal");
                            }


                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(GoalsActivity.this, "Read error" + e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GoalsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID", intentUserID);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void UpdateUserGoalWeight(final String intentUserID,final String startWeight,final String goalWeight,final String weeklyGoalWeight,final String goalStartDate){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.UPDATE_USER_GOAL_WEIGHT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.i("tagerror", "["+error+"]");
                Toast.makeText(GoalsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",intentUserID);
                params.put("startWeight",startWeight);
                params.put("goalWeight",goalWeight);
                params.put("weeklyGoalWeight",weeklyGoalWeight);
                params.put("goalStartDate",goalStartDate);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);



    }

    private void UpdateUserWeightAchievement(final String intentUserID, final String weightLossAchievementStatus){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.UPDATE_USER_WEIGHT_ACHIEVEMENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.i("tagerror", "["+error+"]");
                Toast.makeText(GoalsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",intentUserID);
                params.put("weightLossAchievementStatus",weightLossAchievementStatus);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent(GoalsActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}