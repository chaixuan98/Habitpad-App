package com.example.habitpadapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.habitpadapplication.Chart.FoodChart;
import com.example.habitpadapplication.Chart.ObeseLevelChart;
import com.example.habitpadapplication.Chart.WaterChart;
import com.example.habitpadapplication.Chart.WorkoutChart;
import com.example.habitpadapplication.Settings.FoodFragmentPrefs;
import com.example.habitpadapplication.Settings.FragmentPrefs;
import com.example.habitpadapplication.Settings.UserSettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Variable declarations
    SessionManager sessionManager;
    FoodFragmentPrefs foodFragmentPrefs;
    FragmentPrefs fragmentPrefs;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    private String urlClassify = "https://obese-classify.herokuapp.com/predict";

    private CircleImageView profileView;

    private TextView foodCardConsumedCalories,foodGoalCalories,workoutGoalCalories,workoutCardBurnedCalories,waterCardIntake,waterCardNeed ;

    private LinearLayout foodCardBreakfastBtn, foodCardLunchBtn, foodCardDinnerBtn, foodCardSnackBtn;
    private Button diaryBtn;
    private ImageButton foodChartBtn,workoutAddBtn,workoutChartBtn,waterAddBtn,waterChartBtn, reminderBtn, obeseLevelChartBtn;


    private ProgressBar summaryCardCaloriesBar;
    private TextView summaryCardFinalCalories, summaryCardTDEE01, summaryCardTDEE02, summaryCardFoodCalories, summaryCardWorkoutCalories,
            summaryCardCaloriesRemaining, summaryCardTips, summaryCardObesity;
    private int summaryCardCaloriesBarValue;


    private String strUserCurrentWeight, strUserGoalWeight, strUserWeeklyGoalWeight, strUserStartWeight, strUserAge, strUserHeight,
            strUserGender, strUserActivityLevel, strUserFoodCalories="0", strUserWorkout="0",strUserFinalCalories, strUserFoodGoal,strUserWorkoutGoal,
            strUserTDEE, strUserCaloriesRemaining, strUserBmi, strUserHealthStatus, strFamilySuffered, strUserAlcohol, strUserSmoked;


    private Button weightCardGoalBtn, weightCardRecordBtn;
    private TextView weightCardCurrentWeight, weightCardGoalWeight, weightCardGoalCompleteDescription;
    private ImageButton weightCardWeightMinusBtn, weightCardWeightAddBtn;
    private ProgressBar weightCardGoalProgress;
    private LinearLayout weightCardGoalContainer;

    private TextView bmiCardBmi, bmiCardTips;
    private ImageView bmiCardUnderWeightStatus, bmiCardHealthyWeightStatus, bmiCardOverWeightStatus, bmiCardObesityStatus;

    private String strUserWaterNeed,strUserWaterIntake="0";

    private int strGender,strActivity,strFamily,strSmoked,strAlcohol,strCalories,strWater;

    private String weightGoalStatus="", weightAchievementStatus="", points="0";

    private String userPoint = "0";

    private BroadcastReceiver updateUIReciver;
    private Context context;
    private String userID, username, userPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTitle("Home");
        setContentView(R.layout.activity_home);

        context=getApplicationContext();

        //Hooks//
        TextView textView = findViewById(R.id.usernameView);
        profileView = findViewById(R.id.profile_view);

        drawerLayout = findViewById(R.id.user_drawer_layout);
        navigationView = findViewById(R.id.user_nav_view);
        toolbar = findViewById(R.id.usertoolbar);
        workoutAddBtn = findViewById(R.id.home_workout_add_button);

        diaryBtn = findViewById(R.id.home_summary_card_diary_button);

        foodCardBreakfastBtn = findViewById(R.id.home_food_card_breakfast_btn);
        foodCardLunchBtn =  findViewById(R.id.home_food_card_lunch_btn);
        foodCardDinnerBtn= findViewById(R.id.home_food_card_dinner_btn);
        foodCardSnackBtn= findViewById(R.id.home_food_card_snack_btn);

        summaryCardCaloriesBar = (ProgressBar) findViewById(R.id.home_summary_card_calories_bar);
        summaryCardFinalCalories = (TextView)findViewById(R.id.home_summary_card_final_calories);
        summaryCardTDEE01 = (TextView) findViewById(R.id.home_summary_card_TDEE_01);
        summaryCardTDEE02 = (TextView) findViewById(R.id.home_summary_card_TDEE_02);
        summaryCardFoodCalories = (TextView) findViewById(R.id.home_summary_card_food_calories);
        summaryCardWorkoutCalories = (TextView) findViewById(R.id.home_summary_card_workout_calories);
        summaryCardCaloriesRemaining = (TextView) findViewById(R.id.home_summary_card_calories_remaining);
        summaryCardTips = (TextView)findViewById(R.id.home_summary_card_tips);

        foodCardConsumedCalories = findViewById(R.id.home_food_card_consumed_calories);
        foodGoalCalories = findViewById(R.id.home_food_goal);
        workoutGoalCalories = findViewById(R.id.home_workout_goal);
        workoutCardBurnedCalories = findViewById(R.id.home_workout_card_burned_calories);

        foodChartBtn = findViewById(R.id.home_food_chart_button);
        workoutChartBtn = findViewById(R.id.home_workout_chart_button);

        waterAddBtn = findViewById(R.id.home_water_add_button);
        waterChartBtn = findViewById(R.id.home_water_chart_button);
        waterCardIntake = findViewById(R.id.home_water_card_intake);
        waterCardNeed = findViewById(R.id.home_water_card_need);

        weightCardGoalBtn = (Button) findViewById(R.id.home_weight_card_goal_button);
        weightCardRecordBtn = (Button) findViewById(R.id.home_weight_card_record_button);
        weightCardWeightMinusBtn = (ImageButton) findViewById(R.id.home_weight_card_weight_minus_button);
        weightCardWeightAddBtn = (ImageButton) findViewById(R.id.home_weight_card_weight_add_button);
        weightCardCurrentWeight = (TextView) findViewById(R.id.home_weight_card_current_weight);
        weightCardGoalWeight = (TextView) findViewById(R.id.home_weight_card_goal_weight);
        weightCardGoalProgress = findViewById(R.id.home_weight_card_weight_goal_progress);
        weightCardGoalContainer = findViewById(R.id.home_weight_card_goal_container);
        weightCardGoalContainer.setVisibility(View.VISIBLE);
        weightCardGoalCompleteDescription = findViewById(R.id.home_weight_card_goal_complete_description);
        weightCardGoalCompleteDescription.setVisibility(View.GONE);


        bmiCardBmi = (TextView) findViewById(R.id.home_bmi_card_bmi);
        bmiCardUnderWeightStatus = (ImageView)findViewById(R.id.home_bmi_card_under_weight_status);
        bmiCardUnderWeightStatus.setVisibility(View.INVISIBLE);
        bmiCardHealthyWeightStatus = (ImageView)findViewById(R.id.home_bmi_card_healthy_weight_status);
        bmiCardHealthyWeightStatus.setVisibility(View.INVISIBLE);
        bmiCardOverWeightStatus = (ImageView)findViewById(R.id.home_bmi_card_over_weight_status);
        bmiCardOverWeightStatus.setVisibility(View.INVISIBLE);
        bmiCardObesityStatus = (ImageView)findViewById(R.id.home_bmi_card_obesity_status);
        bmiCardObesityStatus.setVisibility(View.INVISIBLE);
        bmiCardTips = (TextView) findViewById(R.id.home_bmi_card_tips);


        summaryCardObesity = findViewById(R.id.home_classification_level_card);
        obeseLevelChartBtn = findViewById(R.id.home_classification_chart_button);


        reminderBtn = findViewById(R.id.reminderButton);


        sessionManager = new SessionManager(context);
        sessionManager.checkLogin();
        HashMap<String,String> usersDetails = sessionManager.getUsersDetailFromSession();

        foodFragmentPrefs = new FoodFragmentPrefs(context);
        fragmentPrefs = new FragmentPrefs(context);

        userID = usersDetails.get(SessionManager.KEY_USERID);
        userPhoto = usersDetails.get(SessionManager.KEY_USERPHOTO);
        username = usersDetails.get(SessionManager.KEY_USERNAME);
        strUserGender = usersDetails.get(SessionManager.KEY_GENDER);
        strUserAge = usersDetails.get(SessionManager.KEY_AGE);
        strUserActivityLevel = usersDetails.get(SessionManager.KEY_LIFESTYLE);
        //strUserCurrentWeight = usersDetails.get(SessionManager.KEY_WEIGHT);
        strUserHeight = usersDetails.get(SessionManager.KEY_HEIGHT);
        strFamilySuffered = usersDetails.get(SessionManager.KEY_FAMILY_SUFFERED);
        strUserAlcohol = usersDetails.get(SessionManager.KEY_ALCOHOL);
        strUserSmoked = usersDetails.get(SessionManager.KEY_SMOKED);


        textView.setText(username+" "+userID );

        //checkAppFirstTimeRun();
        AddUserPoint(userID,"0");
        AddUserWeightAchievement(userID,"true","false");
        AddUserWeightAchievement(userID,"true","false");
        AddUserWaterConsecutive(userID,DateHandler.getCurrentFormedDate(),"0");
        AddUserWorkoutConsecutive(userID,DateHandler.getCurrentFormedDate(),"0");
        AddUserFoodConsecutive(userID,DateHandler.getCurrentFormedDate(),"0");

        updateView();
        registerUIBroadcastReceiver();

        //Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.bottom_tip:
                        AddUserTask(userID, 4, DateHandler.getCurrentFormedDate());
                        startActivity(new Intent(getApplicationContext(), ViewTipActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.bottom_appointment:
                        Intent appointmentIntent = new Intent(getApplicationContext(), AppointmentActivity.class);
                        appointmentIntent.putExtra("intentUserID", userID);
                        startActivity(appointmentIntent);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.bottom_profile:
                        startActivity(new Intent(getApplicationContext(),UserSettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });


        //Navigation Drawer Menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSendToGamificationPage();

            }
        });

        workoutAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               UserSendToAddWorkoutPage();

            }
        });

        workoutChartBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserSendToWorkoutChartPage();
            }
        });

        foodCardBreakfastBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserSendToFoodListPage("Breakfast");
            }
        });

        foodCardLunchBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserSendToFoodListPage("Lunch");
            }
        });

        foodCardDinnerBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserSendToFoodListPage("Dinner");
            }
        });

        foodCardSnackBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserSendToFoodListPage("Snack");
            }
        });

        foodChartBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserSendToFoodChartPage();
            }
        });

        diaryBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserSendToDiaryPage();

            }
        });

        waterAddBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserSendToWaterPage();

            }
        });

        waterChartBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserSendToWaterChartPage();

            }
        });

        weightCardGoalBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserSendToGoalsPage();
            }
        });

        weightCardRecordBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                OpenWeightEditDialog();
            }
        });

        weightCardWeightMinusBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                WeightMinusCalculation();
            }
        });


        weightCardWeightAddBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                WeightAddCalculation();
            }
        });


        reminderBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(HomeActivity.this, ReminderActivity.class));
            }
        });

        obeseLevelChartBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserSendToObeseLevelChartPage();
            }
        });


    }

    @Override
    public void onBackPressed(){

        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
//            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
//            startActivity(intent);
//
            finish();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.nav_home:
                startActivity(new Intent(this,HomeActivity.class));
                break;

            case R.id.nav_tips:
                AddUserTask(userID, 4, DateHandler.getCurrentFormedDate());
                startActivity(new Intent(this,ViewTipActivity.class));
                break;

            case R.id.nav_reminder:
                startActivity(new Intent(this,ReminderActivity.class));
                break;

            case R.id.nav_appointment:
                Intent appointmentIntent = new Intent(this, AppointmentActivity.class);
                appointmentIntent.putExtra("intentUserID", userID);
                startActivity(appointmentIntent);

                break;

            case R.id.nav_privacy:
                startActivity(new Intent(this,PrivacyPolicyActivity.class));
                break;

            case R.id.nav_setting:
                startActivity(new Intent(this,UserSettingsActivity.class));
                break;


            case R.id.nav_logout:
                sessionManager.logoutUserFromSession();
                foodFragmentPrefs.logoutUserFromFoodReminderSession();
                fragmentPrefs.logoutUserFromWaterReminderSession();

                SharedPreferences sp = getSharedPreferences("Shpr", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();
                ed.clear();
                ed.commit();
                break;

        }
        return true;
    }


