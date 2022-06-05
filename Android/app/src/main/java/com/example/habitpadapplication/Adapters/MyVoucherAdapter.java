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
import com.example.habitpadapplication.Model.MyVoucher;
import com.example.habitpadapplication.R;
import com.example.habitpadapplication.Urls;
import com.example.habitpadapplication.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyVoucherAdapter extends RecyclerView.Adapter<MyVoucherAdapter.MyViewHolder>{

    private Context mContext;
    private List<MyVoucher> myVouchers = new ArrayList<>();
    private String intentUserID, voucherPhoto, voucherPoint, voucherTitle, voucherDesc;


    private TextView voucherTitleTV, voucherPointTV, voucherDescTV;
    private ImageView voucherPhotoView;


    public MyVoucherAdapter(Context context, List<MyVoucher> myVouchers){
        this.mContext = context;
        this.myVouchers = myVouchers;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView myVoucherPhoto;
        private TextView myRewardTitle;
        private ConstraintLayout voucherLayout;

        public MyViewHolder (View view){
            super(view);

            voucherLayout = view.findViewById(R.id.voucher_layout);
            myVoucherPhoto = view.findViewById(R.id.my_reward_image);
            myRewardTitle = view.findViewById(R.id.my_reward);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Intent intent = ((Activity) mContext).getIntent();
//        intentUserID = intent.getExtras().getString("intentUserID");

        View view = LayoutInflater.from(mContext).inflate(R.layout.my_reward_raw,parent,false);
        return new MyVoucherAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final MyVoucher myVoucher = myVouchers.get(position);

        final String voucherID = myVoucher.getVoucherID();
        
        Glide.with(mContext).asBitmap().load(myVoucher.getMyVoucherImage())
                .fitCenter()
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .fallback(R.drawable.ic_baseline_image_not_supported_24)
                .dontAnimate().into(holder.myVoucherPhoto);

        holder.myRewardTitle.setText(myVoucher.getMyVoucherReward());

        holder.voucherLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PopMyVoucherDialog(voucherID);
            }

        });

    }

    private void PopMyVoucherDialog(String voucherID) {
        final Dialog redeemvoucherdialog = new Dialog(mContext);
        redeemvoucherdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        redeemvoucherdialog.setContentView(R.layout.myvoucher_desc_raw);
        redeemvoucherdialog.setTitle("Redeem Voucher window");
        redeemvoucherdialog.show();
        Window giftWindow = redeemvoucherdialog.getWindow();
        giftWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        voucherTitleTV = (TextView) redeemvoucherdialog.findViewById(R.id.dialog_voucher_title);
        voucherPhotoView = (ImageView) redeemvoucherdialog.findViewById(R.id.dialog_voucher_photo);
        voucherPointTV = (TextView) redeemvoucherdialog.findViewById(R.id.dialog_voucher_point);
        voucherDescTV = (TextView) redeemvoucherdialog.findViewById(R.id.dialog_voucher_description);

        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_VOUCHER_DETAILS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("voucherDetail");
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    voucherPhoto = object.getString("voucherPhoto").trim();
                                    voucherTitle = object.getString("voucherTitle").trim();
                                    voucherDesc = object.getString("voucherDesc").trim();

                                    Glide.with(mContext).asBitmap().load(voucherPhoto)
                                            .fitCenter()
                                            .placeholder(R.drawable.ic_baseline_image_24)
                                            .error(R.drawable.ic_baseline_broken_image_24)
                                            .fallback(R.drawable.ic_baseline_image_not_supported_24)
                                            .dontAnimate().into(voucherPhotoView);
                                    voucherTitleTV.setText(voucherTitle);
                                    voucherDescTV.setText(voucherDesc);

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
                params.put("voucherID", voucherID);
                return params;
            }
        };

        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);

        //getVoucherList(voucherID);


        /* cancel button click action */
        Button cancelbtn = (Button)redeemvoucherdialog.findViewById(R.id.voucher_dialog_cancel_button);
        cancelbtn.setEnabled(true);
        cancelbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                redeemvoucherdialog.cancel();
            }
        });


        /* ok button click action */
//        Button redeembtn = (Button)redeemvoucherdialog.findViewById(R.id.voucher_dialog_redeem_button);
//        redeembtn.setEnabled(true);
//        redeembtn.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
////                DisplayUserTotalPoint(intentUserID,giftID,giftPoint);
//                redeemvoucherdialog.cancel();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return myVouchers.size();
    }


}
