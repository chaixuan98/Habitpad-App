package com.example.habitpadapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.habitpadapplication.Adapters.TaskAdapter;
import com.example.habitpadapplication.Model.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MissionFragment extends Fragment {

    private RecyclerView taskList;
    private List<Task> tasks ;
    private TaskAdapter taskAdapter;


    private String intentUserID, strUserGamificationPoint = "0";

    private TextView  gamificationPoint, listEmptyTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_mission, container, false);

        Intent intent = ((Activity) getContext()).getIntent();
        intentUserID = intent.getExtras().getString("intentUserID");


        gamificationPoint = (TextView) view.findViewById(R.id.gamification_point);
        listEmptyTV = (TextView) view.findViewById(R.id.list_empty_tv);

        DisplayUserTotalPoint(intentUserID);


        taskList = (RecyclerView) view.findViewById(R.id.task_list);
        LinearLayoutManager taskLinearLayoutManager = new LinearLayoutManager(getContext());
        taskLinearLayoutManager.setReverseLayout(false);
        taskLinearLayoutManager.setStackFromEnd(true);
        taskList.setHasFixedSize(true);
        taskList.setLayoutManager(taskLinearLayoutManager);

        getTaskList(intentUserID, DateHandler.getCurrentFormedDate());

        return view;
    }

    private void getTaskList(final String intentUserID, final String date){
        // Initializing Request queue

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_TASK_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            tasks = new ArrayList<>();
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("tasklog");
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String taskID = object.getString("taskID").trim();
                                    String taskIcon = object.getString("taskIcon").trim();
                                    String taskTitle = object.getString("taskTitle").trim();
                                    String taskDescription = object.getString("taskDescription").trim();
                                    String taskPoint = object.getString("taskPoint").trim();

                                    listEmptyTV.setVisibility(View.GONE);

                                    Task task = new Task(taskID, taskIcon, taskTitle, taskDescription, taskPoint);
                                    tasks.add(task);
                                }
                            }

                        }catch (Exception e){
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }

                        taskAdapter = new TaskAdapter(getContext(), tasks);
                        taskList.setAdapter(taskAdapter);


                        if (taskAdapter.getItemCount() == 0)
                        {
                            listEmptyTV.setVisibility(View.VISIBLE);
                            //Toast.makeText(GamificationActivity.this, "All tasks are done!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID", intentUserID);
                params.put("addTaskDate", date);
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }


    private void DisplayUserTotalPoint(final String intentUserID){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_TOTAL_POINT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("userPoint");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    strUserGamificationPoint = object.getString("totalPoint");

                                    gamificationPoint.setText(strUserGamificationPoint);


                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",intentUserID);
                return params;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);


    }
}