//    private void checkAppFirstTimeRun() {
//
//        if (sessionManager.getFirstTimeRunPrefs()) {
//
//            saveDateToServer(0, waterNeed1, DateHandler.getCurrentFormedDate());
//            sessionManager.setFirstTimeRunPrefs(false);
//
//        }

//    }
    @Override
    protected void onDestroy(){
        unregisterReceiver(updateUIReciver);
        super.onDestroy();
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


                            strUserStartWeight = object.getString("startWeight").trim();
                            strUserCurrentWeight = object.getString("weight").trim();
                            strUserGoalWeight = object.getString("goalWeight").trim();
                            strUserWeeklyGoalWeight = object.getString("weeklyGoalWeight").trim();


                            weightCardCurrentWeight.setText(strUserCurrentWeight);
                            weightCardGoalWeight.setText(strUserGoalWeight);

                            getUserWeightAchievemnt();


                        }
                    }
                    if (success.equals("0")) {

                        Toast.makeText(HomeActivity.this, "Read error", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(HomeActivity.this, "Read error" + e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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

    private void OpenWeightEditDialog()
    {
        final Dialog weightdialog = new Dialog(HomeActivity.this);
        weightdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        weightdialog.setContentView(R.layout.weight_edit_layout);
        weightdialog.setTitle("weight edit window");
        weightdialog.show();
        Window weightWindow = weightdialog.getWindow();
        weightWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        final EditText editCurrentWeight = (EditText) weightdialog.findViewById(R.id.weight_edit_dialog_inputcurrentweight);
        final TextView errormsg = (TextView) weightdialog.findViewById(R.id.weight_edit_dialog_error_msg);
        errormsg.setVisibility(View.GONE);


        if(!TextUtils.isEmpty(strUserCurrentWeight))
        {
            editCurrentWeight.setText(strUserCurrentWeight);
        }


        /* cancel button click action */
        Button cancelbtn = (Button)weightdialog.findViewById(R.id.weight_edit_dialog_cancel_button);
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
        Button okbtn = (Button)weightdialog.findViewById(R.id.weight_edit_dialog_ok_button);
        okbtn.setEnabled(true);
        okbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(TextUtils.isEmpty(editCurrentWeight.getText().toString()))
                {
                    errormsg.setVisibility(View.VISIBLE);
                }
                else
                {
                    UpdateUserWeight(userID,editCurrentWeight.getText().toString().trim());
                    weightdialog.cancel();
                    updateView();
                }
            }
        });


    }

    private void WeightMinusCalculation()
    {
        if(!TextUtils.isEmpty(strUserCurrentWeight) && (Double.parseDouble(strUserCurrentWeight)) > 10)
        {
            NumberFormat nf = DecimalFormat.getInstance();
            nf.setMaximumFractionDigits(1);

            strUserCurrentWeight = nf.format(Double.parseDouble(strUserCurrentWeight) - 0.1);
            strUserCurrentWeight = strUserCurrentWeight.replace(",","");

            UpdateUserWeight(userID,strUserCurrentWeight);
            weightCardCurrentWeight.setText(strUserCurrentWeight);
            updateView();
        }

        if(TextUtils.isEmpty(strUserCurrentWeight))
        {
            OpenWeightEditDialog();
        }

        if((Double.parseDouble(strUserCurrentWeight)) <= 10)
        {
            Toast.makeText(getApplicationContext(), "Invalid Weight", Toast.LENGTH_SHORT).show();
        }
    }


    private void WeightAddCalculation()
    {
        if(!TextUtils.isEmpty(strUserCurrentWeight))
        {
            NumberFormat nf = DecimalFormat.getInstance();
            nf.setMaximumFractionDigits(1);

            strUserCurrentWeight = nf.format(Double.parseDouble(strUserCurrentWeight) + 0.1);
            strUserCurrentWeight = strUserCurrentWeight.replace(",","");

            UpdateUserWeight(userID,strUserCurrentWeight);
            //weightCardCurrentWeight.setText(strUserCurrentWeight);
            updateView();
        }
        else
        {
            OpenWeightEditDialog();
        }
    }


    /* setting user details */
    private void SetUserDataToHomePage() {
        FormulaCalculations fc = new FormulaCalculations();


        if (!TextUtils.isEmpty(strUserCurrentWeight) && (!TextUtils.isEmpty(strUserHeight))
                && !TextUtils.isEmpty(strUserActivityLevel) && !TextUtils.isEmpty(strUserGender)
                && !TextUtils.isEmpty(strFamilySuffered)&& !TextUtils.isEmpty(strUserSmoked)
                && !TextUtils.isEmpty(strUserAlcohol)){

            strGender = strUserGender == "Female" ? 1 : 0;

            strFamily = strFamilySuffered == "No" ? 0 : 1;

            strSmoked = strUserSmoked == "No" ? 0 : 1;

            switch (strUserActivityLevel) {
                case "No Active":
                    strActivity = 0;
                    break;
                case "Lightly Active":
                    strActivity = 1;
                    break;
                case "Moderately Active":
                    strActivity = 2;
                    break;
                case "Active":
                    strActivity = 3;
                    break;
            }

            switch (strUserAlcohol) {
                case "No":
                    strAlcohol = 0;
                    break;
                case "Sometimes":
                    strAlcohol = 1;
                    break;
                case "Frequently":
                    strAlcohol = 2;
                    break;
            }


            strUserTDEE = fc.TDEE(strUserAge, strUserCurrentWeight, strUserHeight, strUserGender, strUserActivityLevel);



            strUserWaterNeed = fc.WaterNeed(strUserCurrentWeight);
            waterCardNeed.setText("of "+strUserWaterNeed + " ml");

            weightCardCurrentWeight.setText(strUserCurrentWeight);


            /* ###### Start of the BMI Card Calculations ###### */
            strUserBmi = fc.BMI(strUserCurrentWeight, strUserHeight);
            bmiCardBmi.setText(strUserBmi);


            strUserHealthStatus = fc.HealthStatus(strUserBmi);


            /* set health status*/
            if (strUserHealthStatus.equals("Underweight")) {
                bmiCardUnderWeightStatus.setVisibility(View.VISIBLE);
                bmiCardHealthyWeightStatus.setVisibility(View.INVISIBLE);
                bmiCardOverWeightStatus.setVisibility(View.INVISIBLE);
                bmiCardObesityStatus.setVisibility(View.INVISIBLE);
                bmiCardTips.setText("You fall within the Under Weight range. Try to increase your weight.");
            }
            if (strUserHealthStatus.equals("HealthyWeight")) {
                bmiCardUnderWeightStatus.setVisibility(View.INVISIBLE);
                bmiCardHealthyWeightStatus.setVisibility(View.VISIBLE);
                bmiCardOverWeightStatus.setVisibility(View.INVISIBLE);
                bmiCardObesityStatus.setVisibility(View.INVISIBLE);
                bmiCardTips.setText("You fall within the Healthy Weight range. Try to maintain your weight.");
            }
            if (strUserHealthStatus.equals("Overweight")) {
                bmiCardUnderWeightStatus.setVisibility(View.INVISIBLE);
                bmiCardHealthyWeightStatus.setVisibility(View.INVISIBLE);
                bmiCardOverWeightStatus.setVisibility(View.VISIBLE);
                bmiCardObesityStatus.setVisibility(View.INVISIBLE);
                bmiCardTips.setText("You fall within the Over Weight range. Try to decrease your weight.");
            }
            if (strUserHealthStatus.equals("Obesity")) {
                bmiCardUnderWeightStatus.setVisibility(View.INVISIBLE);
                bmiCardHealthyWeightStatus.setVisibility(View.INVISIBLE);
                bmiCardOverWeightStatus.setVisibility(View.INVISIBLE);
                bmiCardObesityStatus.setVisibility(View.VISIBLE);
                bmiCardTips.setText("You fall within the Over Weight range. Try to decrease your weight.");
            }
            /* ###### end of the BMI Card Calculations ###### */


        }
        else
        {
            summaryCardTips.setText("Please setup your profile for more accuracy.");
            /* set default TDEE */
            strUserTDEE = "2300";
            bmiCardTips.setText("Please setup your profile for check your Body Mass Index.");
        }
//
//        /* Set weight card details  */
        if(!TextUtils.isEmpty(strUserWeeklyGoalWeight) && !TextUtils.isEmpty(strUserGoalWeight) && weightGoalStatus.equals("true") &&
                weightAchievementStatus.equals("false"))
        {
            weightCardGoalWeight.setText(strUserGoalWeight);

            String cutCaloriesPerDay = String.format("%.0f",(((Double.parseDouble(strUserWeeklyGoalWeight) * 3500) / 0.45)) / 7);
            strUserTDEE = String.valueOf(Integer.parseInt(strUserTDEE) - Integer.parseInt(cutCaloriesPerDay));

            String lossWeightGoal = String.format("%.1f",(Double.parseDouble(strUserStartWeight) - Double.parseDouble(strUserGoalWeight)));
            String lostWeight = String.format("%.1f",(Double.parseDouble(strUserStartWeight) - Double.parseDouble(strUserCurrentWeight)));

            String weightLossProgressValue = String.format("%.0f",((Double.parseDouble(lostWeight) * 100) / Double.parseDouble(lossWeightGoal)));
            weightCardGoalProgress.setProgress(Integer.parseInt(weightLossProgressValue));
        }
        else
        {
            weightCardGoalContainer.setVisibility(View.GONE);
            weightCardGoalCompleteDescription.setVisibility(View.VISIBLE);
        }

        /* for new the user */
        if(TextUtils.isEmpty(strUserGoalWeight))
        {
            weightCardGoalCompleteDescription.setText("Decrease your weight by Setting a goal and earn points.");
        }


        summaryCardFoodCalories.setText(strUserFoodCalories);
        summaryCardWorkoutCalories.setText(strUserWorkout);
        foodCardConsumedCalories.setText(strUserFoodCalories);
        workoutCardBurnedCalories.setText(strUserWorkout);


        strUserFinalCalories = String.valueOf(Integer.parseInt(strUserFoodCalories) - Integer.parseInt(strUserWorkout));
        summaryCardFinalCalories.setText(strUserFinalCalories);


        summaryCardTDEE01.setText("/ " + strUserTDEE);
        summaryCardTDEE02.setText(strUserTDEE);

        strUserCaloriesRemaining = fc.CaloriesRemaining(strUserTDEE, strUserFoodCalories, strUserWorkout);
        summaryCardCaloriesRemaining.setText(strUserCaloriesRemaining);


        summaryCardCaloriesBarValue = fc.ProgressBarValue(strUserTDEE, strUserCaloriesRemaining);
        summaryCardCaloriesBar.setProgress(summaryCardCaloriesBarValue);

        strUserFoodGoal = fc.FoodCaloriesGoal(strUserTDEE);
        foodGoalCalories.setText("Eat less than "+strUserFoodGoal+" Cal");

        strUserWorkoutGoal = fc.WorkoutCaloriesGoal(strUserTDEE);
        workoutGoalCalories.setText("Burned more than "+strUserWorkoutGoal+" Cal");



        if (Integer.parseInt(strUserCaloriesRemaining) < 0) {
            summaryCardTips.setTextColor(getResources().getColor(R.color.WarningTextColor));
            summaryCardTips.setText("You have eaten too much food.");
            summaryCardCaloriesRemaining.setTextColor(getResources().getColor(R.color.WarningTextColor));
        }

        if(Integer.parseInt(strUserFinalCalories)<=Integer.parseInt(strUserTDEE)){
            strCalories = 0;

        }
        if(Integer.parseInt(strUserFinalCalories)>Integer.parseInt(strUserTDEE)){
            strCalories = 1;
        }

        ObeseClassification();
        strUserFoodCalories = "0";
        strUserWorkout = "0";
        strUserWaterIntake = "0";
    }


    private void UserSendToGamificationPage()
    {
        Intent gamificationIntent = new Intent(this, MissionRewardActivity.class);
        gamificationIntent.putExtra("intentUserID", userID);
        startActivity(gamificationIntent);
    }

    private void UserSendToFoodListPage(String intentFrom)
    {
        Intent foodListIntent = new Intent(this, FoodListActivity.class);
        foodListIntent.putExtra("intentFrom", intentFrom);
        foodListIntent.putExtra("intentUserID", userID);
        foodListIntent.putExtra("intentUserGender", strUserGender);
        foodListIntent.putExtra("intentUserAge", strUserAge);
        foodListIntent.putExtra("intentUserLifestyle", strUserActivityLevel);
        foodListIntent.putExtra("intentUserWeight", strUserCurrentWeight);
        foodListIntent.putExtra("intentUserHeight", strUserHeight);
        startActivity(foodListIntent);
    }

    private void UserSendToAddWorkoutPage()
    {
        Intent addWorkoutIntent = new Intent(this, WorkoutActivity.class);
        addWorkoutIntent.putExtra("intentUserID", userID);
        addWorkoutIntent.putExtra("intentUserGender", strUserGender);
        addWorkoutIntent.putExtra("intentUserAge", strUserAge);
        addWorkoutIntent.putExtra("intentUserLifestyle", strUserActivityLevel);
        addWorkoutIntent.putExtra("intentUserWeight", strUserCurrentWeight);
        addWorkoutIntent.putExtra("intentUserHeight", strUserHeight);
        startActivity(addWorkoutIntent);
    }

    private void UserSendToFoodChartPage()
    {
        Intent getFoodChartIntent = new Intent(this, FoodChart.class);
        getFoodChartIntent.putExtra("intentUserID", userID);
        getFoodChartIntent.putExtra("foodGoal", strUserFoodGoal);
        startActivity(getFoodChartIntent);
    }

    private void UserSendToWorkoutChartPage()
    {
        Intent getWorkoutChartIntent = new Intent(this, WorkoutChart.class);
        getWorkoutChartIntent.putExtra("intentUserID", userID);
        getWorkoutChartIntent.putExtra("workoutGoal", strUserWorkoutGoal);
        startActivity(getWorkoutChartIntent);
    }

    private  void UserSendToDiaryPage()
    {
        Intent diaryIntent = new Intent(HomeActivity.this, DiaryActivity.class);
        diaryIntent.putExtra("intentFrom", "HomeActivity");
        diaryIntent.putExtra("intentUserID", userID);
        diaryIntent.putExtra("intentUserGender", strUserGender);
        diaryIntent.putExtra("intentUserAge", strUserAge);
        diaryIntent.putExtra("intentUserLifestyle", strUserActivityLevel);
        diaryIntent.putExtra("intentUserWeight", strUserCurrentWeight);
        diaryIntent.putExtra("intentUserHeight", strUserHeight);

        startActivity(diaryIntent);
    }

    private void UserSendToWaterPage()
    {
        Intent getWaterIntent = new Intent(this, WaterMainActivity.class);
        getWaterIntent.putExtra("intentUserID", userID);
        getWaterIntent.putExtra("intentUserGender", strUserGender);
        getWaterIntent.putExtra("intentUserAge", strUserAge);
        getWaterIntent.putExtra("intentUserLifestyle", strUserActivityLevel);
        getWaterIntent.putExtra("intentUserWeight", strUserCurrentWeight);
        getWaterIntent.putExtra("intentUserHeight", strUserHeight);
        startActivity(getWaterIntent);
    }

    private void UserSendToWaterChartPage()
    {
        Intent getWaterChartIntent = new Intent(this, WaterChart.class);
        getWaterChartIntent.putExtra("intentUserID", userID);
        getWaterChartIntent.putExtra("waterNeed", strUserWaterNeed);
        startActivity(getWaterChartIntent);
    }

    private void UserSendToGoalsPage()
    {
        Intent goalIntent = new Intent(this, GoalsActivity.class);
        goalIntent.putExtra("intentUserID", userID);
        startActivity(goalIntent);
    }

    private void UserSendToObeseLevelChartPage()
    {
        Intent getObeseLevelChartIntent = new Intent(this, ObeseLevelChart.class);
        getObeseLevelChartIntent.putExtra("intentUserID", userID);
        startActivity(getObeseLevelChartIntent);
    }


    private void  updateView(){
        getUserDetails();
        DisplayUserProfilePic(userID);
        DisplayUserFoodCount(userID,DateHandler.getCurrentFormedDate());
        //ObeseClassification();
    }

    private void registerUIBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.update.view.action");
        updateUIReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateView();
            }
        };
        registerReceiver(updateUIReciver,filter);
    }


    private void DisplayUserProfilePic(final String intentUserID){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_PROFILE_PIC_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("profilepic");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String profilePic= object.getString("userPhoto");

                                    Glide.with(HomeActivity.this).asBitmap().load(profilePic)
                                            .fitCenter()
                                            .dontAnimate().into(profileView);

                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("tagerror", "["+error+"]");
                //Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",intentUserID);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void UpdateUserWeight(final String intentUserID, final String weight){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.UPDATE_USER_WEIGHT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {
                                //Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                                Log.i("tagtoast", "["+message+"]");
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.i("tagerror", "["+error+"]");
                Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",intentUserID);
                params.put("weight",weight);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void DisplayUserFoodCount(final String intentUserID, final String date){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_FOOD_COUNT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("fcount");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    int CountFood= object.getInt("CountFood");

                                    if (CountFood == 0){
                                        summaryCardFoodCalories.setText(strUserFoodCalories);
                                        foodCardConsumedCalories.setText(strUserFoodCalories);
                                        DisplayUserWorkoutCount(userID,DateHandler.getCurrentFormedDate(),strUserFoodCalories);

                                    }

                                    else{
                                        AddUserTask(intentUserID, 3, date);
                                        DisplayUserTotalNutrition(userID, date);
                                    }


                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("tagerror", "["+error+"]");
                //Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",intentUserID);
                params.put("addFoodDate",date);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);



    }

    private void DisplayUserTotalNutrition(final String userID, final String date){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_TOTAL_NUTRITION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("nulog");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    strUserFoodCalories= object.getString("totalHomeCalories");

                                    summaryCardFoodCalories.setText(strUserFoodCalories);
                                    foodCardConsumedCalories.setText(strUserFoodCalories);

                                    DisplayUserWorkoutCount(userID,date,strUserFoodCalories);

                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("tagerror", "["+error+"]");
                //Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",userID);
                params.put("addFoodDate",date);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }

    private void DisplayUserWorkoutCount(final String intentUserID, final String date, final String foodCalories){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_WORKOUT_COUNT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("wcount");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    int CountWorkout= object.getInt("CountWorkout");

                                    if (CountWorkout == 0){
                                        summaryCardWorkoutCalories.setText(strUserWorkout);
                                        workoutCardBurnedCalories.setText(strUserWorkout);
                                        //strUserFoodCalories = foodCalories;

                                        DisplayUserWaterCount(userID,date,foodCalories,strUserWorkout);
                                    }

                                    else{
                                        AddUserTask(intentUserID, 2, date);
                                        DisplayUserTotalCalBurnt(userID, date,foodCalories);
                                    }


                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("tagerror", "["+error+"]");
                //Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",intentUserID);
                params.put("addWorkoutDate",date);
                return params;
            }
        };
