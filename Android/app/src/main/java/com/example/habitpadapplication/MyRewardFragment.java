package com.example.habitpadapplication;

import android.app.Activity;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.habitpadapplication.Adapters.MyGiftAdpater;
import com.example.habitpadapplication.Adapters.MyVoucherAdapter;
import com.example.habitpadapplication.Model.MyGift;
import com.example.habitpadapplication.Model.MyVoucher;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyRewardFragment extends Fragment {

    private RecyclerView myVoucherRecyclerView;
    private List<MyVoucher> myVouchers;
    private MyVoucherAdapter myVoucherAdapter;

    private RecyclerView myGiftRecyclerView;
    private List<MyGift> myGifts;
    private MyGiftAdpater myGiftAdpater;

    private SwipeRefreshLayout swipMyRewardLayout;

    private String intentUserID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =   inflater.inflate(R.layout.fragment_my_reward, container, false);

        Intent intent = ((Activity) getContext()).getIntent();
        intentUserID = intent.getExtras().getString("intentUserID");


        myVoucherRecyclerView = (RecyclerView) view.findViewById(R.id.my_voucher_rv);
        LinearLayoutManager myVoucherLinearLayoutManager = new LinearLayoutManager(getContext());
        myVoucherLinearLayoutManager.setReverseLayout(true);
        myVoucherLinearLayoutManager.setStackFromEnd(true);
        myVoucherRecyclerView.setHasFixedSize(true);
        myVoucherRecyclerView.setLayoutManager(myVoucherLinearLayoutManager);
        myVouchers = new ArrayList<>();

        myGiftRecyclerView = (RecyclerView) view.findViewById(R.id.my_gift_rv);
        LinearLayoutManager myGiftLinearLayoutManager = new LinearLayoutManager(getContext());
        myGiftLinearLayoutManager.setReverseLayout(true);
        myGiftLinearLayoutManager.setStackFromEnd(true);
        myGiftRecyclerView.setHasFixedSize(true);
        myGiftRecyclerView.setLayoutManager(myGiftLinearLayoutManager);
        myGifts = new ArrayList<>();

        swipMyRewardLayout = view.findViewById(R.id.myreward_swipe_layout);
        swipMyRewardLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FragmentManager transaction = getActivity().getSupportFragmentManager();
                if (Build.VERSION.SDK_INT >= 26) {
                    transaction.beginTransaction().setReorderingAllowed(false);
                }
                transaction.beginTransaction().detach(MyRewardFragment.this).commit ();
                transaction.beginTransaction().attach (MyRewardFragment.this).commit();


                swipMyRewardLayout.setRefreshing(false);
            }
        });

        getMyVoucherList();
        getMyGiftList();

        return view;
    }

    private void getMyVoucherList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_VOUCHER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            //Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("uservoucher");

                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String voucherID = object.getString("voucherID").trim();
                                    String voucherPhoto = object.getString("voucherPhoto").trim();
                                    String voucherTitle = object.getString("voucherTitle").trim();


                                    MyVoucher myVoucher = new MyVoucher(voucherID,voucherPhoto, voucherTitle);
                                    myVouchers.add(myVoucher);

                                    myVoucherAdapter = new MyVoucherAdapter(getContext(), myVouchers);
                                    myVoucherRecyclerView.setAdapter(myVoucherAdapter);
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
                params.put("userID", intentUserID);
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void getMyGiftList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_GIFT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            //Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("usergift");

                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String giftID = object.getString("giftID").trim();
                                    String giftPhoto = object.getString("giftPhoto").trim();
                                    String giftTitle = object.getString("giftTitle").trim();


                                    MyGift myGift = new MyGift(giftID,giftPhoto, giftTitle);
                                    myGifts.add(myGift);

                                    myGiftAdpater = new MyGiftAdpater(getContext(), myGifts);
                                    myGiftRecyclerView.setAdapter(myGiftAdpater);
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
                params.put("userID", intentUserID);
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}