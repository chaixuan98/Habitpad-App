package com.example.habitpadapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.habitpadapplication.Adapters.LeaderboardAdapter;
import com.example.habitpadapplication.Model.Leaderboard;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardFragment extends Fragment {
    private RecyclerView leaderboardList;
    private RecyclerView.LayoutManager manager;
    private List<Leaderboard> leaderboards;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =   inflater.inflate(R.layout.fragment_leaderboard, container, false);

        leaderboardList = view.findViewById(R.id.leaderboard_list);
        manager = new LinearLayoutManager(getContext());
        leaderboardList.setLayoutManager(manager);
        leaderboardList.setHasFixedSize(false);
        leaderboards = new ArrayList<>();

        getLeaderboard();

        return view;
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
                        LeaderboardAdapter adapter = new LeaderboardAdapter(getContext(),leaderboards);
                        leaderboardList.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), error.toString(),Toast.LENGTH_LONG).show();

            }
        });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }
}