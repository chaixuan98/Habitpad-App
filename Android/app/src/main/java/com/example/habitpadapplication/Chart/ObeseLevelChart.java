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

public class ObeseLevelChart extends AppCompatActivity {

    private String intentUserID;
    private int level = 0;

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
        setTitle("Obese Level Charts");
        setContentView(R.layout.activity_obese_level_chart);

        Intent intent = getIntent();
        intentUserID = intent.getExtras().getString("intentUserID");

        TextView foodInDay=(TextView) findViewById(R.id.textView);
        TextView foodInWeek=(TextView) findViewById(R.id.textView2);
        TextView foodInMonth=(TextView) findViewById(R.id.textView3);
        TextView foodInYear=(TextView) findViewById(R.id.textView4);

        foodInDay.setText("Obese Level for today :"+ DateHandler.getCurrentFormedDate());
        foodInWeek.setText("Obese Level for current week:");
        foodInMonth.setText("Obese Level for current month:"+DateHandler.monthFormat(DateHandler.getCurrentFormedDate()));
        foodInYear.setText("Obese Level for current year:"+DateHandler.yearFormat(DateHandler.getCurrentFormedDate()));

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
        getODay(intentUserID);

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
        getOWeek(intentUserID);

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
        getOMonth(intentUserID);

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
        getOYear(intentUserID);



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
        Intent intent = new Intent(ObeseLevelChart.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void getODay(final String userID){

        xDay = new ArrayList<String>();
        yDay = new ArrayList<Entry>();
        // Initializing Request queue

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_OBESE_LEVEL_DAY_GRAPH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.i("tagconvertstr", "["+response+"]");

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("obeseday");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String obeseLevel = object.getString("obeseLevel");
                                    String addObeseTime = object.getString("addObeseTime").trim();

                                    switch (obeseLevel) {
                                        case "Insufficient_Weight":
                                            level = 0;
                                            break;
                                        case "Normal_Weight":
                                            level = 1;
                                            break;
                                        case "Overweight_Level_I":
                                            level = 2;
                                            break;
                                        case "Overweight_Level_II":
                                            level = 3;
                                            break;
                                        case "Obesity_Type_I":
                                            level = 4;
                                            break;
                                        case "Obesity_Type_II":
                                            level = 5;
                                            break;
                                        case "Obesity_Type_III":
                                            level = 6;
                                            break;
                                    }

                                    xDay.add(DateHandler.timeFormat(addObeseTime));
                                    yDay.add(new Entry((level),i));

                                }
                                LineDataSet set1 = new LineDataSet(yDay, "Obesity Level" );
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
                Toast.makeText(ObeseLevelChart.this, error.toString(), Toast.LENGTH_LONG).show();
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

    private void getOWeek(final String userID){

        xWeek = new ArrayList<String>();
        yWeek = new ArrayList<Entry>();
        // Initializing Request queue

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_OBESE_LEVEL_WEEK_GRAPH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.i("tagconvertstr", "["+response+"]");

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("obeseweek");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String obeseLevel = object.getString("obeseLevel");
                                    String addObeseDate = object.getString("addObeseDate").trim();

                                    switch (obeseLevel) {
                                        case "Insufficient_Weight":
                                            level = 0;
                                            break;
                                        case "Normal_Weight":
                                            level = 1;
                                            break;
                                        case "Overweight_Level_I":
                                            level = 2;
                                            break;
                                        case "Overweight_Level_II":
                                            level = 3;
                                            break;
                                        case "Obesity_Type_I":
                                            level = 4;
                                            break;
                                        case "Obesity_Type_II":
                                            level = 5;
                                            break;
                                        case "Obesity_Type_III":
                                            level = 6;
                                            break;

                                    }

//                                    level += level;
//                                    level = level/jsonArray.length();
//                                    Toast.makeText(ObeseLevelChart.this, level, Toast.LENGTH_LONG).show();

                                    xWeek.add(addObeseDate);
                                    yWeek.add(new Entry((level),i));

                                }
                                LineDataSet set1 = new LineDataSet(yWeek, "Obesity Level" );
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
                Toast.makeText(ObeseLevelChart.this, error.toString(), Toast.LENGTH_LONG).show();
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

    private void getOMonth(final String userID){

        xMonth = new ArrayList<String>();
        yMonth = new ArrayList<Entry>();
        // Initializing Request queue

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_OBESE_LEVEL_MONTH_GRAPH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.i("tagconvertstr", "["+response+"]");

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("obesemonth");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String obeseLevel = object.getString("obeseLevel");
                                    String addObeseDate = object.getString("addObeseDate").trim();

                                    switch (obeseLevel) {
                                        case "Insufficient_Weight":
                                            level = 0;
                                            break;
                                        case "Normal_Weight":
                                            level = 1;
                                            break;
                                        case "Overweight_Level_I":
                                            level = 2;
                                            break;
                                        case "Overweight_Level_II":
                                            level = 3;
                                            break;
                                        case "Obesity_Type_I":
                                            level = 4;
                                            break;
                                        case "Obesity_Type_II":
                                            level = 5;
                                            break;
                                        case "Obesity_Type_III":
                                            level = 6;
                                            break;

                                    }


                                    xMonth.add(addObeseDate);
                                    yMonth.add(new Entry((level),i));

                                }
                                LineDataSet set1 = new LineDataSet(yMonth, "Obesity Level" );
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
                Toast.makeText(ObeseLevelChart.this, error.toString(), Toast.LENGTH_LONG).show();
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

    private void getOYear(final String userID){

        xYear = new ArrayList<String>();
        yYear = new ArrayList<Entry>();
        // Initializing Request queue

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_OBESE_LEVEL_YEAR_GRAPH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.i("tagconvertstr", "["+response+"]");

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("obeseyear");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String obeseLevel = object.getString("obeseLevel");
                                    String addObeseDate = object.getString("addObeseDate").trim();

                                    switch (obeseLevel) {
                                        case "Insufficient_Weight":
                                            level = 0;
                                            break;
                                        case "Normal_Weight":
                                            level = 1;
                                            break;
                                        case "Overweight_Level_I":
                                            level = 2;
                                            break;
                                        case "Overweight_Level_II":
                                            level = 3;
                                            break;
                                        case "Obesity_Type_I":
                                            level = 4;
                                            break;
                                        case "Obesity_Type_II":
                                            level = 5;
                                            break;
                                        case "Obesity_Type_III":
                                            level = 6;
                                            break;

                                    }


                                    xYear.add(addObeseDate);
                                    yYear.add(new Entry((level),i));

                                }
                                LineDataSet set1 = new LineDataSet(yYear, "Obesity Level" );
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
                Toast.makeText(ObeseLevelChart.this, error.toString(), Toast.LENGTH_LONG).show();
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