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
import com.example.habitpadapplication.ChallengeFragment;
import com.example.habitpadapplication.DateHandler;
import com.example.habitpadapplication.Model.Challenge;
import com.example.habitpadapplication.Model.Voucher;
import com.example.habitpadapplication.R;
import com.example.habitpadapplication.Urls;
import com.example.habitpadapplication.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.MyViewHolder>{

    private Context mContext;
    private List<Challenge> challenges = new ArrayList<>();
    private String intentUserID, challengePhoto, challengeTitle, challengeDesc, challengeStartDate, challengeEndDate, challengeStep;


    private TextView challengeTitleTV, challengeDescTV, challengeStartDateTV, challengeEndDateTV, challengeStepTV;
    private ImageView challengePhotoView;


    public ChallengeAdapter (Context context, List<Challenge> challenges){
        this.mContext = context;
        this.challenges = challenges;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView cImageview;
        private TextView cTitle;
        private ConstraintLayout challengeLayout;

        public MyViewHolder (View view){
            super(view);

            challengeLayout = view.findViewById(R.id.challenge_layout);
            cImageview = view.findViewById(R.id.challenge_image);
            cTitle = view.findViewById(R.id.challenge_title);

        }
    }

    @NonNull
    @Override
    public ChallengeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Intent intent = ((Activity) mContext).getIntent();
        intentUserID = intent.getExtras().getString("intentUserID");

        View view = LayoutInflater.from(mContext).inflate(R.layout.challenge_raw,parent,false);
        return new ChallengeAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeAdapter.MyViewHolder holder, int position) {

        final Challenge challenge = challenges.get(position);

        final String challengeID = challenge.getChallengeID();

        Glide.with(mContext).asBitmap().load(challenge.getChallengeImage())
                .fitCenter()
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .fallback(R.drawable.ic_baseline_image_not_supported_24)
                .dontAnimate().into(holder.cImageview);

        holder.cTitle.setText(challenge.getChallengeTitle());

        holder.challengeLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PopChallengeDialog(challengeID);
            }
        });

    }



    @Override
    public int getItemCount() {
        return challenges.size();
    }

    private void PopChallengeDialog(final String challengeID) {

        final Dialog challengedialog = new Dialog(mContext);
        challengedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        challengedialog.setContentView(R.layout.challenge_desc_raw);
        challengedialog.setTitle("Challenge window");
        challengedialog.show();
        Window challengeWindow = challengedialog.getWindow();
        challengeWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        challengeTitleTV = (TextView) challengedialog.findViewById(R.id.dialog_challenge_title);
        challengePhotoView = (ImageView) challengedialog.findViewById(R.id.dialog_challenge_photo);
        challengeDescTV = (TextView) challengedialog.findViewById(R.id.dialog_challenge_description);
        challengeStepTV = (TextView) challengedialog.findViewById(R.id.dialog_challenge_step);
        challengeStartDateTV = (TextView) challengedialog.findViewById(R.id.dialog_challenge_start_date);
        challengeEndDateTV = (TextView) challengedialog.findViewById(R.id.dialog_challenge_end_date);


        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_CHALLENGE_DETAILS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("challengeDetail");
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    challengePhoto = object.getString("challengePhoto").trim();
                                    challengeTitle = object.getString("challengeTitle").trim();
                                    challengeDesc = object.getString("challengeDescription").trim();
                                    challengeStep = object.getString("challengeStep").trim();
                                    challengeStartDate = object.getString("challengeStartDate").trim();
                                    challengeEndDate = object.getString("challengeEndDate").trim();


                                    Glide.with(mContext).asBitmap().load(challengePhoto)
                                            .fitCenter()
                                            .placeholder(R.drawable.ic_baseline_image_24)
                                            .error(R.drawable.ic_baseline_broken_image_24)
                                            .fallback(R.drawable.ic_baseline_image_not_supported_24)
                                            .dontAnimate().into(challengePhotoView);
                                    challengeTitleTV.setText(challengeTitle);
                                    challengeDescTV.setText(challengeDesc);
                                    challengeStepTV.setText(challengeStep);
                                    challengeStartDateTV.setText(challengeStartDate);
                                    challengeEndDateTV.setText(challengeEndDate);


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
                params.put("challengeID", challengeID);
                return params;
            }
        };

        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);


        /* cancel button click action */
        Button cancelbtn = (Button)challengedialog.findViewById(R.id.challenge_dialog_cancel_button);
        cancelbtn.setEnabled(true);
        cancelbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                challengedialog.cancel();
            }
        });


        /* ok button click action */
        Button joinbtn = (Button)challengedialog.findViewById(R.id.challenge_dialog_join_button);
        joinbtn.setEnabled(true);
        joinbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AddUserChallenge(intentUserID, challengeID, DateHandler.getCurrentFormedDate());
                challengedialog.cancel();

            }
        });

    }

    private void AddUserChallenge(final String intentUserID, final String challengeID, final String addChallengeDate)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.ADD_USER_CHALLENGE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {
                                Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
                                //Log.i("tagtoast", "["+message+"]");
                            }

                            if (success.equals("0")) {
                                Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
                                //Log.i("tagtoast", "["+message+"]");
                            }

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
                params.put("challengeID",challengeID);
                params.put("addChallengeDate",addChallengeDate);
                params.put("isChallegeAchieve","false");
                return params;
            }
        };

        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);

    }
}
