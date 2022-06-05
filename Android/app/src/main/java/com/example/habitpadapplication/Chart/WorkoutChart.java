package com.example.habitpadapplication.Chart;

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

public class WorkoutChart extends AppCompatActivity {

    private String intentUserID, workoutGoal, lastLaunchDate;
    private int counter, workoutDateCal;
//    private TextView consecutiveDay, consecutiveWeek, consecutiveMonth;

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
        setContentView(R.layout.workout_chart);
        setTitle("Workout Charts");

        Intent intent = getIntent();
        intentUserID = intent.getExtras().getString("intentUserID");
        workoutGoal = intent.getExtras().getString("workoutGoal");


        TextView drinkInDay=(TextView) findViewById(R.id.textView);
        TextView drinkInWeek=(TextView) findViewById(R.id.textView2);
        TextView drinkInMonth=(TextView) findViewById(R.id.textView3);
        TextView drinkInYear=(TextView) findViewById(R.id.textView4);

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

        drinkInDay.setText("Today :"+ DateHandler.getCurrentFormedDate());
        drinkInWeek.setText("Current week"+ start +"-"+end);
        drinkInMonth.setText("Current month:"+DateHandler.monthFormat(DateHandler.getCurrentFormedDate()));
        drinkInYear.setText("Current year:"+DateHandler.yearFormat(DateHandler.getCurrentFormedDate()));

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
        getWDay(intentUserID);

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
        getWWeek(intentUserID);

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
        getWMonth(intentUserID);

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
        getWYear(intentUserID);



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
        Intent intent = new Intent(WorkoutChart.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void getWDay(final String userID){

        xDay = new ArrayList<String>();
        yDay = new ArrayList<Entry>();
        // Initializing Request queue

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_WORKOUT_DAY_GRAPH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.i("tagconvertstr", "["+response+"]");

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("wday");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    int calBurntOnce = object.getInt("calBurntOnce");
                                    String addWorkoutTime = object.getString("addWorkoutTime").trim();

                                    xDay.add(DateHandler.timeFormat(addWorkoutTime));
                                    yDay.add(new Entry((calBurntOnce),i));

                                }

//                                getUserWorkoutConsecutive(workoutDateCal);

                                LineDataSet set1 = new LineDataSet(yDay, "Calories burned (kcal)");
                                set1.setLineWidth(1.3f);
                                set1.setColor(Color.BLUE);
                                set1.setCircleRadius(4f);
                                set1.setCircleColor(Color.BLUE);
                                set1.setDrawFilled(false);
                                set1.setDrawValues(true);

                                LineData data = new LineData(xDay, set1);
                                dayLineChart.setData(data);
                                dayLineChart.setDescription("");
                                data.setValueTextColor(Color.BLUE);
                                data.setValueTextSize(12f);
//                                dayLineChart.animateXY(2000, 2000);
                                dayLineChart.invalidate();


                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WorkoutChart.this, error.toString(), Toast.LENGTH_LONG).show();
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

    private void getWWeek(final String userID){

        xWeek = new ArrayList<String>();
        yWeek = new ArrayList<Entry>();
        gWeek = new ArrayList<Entry>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_WORKOUT_WEEK_GRAPH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.i("tagconvertstr", "["+response+"]");

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("wweek");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    workoutDateCal = object.getInt("workoutDateCal");
                                    String addWorkoutDate = object.getString("addWorkoutDate").trim();

                                    xWeek.add(addWorkoutDate);
                                    yWeek.add(new Entry((workoutDateCal),i));
                                    gWeek.add(new Entry(Integer.valueOf(workoutGoal),i));

                                }
//                                getUserWorkoutConsecutive(workoutDateCal);

                                ArrayList<ILineDataSet> lineDataSetsWeek = new ArrayList<>();

                                LineDataSet set1 = new LineDataSet(yWeek, "Calories burned (kcal)");
                                set1.setLineWidth(1.3f);
                                set1.setColor(Color.BLUE);
                                set1.setCircleColor(Color.BLUE);
                                set1.setCircleRadius(4f);
                                set1.setDrawFilled(false);
                                set1.setDrawValues(true);

                                LineDataSet set2 = new LineDataSet(gWeek, "Calories burned goal (kcal)");
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
//                                weekLineChart.animateXY(2000, 2000);
                                weekLineChart.invalidate();


                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WorkoutChart.this, error.toString(), Toast.LENGTH_LONG).show();
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

    private void getWMonth(final String userID){

        xMonth = new ArrayList<String>();
        yMonth = new ArrayList<Entry>();
        gMonth = new ArrayList<Entry>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_WORKOUT_MONTH_GRAPH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.i("tagconvertstr", "["+response+"]");

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("wmonth");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    workoutDateCal = object.getInt("workoutDateCal");
                                    String addWorkoutDate = object.getString("addWorkoutDate").trim();


                                    xMonth.add(DateHandler.dateFormat2(addWorkoutDate));
                                    yMonth.add(new Entry((workoutDateCal),i));
                                    gMonth.add(new Entry(Integer.valueOf(workoutGoal),i));

                                }

