package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.habitpadapplication.Adapters.FoodRecentAdapter;
import com.example.habitpadapplication.Adapters.WorkoutAdapter;
import com.example.habitpadapplication.Adapters.WorkoutRecentAdapter;
import com.example.habitpadapplication.Model.FoodRecent;
import com.example.habitpadapplication.Model.Workout;
import com.example.habitpadapplication.Model.WorkoutRecent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WorkoutActivity extends AppCompatActivity {

    private SearchView workoutSearchView;
    private RecyclerView workoutRecyclerView, workoutRecentRecyclerView;

    private WorkoutAdapter Wadapter;
    private List<Workout> workouts;

    private WorkoutRecentAdapter Wradapter;
    private List<WorkoutRecent> workoutRecents;

    private Context context;

    private TextView recentWorkoutTV, workoutTV;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_workout);
        setTitle("Workout List");

        context=getApplicationContext();

        workoutSearchView = findViewById(R.id.workout_search);
        recentWorkoutTV = findViewById(R.id.recent_workout_tv);
        workoutTV = findViewById(R.id.workout_tv);

        userID= getIntent().getExtras().getString("intentUserID");

        workoutRecyclerView = findViewById(R.id.workout_list);
        LinearLayoutManager workoutLinearLayoutManager = new LinearLayoutManager(this);
        workoutRecyclerView.setLayoutManager(workoutLinearLayoutManager);

        workoutRecentRecyclerView = findViewById(R.id.workout_recent_list);
        LinearLayoutManager workoutRecentLinearLayoutManager = new LinearLayoutManager(this);
        workoutRecentRecyclerView.setLayoutManager(workoutRecentLinearLayoutManager);

        getRecentWorkoutsList();
        getWorkoutsList();

        workoutSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                filterRecent(newText);
                return false;
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed(){
        Intent intent = new Intent(WorkoutActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Workout> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (Workout item : workouts) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getWorkoutType().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            //Toast.makeText(this, "No Data Found in Workout List..", Toast.LENGTH_SHORT).show();
            workoutTV.setText("No Data Found");
        }
        else {
            // at last we are passing that filtered
            // list to our adapter class.
            Wadapter.filterList(filteredlist);
        }
    }

    private void filterRecent(String text) {
        // creating a new array list to filter our data.
        ArrayList<WorkoutRecent> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (WorkoutRecent item : workoutRecents) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getWorkoutType().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            //Toast.makeText(this, "No Data Found in Recent Added..", Toast.LENGTH_SHORT).show();
            recentWorkoutTV.setText("No Data Found");
        }
        else {
            // at last we are passing that filtered
            // list to our adapter class.
            Wradapter.filterList(filteredlist);
        }
    }

    private void getWorkoutsList(){
        // Initializing Request queue

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.GET_WORKOUT_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            workouts = new ArrayList<>();
                            progressDialog.dismiss();
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("workout");

                            for (int i = 0; i<jsonArray.length(); i++){

                                JSONObject object = jsonArray.getJSONObject(i);
                                String workoutID = object.getString("workoutID").trim();
                                String workoutType = object.getString("workoutType").trim();
                                String workoutMET = object.getString("workoutMET");

                                Workout workout = new Workout(workoutID,workoutType,workoutMET);
                                workouts.add(workout);
                                Wadapter = new WorkoutAdapter(WorkoutActivity.this,workouts);
                                workoutRecyclerView.setAdapter(Wadapter);
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
                Toast.makeText(WorkoutActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void getRecentWorkoutsList(){
        // Initializing Request queue

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_RECENT_WORKOUT_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            workoutRecents = new ArrayList<>();
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("recentworkout");

                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String workoutID = object.getString("workoutID").trim();
                                    String workoutType = object.getString("workoutType").trim();
                                    String workoutMET = object.getString("workoutMET");


                                    WorkoutRecent workoutRecent = new WorkoutRecent(workoutID, workoutType, workoutMET);
                                    workoutRecents.add(workoutRecent);

                                    Wradapter = new WorkoutRecentAdapter(WorkoutActivity.this, workoutRecents);
                                    workoutRecentRecyclerView.setAdapter(Wradapter);
                                }
                            }

                            if(success.equals("0")) {
                                recentWorkoutTV.setText("No recent workout added");
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
                Toast.makeText(WorkoutActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userID", userID);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }



}