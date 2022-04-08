package com.example.habitpadapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.habitpadapplication.Adapters.TipViewAdpater;
import com.example.habitpadapplication.Chart.FoodChart;
import com.example.habitpadapplication.Chart.WaterChart;
import com.example.habitpadapplication.Chart.WorkoutChart;
import com.example.habitpadapplication.Dialogs.OtherSizeDialog;
import com.example.habitpadapplication.Model.Tip;
import com.example.habitpadapplication.Settings.FoodFragmentPrefs;
import com.example.habitpadapplication.Settings.FragmentPrefs;
import com.example.habitpadapplication.Settings.PrefsHelper;
import com.example.habitpadapplication.Settings.WaterReminderActivity;
import com.example.habitpadapplication.Settings.UserSettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.opencsv.CSVWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Variable declarations
    SessionManager sessionManager;
    FoodFragmentPrefs foodFragmentPrefs;
    FragmentPrefs fragmentPrefs;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    private String urlClassify = "https://obese-classify.herokuapp.com/predict";

    private ImageView profileView;

    private TextView foodCardConsumedCalories,workoutCardBurnedCalories,waterCardIntake,waterCardNeed ;

    private LinearLayout foodCardBreakfastBtn, foodCardLunchBtn, foodCardDinnerBtn, foodCardSnackBtn;
    private Button diaryBtn;
    private ImageButton foodChartBtn,workoutAddBtn,workoutChartBtn,waterAddBtn,waterChartBtn, reminderBtn;


    private ProgressBar summaryCardCaloriesBar;
    private TextView summaryCardFinalCalories, summaryCardTDEE01, summaryCardTDEE02, summaryCardFoodCalories, summaryCardWorkoutCalories,
            summaryCardCaloriesRemaining, summaryCardTips, summaryCardObesity;
    private int summaryCardCaloriesBarValue;


    private String strUserCurrentWeight, strUserGoalWeight, strUserWeeklyGoalWeight, strUserStartWeight, strUserBYear, strUserAge, strUserHeight,
            strUserGender, strUserActivityLevel, strUserFoodCalories="0", strUserWorkout="0",strUserFinalCalories,
            strUserTDEE, strUserCaloriesRemaining, strUserBmi, strUserHealthStatus, strFamilySuffered, strUserAlcohol, strUserSmoked;

    private TextView bmiCardBmi, bmiCardTips;
    private ImageView bmiCardUnderWeightStatus, bmiCardHealthyWeightStatus, bmiCardOverWeightStatus, bmiCardObesityStatus;

    private String strUserWaterNeed,strUserWaterIntake="0";

    private int strGender,strActivity,strFamily,strSmoked,strAlcohol,strCalories,strWater;

    private BroadcastReceiver updateUIReciver;
    private Context context;
    private String userID, username;



    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager manager;
    private List<Tip> tips;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        workoutCardBurnedCalories = findViewById(R.id.home_workout_card_burned_calories);

        foodChartBtn = findViewById(R.id.home_food_chart_button);
        workoutChartBtn = findViewById(R.id.home_workout_chart_button);

        waterAddBtn = findViewById(R.id.home_water_add_button);
        waterChartBtn = findViewById(R.id.home_water_chart_button);
        waterCardIntake = findViewById(R.id.home_water_card_intake);
        waterCardNeed = findViewById(R.id.home_water_card_need);


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


        reminderBtn = findViewById(R.id.reminderButton);


        sessionManager = new SessionManager(context);
        sessionManager.checkLogin();
        HashMap<String,String> usersDetails = sessionManager.getUsersDetailFromSession();

        foodFragmentPrefs = new FoodFragmentPrefs(context);
        fragmentPrefs = new FragmentPrefs(context);

        userID = usersDetails.get(SessionManager.KEY_USERID);
        username = usersDetails.get(SessionManager.KEY_USERNAME);
        strUserGender = usersDetails.get(SessionManager.KEY_GENDER);
        strUserAge = usersDetails.get(SessionManager.KEY_AGE);
        strUserActivityLevel = usersDetails.get(SessionManager.KEY_LIFESTYLE);
        strUserCurrentWeight = usersDetails.get(SessionManager.KEY_WEIGHT);
        strUserHeight = usersDetails.get(SessionManager.KEY_HEIGHT);
        strFamilySuffered = usersDetails.get(SessionManager.KEY_FAMILY_SUFFERED);
        strUserAlcohol = usersDetails.get(SessionManager.KEY_ALCOHOL);
        strUserSmoked = usersDetails.get(SessionManager.KEY_SMOKED);



        textView.setText(username+" "+userID );

        //checkAppFirstTimeRun();

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
                        startActivity(new Intent(getApplicationContext(), ViewTipActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.bottom_appointment:
//                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;

                    case R.id.bottom_profile:
//                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;

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

        reminderBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(HomeActivity.this, ReminderActivity.class));

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
//            finish();
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




    /* setting user details */
    private void SetUserDataToHomePage() {
        FormulaCalculations fc = new FormulaCalculations();


        if (!TextUtils.isEmpty(strUserCurrentWeight) && (!TextUtils.isEmpty(strUserHeight))
                && !TextUtils.isEmpty(strUserActivityLevel) && !TextUtils.isEmpty(strUserGender)
                && !TextUtils.isEmpty(strFamilySuffered)&& !TextUtils.isEmpty(strUserSmoked)
                && !TextUtils.isEmpty(strUserAlcohol)){

            strGender = strUserGender == "Male" ? 0 : 1;

            strFamily = strFamilySuffered == "Yes" ? 1 : 0;

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

            //weightCardCurrentWeight.setText(strUserCurrentWeight);


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
//        if(!TextUtils.isEmpty(strUserWeeklyGoalWeight) && !TextUtils.isEmpty(strUserGoalWeight) && weightGoalStatus.equals("true") &&
//                weightAchievementStatus.equals("false"))
//        {
//            weightCardGoalWeight.setText(strUserGoalWeight);
//
//            String cutCaloriesPerDay = String.format(Locale.US,"%.0f",(((Double.parseDouble(strUserWeeklyGoalWeight) * 3500) / 0.45)) / 7);
//            strUserTDEE = String.valueOf(Integer.parseInt(strUserTDEE) - Integer.parseInt(cutCaloriesPerDay));
//
//            String lossWeightGoal = String.format(Locale.US,"%.1f",(Double.parseDouble(strUserStartWeight) - Double.parseDouble(strUserGoalWeight)));
//            String lostWeight = String.format(Locale.US,"%.1f",(Double.parseDouble(strUserStartWeight) - Double.parseDouble(strUserCurrentWeight)));
//
//            String weightLossProgressValue = String.format(Locale.US,"%.0f",((Double.parseDouble(lostWeight) * 100) / Double.parseDouble(lossWeightGoal)));
//            weightCardGoalProgress.setProgress(Integer.parseInt(weightLossProgressValue));
//        }
//        else
//        {
//            weightCardGoalContainer.setVisibility(View.GONE);
//            weightCardGoalCompleteDescription.setVisibility(View.VISIBLE);
//        }
//
//        /* for new the user */
//        if(TextUtils.isEmpty(strUserGoalWeight))
//        {
//            weightCardGoalCompleteDescription.setText("Decrease your weight by Setting a goal and earn points.");
//        }


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

//    private void UserClassificationPage() {
//        if(ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
//
//        }else{
//
//            try{
//                String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
//                String fileName = "AnalysisData.csv";
//                String filePath = baseDir + File.separator + fileName;
//                File f = new File(filePath);
//                CSVWriter writer;
//
//                double height2 = Double.parseDouble(strUserHeight)/100;
//
//                // File exist
//                if(f.exists()&&!f.isDirectory())
//                {
//
//                    writer = new CSVWriter(new FileWriter(filePath, true));
//                }
//                else
//                {
//                    writer = new CSVWriter(new FileWriter(filePath));
//                }
//
//                String[] header = { "Gender", "Age", "Weight", "Height", "Family Suffered", "Activity Level", "Smoked", "Alcohol", "WaterIntake", "FoodCalories", "Workout Calories" };
//                writer.writeNext(header);
//
//                String[] data = new String[]{strUserGender, strUserAge, strUserCurrentWeight, String.valueOf(height2), strFamilySuffered, strUserActivityLevel, strUserSmoked, strUserAlcohol, strUserWaterIntake, strUserFoodCalories, strUserWorkout };
//
//                writer.writeNext(data);
//
//                writer.close();
//                Toast.makeText(HomeActivity.this,"Csv created sucessfully "+ filePath,Toast.LENGTH_LONG).show();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//
//    }
    private void UserSendToGamificationPage()
    {
        Intent gamificationIntent = new Intent(this, GamificationActivity.class);
        gamificationIntent.putExtra("intentUserID", userID);
        startActivity(gamificationIntent);
    }

    /* User redirect to nutrition page */
    private void UserSendToFoodListPage(String intentFrom)
    {
        Intent foodListIntent = new Intent(this, FoodListActivity.class);
        foodListIntent.putExtra("intentFrom", intentFrom);
        foodListIntent.putExtra("intentUserGender", strUserGender);
        foodListIntent.putExtra("intentUserAge", strUserAge);
        foodListIntent.putExtra("intentUserLifestyle", strUserActivityLevel);
        foodListIntent.putExtra("intentUserWeight", strUserCurrentWeight);
        foodListIntent.putExtra("intentUserHeight", strUserHeight);
        startActivity(foodListIntent);
    }

    private void UserSendToAddWorkoutPage()
    {
        Intent addWorkoutIntent = new Intent(this, TrackExerciseActivity.class);
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
        startActivity(getFoodChartIntent);
    }

    private void UserSendToWorkoutChartPage()
    {
        Intent getWorkoutChartIntent = new Intent(this, WorkoutChart.class);
        getWorkoutChartIntent.putExtra("intentUserID", userID);
        startActivity(getWorkoutChartIntent);
    }

    private  void UserSendToDiaryPage(){
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
        startActivity(getWaterChartIntent);
    }


    private void  updateView(){
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
                Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
                                        //SetUserDataToHomePage();
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
                Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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

                                    //SetUserDataToHomePage();


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
                Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
                                Toast.makeText(HomeActivity.this,message,Toast.LENGTH_SHORT).show();
                            }
                            if (success.equals("0")) {
                                //Toast.makeText(HomeActivity.this,message,Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            Toast.makeText(HomeActivity.this,"Save Error!" + e.toString(),Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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


                        } catch (JSONException e) {
                            Toast.makeText(HomeActivity.this,"Save Error!" + e.toString(),Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HomeActivity.this,error.toString(),Toast.LENGTH_LONG).show();
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

}