//        stringRequest.setRetryPolicy(new RetryPolicy() {
//            @Override
//            public int getCurrentTimeout() {
//                return 50000;
//            }
//
//            @Override
//            public int getCurrentRetryCount() {
//                return 50000;
//            }
//
//            @Override
//            public void retry(VolleyError error) throws VolleyError {
//
//            }
//        });
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }

    private void DisplayUserTotalCalBurnt(final String intentUserID, final String date, final String foodCalories1){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_TOTAL_CAL_BURNT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("workoutCal");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    strUserWorkout= object.getString("totalHomeCaloriesBurnt");
                                    //strUserFoodCalories = foodCalories1;

                                    summaryCardWorkoutCalories.setText(strUserWorkout);
                                    workoutCardBurnedCalories.setText(strUserWorkout);

                                    DisplayUserWaterCount(userID,date,foodCalories1,strUserWorkout);



                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("tagerror", "["+error+"]");
                //Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",intentUserID);
                params.put("addWorkoutDate",date);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void DisplayUserWaterCount(final String intentUserID, final String date, final String foodCalories2, final String workoutCalories){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_WATER_COUNT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("watercount");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    int CountWater= object.getInt("CountWater");


                                    if (CountWater == 0){
                                        waterCardIntake.setText(strUserWaterIntake);

                                        strUserFoodCalories = foodCalories2;
                                        strUserWorkout = workoutCalories;
                                        SetUserDataToHomePage();
                                    }

                                    else{
                                        AddUserTask(intentUserID, 1, date);
                                        DisplayUserTotalWater(intentUserID, date, foodCalories2,workoutCalories);
                                    }

                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("tagerror", "["+error+"]");
                //Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",intentUserID);
                params.put("waterDateTime",date);
                return params;
            }
        };


        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);



    }

    private void DisplayUserTotalWater(final String userID, final String date, final String foodCalories3, final String workoutCalories1){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_TOTAL_WATER_INTAKE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("waterIntake");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    strUserWaterIntake= object.getString("totalWaterIntake");

                                    waterCardIntake.setText(strUserWaterIntake);

                                    if(Integer.parseInt(strUserWaterIntake)<1000){
                                        strWater =1;
                                    }
                                    if(Integer.parseInt(strUserWaterIntake)>=1000 && Integer.parseInt(strUserWaterIntake)<2000){
                                        strWater =2;
                                    }
                                    if(Integer.parseInt(strUserWaterIntake)>2000){
                                        strWater =3;
                                    }

                                    strUserFoodCalories =foodCalories3;
                                    strUserWorkout = workoutCalories1;

                                    SetUserDataToHomePage();


                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("tagerror", "["+error+"]");
                //Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",userID);
                params.put("waterDateTime",date);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void AddUserTask(final String intentUserID, final int taskID, final String date) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.ADD_USER_TASK_DETAIL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {
                                //Toast.makeText(HomeActivity.this,message,Toast.LENGTH_SHORT).show();
                                Log.i("tagtoast", "["+message+"]");
                                getTaskPoint(taskID);

                            }
                            if (success.equals("0")) {
                                //Toast.makeText(HomeActivity.this,message,Toast.LENGTH_SHORT).show();
                                Log.i("tagtoast", "["+message+"]");
                            }

                        } catch (JSONException e) {
                            Toast.makeText(HomeActivity.this,"Add task Error!" + e.toString(),Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("tagerror", "["+error+"]");
                        Toast.makeText(HomeActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userID", intentUserID);
                params.put("taskID", String.valueOf(taskID));
                params.put("addTaskDate", date);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void ObeseClassification() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlClassify,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");

                            JSONObject jsonObject = new JSONObject(response);
                            String data = jsonObject.getString("NObeyesdad");

                            summaryCardObesity.setText(data);

                            AddObeseLevel(userID,data, DateHandler.getCurrentTime(),DateHandler.getCurrentFormedDate());
                            UpdateUserObeseLevel(userID,data, DateHandler.getCurrentTime(),DateHandler.getCurrentFormedDate());

                        } catch (JSONException e) {
                            Toast.makeText(HomeActivity.this,"shown obese Error!" + e.toString(),Toast.LENGTH_LONG).show();
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
                params.put("Gender", String.valueOf(strGender));
                params.put("Age", strUserAge);
                params.put("Height", String.valueOf(Double.parseDouble(strUserHeight)/100));
                params.put("Weight", strUserCurrentWeight);
                params.put("Family", String.valueOf(strFamily));
                params.put("FAVC", String.valueOf(strCalories));
                params.put("Smoke", String.valueOf(strSmoked));
                params.put("CH2O", String.valueOf(strWater));
                params.put("FAF", String.valueOf(strActivity));
                params.put("CALC", String.valueOf(strAlcohol));
                Log.i("tagstr", "["+params+"]");

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void AddObeseLevel(final String intentUserID,final String level, final String time, final String date) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.ADD_USER_OBESE_LEVEL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {
                                //Toast.makeText(HomeActivity.this,message,Toast.LENGTH_SHORT).show();
                                Log.i("tagtoast", "["+message+"]");
                            }

                        } catch (JSONException e) {
                            Toast.makeText(HomeActivity.this,"Add obese Error!" + e.toString(),Toast.LENGTH_LONG).show();
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
                params.put("userID", intentUserID);
                params.put("obeseLevel",level);
                params.put("addObeseTime", time);
                params.put("addObeseDate", date);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void UpdateUserObeseLevel(final String intentUserID,final String level, final String time, final String date){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.UPDATE_USER_OBESE_LEVEL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {
                                //Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                                Log.i("tagtoast", "["+message+"]");
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.i("tagerror", "["+error+"]");
                Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",intentUserID);
                params.put("obeseLevel",level);
                params.put("addObeseTime",time);
                params.put("addObeseDate",date);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);



    }

    private void AddUserWeightAchievement(final String intentUserID,final String weightLossGoalStatus, final String weightLossAchievementStatus)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.ADD_USER_WEIGHT_ACHIEVEMENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {
                                //Toast.makeText(HomeActivity.this,message,Toast.LENGTH_SHORT).show();
                                Log.i("tagtoast", "["+message+"]");
                            }

                            if (success.equals("0")) {
                                //Toast.makeText(HomeActivity.this,message,Toast.LENGTH_SHORT).show();
                                Log.i("tagtoast", "["+message+"]");
                            }

                        } catch (JSONException e) {
                            Toast.makeText(HomeActivity.this,"Save Error!" + e.toString(),Toast.LENGTH_LONG).show();
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
                params.put("userID", intentUserID);
                params.put("weightLossGoalStatus",weightLossGoalStatus);
                params.put("weightLossAchievementStatus", weightLossAchievementStatus);

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

                            weightGoalStatus = object.getString("weightLossGoalStatus").trim();
                            weightAchievementStatus = object.getString("weightLossAchievementStatus").trim();


                            if(weightGoalStatus.equals("true") && weightAchievementStatus.equals("false"))
                            {
                                if(!strUserCurrentWeight.isEmpty() && !strUserGoalWeight.isEmpty())
                                {
                                    if(Double.parseDouble(strUserCurrentWeight) <= Double.parseDouble(strUserGoalWeight))
                                    {
                                        PopupAchievementDialog();

                                    }
                                }
                            }

                            if(weightGoalStatus.equals("true") && weightAchievementStatus.equals("false"))
                            {
                                weightCardGoalContainer.setVisibility(View.VISIBLE);
                                weightCardGoalCompleteDescription.setVisibility(View.GONE);
                            }
                            else
                            {
                                weightCardGoalContainer.setVisibility(View.GONE);
                                weightCardGoalCompleteDescription.setVisibility(View.VISIBLE);
                            }


                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(HomeActivity.this, "Get user weight error" + e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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

    private void PopupAchievementDialog()
    {

        UpdateUserWeightAchievement(userID);
        AddUserTask(userID, 5, DateHandler.getCurrentFormedDate());


        final Dialog achievementDialog = new Dialog(HomeActivity.this);
        achievementDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        achievementDialog.setContentView(R.layout.achievements_layout);
        achievementDialog.setTitle("Achievement Window");
        achievementDialog.show();
        achievementDialog.setCanceledOnTouchOutside(false);
        achievementDialog.setCancelable(false);
        Window window = achievementDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView dialogDescription = achievementDialog.findViewById(R.id.achievement_dialog_description);
        dialogDescription.setText("Congratulations, You have achieved your weight loss goal.");

        TextView dialogPoints = achievementDialog.findViewById(R.id.achievement_dialog_points);
        dialogPoints.setText("+5 Points");

        ImageView cancelBtn = achievementDialog.findViewById(R.id.achievement_dialog_close_button);
        cancelBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                achievementDialog.dismiss();
            }
        });
    }

    private void UpdateUserWeightAchievement(final String intentUserID){

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
                                //Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                                Log.i("tagtoast", "["+message+"]");
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(HomeActivity.this, "update user weight achievement error" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.i("tagerror", "["+error+"]");
                Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",intentUserID);
                params.put("weightLossAchievementStatus","true");
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);



    }

    private void AddUserWaterConsecutive(final String intentUserID,final String launchDate, final String counter)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.ADD_USER_WATER_CONSECUTIVE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {
                                //Toast.makeText(HomeActivity.this,message,Toast.LENGTH_SHORT).show();
                                Log.i("tagtoast", "["+message+"]");
                            }

                            if (success.equals("0")) {
                                //Toast.makeText(HomeActivity.this,message,Toast.LENGTH_SHORT).show();
                                Log.i("tagtoast", "["+message+"]");
                            }

                        } catch (JSONException e) {
                            Toast.makeText(HomeActivity.this,"Insert water consecutive Error!" + e.toString(),Toast.LENGTH_LONG).show();
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
                params.put("userID", intentUserID);
                params.put("lastLaunchDate",launchDate);
                params.put("counterDay", counter);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void AddUserWorkoutConsecutive(final String intentUserID,final String launchDate, final String counter)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.ADD_USER_WORKOUT_CONSECUTIVE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {
                                //Toast.makeText(HomeActivity.this,message,Toast.LENGTH_SHORT).show();
                                Log.i("tagtoast", "["+message+"]");
                            }

                            if (success.equals("0")) {
                                //Toast.makeText(HomeActivity.this,message,Toast.LENGTH_SHORT).show();
                                Log.i("tagtoast", "["+message+"]");
                            }

                        } catch (JSONException e) {
                            Toast.makeText(HomeActivity.this,"Insert water consecutive Error!" + e.toString(),Toast.LENGTH_LONG).show();
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
                params.put("userID", intentUserID);
                params.put("workoutLastLaunchDate",launchDate);
                params.put("workoutCounterDay", counter);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void AddUserFoodConsecutive(final String intentUserID,final String launchDate, final String counter)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.ADD_USER_FOOD_CONSECUTIVE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {
                                //Toast.makeText(HomeActivity.this,message,Toast.LENGTH_SHORT).show();
                                Log.i("tagtoast", "["+message+"]");
                            }

                            if (success.equals("0")) {
                                //Toast.makeText(HomeActivity.this,message,Toast.LENGTH_SHORT).show();
                                Log.i("tagtoast", "["+message+"]");
                            }

                        } catch (JSONException e) {
                            Toast.makeText(HomeActivity.this,"Insert water consecutive Error!" + e.toString(),Toast.LENGTH_LONG).show();
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
                params.put("userID", intentUserID);
                params.put("foodLastLaunchDate",launchDate);
                params.put("foodCounterDay", counter);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void getTaskPoint(final int taskID)
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_TASK_POINT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.i("tagconvertstr", "[" + response + "]");
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    JSONArray jsonArray = jsonObject.getJSONArray("taskPoint");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String taskPoint = object.getString("taskPoint").trim();
                            DisplayUserTotalPoint(userID,taskPoint);

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(HomeActivity.this, "Get user weight error" + e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("taskID", String.valueOf(taskID));

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void DisplayUserTotalPoint(final String intentUserID, final String taskPoint){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_TOTAL_POINT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("userPoint");
                            String success = jsonObject.getString("success") ;

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    userPoint = object.getString("totalPoint");

                                    userPoint = String.valueOf(Integer.parseInt(userPoint) + Integer.parseInt(taskPoint));
                                    UpdateUserPoint(userID, userPoint);
                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",intentUserID);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }


    private void AddUserPoint(final String intentUserID, final String point)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.ADD_USER_POINT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {
                                //Toast.makeText(HomeActivity.this,message,Toast.LENGTH_SHORT).show();
                                Log.i("tagtoast", "["+message+"]");
                            }

                            if (success.equals("0")) {
                                //Toast.makeText(HomeActivity.this,message,Toast.LENGTH_SHORT).show();
                                Log.i("tagtoast", "["+message+"]");
                            }

                        } catch (JSONException e) {
                            Toast.makeText(HomeActivity.this,"Insert user point Error!" + e.toString(),Toast.LENGTH_LONG).show();
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
                params.put("userID", intentUserID);
                params.put("totalPoint",point);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void UpdateUserPoint(final String intentUserID, final String point){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.UPDATE_USER_POINT_URL,
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
                                Log.i("tagtoast", "["+message+"]");
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(HomeActivity.this, "update user point error" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.i("tagerror", "["+error+"]");
                Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",intentUserID);
                params.put("totalPoint",point);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

}