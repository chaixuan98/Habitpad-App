package com.example.habitpadapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.example.habitpadapplication.Adapters.ChallengeAdapter;
import com.example.habitpadapplication.Adapters.MyChallengeAdapter;
import com.example.habitpadapplication.Adapters.VoucherAdapter;
import com.example.habitpadapplication.Model.Challenge;
import com.example.habitpadapplication.Model.MyChallenge;
import com.example.habitpadapplication.Model.UserAppointment;
import com.example.habitpadapplication.Model.Voucher;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChallengeFragment extends Fragment {

    private RecyclerView challengeRecyclerView;
    private List<Challenge> challenges;
    private ChallengeAdapter challengeAdapter;

    private RecyclerView myChallengeRecyclerView;
    private List<MyChallenge> myChallenges;
    private MyChallengeAdapter myChallengeAdapter;

    private RecyclerView myPastChallengeRecyclerView;

    private String intentUserID;
    private SwipeRefreshLayout challengeSwipLayout;

    private TextView noMyChaTV, noMyPastChaTV;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =   inflater.inflate(R.layout.fragment_challenge, container, false);

        Intent intent = ((Activity) getContext()).getIntent();
        intentUserID = intent.getExtras().getString("intentUserID");

        noMyChaTV = view.findViewById(R.id.no_my_challenge_tv);
        noMyPastChaTV = view.findViewById(R.id.no_my_past_challenge_tv);

        challengeSwipLayout = view.findViewById(R.id.challenge_swipe_layout);
        challengeSwipLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FragmentManager transaction = getActivity().getSupportFragmentManager();
                if (Build.VERSION.SDK_INT >= 26) {
                    transaction.beginTransaction().setReorderingAllowed(false);
                }
                transaction.beginTransaction().detach(ChallengeFragment.this).commit ();
                transaction.beginTransaction().attach (ChallengeFragment.this).commit();

                challengeSwipLayout.setRefreshing(false);
            }
        });

        challengeRecyclerView = (RecyclerView) view.findViewById(R.id.challenge_list_rv);
        challengeRecyclerView.setHasFixedSize(true);
        LinearLayoutManager challengeLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        challengeRecyclerView.setLayoutManager(challengeLinearLayoutManager);

        myChallengeRecyclerView = (RecyclerView) view.findViewById(R.id.my_challenge_rv);
        LinearLayoutManager myChallengeLinearLayoutManager = new LinearLayoutManager(getContext());
        myChallengeLinearLayoutManager.setReverseLayout(true);
        myChallengeLinearLayoutManager.setStackFromEnd(true);
        myChallengeRecyclerView.setHasFixedSize(true);
        myChallengeRecyclerView.setLayoutManager(myChallengeLinearLayoutManager);


        myPastChallengeRecyclerView = (RecyclerView) view.findViewById(R.id.past_challenge_rv);
        LinearLayoutManager myPastChallengeLinearLayoutManager = new LinearLayoutManager(getContext());
        myPastChallengeLinearLayoutManager.setReverseLayout(true);
        myPastChallengeLinearLayoutManager.setStackFromEnd(true);
        myPastChallengeRecyclerView.setHasFixedSize(true);
        myPastChallengeRecyclerView.setLayoutManager(myPastChallengeLinearLayoutManager);

        GetChallengeList();
        GetMyChallengeList();
        GetMyPastChallengeList();

        return view;
    }

    private void GetChallengeList(){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.GET_CHALLENGE_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            challenges = new ArrayList<>();
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("challenge");
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String challengeID = object.getString("challengeID").trim();
                                    String challengePhoto = object.getString("challengePhoto").trim();
                                    String challengeTitle = object.getString("challengeTitle").trim();
                                    String challengeDescription = object.getString("challengeDescription").trim();
                                    String challengeStartDate = object.getString("challengeStartDate").trim();
                                    String challengeEndDate = object.getString("challengeEndDate").trim();
                                    String challengeStep = object.getString("challengeStep").trim();


                                    if (challengeStartDate.compareTo(DateHandler.getCurrentFormedDate()) <= 0 && DateHandler.getCurrentFormedDate().compareTo(challengeEndDate) <= 0) {

                                        Challenge challenge = new Challenge(challengeID, challengePhoto, challengeTitle);
                                        challenges.add(challenge);

                                        challengeAdapter = new ChallengeAdapter(getContext(), challenges);
                                        challengeRecyclerView.setAdapter(challengeAdapter);
                                    }

//                                    if (userAppAdapter.getItemCount() == 0)
//                                    {
//                                        noAppTV.setVisibility(View.VISIBLE);
//                                    }



                                }
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
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }

    private void GetMyChallengeList(){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_CHALLENGE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            myChallenges = new ArrayList<>();
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("userchallenge");
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String challengeID = object.getString("challengeID").trim();
                                    String challengePhoto = object.getString("challengePhoto").trim();
                                    String challengeTitle = object.getString("challengeTitle").trim();
                                    String challengeEndDate = object.getString("challengeEndDate").trim();
//
//                                    dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                                    date = dateFormat.format(calendar.getTime());

                                    noMyChaTV.setVisibility(View.GONE);

                                    if (DateHandler.getCurrentFormedDate().compareTo(challengeEndDate) <= 0) {

                                        MyChallenge myChallenge = new MyChallenge(challengeID, challengePhoto, challengeTitle,challengeEndDate);
                                        myChallenges.add(myChallenge);

                                        myChallengeAdapter = new MyChallengeAdapter(getContext(), myChallenges);
                                        myChallengeRecyclerView.setAdapter(myChallengeAdapter);
                                    }

//                                    if (myChallengeAdapter.getItemCount() == 0)
//                                    {
//                                        noMyChaTV.setVisibility(View.VISIBLE);
//                                    }



                                }
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
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID", intentUserID);
                return params;
            }
        };


        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }

    private void GetMyPastChallengeList(){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_CHALLENGE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            myChallenges = new ArrayList<>();
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("userchallenge");
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String challengeID = object.getString("challengeID").trim();
                                    String challengePhoto = object.getString("challengePhoto").trim();
                                    String challengeTitle = object.getString("challengeTitle").trim();
                                    String challengeEndDate = object.getString("challengeEndDate").trim();
//
//                                    dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                                    date = dateFormat.format(calendar.getTime());

                                    noMyPastChaTV.setVisibility(View.GONE);

                                    if (challengeEndDate.compareTo(DateHandler.getCurrentFormedDate()) < 0) {

                                        MyChallenge myChallenge = new MyChallenge(challengeID, challengePhoto, challengeTitle,challengeEndDate);
                                        myChallenges.add(myChallenge);

                                        myChallengeAdapter = new MyChallengeAdapter(getContext(), myChallenges);
                                        myPastChallengeRecyclerView.setAdapter(myChallengeAdapter);
                                    }

                                    if (myChallengeAdapter.getItemCount() == 0)
                                    {

                                        noMyPastChaTV.setVisibility(View.VISIBLE);
                                    }



                                }
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
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID", intentUserID);
                return params;
            }
        };


        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }
}