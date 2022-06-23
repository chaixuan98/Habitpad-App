package com.example.habitpadapplication.Chart;

import static com.example.habitpadapplication.DateHandler.*;

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

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.habitpadapplication.DateHandler;
import com.example.habitpadapplication.HomeActivity;
import com.example.habitpadapplication.R;
import com.example.habitpadapplication.SessionManager;
import com.example.habitpadapplication.Urls;
import com.example.habitpadapplication.VolleySingleton;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WaterChart extends AppCompatActivity {

    private String intentUserID, userWaterNeed, lastLaunchDate, thisDay;
    private int counter = 0, drunkWater;
    private TextView consecutiveDay, consecutiveWeek, consecutiveMonth;

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
        setContentView(R.layout.water_chart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Water Charts");

        intentUserID = getIntent().getExtras().getString("intentUserID");
        userWaterNeed = getIntent().getExtras().getString("waterNeed");

        TextView drinkInDay=(TextView) findViewById(R.id.textView);
        TextView drinkInWeek=(TextView) findViewById(R.id.textView2);
        TextView drinkInMonth=(TextView) findViewById(R.id.textView3);
        TextView drinkInYear=(TextView) findViewById(R.id.textView4);

        //consecutiveDay = (TextView) findViewById(R.id.consecutive_day_tv);
        consecutiveWeek = (TextView) findViewById(R.id.consecutive_week_tv);
        consecutiveMonth = (TextView) findViewById(R.id.consecutive_month_tv);

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


        drinkInDay.setText("Today :"+ getCurrentFormedDate());
        drinkInWeek.setText("Current week "+ start +"-"+end);
        drinkInMonth.setText("Current month:"+ monthFormat(getCurrentFormedDate()));
        drinkInYear.setText("Current year:"+ yearFormat(getCurrentFormedDate()));


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
        getDay(intentUserID);

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
        getWeek(intentUserID);

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
        getMonth(intentUserID);

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
        getYear(intentUserID);



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
        Intent intent = new Intent(WaterChart.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void getDay(final String userID){

        xDay = new ArrayList<String>();
        yDay = new ArrayList<Entry>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_WATER_DAY_GRAPH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.i("tagconvertstr", "["+response+"]");

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("day");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    int drunkWaterOnce = object.getInt("drunkWaterOnce");
                                    String waterTime = object.getString("waterTime").trim();

                                    xDay.add(timeFormat(waterTime));
                                    yDay.add(new Entry((drunkWaterOnce),i));

                                }
//                                getUserWaterConsecutive(drunkWater);
                                LineDataSet set1 = new LineDataSet(yDay, "Consumed water(ml)");
                                set1.setLineWidth(1.3f);
                                set1.setColor(Color.BLUE);
                                set1.setCircleRadius(4f);
                                set1.setCircleColor(Color.BLUE);
                                set1.setDrawFilled(false);
                                set1.setDrawValues(true);

                                LineData data = new LineData(xDay,set1);
                                dayLineChart.setData(data);
                                dayLineChart.setDescription("");
                                data.setValueTextColor(Color.BLUE);
                                data.setValueTextSize(10f);
                                dayLineChart.invalidate();


                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WaterChart.this, error.toString(), Toast.LENGTH_LONG).show();
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

    private void getWeek(final String userID){

        xWeek = new ArrayList<String>();
        yWeek = new ArrayList<Entry>();
        gWeek = new ArrayList<Entry>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_WATER_WEEK_GRAPH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.i("tagconvertstr", "["+response+"]");

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("week");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    drunkWater = object.getInt("drunkWater");
                                    String waterDateTime = object.getString("waterDateTime").trim();



                                    xWeek.add(waterDateTime);
                                    yWeek.add(new Entry((drunkWater),i));
                                    gWeek.add(new Entry(Integer.valueOf(userWaterNeed),i));

                                }
//                                getUserWaterConsecutive(drunkWater);
                                ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

                                LineDataSet set1 = new LineDataSet(yWeek, "Consumed water(ml)");
                                set1.setLineWidth(1.3f);
                                set1.setCircleRadius(4f);
                                set1.setColor(Color.BLUE);
                                set1.setCircleColor(Color.BLUE);
                                set1.setDrawFilled(false);
                                set1.setDrawValues(true);

                                LineDataSet set2 = new LineDataSet(gWeek, "Water Intake goal (ml)");
                                set2.setLineWidth(1.3f);
                                set2.setColor(Color.RED);
                                set2.setCircleColor(Color.RED);
                                set2.setCircleRadius(4f);
                                set2.setDrawFilled(false);
                                set2.setDrawValues(true);

                                lineDataSets.add(set1);
                                lineDataSets.add(set2);

                                LineData data = new LineData(xWeek,lineDataSets);
                                weekLineChart.setData(data);
                                weekLineChart.setDescription("");
                                data.setValueTextColor(Color.BLUE);
                                data.setValueTextSize(10f);
                                //weekLineChart.animateXY(500, 500);
                                weekLineChart.invalidate();


                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WaterChart.this, error.toString(), Toast.LENGTH_LONG).show();
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

    private void getMonth(final String userID){

        xMonth = new ArrayList<String>();
        yMonth = new ArrayList<Entry>();
        gMonth = new ArrayList<Entry>();
        // Initializing Request queue

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_WATER_MONTH_GRAPH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.i("tagconvertstr", "["+response+"]");

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("month");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    drunkWater = object.getInt("drunkWater");
                                    String waterDateTime = object.getString("waterDateTime").trim();

                                    xMonth.add(dateFormat2(waterDateTime));
                                    yMonth.add(new Entry((drunkWater),i));
                                    gMonth.add(new Entry(Integer.valueOf(userWaterNeed),i));

                                }
                                if(jsonArray.length()>2) {
                                    for (int i = jsonArray.length()-3; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        drunkWater = object.getInt("drunkWater");

                                        if (drunkWater >= Integer.valueOf(userWaterNeed)) {
                                            counter++;
                                        }

                                    }
                                }
                                consecutiveMonth.setText("Achieve goal " + counter + " days for the last 3 days");
                                consecutiveWeek.setText("Achieve goal " + counter + " days for the last 3 days");

                                if(counter > 2){
                                    consecutiveMonth.setText("Achieve goal for the last 3 days (Water intake habit is changed)");
                                    consecutiveWeek.setText("Achieve goal for the last 3 days (Water intake habit is changed)");

                                }


                                ArrayList<ILineDataSet> lineDataSetsMonth = new ArrayList<>();

                                LineDataSet set1 = new LineDataSet(yMonth, "Consumed water(ml)");
                                set1.setLineWidth(1.3f);
                                set1.setCircleRadius(4f);
                                set1.setColor(Color.BLUE);
                                set1.setCircleColor(Color.BLUE);
                                set1.setDrawFilled(false);
                                set1.setDrawValues(true);

                                LineDataSet set2 = new LineDataSet(gMonth, "Water Intake goal (ml)");
                                set2.setLineWidth(1.3f);
                                set2.setColor(Color.RED);
                                set2.setCircleColor(Color.RED);
                                set2.setCircleRadius(4f);
                                set2.setDrawFilled(false);
                                set2.setDrawValues(true);

                                lineDataSetsMonth.add(set1);
                                lineDataSetsMonth.add(set2);

                                LineData data = new LineData(xMonth,lineDataSetsMonth);
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
                Toast.makeText(WaterChart.this, error.toString(), Toast.LENGTH_LONG).show();
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

    private void getYear(final String userID){

        xYear = new ArrayList<String>();
        yYear = new ArrayList<Entry>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_WATER_YEAR_GRAPH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.i("tagconvertstr", "["+response+"]");

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("year");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    int drunkWater = object.getInt("drunkWater");
                                    String waterDateTime = object.getString("waterDateTime").trim();

                                    xYear.add(waterDateTime);
                                    yYear.add(new Entry((drunkWater),i));

                                }
                                LineDataSet set1 = new LineDataSet(yYear, "Consumed water(ml)");
                                set1.setLineWidth(1.3f);
                                set1.setColor(Color.BLUE);
                                set1.setCircleRadius(4f);
                                set1.setCircleColor(Color.BLUE);
                                set1.setDrawFilled(false);
                                set1.setDrawValues(true);

                                LineData data = new LineData(xYear,set1);
                                yearLineChart.setData(data);
                                //yearLineChart.setDescription("");
                                data.setValueTextColor(Color.BLUE);
                                data.setValueTextSize(10f);
                                //yearLineChart.animateXY(500, 500);
                                yearLineChart.invalidate();


                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WaterChart.this, error.toString(), Toast.LENGTH_LONG).show();
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


