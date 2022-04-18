package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DoctorSchedule extends AppCompatActivity {

    private TextView timer1, timer2;
    private MaterialButton btn;
    private String doctorID;

    int t1Hr, t1Min, t2Hr, t2Min;
    SimpleDateFormat sdf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Schedule");
        setContentView(R.layout.activity_doctor_schedule);

        timer1 = findViewById(R.id.timer1);
        timer2 = findViewById(R.id.timer2);
        btn = findViewById(R.id.confirm_btn);

        Intent intent = getIntent();
        doctorID = intent.getExtras().getString("doctorID");


        timer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(DoctorSchedule.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                t1Hr = hourOfDay;
                                t1Min = minute;

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0, 0, 0, t1Hr, t1Min);

                                String dateAndroid = android.text.format.DateFormat.format(
                                        "kk:mm aa", calendar).toString();

                                timer1.setText(dateAndroid);
                            }
                        }, 12, 0, false);
                timePickerDialog.updateTime(t1Hr, t1Min);
                timePickerDialog.show();
            }
        });

        timer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(DoctorSchedule.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                t2Hr = hourOfDay;
                                t2Min = minute;

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0, 0, 0, t2Hr, t2Min);

                                String dateAndroid = android.text.format.DateFormat.format(
                                        "kk:mm aa", calendar).toString();

                                timer2.setText(dateAndroid);
                            }
                        }, 12, 0, false);
                timePickerDialog.updateTime(t2Hr, t2Min);
                timePickerDialog.show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {
                int min = findMinutes();

                Calendar calendar = Calendar.getInstance();
                ArrayList<String> results = new ArrayList<>();
                sdf = new SimpleDateFormat("hh:mm aaa");

                calendar.set(0, 0, 0, t1Hr, t1Min);

                for (int i = 0; i < min; i++) {
                    String  day1 = sdf.format(calendar.getTime());

                    // add 30 minutes to the current time; the hour adjusts automatically!
                    calendar.add(Calendar.MINUTE, 30);
                    //String day2 = sdf.format(calendar.getTime());

                    //String day = day1 + " - " + day2;
                    results.add(day1);
                }

                if(results.size() != 0) {
//                    ref.setValue(null);
                    for(String slot: results) {
//                        ref.child(slot).setValue("Available");
                          Toast.makeText(getApplicationContext(), slot, Toast.LENGTH_SHORT).show();
                          AddDoctorSlot(doctorID, slot, "Available");
//                        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
//                        MainActivity.redirectActivity(DoctorSchedule.this, MainActivity.class);
                          finish();
                   }
                }
                else
                    Toast.makeText(getApplicationContext(), "Please select valid Time slot", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private int findMinutes() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(0, 0, 0, t1Hr, t1Min);
        String d1 = android.text.format.DateFormat.format(
                "kk:mm", calendar).toString();

        calendar.set(0, 0, 0, t2Hr, t2Min);
        String d2 = android.text.format.DateFormat.format(
                "kk:mm", calendar).toString();

        sdf = new SimpleDateFormat("hh:mm");

        Date date1 = null;
        try {
            date1 = sdf.parse(d1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = sdf.parse(d2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = date2.getTime() - date1.getTime();
        int min = (int) (diff / (1000 * 60));
        min = min/30;
        return (min);
    }

    private void AddDoctorSlot(String doctorID, String slotTime, String slotAvailable) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.ADD_DOCTOR_SLOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {
                                Toast.makeText(DoctorSchedule.this,message,Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            Toast.makeText(DoctorSchedule.this,"Save Error!" + e.toString(),Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DoctorSchedule.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("doctorID", doctorID);
                params.put("slotTime",slotTime);
                params.put("slotAvailable", slotAvailable);



                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed(){
        Intent intent = new Intent(DoctorSchedule.this, DoctorMainActivity.class);
        startActivity(intent);

        finish();
    }
}