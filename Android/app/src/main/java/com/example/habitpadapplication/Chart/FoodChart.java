package com.example.habitpadapplication.Chart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FoodChart extends AppCompatActivity {

    private String intentUserID;

    LineChart dayLineChart;
    LineChart weekLineChart;
    LineChart monthLineChart;
    LineChart yearLineChart;

    ArrayList<String> xDay;
    ArrayList<Entry> yDay;

    ArrayList<String> xWeek;
    ArrayList<Entry> yWeek;

    ArrayList<String> xMonth;
    ArrayList<Entry> yMonth;

    ArrayList<String> xYear;
    ArrayList<Entry> yYear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Food Calories Consumed Charts");
        setContentView(R.layout.activity_food_chart);

        Intent intent = getIntent();
        intentUserID = intent.getExtras().getString("intentUserID");

        TextView foodInDay=(TextView) findViewById(R.id.textView);
        TextView foodInWeek=(TextView) findViewById(R.id.textView2);
        TextView foodInMonth=(TextView) findViewById(R.id.textView3);
        TextView foodInYear=(TextView) findViewById(R.id.textView4);

        foodInDay.setText("Total Cal consumed for today :"+ DateHandler.getCurrentFormedDate());
        foodInWeek.setText("Total Cal consumed for current wee:");
        foodInMonth.setText("Total Cal consumed for current month:"+DateHandler.monthFormat(DateHandler.getCurrentFormedDate()));
        foodInYear.setText("Total Cal consumed for current year:"+DateHandler.yearFormat(DateHandler.getCurrentFormedDate()));

        dayLineChart = (LineChart) findViewById(R.id.day_chart);

        dayLineChart.setDrawGridBackground(false);
        dayLineChart.setDescription("");
        dayLineChart.setTouchEnabled(true);
        dayLineChart.setDragEnabled(true);
        dayLineChart.setScaleEnabled(true);
        dayLineChart.setPinchZoom(true);

        dayLineChart.getXAxis().setTextSize(15f);
        dayLineChart.getAxisLeft().setTextSize(15f);
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

        weekLineChart.getXAxis().setTextSize(15f);
        weekLineChart.getAxisLeft().setTextSize(15f);
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

        monthLineChart.getXAxis().setTextSize(15f);
        monthLineChart.getAxisLeft().setTextSize(15f);
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

        yearLineChart.getXAxis().setTextSize(15f);
        yearLineChart.getAxisLeft().setTextSize(15f);
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
                                LineDataSet set1 = new LineDataSet(yDay, "The calories consumed in kcal");
                                set1.setLineWidth(1.5f);
                                set1.setCircleRadius(4f);
                                set1.setDrawFilled(true);
                                set1.setDrawValues(false);

                                LineData data = new LineData(xDay, set1);
                                dayLineChart.setData(data);
                                dayLineChart.setDescription("");
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
        // Initializing Request queue

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

                                    int totalCalories = object.getInt("totalCalories");
                                    String addFoodDate = object.getString("addFoodDate").trim();

                                    xWeek.add(addFoodDate);
                                    yWeek.add(new Entry((totalCalories),i));

                                }
                                LineDataSet set1 = new LineDataSet(yWeek, "The calories consumed in kcal");
                                set1.setLineWidth(1.5f);
                                set1.setCircleRadius(4f);
                                set1.setDrawFilled(true);
                                set1.setDrawValues(false);

                                LineData data = new LineData(xWeek, set1);
                                weekLineChart.setData(data);
                                weekLineChart.setDescription("");
                                weekLineChart.animateXY(500, 500);
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
        // Initializing Request queue

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

                                    int totalCalories = object.getInt("totalCalories");
                                    String addFoodDate = object.getString("addFoodDate").trim();

                                    xMonth.add(addFoodDate);
                                    yMonth.add(new Entry((totalCalories),i));

                                }
                                LineDataSet set1 = new LineDataSet(yMonth, "The calories consumed in kcal");
                                set1.setLineWidth(1.5f);
                                set1.setCircleRadius(4f);
                                set1.setDrawFilled(true);
                                set1.setDrawValues(false);

                                LineData data = new LineData(xMonth, set1);
                                monthLineChart.setData(data);
                                monthLineChart.setDescription("");
                                monthLineChart.animateXY(500, 500);
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
                                LineDataSet set1 = new LineDataSet(yYear, "The calories consumed in kcal");
                                set1.setLineWidth(1.5f);
                                set1.setCircleRadius(4f);
                                set1.setDrawFilled(true);
                                set1.setDrawValues(false);

                                LineData data = new LineData(xYear, set1);
                                yearLineChart.setData(data);
                                yearLineChart.setDescription("");
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
}