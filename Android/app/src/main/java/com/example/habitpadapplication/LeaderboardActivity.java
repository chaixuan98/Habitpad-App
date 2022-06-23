package com.example.habitpadapplication;

import static com.example.habitpadapplication.DateHandler.getCurrentFormedDate;
import static com.example.habitpadapplication.DateHandler.monthFormat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.habitpadapplication.Adapters.LeaderboardAdapter;
import com.example.habitpadapplication.Adapters.TipViewAdpater;
import com.example.habitpadapplication.Model.Leaderboard;
import com.example.habitpadapplication.Model.Tip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView leaderboardList;
    private RecyclerView.LayoutManager manager;
    private List<Leaderboard> leaderboards;
    private TextView leaderboardMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Leaderboards");
        setContentView(R.layout.activity_leaderboard);

        leaderboardMonth = findViewById(R.id.leaderboard_month);
        leaderboardMonth.setText("Current month:"+ monthFormat(getCurrentFormedDate()));

        leaderboardList = findViewById(R.id.leaderboard_list);
        manager = new LinearLayoutManager(LeaderboardActivity.this);
        leaderboardList.setLayoutManager(manager);
        leaderboardList.setHasFixedSize(true);
        leaderboards = new ArrayList<>();

        getLeaderboard();
    }

    private void getLeaderboard(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.GET_USER_RANKING_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("rankingList");

                            for (int i = 0; i<jsonArray.length(); i++){

                                JSONObject object = jsonArray.getJSONObject(i);
                                String rank = object.getString("rank").trim();
                                String username = object.getString("username").trim();
                                String totalUserStep = object.getString("totalUserStep").trim();


                                Leaderboard leaderboard = new Leaderboard(rank,username, totalUserStep);
                                leaderboards.add(leaderboard);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        LeaderboardAdapter adapter = new LeaderboardAdapter(LeaderboardActivity.this,leaderboards);
                        leaderboardList.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(LeaderboardActivity.this, error.toString(),Toast.LENGTH_LONG).show();

            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}