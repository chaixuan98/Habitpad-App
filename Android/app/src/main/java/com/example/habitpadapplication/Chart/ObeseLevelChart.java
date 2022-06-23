package com.example.habitpadapplication.Chart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
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
    private int level = 0, todaylevel = 0, level2 = 0;
    private TextView habitChange;
    private ImageView monthOption;
    private TextView monthView;

    LineChart monthLineChart;

    ArrayList<String> xMonth;
    ArrayList<Entry> yMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Obese Level Charts");
        setContentView(R.layout.activity_obese_level_chart);

        Intent intent = getIntent();
        intentUserID = intent.getExtras().getString("intentUserID");

        monthView = findViewById(R.id.month_view);
        monthOption = findViewById(R.id.month_option);

        monthView.setText(DateHandler.monthFormat(DateHandler.getCurrentFormedDate()));
        monthOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenMonthOptionDialog();
            }
        });

        habitChange = findViewById(R.id.obese_habit_change);

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
        getOMonth(intentUserID, DateHandler.monthFormat2(DateHandler.getCurrentFormedDate()));

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

    private void OpenMonthOptionDialog()
    {
        final Dialog monthdialog = new Dialog(ObeseLevelChart.this);
        monthdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        monthdialog.setContentView(R.layout.month_select_dialog);
        monthdialog.setTitle("Select Month");
        monthdialog.show();
        Window weightWindow = monthdialog.getWindow();
        weightWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        final NumberPicker monthSelectNumberPicker= (NumberPicker) monthdialog.findViewById(R.id.month_select_numberPicker);
        TextView monthSelectTV = monthdialog.findViewById(R.id.month_select_tv);
        final String[] monthSelectValue;


        monthSelectValue= getResources().getStringArray(R.array.month_select);
        monthSelectNumberPicker.setMaxValue(11);
        monthSelectNumberPicker.setMinValue(0);
        monthSelectNumberPicker.setWrapSelectorWheel(false);
        monthSelectNumberPicker.setDisplayedValues(monthSelectValue);
        monthSelectTV.setText(String.format("Month: %s",monthSelectNumberPicker.getValue()));



        monthSelectNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                monthSelectTV.setText(String.format("Month: %s",monthSelectValue[i1]));
                monthView.setText(monthSelectValue[i1]);

            }
        });


        /* cancel button click action */
        Button cancelbtn = (Button)monthdialog.findViewById(R.id.months_select_cancel_button);
        cancelbtn.setEnabled(true);
        cancelbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                monthdialog.cancel();
            }
        });


        /* ok button click action */
        Button confirmBtn = (Button)monthdialog.findViewById(R.id.months_select_confirm_button);
        confirmBtn.setEnabled(true);
        confirmBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                monthdialog.cancel();

                getOMonth(intentUserID,DateHandler.monthFormat3(monthView.getText().toString()));

                Toast.makeText(ObeseLevelChart.this, monthView.getText(), Toast.LENGTH_LONG).show();

            }
        });


    }


    private void getOMonth(final String userID, final String month){

        xMonth = new ArrayList<String>();
        yMonth = new ArrayList<Entry>();

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

                                    habitChange.setVisibility(View.VISIBLE);

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


                                    xMonth.add(DateHandler.dateFormat2(addObeseDate));
                                    yMonth.add(new Entry((level),i));

                                }

                                for (int i = jsonArray.length()-1; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String obeseLevel = object.getString("obeseLevel");
                                    String addObeseDate = object.getString("addObeseDate").trim();

                                    switch (obeseLevel) {
                                        case "Insufficient_Weight":
                                            todaylevel = 0;
                                            break;
                                        case "Normal_Weight":
                                            todaylevel = 1;
                                            break;
                                        case "Overweight_Level_I":
                                            todaylevel = 2;
                                            break;
                                        case "Overweight_Level_II":
                                            todaylevel = 3;
                                            break;
                                        case "Obesity_Type_I":
                                            todaylevel = 4;
                                            break;
                                        case "Obesity_Type_II":
                                            todaylevel = 5;
                                            break;
                                        case "Obesity_Type_III":
                                            todaylevel = 6;
                                            break;

                                    }
                                    //Toast.makeText(ObeseLevelChart.this, addObeseDate + level, Toast.LENGTH_LONG).show();

                                }

                                for (int i = jsonArray.length()-3; i < jsonArray.length()-1; i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String obeseLevel = object.getString("obeseLevel");
                                    String addObeseDate = object.getString("addObeseDate").trim();

                                    switch (obeseLevel) {
                                        case "Insufficient_Weight":
                                            level2 = 0;
                                            break;
                                        case "Normal_Weight":
                                            level2 = 1;
                                            break;
                                        case "Overweight_Level_I":
                                            level2 = 2;
                                            break;
                                        case "Overweight_Level_II":
                                            level2 = 3;
                                            break;
                                        case "Obesity_Type_I":
                                            level2 = 4;
                                            break;
                                        case "Obesity_Type_II":
                                            level2 = 5;
                                            break;
                                        case "Obesity_Type_III":
                                            level2 = 6;
                                            break;

                                    }


                                }

                                if(todaylevel < level2){
                                    habitChange.setText("Your Habit have changed compare to two previous record(obese level decrease)");
                                    //Toast.makeText(ObeseLevelChart.this, "Your Habit have changed (obese level decrease )", Toast.LENGTH_LONG).show();
                                }

                                if(todaylevel > level2){
                                    habitChange.setText("Your Habit have changed compare to two previous record(obese level increase)");
                                    //Toast.makeText(ObeseLevelChart.this, "Your Habit have changed (obese level increase)", Toast.LENGTH_LONG).show();
                                }

                                if(level2 == todaylevel){
                                    habitChange.setText("Your Habit no changes compare to two previous record");
                                    //Toast.makeText(ObeseLevelChart.this, "Your Habit no change", Toast.LENGTH_LONG).show();
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

                            if(success.equals("0")) {
                                Toast.makeText(ObeseLevelChart.this, "No data for this month", Toast.LENGTH_LONG).show();
                                habitChange.setVisibility(View.GONE);
                                monthLineChart.setNoDataText("No data available for this month");
                                monthLineChart.clear();
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
                params.put("obeseMonth",month);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

}