//                                getUserWorkoutConsecutive(workoutDateCal);

                                ArrayList<ILineDataSet> lineDataSetsMonth = new ArrayList<>();

                                LineDataSet set1 = new LineDataSet(yMonth, "Calories burned (kcal)");
                                set1.setLineWidth(1.3f);
                                set1.setColor(Color.BLUE);
                                set1.setCircleColor(Color.BLUE);
                                set1.setCircleRadius(4f);
                                set1.setDrawFilled(false);
                                set1.setDrawValues(true);

                                LineDataSet set2 = new LineDataSet(gMonth, "Calories burned goal (kcal)");
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
                                data.setValueTextSize(12f);
//                                monthLineChart.animateXY(2000, 2000);
                                monthLineChart.invalidate();


                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WorkoutChart.this, error.toString(), Toast.LENGTH_LONG).show();
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

    private void getWYear(final String userID){

        xYear = new ArrayList<String>();
        yYear = new ArrayList<Entry>();
        // Initializing Request queue

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_WORKOUT_YEAR_GRAPH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.i("tagconvertstr", "["+response+"]");

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("wyear");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    int workoutDateCal = object.getInt("workoutDateCal");
                                    String MonthName = object.getString("MonthName").trim();

                                    xYear.add(MonthName);
                                    yYear.add(new Entry((workoutDateCal),i));

                                }
                                LineDataSet set1 = new LineDataSet(yYear, "Calories burned (kcal)");
                                set1.setLineWidth(1.3f);
                                set1.setColor(Color.BLUE);
                                set1.setCircleRadius(4f);
                                set1.setCircleColor(Color.BLUE);
                                set1.setDrawFilled(false);
                                set1.setDrawValues(true);

                                LineData data = new LineData(xYear, set1);
                                yearLineChart.setData(data);
                                yearLineChart.setDescription("");
                                data.setValueTextColor(Color.BLUE);
                                data.setValueTextSize(12f);
                                yearLineChart.animateXY(2000, 2000);
                                yearLineChart.invalidate();


                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WorkoutChart.this, error.toString(), Toast.LENGTH_LONG).show();
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
//
//    private void getUserWorkoutConsecutive(final int work)
//    {
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_WORKOUT_CONSECUTIVE_URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                try {
//                    Log.i("tagconvertstr", "[" + response + "]");
//                    JSONObject jsonObject = new JSONObject(response);
//
//                    String success = jsonObject.getString("success");
//                    JSONArray jsonArray = jsonObject.getJSONArray("workoutcon");
//
//                    if (success.equals("1")) {
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject object = jsonArray.getJSONObject(i);
//
//                            lastLaunchDate = object.getString("workoutLastLaunchDate").trim();
//                            counter = object.getInt("workoutCounterDay");
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
//                            if (work >= Integer.parseInt(workoutGoal)) {
//
//                                if (Integer.parseInt(dayDifference) == 1) {
//                                    counter = counter + 1;
//                                }
//
//                                if (Integer.parseInt(dayDifference) > 1){
//                                    counter = 1;
//                                }
//                                UpdateUserWorkoutConsecutive(DateHandler.getCurrentFormedDate(), counter);
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
//                            Toast.makeText(WorkoutChart.this, "Unable to find difference", Toast.LENGTH_SHORT).show();
//                            e.printStackTrace();
//                        }
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(WorkoutChart.this, "Get user water consecutive error" + e.toString(), Toast.LENGTH_SHORT).show();
//                }
//
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(WorkoutChart.this, error.toString(), Toast.LENGTH_SHORT).show();
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
//    private void UpdateUserWorkoutConsecutive(final String date, final int counter){
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.UPDATE_USER_WORKOUT_CONSECUTIVE_URL,
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
//                            Toast.makeText(WorkoutChart.this, "update user workout consecutive error" + e.toString(), Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //Log.i("tagerror", "["+error+"]");
//                Toast.makeText(WorkoutChart.this, error.toString(), Toast.LENGTH_LONG).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String, String> params = new HashMap<>();
//                params.put("userID",intentUserID);
//                params.put("workoutLastLaunchDate", date);
//                params.put("workoutCounterDay",String.valueOf(counter));
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
//    private void PopupAchievementDialog() {
//
//        final Dialog achievementDialog = new Dialog(WorkoutChart.this);
//        achievementDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        achievementDialog.setContentView(R.layout.achievements_layout);
//        achievementDialog.setTitle("Calories Burned Habit Change");
//        achievementDialog.show();
//        achievementDialog.setCanceledOnTouchOutside(false);
//        achievementDialog.setCancelable(false);
//        Window window = achievementDialog.getWindow();
//        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//        TextView dialogDescription = achievementDialog.findViewById(R.id.achievement_dialog_description);
//        dialogDescription.setText("Congratulations, You have achieved your calories burned goal 3 days in a row.");
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