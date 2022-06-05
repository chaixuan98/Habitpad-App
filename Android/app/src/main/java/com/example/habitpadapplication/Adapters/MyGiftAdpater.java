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
import com.example.habitpadapplication.Model.MyGift;
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

public class MyGiftAdpater extends RecyclerView.Adapter<MyGiftAdpater.MyViewHolder>{

    private Context mContext;
    private List<MyGift> myGifts = new ArrayList<>();

    private String intentUserID, giftPhoto, giftPoint, giftTitle, giftDesc;
    private TextView giftTitleTV, giftPointTV, giftDescTV;
    private ImageView giftPhotoView;


    public MyGiftAdpater(Context context, List<MyGift> myGifts){
        this.mContext = context;
        this.myGifts = myGifts;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView myGiftPhoto;
        private TextView myGiftTitle;
        private ConstraintLayout giftLayout;

        public MyViewHolder (View view){
            super(view);

            giftLayout = view.findViewById(R.id.gift_layout);
            myGiftPhoto = view.findViewById(R.id.my_gift_image);
            myGiftTitle = view.findViewById(R.id.my_gift_reward);

        }
    }

    @NonNull
    @Override
    public MyGiftAdpater.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Intent intent = ((Activity) mContext).getIntent();
//        intentUserID = intent.getExtras().getString("intentUserID");

        View view = LayoutInflater.from(mContext).inflate(R.layout.my_gift_raw,parent,false);
        return new MyGiftAdpater.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyGiftAdpater.MyViewHolder holder, int position) {

        final MyGift myGift = myGifts.get(position);

        final String giftID = myGift.getGiftID();

        Glide.with(mContext).asBitmap().load(myGift.getMyGiftImage())
                .fitCenter()
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .fallback(R.drawable.ic_baseline_image_not_supported_24)
                .dontAnimate().into(holder.myGiftPhoto);

        holder.myGiftTitle.setText(myGift.getMyGiftReward());

        holder.giftLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PopMyGiftDialog(giftID);
            }

        });

    }



    @Override
    public int getItemCount() {
        return myGifts.size();
    }

    private void PopMyGiftDialog(String giftID) {

        final Dialog redeemgiftdialog = new Dialog(mContext);
        redeemgiftdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        redeemgiftdialog.setContentView(R.layout.mygift_desc_raw);
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
                                    giftTitle = object.getString("giftTitle").trim();
                                    giftDesc = object.getString("giftDesc").trim();

                                    Glide.with(mContext).asBitmap().load(giftPhoto)
                                            .fitCenter()
                                            .placeholder(R.drawable.ic_baseline_image_24)
                                            .error(R.drawable.ic_baseline_broken_image_24)
                                            .fallback(R.drawable.ic_baseline_image_not_supported_24)
                                            .dontAnimate().into(giftPhotoView);
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


//        /* ok button click action */
//        Button redeembtn = (Button)redeemgiftdialog.findViewById(R.id.gift_dialog_redeem_button);
//        redeembtn.setEnabled(true);
//        redeembtn.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
////                DisplayUserTotalPoint(intentUserID,giftID,giftPoint);
//                redeemgiftdialog.cancel();
//            }
//        });

    }
}
