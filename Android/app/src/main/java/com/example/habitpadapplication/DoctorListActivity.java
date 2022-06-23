package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.habitpadapplication.Adapters.DoctorAdapter;
import com.example.habitpadapplication.Adapters.FoodAdapter;
import com.example.habitpadapplication.Model.Doctor;
import com.example.habitpadapplication.Model.Food;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DoctorListActivity extends AppCompatActivity {



    private RecyclerView doctorRecyclerView;
    private RecyclerView.LayoutManager manager;
    private DoctorAdapter Dadapter;
    private List<Doctor> doctors;

    private Context context;
    private String intentUserID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Doctor List");
        setContentView(R.layout.activity_doctor_list);

        intentUserID = getIntent().getExtras().getString("intentUserID");
        doctorRecyclerView = findViewById(R.id.drCards);

        manager = new LinearLayoutManager(DoctorListActivity.this);
        doctorRecyclerView.setLayoutManager(manager);
        doctorRecyclerView.setHasFixedSize(true);
        doctors = new ArrayList<>();
        Dadapter = new DoctorAdapter(DoctorListActivity.this, doctors);

        getDoctorsList();


    }

    private void getDoctorsList() {

        // Initializing Request queue

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.GET_DOCTOR_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            //Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("doctorlist");

                            for (int i = 0; i<jsonArray.length(); i++){

                                JSONObject object = jsonArray.getJSONObject(i);
                                String doctorID = object.getString("doctorID").trim();
                                String doctorPhoto = object.getString("doctorPhoto").trim();
                                String doctorName = object.getString("doctorName").trim();
                                String doctorEmail = object.getString("doctorEmail").trim();
                                String doctorPhone = object.getString("doctorPhone").trim();
                                String doctorEducation = object.getString("doctorEducation").trim();
                                String doctorExp = object.getString("doctorExp").trim();
                                String doctorHospital = object.getString("doctorHospital").trim();


                                Doctor doctor = new Doctor(doctorID,doctorPhoto,doctorName,doctorEmail,doctorPhone,doctorEducation,doctorExp,doctorHospital);
                                doctors.add(doctor);
                            }

                        }catch (Exception e){
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }

                        doctorRecyclerView.setAdapter(Dadapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(DoctorListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

//    public void onBackPressed() {
//        Intent intent = new Intent(DoctorListActivity.this, HomeActivity.class);
//        startActivity(intent);
//        finish();
//    }
}