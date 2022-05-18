package com.example.habitpadapplication.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.habitpadapplication.DateHandler;
import com.example.habitpadapplication.MissionRewardActivity;
import com.example.habitpadapplication.Model.Gift;
import com.example.habitpadapplication.Model.Voucher;
import com.example.habitpadapplication.R;
import com.example.habitpadapplication.RedeemRewardFragment;
import com.example.habitpadapplication.Urls;
import com.example.habitpadapplication.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.MyViewHolder> {

    private Context mContext;
    private List<Gift> gifts = new ArrayList<>();
    private String intentUserID, giftPhoto, giftPoint, giftTitle, giftDesc;


    private TextView giftTitleTV, giftPointTV, giftDescTV;
    private ImageView giftPhotoView;


    public GiftAdapter (Context context, List<Gift> gifts){
        this.mContext = context;
        this.gifts = gifts;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView gImageview;
        private TextView gPoint, gReward;
        private ConstraintLayout gLayout;

        public MyViewHolder (View view){
            super(view);

            gLayout = view.findViewById(R.id.gift_layout);
            gImageview = view.findViewById(R.id.redeem_gift_image);
            gPoint = view.findViewById(R.id.redeem_gift_point);
            gReward = view.findViewById(R.id.redeem_gift_reward);

        }
    }

    @NonNull
    @Override
    public GiftAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Intent intent = ((Activity) mContext).getIntent();
        intentUserID = intent.getExtras().getString("intentUserID");

        View view = LayoutInflater.from(mContext).inflate(R.layout.redeem_gift_raw,parent,false);
        return new GiftAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftAdapter.MyViewHolder holder, int position) {

        final Gift gift = gifts.get(position);

        final String giftID = gift.getGiftID();

        Glide.with(mContext).asBitmap().load(gift.getGiftimage())
                .fitCenter()
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .fallback(R.drawable.ic_baseline_image_not_supported_24)
                .dontAnimate().into(holder.gImageview);
        holder.gPoint.setText(gift.getGiftpoint()+" pts");
        holder.gReward.setText(gift.getGiftreward());

        holder.gLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PopRedeemGiftDialog(giftID);
            }

        });

    }

    @Override
    public int getItemCount() {
        return gifts.size();
    }

    private void PopRedeemGiftDialog(String giftID) {

        final Dialog redeemgiftdialog = new Dialog(mContext);
        redeemgiftdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        redeemgiftdialog.setContentView(R.layout.gift_desc_raw);
        redeemgiftdialog.setTitle("Redeem Gift window");
        redeemgiftdialog.show();
        Window giftWindow = redeemgiftdialog.getWindow();
        giftWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        giftTitleTV = (TextView) redeemgiftdialog.findViewById(R.id.dialog_gift_title);
        giftPhotoView = (ImageView) redeemgiftdialog.findViewById(R.id.dialog_gift_photo);
        giftPointTV = (TextView) redeemgiftdialog.findViewById(R.id.dialog_gift_point);
        giftDescTV = (TextView) redeemgiftdialog.findViewById(R.id.dialog_gift_description);

        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_GIFT_DETAILS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("giftDetail");
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    giftPhoto = object.getString("giftPhoto").trim();
                                    giftPoint = object.getString("giftPoint").trim();
                                    giftTitle = object.getString("giftTitle").trim();
                                    giftDesc = object.getString("giftDesc").trim();

                                    Glide.with(mContext).asBitmap().load(giftPhoto)
                                            .fitCenter()
                                            .placeholder(R.drawable.ic_baseline_image_24)
                                            .error(R.drawable.ic_baseline_broken_image_24)
                                            .fallback(R.drawable.ic_baseline_image_not_supported_24)
                                            .dontAnimate().into(giftPhotoView);
                                    giftPointTV.setText(giftPoint + " pts");
                                    giftTitleTV.setText(giftTitle);
                                    giftDescTV.setText(giftDesc);

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
                Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("giftID", giftID);
                return params;
            }
        };

        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);

        //getVoucherList(voucherID);


        /* cancel button click action */
        Button cancelbtn = (Button)redeemgiftdialog.findViewById(R.id.gift_dialog_cancel_button);
        cancelbtn.setEnabled(true);
        cancelbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                redeemgiftdialog.cancel();
            }
        });


        /* ok button click action */
        Button redeembtn = (Button)redeemgiftdialog.findViewById(R.id.gift_dialog_redeem_button);
        redeembtn.setEnabled(true);
        redeembtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DisplayUserTotalPoint(intentUserID,giftID,giftPoint);
                redeemgiftdialog.cancel();
            }
        });

    }

    private void DisplayUserTotalPoint(final String intentUserID, final String giftID, final String gPoint){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_USER_TOTAL_POINT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("userPoint");
                            String success = jsonObject.getString("success") ;

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String strTotalPoint = object.getString("totalPoint");

                                    if(Integer.parseInt(strTotalPoint) >= Integer.parseInt(gPoint)){
                                        strTotalPoint = String.valueOf(Integer.parseInt(strTotalPoint) - Integer.parseInt(gPoint));
                                        UpdateUserPoint(intentUserID, strTotalPoint);
                                        AddUserGift(intentUserID,giftID, DateHandler.getCurrentFormedDate());
                                    }
                                    else{
                                        Toast.makeText(mContext, "No enough points to redeem it", Toast.LENGTH_LONG).show();
                                    }

                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",intentUserID);
                return params;
            }
        };
        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);


    }

    private void UpdateUserPoint(final String intentUserID, final String point){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.UPDATE_USER_POINT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {
                                //Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                                Log.i("tagtoast", "["+message+"]");
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(mContext, "update user point error" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.i("tagerror", "["+error+"]");
                Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userID",intentUserID);
                params.put("totalPoint",point);
                return params;
            }
        };

        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
    }

    private void AddUserGift(final String intentUserID, final String giftID, final String addGiftDate)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.ADD_USER_GIFT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {
                                //Toast.makeText(HomeActivity.this,message,Toast.LENGTH_SHORT).show();
                                Log.i("tagtoast", "["+message+"]");
                            }

                            if (success.equals("0")) {
                                //Toast.makeText(HomeActivity.this,message,Toast.LENGTH_SHORT).show();
                                Log.i("tagtoast", "["+message+"]");
                            }

                        } catch (JSONException e) {
                            Toast.makeText(mContext,"Insert user gift Error!" + e.toString(),Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("tagerror", "["+error+"]");
                        //Toast.makeText(HomeActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userID", intentUserID);
                params.put("giftID",giftID);
                params.put("addGiftDate",addGiftDate);
                return params;
            }
        };

        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);

    }
}
