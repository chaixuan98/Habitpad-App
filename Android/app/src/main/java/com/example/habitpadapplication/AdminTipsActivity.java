package com.example.habitpadapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.example.habitpadapplication.Adapters.TipAdapter;
import com.example.habitpadapplication.Model.Tip;
import com.google.android.material.button.MaterialButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AdminTipsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Variable declarations
    private MaterialButton addtip_btn;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager manager;
    private List<Tip> tips;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;


    SharedPreferences sharedPreferences;

    public static final String fileName = "adminLogin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_admin_tips);

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);


        // Getting UI views from our xml file
        addtip_btn = findViewById(R.id.addtip_button);

        addtip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminTipsActivity.this,AddTipsActivity.class));

            }
        });

        recyclerView = findViewById(R.id.tip_recyclerview);
        manager = new LinearLayoutManager(AdminTipsActivity.this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        tips = new ArrayList<>();

        getTips();


        drawerLayout = findViewById(R.id.tip_drawerlayout);
        navigationView = findViewById(R.id.tip_navigationview);
        toolbar = findViewById(R.id.tiptoolbar);


        //Navigation Drawer Menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed(){

        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }


    private void getTips(){
        // Initializing Request queue

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.GET_TIP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
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
                        TipAdapter adapter = new TipAdapter(AdminTipsActivity.this,tips);
                        recyclerView.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminTipsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.admin_nav_logout:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent i = new Intent(getApplicationContext(), UserOptionActivity.class);
                startActivity(i);
                //((AdminTipsActivity) getApplicationContext()).finish();


                break;
        }
        return true;
    }
}