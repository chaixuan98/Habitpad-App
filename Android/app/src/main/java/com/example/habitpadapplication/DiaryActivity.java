package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.habitpadapplication.Adapters.FoodLogAdapter;
import com.example.habitpadapplication.Adapters.WaterLogAdapter;
import com.example.habitpadapplication.Adapters.WorkoutLogAdapter;
import com.example.habitpadapplication.Model.FoodLog;
import com.example.habitpadapplication.Model.WaterLog;
import com.example.habitpadapplication.Model.WorkoutLog;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiaryActivity extends AppCompatActivity {


    private String intentFrom;


    private List<FoodLog> foodLogs,lunchLogs,dinnerLogs,snackLogs;
    private List<WorkoutLog> workoutLogs;
    private List<WaterLog> waterLogs;

    private Context context;
    private String intentUserID, userPoint;

    private LinearLayout dateButton;
    private TextView diaryDate;

    private TextView userCaloriesRemaining, userFoodCalories, userWorkoutCalories, userDailyCaloriesIntake, userCarbs, userFat, userProtein, userWaterIntake,userWaterNeed ;

    private RecyclerView userBreakfastFoodList, userLunchFoodList, userDinnerFoodList, userSnacksFoodList, userWorkoutList, userWaterList;

    private String strUserCurrentWeight, strUserGoalWeight, strUserWeeklyGoalWeight, strUserStartWeight, strUserAge, strUserHeight, strUserGender, strUserActivityLevel,
            strUserFood="0", strUserCarbs="0.0", strUserFat="0.0", strUserProtein="0.0", strUserWorkout="0", userServingSize="1",
            strUserDailyCalorieIntake, strUserCaloriesRemaining;

    private String strUserWaterNeed,strUserWaterIntake="0";
    private String weightGoalStatus="", weightAchievementStatus="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Diary");
        setContentView(R.layout.activity_diary);

        Intent intent = getIntent();
        intentFrom = intent.getExtras().getString("intentFrom");
        if(intentFrom.equals("AddFoodActivity")|| intentFrom.equals("HomeActivity")||
                intentFrom.equals("AddWorkoutActivity")|| intentFrom.equals("Water")|| intentFrom.equals("WaterOtherSizeDialog"))
        {
            intentUserID = intent.getExtras().getString("intentUserID");
            strUserGender = intent.getExtras().getString("intentUserGender");
            strUserAge = intent.getExtras().getString("intentUserAge");
            strUserActivityLevel = intent.getExtras().getString("intentUserLifestyle");
            strUserCurrentWeight = intent.getExtras().getString("intentUserWeight");
            strUserHeight = intent.getExtras().getString("intentUserHeight");

        }


        dateButton = (LinearLayout) findViewById(R.id.diary_date_button);
        diaryDate = (TextView) findViewById(R.id.diary_date);


        userCaloriesRemaining = (TextView) findViewById(R.id.diary_caloriecard_calorieremaining);
        userDailyCaloriesIntake = (TextView) findViewById(R.id.diary_caloriecard_dailycalorieintake);
        userFoodCalories = (TextView) findViewById(R.id.diary_caloriecard_foodcalorie);
        userWorkoutCalories = (TextView) findViewById(R.id.diary_caloriecard_workoutcalories);


        userCarbs = (TextView) findViewById(R.id.diary_summary_card_carbs);
        userFat = (TextView) findViewById(R.id.diary_summary_card_fat);
        userProtein = (TextView) findViewById(R.id.diary_summary_card_protein);

        userWaterIntake = findViewById(R.id.diary_summary_card_water_intake);
        userWaterNeed = findViewById(R.id.diary_summary_card_water_need);


        userBreakfastFoodList = (RecyclerView)findViewById(R.id.diary_breakfast_foodlist);
        userBreakfastFoodList.setNestedScrollingEnabled(false);

        userLunchFoodList = (RecyclerView)findViewById(R.id.diary_lunch_foodlist);
        userLunchFoodList.setNestedScrollingEnabled(false);

        userDinnerFoodList = (RecyclerView)findViewById(R.id.diary_dinner_foodlist);
        userDinnerFoodList.setNestedScrollingEnabled(false);

        userSnacksFoodList = (RecyclerView)findViewById(R.id.diary_snack_foodlist);
        userSnacksFoodList.setNestedScrollingEnabled(false);

        userWorkoutList = (RecyclerView)findViewById(R.id.diary_workout_list);
        userWorkoutList.setNestedScrollingEnabled(false);

        userWaterList = (RecyclerView)findViewById(R.id.diary_water_list);
        userWaterList.setNestedScrollingEnabled(false);

        LinearLayoutManager breakfastLinearLayoutManager = new LinearLayoutManager(this);
        breakfastLinearLayoutManager.setReverseLayout(true);
        breakfastLinearLayoutManager.setStackFromEnd(true);
        userBreakfastFoodList.setLayoutManager(breakfastLinearLayoutManager);

        LinearLayoutManager lunchLinearLayoutManager = new LinearLayoutManager(this);
        lunchLinearLayoutManager.setReverseLayout(true);
        lunchLinearLayoutManager.setStackFromEnd(true);
        userLunchFoodList.setLayoutManager(lunchLinearLayoutManager);

        LinearLayoutManager dinnerLinearLayoutManager = new LinearLayoutManager(this);
        dinnerLinearLayoutManager.setReverseLayout(true);
        dinnerLinearLayoutManager.setStackFromEnd(true);
        userDinnerFoodList.setLayoutManager(dinnerLinearLayoutManager);


        LinearLayoutManager snacksLinearLayoutManager = new LinearLayoutManager(this);
        snacksLinearLayoutManager.setReverseLayout(true);
        snacksLinearLayoutManager.setStackFromEnd(true);
        userSnacksFoodList.setLayoutManager(snacksLinearLayoutManager);


        LinearLayoutManager workoutLinearLayoutManager = new LinearLayoutManager(this);
        workoutLinearLayoutManager.setReverseLayout(true);
        workoutLinearLayoutManager.setStackFromEnd(true);
        userWorkoutList.setLayoutManager(workoutLinearLayoutManager);

        LinearLayoutManager waterLinearLayoutManager = new LinearLayoutManager(this);
        waterLinearLayoutManager.setReverseLayout(true);
        waterLinearLayoutManager.setStackFromEnd(true);
        userWaterList.setLayoutManager(waterLinearLayoutManager);

        getUserDetails();

        dateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PopupCalendar();
            }
        });

        //GetUserData(currentUserID, currentDate);
        DisplayUserFoodCount(intentUserID, DateHandler.getCurrentFormedDate());
        DisplayUserAllBreakfastFoods(intentUserID, DateHandler.getCurrentFormedDate());
        DisplayUserAllLunchFoods(intentUserID, DateHandler.getCurrentFormedDate());
        DisplayUserAllDinnerFoods(intentUserID, DateHandler.getCurrentFormedDate());
        DisplayUserAllSnacks(intentUserID, DateHandler.getCurrentFormedDate());
        DisplayUserAllWorkouts(intentUserID, DateHandler.getCurrentFormedDate());
        DisplayUserAllWater(intentUserID, DateHandler.getCurrentFormedDate());




    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent(DiaryActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void PopupCalendar() {
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day)
            {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);

                /* date formats*/
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                SimpleDateFormat dayFormat = new SimpleDateFormat("dd");

                String diaryChangedDate;

                /* getting the chosen date */
                diaryChangedDate = yearFormat.format(myCalendar.getTime())+"-"+  monthFormat.format(myCalendar.getTime()) +"-"+dayFormat.format(myCalendar.getTime()) ;
                diaryDate.setText(diaryChangedDate);


                //GetUserData(currentUserID, diaryChangedDate);
                DisplayUserFoodCount(intentUserID,diaryChangedDate);
                DisplayUserAllBreakfastFoods(intentUserID, diaryChangedDate);
                DisplayUserAllLunchFoods(intentUserID, diaryChangedDate);
                DisplayUserAllDinnerFoods(intentUserID, diaryChangedDate);
                DisplayUserAllSnacks(intentUserID, diaryChangedDate);
                DisplayUserAllWorkouts(intentUserID, diaryChangedDate);
                DisplayUserAllWater(intentUserID, diaryChangedDate);

            }
        };

        /* set today date */
        new DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),  myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    private void SetSummaryCardsDetails() {
        FormulaCalculations fc = new FormulaCalculations();

        if (!TextUtils.isEmpty(strUserCurrentWeight) && !TextUtils.isEmpty(strUserAge) &&
                !TextUtils.isEmpty(strUserHeight) && !TextUtils.isEmpty(strUserActivityLevel) &&
                !TextUtils.isEmpty(strUserGender))
        {

            strUserDailyCalorieIntake = fc.TDEE(strUserAge, strUserCurrentWeight, strUserHeight, strUserGender, strUserActivityLevel);
            strUserWaterNeed = fc.WaterNeed(strUserCurrentWeight);
            userWaterNeed.setText("of "+strUserWaterNeed + " ml");

        }
        else
        {
            strUserDailyCalorieIntake="2300";

        }

        if(!TextUtils.isEmpty(strUserWeeklyGoalWeight) && !TextUtils.isEmpty(strUserGoalWeight) && weightGoalStatus.equals("true") &&
                weightAchievementStatus.equals("false"))
        {

            String cutCaloriesPerDay = String.format("%.0f",(((Double.parseDouble(strUserWeeklyGoalWeight) * 3500) / 0.45)) / 7);
            strUserDailyCalorieIntake = String.valueOf(Integer.parseInt(strUserDailyCalorieIntake) - Integer.parseInt(cutCaloriesPerDay));

        }

        userDailyCaloriesIntake.setText(strUserDailyCalorieIntake);



        userFoodCalories.setText(strUserFood);
        userWorkoutCalories.setText(strUserWorkout);
        userCarbs.setText(strUserCarbs+"g");
        userFat.setText(strUserFat+"g");
        userProtein.setText(strUserProtein+"g");
        userWaterIntake.setText(strUserWaterIntake);

        strUserCaloriesRemaining = fc.CaloriesRemaining(strUserDailyCalorieIntake, strUserFood, strUserWorkout);
        userCaloriesRemaining.setText(strUserCaloriesRemaining);

        if(Integer.parseInt(strUserCaloriesRemaining) < 0)
        {
            userCaloriesRemaining.setTextColor(getResources().getColor(R.color.WarningTextColor));
        }

        strUserFood="0";
        strUserCarbs="0";
        strUserFat="0";
        strUserProtein="0";
        strUserWorkout="0";
        strUserWaterIntake="0";
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

                            getUserWeightAchievemnt();


                        }
                    }
                    if (success.equals("0")) {

                        Toast.makeText(DiaryActivity.this, "Read error", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(DiaryActivity.this, "Read error" + e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(DiaryActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                    JSONArray jsonArray = jsonObject.getJSONArray("weightAchieve");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            weightGoalStatus = object.getString("weightLossGoalStatus").trim();
                            weightAchievementStatus = object.getString("weightLossAchievementStatus").trim();

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DiaryActivity.this, "Read error" + e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DiaryActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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

    private void DisplayUserAllBreakfastFoods(final String intentUserID, final String date){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_BREAKFAST_LOG_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            foodLogs = new ArrayList<>();
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("breakfastlog");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String addFoodDetailID = object.getString("addFoodDetailID").trim();
                                    String foodName = object.getString("foodName").trim();
                                    int numberOfServing = object.getInt("numberOfServing");
                                    int totalCalories = object.getInt("totalCalories");
                                    String totalFat = object.getString("totalFat");
                                    String totalProtein = object.getString("totalProtein");
                                    String totalCarbs = object.getString("totalCarbs");
                                    String addFoodDate = object.getString("addFoodDate").trim();
                                    String addFoodTime = object.getString("addFoodTime").trim();

                                    //GetAndSetFoodDetails(foodID);

                                    FoodLog foodLog = new FoodLog(addFoodDetailID,foodName,numberOfServing,totalCalories,totalFat,totalProtein,totalCarbs,addFoodDate,addFoodTime);
                                    foodLogs.add(foodLog);


                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        FoodLogAdapter adapter = new FoodLogAdapter(DiaryActivity.this, foodLogs);
                        userBreakfastFoodList.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DiaryActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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

    private void DisplayUserAllLunchFoods(final String intentUserID, final String date){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_LUNCH_LOG_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            lunchLogs = new ArrayList<>();
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("lunchlog");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String addFoodDetailID = object.getString("addFoodDetailID").trim();
                                    String foodName = object.getString("foodName").trim();
                                    int numberOfServing = object.getInt("numberOfServing");
                                    int totalCalories = object.getInt("totalCalories");
                                    String totalFat = object.getString("totalFat");
                                    String totalProtein = object.getString("totalProtein");
                                    String totalCarbs = object.getString("totalCarbs");
                                    String addFoodDate = object.getString("addFoodDate").trim();
                                    String addFoodTime = object.getString("addFoodTime").trim();

                                    FoodLog foodLog = new FoodLog(addFoodDetailID,foodName,numberOfServing,totalCalories,totalFat,totalProtein,totalCarbs,addFoodDate,addFoodTime);
                                    lunchLogs.add(foodLog);


                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        FoodLogAdapter adapter = new FoodLogAdapter(DiaryActivity.this,lunchLogs);
                        userLunchFoodList.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DiaryActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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

    private void DisplayUserAllDinnerFoods(final String intentUserID, final String date){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_DINNER_LOG_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dinnerLogs = new ArrayList<>();
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("dinnerlog");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String addFoodDetailID = object.getString("addFoodDetailID").trim();
                                    String foodName = object.getString("foodName").trim();
                                    int numberOfServing = object.getInt("numberOfServing");
                                    int totalCalories = object.getInt("totalCalories");
                                    String totalFat = object.getString("totalFat");
                                    String totalProtein = object.getString("totalProtein");
                                    String totalCarbs = object.getString("totalCarbs");
                                    String addFoodDate = object.getString("addFoodDate").trim();
                                    String addFoodTime = object.getString("addFoodTime").trim();

                                    FoodLog foodLog = new FoodLog(addFoodDetailID,foodName,numberOfServing,totalCalories,totalFat,totalProtein,totalCarbs,addFoodDate,addFoodTime);
                                    dinnerLogs.add(foodLog);


                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        FoodLogAdapter adapter = new FoodLogAdapter(DiaryActivity.this,dinnerLogs);
                        userDinnerFoodList.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DiaryActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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

    private void DisplayUserAllSnacks(final String intentUserID, final String date){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_SNACK_LOG_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            snackLogs = new ArrayList<>();
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("snacklog");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String addFoodDetailID = object.getString("addFoodDetailID").trim();
                                    String foodName = object.getString("foodName").trim();
                                    int numberOfServing = object.getInt("numberOfServing");
                                    int totalCalories = object.getInt("totalCalories");
                                    String totalFat = object.getString("totalFat");
                                    String totalProtein = object.getString("totalProtein");
                                    String totalCarbs = object.getString("totalCarbs");
                                    String addFoodDate = object.getString("addFoodDate").trim();
                                    String addFoodTime = object.getString("addFoodTime").trim();

                                    FoodLog foodLog = new FoodLog(addFoodDetailID,foodName,numberOfServing,totalCalories,totalFat,totalProtein,totalCarbs,addFoodDate,addFoodTime);
                                    snackLogs.add(foodLog);


                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        FoodLogAdapter adapter = new FoodLogAdapter(DiaryActivity.this,snackLogs);
                        userSnacksFoodList.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DiaryActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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

    private void DisplayUserAllWorkouts(final String intentUserID, final String date){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_WORKOUT_LOG_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            workoutLogs = new ArrayList<>();
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("workoutlog");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String addWorkoutDetailID = object.getString("addWorkoutDetailID").trim();
                                    String workoutName = object.getString("workoutName").trim();
                                    String numberOfDuration = object.getString("numberOfDuration");
                                    String totalCalories = object.getString("totalCalories");
                                    String addWorkoutDate = object.getString("addWorkoutDate").trim();
                                    String addWorkoutTime = object.getString("addWorkoutTime").trim();

                                    WorkoutLog workoutLog = new WorkoutLog(addWorkoutDetailID,workoutName,numberOfDuration,totalCalories,addWorkoutDate,addWorkoutTime);
                                    workoutLogs.add(workoutLog);


                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        WorkoutLogAdapter adapter = new WorkoutLogAdapter(DiaryActivity.this,workoutLogs);
                        userWorkoutList.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DiaryActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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

    private void DisplayUserAllWater(final String intentUserID, final String date){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_WATER_TIME_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            waterLogs = new ArrayList<>();
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("timelog");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);


                                    String waterTimeID = object.getString("waterTimeID").trim();
                                    String userID = object.getString("userID").trim();
                                    int drunkWaterOnce = object.getInt("drunkWaterOnce");
                                    String containerTyp = object.getString("containerTyp").trim();
                                    String waterDateTime = object.getString("waterDateTime").trim();
                                    String waterTime = object.getString("waterTime").trim();

                                    WaterLog waterLog = new WaterLog(waterTimeID,userID,drunkWaterOnce,containerTyp,waterDateTime,waterTime);
                                    waterLogs.add(waterLog);


                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        WaterLogAdapter adapter = new WaterLogAdapter(DiaryActivity.this, waterLogs);
                        userWaterList.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DiaryActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",intentUserID);
                params.put("waterDate",date);
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
                                        userFoodCalories.setText(strUserFood);
                                        userFat.setText(strUserFat + " g");
                                        userProtein.setText(strUserProtein + " g");
                                        userCarbs.setText(strUserCarbs + " g");
                                        DisplayUserWorkoutCount(intentUserID,date,strUserFood);
                                    }

                                    else{
                                        AddUserTask(intentUserID, 3, date);
                                        DisplayUserTotalNutrition(intentUserID, date);
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
                Toast.makeText(DiaryActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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

    private void DisplayUserTotalNutrition(final String intentUserID, final String date1){

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

                                    strUserFood = object.getString("totalHomeCalories");
                                    strUserFat = object.getString("totalHomeFat");
                                    strUserProtein = object.getString("totalHomeProtein");
                                    strUserCarbs = object.getString("totalHomeCarbs");
                                    //String addFoodDate = object.getString("addFoodDate").trim();

                                    userFoodCalories.setText(strUserFood);
                                    userFat.setText(strUserFat + " g");
                                    userProtein.setText(strUserProtein + " g");
                                    userCarbs.setText(strUserCarbs + " g");

                                    DisplayUserWorkoutCount(intentUserID,date1,strUserFood);


                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DiaryActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",intentUserID);
                params.put("addFoodDate",date1);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }

    private void DisplayUserWorkoutCount(final String intentUserID, final String date2, final String foodCalories){

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
                                        userWorkoutCalories.setText(strUserWorkout);
                                        //strUserFood = foodCalories;
                                        DisplayUserWaterCount(intentUserID,date2,foodCalories,strUserWorkout);
                                    }

                                    else{
                                        AddUserTask(intentUserID, 2, date2);
                                        DisplayUserTotalCalBurnt(intentUserID, date2,foodCalories);
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
                Toast.makeText(DiaryActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",intentUserID);
                params.put("addWorkoutDate",date2);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }

    private void DisplayUserTotalCalBurnt(final String intentUserID, final String date3, final String foodCalories1){


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

                                    strUserWorkout = object.getString("totalHomeCaloriesBurnt");
                                    //strUserFood = foodCalories1;

                                    //String addWorkoutDate = object.getString("addWorkoutDate").trim();

                                    userWorkoutCalories.setText(strUserWorkout);

                                    DisplayUserWaterCount(intentUserID,date3,foodCalories1,strUserWorkout);

                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DiaryActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",intentUserID);
                params.put("addWorkoutDate",date3);
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
                                        userWaterIntake.setText(strUserWaterIntake);

                                        strUserFood = foodCalories2;
                                        strUserWorkout = workoutCalories;
                                        SetSummaryCardsDetails();


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
                Toast.makeText(DiaryActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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

                                    userWaterIntake.setText(strUserWaterIntake);

                                    strUserFood =foodCalories3;
                                    strUserWorkout = workoutCalories1;

                                    SetSummaryCardsDetails();

                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DiaryActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
                                Toast.makeText(DiaryActivity.this,message,Toast.LENGTH_SHORT).show();
                                getTaskPoint(taskID);
                            }
                            if (success.equals("0")) {
                                //Toast.makeText(DiaryActivity.this,message,Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(DiaryActivity.this,"Save Error!" + e.toString(),Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DiaryActivity.this,error.toString(),Toast.LENGTH_LONG).show();
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
                            DisplayUserTotalPoint(intentUserID,taskPoint);

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DiaryActivity.this, "Get user weight error" + e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DiaryActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                                    UpdateUserPoint(intentUserID, userPoint);
                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DiaryActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
                            Toast.makeText(DiaryActivity.this, "update user point error" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.i("tagerror", "["+error+"]");
                Toast.makeText(DiaryActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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