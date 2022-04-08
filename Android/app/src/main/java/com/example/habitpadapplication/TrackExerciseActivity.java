package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.habitpadapplication.Adapters.WorkoutAdapter;
import com.example.habitpadapplication.Model.Workout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TrackExerciseActivity extends AppCompatActivity {

    private SearchView workoutSearchView;
    private RecyclerView workoutRecyclerView;
    private RecyclerView.LayoutManager manager;
    private WorkoutAdapter Wadapter;
    private List<Workout> workouts;

    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_track_exercise);
        setTitle("Workout List");

        context=getApplicationContext();

        workoutSearchView = findViewById(R.id.workout_search);
        workoutRecyclerView = findViewById(R.id.workout_list);

        manager = new LinearLayoutManager(TrackExerciseActivity.this);
        workoutRecyclerView.setLayoutManager(manager);
        workoutRecyclerView.setHasFixedSize(true);
        workouts = new ArrayList<>();
        Wadapter = new WorkoutAdapter(TrackExerciseActivity.this,workouts);

        getWorkoutsList();

        workoutSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
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
        Intent intent = new Intent(TrackExerciseActivity.this, HomeActivity.class);
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
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        }
        else {
            // at last we are passing that filtered
            // list to our adapter class.
            Wadapter.filterList(filteredlist);
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
                            }

                        }catch (Exception e){
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                        //WorkoutAdapter adapter = new WorkoutAdapter(TrackExerciseActivity.this,workouts);
                        workoutRecyclerView.setAdapter(Wadapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(TrackExerciseActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

}