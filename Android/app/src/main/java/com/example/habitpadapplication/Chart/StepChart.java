package com.example.habitpadapplication.Chart;

import static com.example.habitpadapplication.DateHandler.dateFormat2;
import static com.example.habitpadapplication.DateHandler.getCurrentFormedDate;
import static com.example.habitpadapplication.DateHandler.monthFormat;
import static com.example.habitpadapplication.DateHandler.yearFormat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
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
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class StepChart extends AppCompatActivity {

    private String intentUserID, userStepGoal, lastLaunchDate, thisDay;
    private int counter = 0, stepCount;
    private TextView consecutiveDay, consecutiveWeek, consecutiveMonth;


    LineChart weekLineChart;
    LineChart monthLineChart;
    LineChart yearLineChart;

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
        setTitle("Step Charts");
        setContentView(R.layout.activity_step_chart);

        intentUserID = getIntent().getExtras().getString("intentUserID");
        userStepGoal = getIntent().getExtras().getString("stepGoal");


        TextView stepInWeek=(TextView) findViewById(R.id.textView2);
        TextView stepInMonth=(TextView) findViewById(R.id.textView3);
        TextView stepInYear=(TextView) findViewById(R.id.textView4);


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

        stepInWeek.setText("Current week "+ start +"-"+end);
        stepInMonth.setText("Current month:"+ monthFormat(getCurrentFormedDate()));
        stepInYear.setText("Current year:"+ yearFormat(getCurrentFormedDate()));


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
        getSWeek(intentUserID);

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
        getSMonth(intentUserID);

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
        getSYear(intentUserID);



        TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();

//        //Tab 1
//        TabHost.TabSpec spec = host.newTabSpec("Today");
//        spec.setContent(R.id.linearLayout);
//        spec.setIndicator("Today");
//        host.addTab(spec);

        //Tab 2
        TabHost.TabSpec spec = host.newTabSpec("week");
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
        Intent intent = new Intent(StepChart.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void getSWeek(final String userID){

        xWeek = new ArrayList<String>();
        yWeek = new ArrayList<Entry>();
        gWeek = new ArrayList<Entry>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_STEP_WEEK_GRAPH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.i("tagconvertstr", "["+response+"]");

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("sweek");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    stepCount = object.getInt("totalStep");
                                    String addStepDate = object.getString("stepDate").trim();



                                    xWeek.add(addStepDate);
                                    yWeek.add(new Entry((stepCount),i));
                                    gWeek.add(new Entry(Integer.valueOf(userStepGoal),i));

                                }

                                ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

                                LineDataSet set1 = new LineDataSet(yWeek, "Step Count");
                                set1.setLineWidth(1.3f);
                                set1.setCircleRadius(4f);
                                set1.setColor(Color.BLUE);
                                set1.setCircleColor(Color.BLUE);
                                set1.setDrawFilled(false);
                                set1.setDrawValues(true);

                                LineDataSet set2 = new LineDataSet(gWeek, "Step goal");
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
                Toast.makeText(StepChart.this, error.toString(), Toast.LENGTH_LONG).show();
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

    private void getSMonth(final String userID){

        xMonth = new ArrayList<String>();
        yMonth = new ArrayList<Entry>();
        gMonth = new ArrayList<Entry>();
        // Initializing Request queue

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_STEP_MONTH_GRAPH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.i("tagconvertstr", "["+response+"]");

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("smonth");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    stepCount = object.getInt("totalStep");
                                    String addStepDate = object.getString("stepDate").trim();


                                    xMonth.add(dateFormat2(addStepDate));
                                    yMonth.add(new Entry((stepCount),i));
                                    gMonth.add(new Entry(Integer.valueOf(userStepGoal),i));

                                }
                                if(jsonArray.length()>2) {
                                    for (int i = jsonArray.length()-3; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        stepCount = object.getInt("totalStep");

                                        if (stepCount >= Integer.valueOf(userStepGoal)) {
                                            counter++;
                                        }
                                    }
                                }
                                consecutiveMonth.setText("Achieve goal " + counter + " days for the last 3 days");
                                consecutiveWeek.setText("Achieve goal " + counter + " days for the last 3 days");

                                if(counter > 2){
                                    consecutiveMonth.setText("Achieve goal for the last 3 days (Step count habit is changed)");
                                    consecutiveWeek.setText("Achieve goal for the last 3 days (Step count habit is changed)");

                                }


                                ArrayList<ILineDataSet> lineDataSetsMonth = new ArrayList<>();

                                LineDataSet set1 = new LineDataSet(yMonth, "Step count");
                                set1.setLineWidth(1.3f);
                                set1.setCircleRadius(4f);
                                set1.setColor(Color.BLUE);
                                set1.setCircleColor(Color.BLUE);
                                set1.setDrawFilled(false);
                                set1.setDrawValues(true);

                                LineDataSet set2 = new LineDataSet(gMonth, "Step goal");
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
                Toast.makeText(StepChart.this, error.toString(), Toast.LENGTH_LONG).show();
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

    private void getSYear(final String userID){

        xYear = new ArrayList<String>();
        yYear = new ArrayList<Entry>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_STEP_YEAR_GRAPH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.i("tagconvertstr", "["+response+"]");

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("syear");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    stepCount = object.getInt("yearStep");
                                    String MonthName = object.getString("MonthName").trim();

                                    xYear.add(MonthName);
                                    yYear.add(new Entry((stepCount),i));

                                }
                                LineDataSet set1 = new LineDataSet(yYear, "Step count");
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
                Toast.makeText(StepChart.this, error.toString(), Toast.LENGTH_LONG).show();
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