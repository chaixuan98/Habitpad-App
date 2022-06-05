package com.example.habitpadapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.habitpadapplication.Adapters.GiftAdapter;
import com.example.habitpadapplication.Adapters.TaskAdapter;
import com.example.habitpadapplication.Adapters.VoucherAdapter;
import com.example.habitpadapplication.Model.Gift;
import com.example.habitpadapplication.Model.Task;
import com.example.habitpadapplication.Model.Voucher;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RedeemRewardFragment extends Fragment {

    private RecyclerView redeemVoucherRecyclerView;
    private List<Voucher> vouchers;
    private VoucherAdapter voucherAdapter;

    private RecyclerView redeemGiftRecyclerView;
    private List<Gift> gifts;
    private GiftAdapter giftAdapter;

    private SwipeRefreshLayout swipLayout;
    private TextView totalPoint;
    private String intentUserID, strTotalPoint;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_redeem_reward, container, false);

        Intent intent = ((Activity) getContext()).getIntent();
        intentUserID = intent.getExtras().getString("intentUserID");

        totalPoint = view.findViewById(R.id.total_point);
        totalPoint.setText(strTotalPoint + " pts");

        swipLayout = view.findViewById(R.id.swipe_layout);
        swipLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               FragmentManager transaction = getActivity().getSupportFragmentManager();
                if (Build.VERSION.SDK_INT >= 26) {
                    transaction.beginTransaction().setReorderingAllowed(false);
                }
                transaction.beginTransaction().detach(RedeemRewardFragment.this).commit ();
                transaction.beginTransaction().attach (RedeemRewardFragment.this).commit();

                swipLayout.setRefreshing(false);
            }
        });

        DisplayUserTotalPoint(intentUserID);

        redeemVoucherRecyclerView = (RecyclerView) view.findViewById(R.id.redeem_voucher_rv);
        redeemVoucherRecyclerView.setHasFixedSize(true);
        LinearLayoutManager voucherLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        redeemVoucherRecyclerView.setLayoutManager(voucherLinearLayoutManager);

        redeemGiftRecyclerView = (RecyclerView) view.findViewById(R.id.redeem_gift_rv);
        redeemGiftRecyclerView.setHasFixedSize(true);
        LinearLayoutManager giftLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        redeemGiftRecyclerView.setLayoutManager(giftLinearLayoutManager);

        GetVoucherList();
        GetGiftList();
        return view;
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

                                    strTotalPoint = object.getString("totalPoint");

                                    totalPoint.setText(strTotalPoint + " pts");
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

    private void GetVoucherList(){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.GET_VOUCHER_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            vouchers = new ArrayList<>();
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("voucher");
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String voucherID = object.getString("voucherID").trim();
                                    String voucherPhoto = object.getString("voucherPhoto").trim();
                                    String voucherPoint = object.getString("voucherPoint").trim();
                                    String voucherTitle = object.getString("voucherTitle").trim();
                                    String voucherDesc = object.getString("voucherDesc").trim();

                                    Voucher voucher = new Voucher(voucherID, voucherPhoto, voucherPoint, voucherTitle);
                                    vouchers.add(voucher);

                                    voucherAdapter = new VoucherAdapter(getContext(), vouchers);
                                    redeemVoucherRecyclerView.setAdapter(voucherAdapter);
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

    private void GetGiftList(){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.GET_GIFT_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            gifts = new ArrayList<>();
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("gift");
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String giftID = object.getString("giftID").trim();
                                    String giftPhoto = object.getString("giftPhoto").trim();
                                    String giftPoint = object.getString("giftPoint").trim();
                                    String giftTitle = object.getString("giftTitle").trim();
                                    String giftDesc = object.getString("giftDesc").trim();


                                    Gift gift = new Gift(giftID, giftPhoto, giftPoint, giftTitle);
                                    gifts.add(gift);

                                    giftAdapter = new GiftAdapter(getContext(), gifts);
                                    redeemGiftRecyclerView.setAdapter(giftAdapter);
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
}