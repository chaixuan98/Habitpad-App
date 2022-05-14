package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.habitpadapplication.Adapters.FoodAdapter;
import com.example.habitpadapplication.Adapters.FoodRecentAdapter;
import com.example.habitpadapplication.Model.Food;
import com.example.habitpadapplication.Model.FoodRecent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodListActivity extends AppCompatActivity {

    private SearchView foodSearchView;
    private RecyclerView foodRecyclerView, foodRecentRecyclerView;
    private FoodAdapter Fadapter;
    private FoodRecentAdapter Fradapter;
    private List<Food> foods;
    private List<FoodRecent> foodRecents;
    private TextView recentFoodTV;

    private Context context;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_food_list);
        setTitle("Food List");


        foodSearchView = findViewById(R.id.food_search_bar);
        recentFoodTV = findViewById(R.id.recent_food_tv);

        userID= getIntent().getExtras().getString("intentUserID");


        foodRecyclerView = findViewById(R.id.food_list);
        LinearLayoutManager foodLinearLayoutManager = new LinearLayoutManager(this);
        foodRecyclerView.setLayoutManager(foodLinearLayoutManager);

        foodRecentRecyclerView = findViewById(R.id.food_recent_list);
        LinearLayoutManager foodRecentLinearLayoutManager = new LinearLayoutManager(this);
        foodRecentRecyclerView.setLayoutManager(foodRecentLinearLayoutManager);

        getRecentFoodsList();
        getFoodsList();


        foodSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

//        foodRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                int action = e.getAction();
//                switch (action) {
//                    case MotionEvent.ACTION_MOVE:
//                        rv.getParent().requestDisallowInterceptTouchEvent(true);
//                        break;
//                }
//                return false;
//            }
//
//            @Override
//            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//            }
//        });
//
//        foodRecentRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                int action = e.getAction();
//                switch (action) {
//                    case MotionEvent.ACTION_MOVE:
//                        rv.getParent().requestDisallowInterceptTouchEvent(true);
//                        break;
//                }
//                return false;
//            }
//
//            @Override
//            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//            }
//        });
//

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent(FoodListActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Food> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (Food item : foods) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getFoodname().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found in Food list..", Toast.LENGTH_SHORT).show();
        }
        else {
            // at last we are passing that filtered
            // list to our adapter class.
            Fadapter.filterList(filteredlist);
        }
    }

    private void filterRecent(String text) {
        // creating a new array list to filter our data.
        ArrayList<FoodRecent> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (FoodRecent item : foodRecents) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getFoodname().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found in Recent Added..", Toast.LENGTH_SHORT).show();
        }
        else {
            // at last we are passing that filtered
            // list to our adapter class.
            Fradapter.filterList(filteredlist);
        }
    }

    private void getFoodsList(){
        // Initializing Request queue

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.GET_FOOD_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            foods = new ArrayList<>();
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("food");

                            for (int i = 0; i<jsonArray.length(); i++){

                                JSONObject object = jsonArray.getJSONObject(i);
                                String foodID = object.getString("foodID").trim();
                                String foodName = object.getString("foodName").trim();
                                String foodServingSizeUnit = object.getString("foodServingSizeUnit").trim();
                                String foodCalories = object.getString("foodCalories").trim();
                                String foodFat = object.getString("foodFat").trim();
                                String foodProtein = object.getString("foodProtein").trim();
                                String foodCarbs = object.getString("foodCarbs").trim();


                                Food food = new Food(foodID,foodName,foodServingSizeUnit,foodCalories,foodFat,foodProtein,foodCarbs);
                                foods.add(food);

                                Fadapter = new FoodAdapter(FoodListActivity.this, foods);
                                foodRecyclerView.setAdapter(Fadapter);
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
                Toast.makeText(FoodListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void getRecentFoodsList(){
        // Initializing Request queue

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_RECENT_FOOD_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            foodRecents = new ArrayList<>();
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("recentfood");

                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String foodID = object.getString("foodID").trim();
                                    String foodName = object.getString("foodName").trim();
                                    String foodCalories = object.getString("foodCalories").trim();
                                    recentFoodTV.setVisibility(View.GONE);

                                    FoodRecent foodRecent = new FoodRecent(foodID, foodName, foodCalories);
                                    foodRecents.add(foodRecent);
                                    Fradapter = new FoodRecentAdapter(FoodListActivity.this, foodRecents);
                                    foodRecentRecyclerView.setAdapter(Fradapter);
                                }
                            }

                            if(success.equals("0")) {
                                recentFoodTV.setVisibility(View.VISIBLE);
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
                Toast.makeText(FoodListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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