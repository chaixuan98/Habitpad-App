package com.example.habitpadapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.habitpadapplication.Adapters.TipViewAdpater;
import com.example.habitpadapplication.Model.Tip;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewTipActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager manager;
    private List<Tip> tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Tips and Information");
        setContentView(R.layout.activity_view_tip);

        recyclerView = findViewById(R.id.tip_recyclerview);
        manager = new LinearLayoutManager(ViewTipActivity.this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        tips = new ArrayList<>();

        getTips();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.bottom_tip);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.bottom_tip:
                        startActivity(new Intent(getApplicationContext(), ViewTipActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.bottom_appointment:
//                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;

                    case R.id.bottom_profile:
//                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;

                }
                return false;
            }
        });

    }

    private void getTips(){
        // Initializing Request queue
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.GET_TIP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("tip");

                            for (int i = 0; i<jsonArray.length(); i++){

                                JSONObject object = jsonArray.getJSONObject(i);
                                String tipID = object.getString("tipID").trim();
                                String tipDetails = object.getString("tipDetails").trim();
                                String tipPhoto = object.getString("tipPhoto").trim();
                                String tipDateTime = object.getString("tipDateTime").trim();


                                Tip tip = new Tip(tipID,tipDetails,tipPhoto, tipDateTime);
                                tips.add(tip);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        TipViewAdpater adapter = new TipViewAdpater(ViewTipActivity.this,tips);
                        recyclerView.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ViewTipActivity.this, error.toString(),Toast.LENGTH_LONG).show();

            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent(ViewTipActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}