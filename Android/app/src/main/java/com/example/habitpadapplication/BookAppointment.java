package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.habitpadapplication.Adapters.BookAppointmentAdapter;
import com.example.habitpadapplication.Model.Doctor;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookAppointment extends AppCompatActivity {

    private TextView drName, drExp, drDegree, drPlace, drPhone, drEmail, noSlotTV;
    private CircleImageView drImg;
    private TextInputEditText drRemark;
    private MaterialButton drBtn;
    private EditText bookDate;
    private String intentDrID, strDrImg, strDrName, strDrExp, strDrDegree, strDrPlace, strDrPhone, strDrEmail;

    private RecyclerView slotCards;
    ArrayList<String> slotTimes = new ArrayList<>();
    ArrayList<String> docTimings = new ArrayList<>();
    ArrayList<String> docReserved = new ArrayList<>();
    BookAppointmentAdapter adapter;

    HashMap<String, ArrayList<String>> itemList = new HashMap<>();

    Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    private DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Book Appointment");
        setContentView(R.layout.activity_book_appointment);

        drImg = findViewById(R.id.dr_app_img);
        drName = findViewById(R.id.dr_app_name);
        drExp = findViewById(R.id.dr_app_exp);
        drDegree = findViewById(R.id.dr_app_degree);
        drPlace = findViewById(R.id.dr_app_place);
        drPhone = findViewById(R.id.dr_app_phone);
        drEmail = findViewById(R.id.dr_app_email);
        slotCards = findViewById(R.id.dr_app_slot_rv);
        drRemark = findViewById(R.id.remarks);
        drBtn = findViewById(R.id.dr_app_btn);
        bookDate = findViewById(R.id.book_date);
        noSlotTV = findViewById(R.id.noslot_tv);


        intentDrID = getIntent().getExtras().getString("dr_app_ID");
        strDrImg = getIntent().getExtras().getString("dr_app_img");
        strDrName = getIntent().getExtras().getString("dr_app_name");
        strDrExp = getIntent().getExtras().getString("dr_app_exp");
        strDrDegree = getIntent().getExtras().getString("dr_app_degree");
        strDrPlace = getIntent().getExtras().getString("dr_app_place");
        strDrPhone = getIntent().getExtras().getString("dr_app_phone");
        strDrEmail = getIntent().getExtras().getString("dr_app_email");

        Glide.with(this).asBitmap().load(strDrImg)
                .error(R.drawable.doctor_avatar_white).fitCenter().into(drImg);
        drName.setText(strDrName);
        drExp.setText(strDrExp);
        drDegree.setText(strDrDegree);
        drPlace.setText(strDrPlace);
        drPhone.setText(strDrPhone);
        drEmail.setText(strDrEmail);

        bookDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(BookAppointment.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,
                        year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

//        setListener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int day) {
//
//                month = month + 1;
//                String date = day + "-" + month + "-" + year;
//                bookDate.setText(DateHandler.dateFormat1(date));
//                getDoctorsSlot(bookDate.getText().toString().trim());
//
//            }
//        };

        bookDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(BookAppointment.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        month = month + 1;
                        String date = day + "-" + month + "-" + year;
                        bookDate.setText(DateHandler.dateFormat1(date));
                        getDoctorsSlot(bookDate.getText().toString().trim());
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });



    }

    private void getDoctorsSlot(final String date) {

        // Initializing Request queue

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        slotTimes.clear();
        docTimings.clear();
        docReserved.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_DOCTOR_SLOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("dslot");

                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String slotTime = object.getString("slotTime").trim();
                                    String slotAvailable = object.getString("slotAvailable").trim();
                                    noSlotTV.setVisibility(View.GONE);
                                    slotCards.setVisibility(View.VISIBLE);

                                    slotTimes.add(slotTime);

                                    if (slotAvailable.equals("Available")) {
                                        docTimings.add(slotTime);
                                    }
                                    else if(slotAvailable.equals("Reserved")) {
                                        docReserved.add(slotTime);
                                    }

                                }
                                Collections.sort(slotTimes, new Comparator<String>() {
                                    @SuppressLint("SimpleDateFormat")
                                    @Override
                                    public int compare(String o1, String o2) {
                                        try {
                                            return new SimpleDateFormat("hh:mm a").parse(o1).compareTo(new SimpleDateFormat("hh:mm a").parse(o2));
                                        } catch (ParseException e) {
                                            return 0;
                                        }
                                    }
                                });

                                slotCards(slotTimes);
                            }

                            if(success.equals("0")) {
//                                slotTimes.clear();
//                                docTimings.clear();
//                                docReserved.clear();
                                slotCards.setVisibility(View.GONE);
                                noSlotTV.setVisibility(View.VISIBLE);

                                //Toast.makeText(BookAppointment.this, message, Toast.LENGTH_LONG).show();
                            }

                        }catch (Exception e){
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(BookAppointment.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("doctorID",intentDrID);
                params.put("slotDate",date);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void slotCards(ArrayList<String> slotTimes) {
        slotCards.setHasFixedSize(true);
        slotCards.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<String> sectionList = new ArrayList<>();
        sectionList.add("Today's Slots");
        itemList.put(sectionList.get(0), slotTimes);
        adapter = new BookAppointmentAdapter(this, sectionList, itemList, docTimings, docReserved, intentDrID);
        GridLayoutManager manager = new GridLayoutManager(this, 3);

        slotCards.setLayoutManager(manager);
        adapter.setLayoutManager(manager);
        slotCards.setAdapter(adapter);

        drBtn.setOnClickListener(v -> {
            String sItem = adapter.getSelected();
            if (sItem != null) {
//                Intent intent = new Intent(getApplicationContext(), ActiveAppointments.class);
//
//                //Toast.makeText(this, sItem, Toast.LENGTH_SHORT).show();
//
//                intent.putExtra("slot", sItem);
//                intent.putExtra("msg", drRemark.getText().toString());
//                intent.putExtra("dr_name", strDrName);
//
//
//                startActivity(intent);
//                finish();
            }
            else
                Toast.makeText(this, "Please select a Slot", Toast.LENGTH_SHORT).show();
        });
    }

    private void UpdateSlot(String slotAvailable) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.UPDATE_DOCTOR_SLOT_URL,
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

                            }

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),"Save Error!" + e.toString(),Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put("slotID", slotID);
                params.put("slotAvailable", slotAvailable);



                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent(BookAppointment.this, DoctorListActivity.class);
        startActivity(intent);
        finish();
    }
}