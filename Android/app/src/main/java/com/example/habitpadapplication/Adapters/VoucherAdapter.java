package com.example.habitpadapplication.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.habitpadapplication.HomeActivity;
import com.example.habitpadapplication.Model.Doctor;
import com.example.habitpadapplication.Model.Task;
import com.example.habitpadapplication.Model.Voucher;
import com.example.habitpadapplication.R;
import com.example.habitpadapplication.Urls;
import com.example.habitpadapplication.VolleySingleton;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.MyViewHolder>{

    private Context mContext;
    private List<Voucher> vouchers = new ArrayList<>();
    private String intentUserID, voucherPhoto, voucherPoint, voucherTitle, voucherDesc;


    private TextView voucherTitleTV, voucherPointTV, voucherDescTV;
    private ImageView voucherPhotoView;


    public VoucherAdapter (Context context, List<Voucher> vouchers){
        this.mContext = context;
        this.vouchers = vouchers;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageview;
        private TextView mPoint, mReward;
        private ConstraintLayout voucherLayout;

        public MyViewHolder (View view){
            super(view);

            voucherLayout = view.findViewById(R.id.voucher_layout);
            mImageview = view.findViewById(R.id.redeem_voucher_image);
            mPoint = view.findViewById(R.id.redeem_point);
            mReward = view.findViewById(R.id.redeem_reward);

        }
    }

    @NonNull
    @Override
    public VoucherAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Intent intent = ((Activity) mContext).getIntent();
        intentUserID = intent.getExtras().getString("intentUserID");

        View view = LayoutInflater.from(mContext).inflate(R.layout.redeem_voucher_raw,parent,false);
        return new VoucherAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Voucher voucher = vouchers.get(position);

        final String voucherID = voucher.getVoucherID();

        Glide.with(mContext).asBitmap().load(voucher.getVoucherImage())
                .fitCenter()
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .fallback(R.drawable.ic_baseline_image_not_supported_24)
                .dontAnimate().into(holder.mImageview);
        holder.mPoint.setText(voucher.getPoint()+" pts");
        holder.mReward.setText(voucher.getReward());

        holder.voucherLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PopRedeemDialog(voucherID);
            }
        });

    }


    @Override
    public int getItemCount() {
        return vouchers.size();
    }

    private void PopRedeemDialog(String voucherID) {

        final Dialog redeemdialog = new Dialog(mContext);
        redeemdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        redeemdialog.setContentView(R.layout.voucher_desc_raw);
        redeemdialog.setTitle("Redeem Voucher window");
        redeemdialog.show();
        Window voucherWindow = redeemdialog.getWindow();
        voucherWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        voucherTitleTV = (TextView) redeemdialog.findViewById(R.id.dialog_voucher_title);
        voucherPhotoView = (ImageView) redeemdialog.findViewById(R.id.dialog_voucher_photo);
        voucherPointTV = (TextView) redeemdialog.findViewById(R.id.dialog_voucher_point);
        voucherDescTV = (TextView) redeemdialog.findViewById(R.id.dialog_vocuher_description);

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
                                    //voucherID = object.getString("voucherID").trim();
                                    voucherPhoto = object.getString("voucherPhoto").trim();
                                    voucherPoint = object.getString("voucherPoint").trim();
                                    voucherTitle = object.getString("voucherTitle").trim();
                                    voucherDesc = object.getString("voucherDesc").trim();

                                    Glide.with(mContext).asBitmap().load(voucherPhoto)
                                            .fitCenter()
                                            .placeholder(R.drawable.ic_baseline_image_24)
                                            .error(R.drawable.ic_baseline_broken_image_24)
                                            .fallback(R.drawable.ic_baseline_image_not_supported_24)
                                            .dontAnimate().into(voucherPhotoView);
                                    voucherPointTV.setText(voucherPoint + " pts");
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
        Button cancelbtn = (Button)redeemdialog.findViewById(R.id.voucher_dialog_cancel_button);
        cancelbtn.setEnabled(true);
        cancelbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                redeemdialog.cancel();
            }
        });


        /* ok button click action */
        Button redeembtn = (Button)redeemdialog.findViewById(R.id.voucher_dialog_redeem_button);
        redeembtn.setEnabled(true);
        redeembtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                DisplayUserTotalPoint(intentUserID,voucherID,voucherPoint);
                redeemdialog.cancel();


            }
        });

    }

//    private void getVoucherList(final String voucherID){
//
//        final ProgressDialog progressDialog = new ProgressDialog(mContext);
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_VOUCHER_DETAILS_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        progressDialog.dismiss();
//                        try {
//                            Log.i("tagconvertstr", "["+response+"]");
//                            //JSONArray array = new JSONArray(response);
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONArray jsonArray = jsonObject.getJSONArray("voucherDetail");
//                            String success = jsonObject.getString("success");
//
//                            if (success.equals("1")) {
//                                for (int i = 0; i < jsonArray.length(); i++) {
//
//                                    JSONObject object = jsonArray.getJSONObject(i);
//                                    String voucherID = object.getString("voucherID").trim();
//                                    voucherPhoto = object.getString("voucherPhoto").trim();
//                                    voucherPoint = object.getString("voucherPoint").trim();
//                                    voucherTitle = object.getString("voucherTitle").trim();
//                                    voucherDesc = object.getString("voucherDesc").trim();
//
//                                }
//                            }
//
//                        }catch (Exception e){
//                            progressDialog.dismiss();
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
//                Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
//            }
//        })
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String, String> params = new HashMap<>();
//                params.put("voucherID", voucherID);
//                return params;
//            }
//        };
//
//        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
//
//    }

    private void DisplayUserTotalPoint(final String intentUserID, final String voucherID, final String vPoint){

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

                                    if(Integer.parseInt(strTotalPoint) >= Integer.parseInt(vPoint)){
                                        strTotalPoint = String.valueOf(Integer.parseInt(strTotalPoint) - Integer.parseInt(vPoint));
                                        UpdateUserPoint(intentUserID, strTotalPoint);
                                        AddUserVoucher(intentUserID,voucherID,DateHandler.getCurrentFormedDate());
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

    private void AddUserVoucher(final String intentUserID, final String voucherID, final String addVoucherDate)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.ADD_USER_VOUCHER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {
                                Toast.makeText(mContext,"Redeem Successfully",Toast.LENGTH_SHORT).show();
                                Log.i("tagtoast", "["+message+"]");
                            }

//                            if (success.equals("0")) {
////                                Toast.makeText(mContext.this,message,Toast.LENGTH_SHORT).show();
//                                Log.i("tagtoast", "["+message+"]");
//                            }

                        } catch (JSONException e) {
                            Toast.makeText(mContext,"Insert user voucher Error!" + e.toString(),Toast.LENGTH_LONG).show();
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
                params.put("voucherID",voucherID);
                params.put("addVoucherDate",addVoucherDate);
                return params;
            }
        };

        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);

    }
}
