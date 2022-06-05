package com.example.habitpadapplication.Chart;

import static com.example.habitpadapplication.DateHandler.dateFormat2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.habitpadapplication.DateHandler;
import com.example.habitpadapplication.HomeActivity;
import com.example.habitpadapplication.R;
import com.example.habitpadapplication.Urls;
import com.example.habitpadapplication.VolleySingleton;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FoodChart extends AppCompatActivity {

    private String intentUserID, foodGoal, lastLaunchDate;
    private int counter, totalCalories;
//    private TextView consecutiveDay,consecutiveWeek, consecutiveMonth;

    LineChart dayLineChart;
    LineChart weekLineChart;
    LineChart monthLineChart;
    LineChart yearLineChart;

    ArrayList<String> xDay;
    ArrayList<Entry> yDay;

    ArrayList<String> xWeek;
    ArrayList<Entry> yWeek;
    ArrayList<Entry> gWeek;

    ArrayList<String> xMonth;
    ArrayList<Entry> yMonth;
    ArrayList<Entry> gMonth;

    ArrayList<String> xYear;
    ArrayList<Entry> yYear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Food Calories Consumed Charts");
        setContentView(R.layout.activity_food_chart);


        intentUserID = getIntent().getExtras().getString("intentUserID");
        foodGoal = getIntent().getExtras().getString("foodGoal");

        TextView foodInDay=(TextView) findViewById(R.id.textView);
        TextView foodInWeek=(TextView) findViewById(R.id.textView2);
        TextView foodInMonth=(TextView) findViewById(R.id.textView3);
        TextView foodInYear=(TextView) findViewById(R.id.textView4);

//        consecutiveDay = (TextView) findViewById(R.id.consecutive_day_tv);
//        consecutiveWeek = (TextView) findViewById(R.id.consecutive_week_tv);
//        consecutiveMonth = (TextView) findViewById(R.id.consecutive_month_tv);

        // Get calendar set to current date and time
        Calendar c = Calendar.getInstance();
        // Set the calendar to Sunday of the current week
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        DateFormat df = new SimpleDateFormat("dd/MM");
        String start = df.format(c.getTime());
        for (int i = 0; i <6; i++) {
            c.add(Calendar.DATE, 1);
        }
        String end = df.format(c.getTime());

        foodInDay.setText("Today :"+ DateHandler.getCurrentFormedDate());
        foodInWeek.setText("Current week:"+ start +"-"+end);
        foodInMonth.setText("Current month:"+DateHandler.monthFormat(DateHandler.getCurrentFormedDate()));
        foodInYear.setText("Current year:"+DateHandler.yearFormat(DateHandler.getCurrentFormedDate()));

        dayLineChart = (LineChart) findViewById(R.id.day_chart);

        dayLineChart.setDrawGridBackground(false);
        dayLineChart.setDescription("");
        dayLineChart.setTouchEnabled(true);
        dayLineChart.setDragEnabled(true);
        dayLineChart.setScaleEnabled(true);
        dayLineChart.setPinchZoom(true);


        dayLineChart.getXAxis().setTextSize(13f);
        dayLineChart.getAxisLeft().setTextSize(13f);
        dayLineChart.getXAxis().setDrawGridLines(false);
        dayLineChart.getAxisRight().setDrawAxisLine(false);
        dayLineChart.getAxisRight().setDrawLimitLinesBehindData(false);
        dayLineChart.getAxisRight().setDrawAxisLine(false);
        dayLineChart.getAxisRight().setDrawGridLines(false);

        dayLineChart.getAxisLeft().setDrawAxisLine(false);
        dayLineChart.getAxisLeft().setDrawLimitLinesBehindData(false);
        dayLineChart.getAxisLeft().setDrawAxisLine(false);
        dayLineChart.getAxisLeft().setDrawGridLines(false);
        getFDay(intentUserID);

        weekLineChart = (LineChart) findViewById(R.id.week_chart);

        weekLineChart.setDrawGridBackground(false);
        weekLineChart.setDescription("");
        weekLineChart.setTouchEnabled(true);
        weekLineChart.setDragEnabled(true);
        weekLineChart.setScaleEnabled(true);
        weekLineChart.setPinchZoom(true);

        weekLineChart.getXAxis().setTextSize(13f);
        weekLineChart.getAxisLeft().setTextSize(13f);
        weekLineChart.getXAxis().setDrawGridLines(false);
        weekLineChart.getAxisRight().setDrawAxisLine(false);
        weekLineChart.getAxisRight().setDrawLimitLinesBehindData(false);
        weekLineChart.getAxisRight().setDrawAxisLine(false);
        weekLineChart.getAxisRight().setDrawGridLines(false);

        weekLineChart.getAxisLeft().setDrawAxisLine(false);
        weekLineChart.getAxisLeft().setDrawLimitLinesBehindData(false);
        weekLineChart.getAxisLeft().setDrawAxisLine(false);
        weekLineChart.getAxisLeft().setDrawGridLines(false);
        getFWeek(intentUserID);

        monthLineChart = (LineChart) findViewById(R.id.month_chart);

        monthLineChart.setDrawGridBackground(false);
        monthLineChart.setDescription("");
        monthLineChart.setTouchEnabled(true);
        monthLineChart.setDragEnabled(true);
        monthLineChart.setScaleEnabled(true);
        monthLineChart.setPinchZoom(true);

        monthLineChart.getXAxis().setTextSize(13f);
        monthLineChart.getAxisLeft().setTextSize(13f);
        monthLineChart.getXAxis().setDrawGridLines(false);
        monthLineChart.getAxisRight().setDrawAxisLine(false);
        monthLineChart.getAxisRight().setDrawLimitLinesBehindData(false);
        monthLineChart.getAxisRight().setDrawAxisLine(false);
        monthLineChart.getAxisRight().setDrawGridLines(false);

        monthLineChart.getAxisLeft().setDrawAxisLine(false);
        monthLineChart.getAxisLeft().setDrawLimitLinesBehindData(false);
        monthLineChart.getAxisLeft().setDrawAxisLine(false);
        monthLineChart.getAxisLeft().setDrawGridLines(false);
        getFMonth(intentUserID);

        yearLineChart = (LineChart) findViewById(R.id.year_chart);

        yearLineChart.setDrawGridBackground(false);
        yearLineChart.setDescription("");
        yearLineChart.setTouchEnabled(true);
        yearLineChart.setDragEnabled(true);
        yearLineChart.setScaleEnabled(true);
        yearLineChart.setPinchZoom(true);

        yearLineChart.getXAxis().setTextSize(13f);
        yearLineChart.getAxisLeft().setTextSize(13f);
        yearLineChart.getXAxis().setDrawGridLines(false);
        yearLineChart.getAxisRight().setDrawAxisLine(false);
        yearLineChart.getAxisRight().setDrawLimitLinesBehindData(false);
        yearLineChart.getAxisRight().setDrawAxisLine(false);
        yearLineChart.getAxisRight().setDrawGridLines(false);

        yearLineChart.getAxisLeft().setDrawAxisLine(false);
        yearLineChart.getAxisLeft().setDrawLimitLinesBehindData(false);
        yearLineChart.getAxisLeft().setDrawAxisLine(false);
        yearLineChart.getAxisLeft().setDrawGridLines(false);
        getFYear(intentUserID);



        TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Today");
        spec.setContent(R.id.linearLayout);
        spec.setIndicator("Today");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("week");
        spec.setContent(R.id.linearLayout2);
        spec.setIndicator(" week");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("month");
        spec.setContent(R.id.linearLayout3);
        spec.setIndicator("month");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("year");
        spec.setContent(R.id.linearLayout4);
        spec.setIndicator("year");
        host.addTab(spec);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed(){
        Intent intent = new Intent(FoodChart.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void getFDay(final String userID){

        xDay = new ArrayList<String>();
        yDay = new ArrayList<Entry>();
        // Initializing Request queue

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_FOOD_DAY_GRAPH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.i("tagconvertstr", "["+response+"]");

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("fday");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    int caloriesOnce = object.getInt("caloriesOnce");
                                    String addFoodTime = object.getString("addFoodTime").trim();

                                    xDay.add(DateHandler.timeFormat(addFoodTime));
                                    yDay.add(new Entry((caloriesOnce),i));

                                }
                                //getUserFoodConsecutive(totalCalories);

                                LineDataSet set1 = new LineDataSet(yDay, "Calories consumed (kcal)");
                                set1.setLineWidth(1.3f);
                                set1.setColor(Color.BLUE);
                                set1.setCircleColor(Color.BLUE);
                                set1.setCircleRadius(4f);
                                set1.setDrawFilled(false);
                                set1.setDrawValues(true);

                                LineData data = new LineData(xDay, set1);
                                dayLineChart.setData(data);
                                dayLineChart.setDescription("");
                                data.setValueTextColor(Color.BLUE);
                                data.setValueTextSize(12f);
                                dayLineChart.animateXY(500, 500);
                                dayLineChart.invalidate();


                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FoodChart.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",userID);

                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void getFWeek(final String userID){

        xWeek = new ArrayList<String>();
        yWeek = new ArrayList<Entry>();
        gWeek = new ArrayList<Entry>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_FOOD_WEEK_GRAPH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.i("tagconvertstr", "["+response+"]");

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("fweek");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    totalCalories = object.getInt("totalCalories");
                                    String addFoodDate = object.getString("addFoodDate").trim();



                                    xWeek.add(addFoodDate);
                                    yWeek.add(new Entry((totalCalories),i));
                                    gWeek.add(new Entry(Integer.valueOf(foodGoal),i));

                                }
                                //getUserFoodConsecutive(totalCalories);
                                ArrayList<ILineDataSet> lineDataSetsWeek = new ArrayList<>();

                                LineDataSet set1 = new LineDataSet(yWeek, "Calories consumed (kcal)");
                                set1.setLineWidth(1.3f);
                                set1.setColor(Color.BLUE);
                                set1.setCircleColor(Color.BLUE);
                                set1.setCircleRadius(4f);
                                set1.setDrawFilled(false);
                                set1.setDrawValues(true);

                                LineDataSet set2 = new LineDataSet(gWeek, "Calories Intake goal (kcal)");
                                set2.setLineWidth(1.3f);
                                set2.setCircleColor(Color.RED);
                                set2.setCircleRadius(4f);
                                set2.setColor(Color.RED);
                                set2.setDrawFilled(false);
                                set2.setDrawValues(true);

                                lineDataSetsWeek.add(set1);
                                lineDataSetsWeek.add(set2);

                                LineData data = new LineData(xWeek, lineDataSetsWeek);
                                weekLineChart.setData(data);
                                weekLineChart.setDescription("");
                                data.setValueTextColor(Color.BLUE);
                                data.setValueTextSize(12f);
//                                weekLineChart.animateXY(500, 500);
                                weekLineChart.invalidate();


                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FoodChart.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",userID);

                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void getFMonth(final String userID){

        xMonth = new ArrayList<String>();
        yMonth = new ArrayList<Entry>();
        gMonth = new ArrayList<Entry>();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_FOOD_MONTH_GRAPH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.i("tagconvertstr", "["+response+"]");

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("fmonth");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    totalCalories = object.getInt("totalCalories");
                                    String addFoodDate = object.getString("addFoodDate").trim();

                                    xMonth.add(dateFormat2(addFoodDate));
                                    yMonth.add(new Entry((totalCalories),i));
                                    gMonth.add(new Entry(Integer.valueOf(foodGoal),i));
                                }

                                //getUserFoodConsecutive(totalCalories);

                                ArrayList<ILineDataSet> lineDataSetsMonth = new ArrayList<>();

                                LineDataSet set1 = new LineDataSet(yMonth, "Calories consumed (kcal)");
                                set1.setLineWidth(1.3f);
                                set1.setColor(Color.BLUE);
                                set1.setCircleColor(Color.BLUE);
                                set1.setCircleRadius(4f);
                                set1.setDrawFilled(false);
                                set1.setDrawValues(true);

                                LineDataSet set2 = new LineDataSet(gMonth, "Calories Intake goal (kcal)");
                                set2.setLineWidth(1.3f);
                                set2.setCircleColor(Color.RED);
                                set2.setCircleRadius(4f);
                                set2.setColor(Color.RED);
                                set2.setDrawFilled(false);
                                set2.setDrawValues(true);

                                lineDataSetsMonth.add(set1);
                                lineDataSetsMonth.add(set2);

                                LineData data = new LineData(xMonth, lineDataSetsMonth);
                                monthLineChart.setData(data);
                                monthLineChart.setDescription("");
                                data.setValueTextColor(Color.BLUE);
                                data.setValueTextSize(10f);
                                monthLineChart.invalidate();


                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FoodChart.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",userID);

                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void getFYear(final String userID){

        xYear = new ArrayList<String>();
        yYear = new ArrayList<Entry>();
        // Initializing Request queue

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_FOOD_YEAR_GRAPH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.i("tagconvertstr", "["+response+"]");

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("fyear");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    int totalCalories = object.getInt("totalCalories");
                                    String MonthName = object.getString("MonthName").trim();

                                    xYear.add(MonthName);
                                    yYear.add(new Entry((totalCalories),i));

                                }
                                LineDataSet set1 = new LineDataSet(yYear, "Calories consumed (kcal)");
                                set1.setLineWidth(1.3f);
                                set1.setColor(Color.BLUE);
                                set1.setCircleColor(Color.BLUE);
                                set1.setCircleRadius(4f);
                                set1.setDrawFilled(false);
                                set1.setDrawValues(true);

                                LineData data = new LineData(xYear, set1);
                                yearLineChart.setData(data);
                                yearLineChart.setDescription("");
                                data.setValueTextColor(Color.BLUE);
                                data.setValueTextSize(12f);
                                yearLineChart.animateXY(500, 500);
                                yearLineChart.invalidate();


                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FoodChart.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",userID);

                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

//    private void getUserFoodConsecutive(final int food)
//    {
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_FOOD_CONSECUTIVE_URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                try {
//                    Log.i("tagconvertstr", "[" + response + "]");
//                    JSONObject jsonObject = new JSONObject(response);
//
//                    String success = jsonObject.getString("success");
//                    JSONArray jsonArray = jsonObject.getJSONArray("foodcon");
//
//                    if (success.equals("1")) {
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject object = jsonArray.getJSONObject(i);
//
//                            lastLaunchDate = object.getString("foodLastLaunchDate").trim();
//                            counter = object.getInt("foodCounterDay");
//                        }
//
//                        try {
//                            Date date1;
//                            Date date2;
//                            SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
//                            date1 = dates.parse(DateHandler.getCurrentFormedDate());
//                            date2 = dates.parse(lastLaunchDate);
//                            long difference = Math.abs(date1.getTime() - date2.getTime());
//                            long differenceDates = difference / (24 * 60 * 60 * 1000);
//                            String dayDifference = Long.toString(differenceDates);
//                            Log.i("tagdiff", "[" + dayDifference + "]");
//
//                            if (food >= 500 && food <= Integer.parseInt(foodGoal)) {
//
//                                if (Integer.parseInt(dayDifference) == 1) {
//                                    counter = counter + 1;
//                                }
//
//                                if (Integer.parseInt(dayDifference) > 1){
//                                    counter = 1;
//                                }
//                                UpdateUserFoodConsecutive(DateHandler.getCurrentFormedDate(), counter);
//                            }
//
//                            consecutiveDay.setText("Achieve goal " + counter + " day in a row");
//                            consecutiveWeek.setText("Achieve goal " + counter + " day in a row");
//                            consecutiveMonth.setText("Achieve goal " + counter + " day in a row");
//
//                            if (counter >=3){
//                                PopupAchievementDialog();
//                            }
//                        } catch (Exception e) {
//                            Toast.makeText(FoodChart.this, "Unable to find difference", Toast.LENGTH_SHORT).show();
//                            e.printStackTrace();
//                        }
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(FoodChart.this, "Get user water consecutive error" + e.toString(), Toast.LENGTH_SHORT).show();
//                }
//
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(FoodChart.this, error.toString(), Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String, String> params = new HashMap<>();
//                params.put("userID", intentUserID);
//
//                return params;
//            }
//        };
//
//        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
//    }
//
//    private void UpdateUserFoodConsecutive(final String date, final int counter){
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.UPDATE_USER_FOOD_CONSECUTIVE_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            Log.i("tagconvertstr", "["+response+"]");
//                            JSONObject jsonObject = new JSONObject(response);
//
//                            String success = jsonObject.getString("success");
//                            String message = jsonObject.getString("message");
//
//                            if (success.equals("1")) {
//                                //Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
//                                Log.i("tagtoast", "["+message+"]");
//                            }
//
//                        }catch (Exception e){
//                            e.printStackTrace();
//                            Toast.makeText(FoodChart.this, "update user food consecutive error" + e.toString(), Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //Log.i("tagerror", "["+error+"]");
//                Toast.makeText(FoodChart.this, error.toString(), Toast.LENGTH_LONG).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String, String> params = new HashMap<>();
//                params.put("userID",intentUserID);
//                params.put("foodLastLaunchDate", date);
//                params.put("foodCounterDay",String.valueOf(counter));
//                return params;
//            }
//        };
//
//        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
//
//
//
//    }
//
//    private void PopupAchievementDialog()
//    {
//
//        final Dialog achievementDialog = new Dialog(FoodChart.this);
//        achievementDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        achievementDialog.setContentView(R.layout.achievements_layout);
//        achievementDialog.setTitle("Calories Intake Habit Change");
//        achievementDialog.show();
//        achievementDialog.setCanceledOnTouchOutside(false);
//        achievementDialog.setCancelable(false);
//        Window window = achievementDialog.getWindow();
//        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//        TextView dialogDescription = achievementDialog.findViewById(R.id.achievement_dialog_description);
//        dialogDescription.setText("Congratulations, You have achieved your calories intake goal 3 days in a row.");
//
////        TextView dialogPoints = achievementDialog.findViewById(R.id.achievement_dialog_points);
////        dialogPoints.setText("+5 Points");
//
//        ImageView cancelBtn = achievementDialog.findViewById(R.id.achievement_dialog_close_button);
//        cancelBtn.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                achievementDialog.dismiss();
//            }
//        });
//    }
}