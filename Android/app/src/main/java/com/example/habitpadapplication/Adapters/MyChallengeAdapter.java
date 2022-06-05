package com.example.habitpadapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.habitpadapplication.Model.MyChallenge;
import com.example.habitpadapplication.R;
import com.example.habitpadapplication.MyChallengeActivity;

import java.util.ArrayList;
import java.util.List;

public class MyChallengeAdapter extends RecyclerView.Adapter<MyChallengeAdapter.MyViewHolder>{
    private Context mContext;
    private List<MyChallenge> myChallenges = new ArrayList<>();
    private String intentUserID, myChallengePhoto, myChallengeTitle, myChallengeDesc, myChallengeStartDate, myChallengeEndDate, myChallengeStep;


    private TextView myChallengeTitleTV, myChallengeDescTV, myChallengeStartDateTV, myChallengeEndDateTV, myChallengeStepTV;
    private ImageView myChallengePhotoView;


    public MyChallengeAdapter (Context context, List<MyChallenge> myChallenges){
        this.mContext = context;
        this.myChallenges = myChallenges;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView mcImageview;
        private TextView mcTitle;
        private TextView mcEndDate;
        private LinearLayout mchallengeLayout;

        public MyViewHolder (View view){
            super(view);

            mchallengeLayout = view.findViewById(R.id.my_challenge_layout);
            mcImageview = view.findViewById(R.id.my_challenge_image);
            mcTitle = view.findViewById(R.id.my_challenge_title);
            mcEndDate = view.findViewById(R.id.my_challenge_end_date);

        }
    }
    @NonNull
    @Override
    public MyChallengeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Intent intent = ((Activity) mContext).getIntent();
        intentUserID = intent.getExtras().getString("intentUserID");

        View view = LayoutInflater.from(mContext).inflate(R.layout.my_challenge_raw,parent,false);
        return new MyChallengeAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyChallengeAdapter.MyViewHolder holder, int position) {
        final MyChallenge myChallenge = myChallenges.get(position);

        final String mchallengeID = myChallenge.getMchallengeID();

        Glide.with(mContext).asBitmap().load(myChallenge.getMchallengeImage())
                .fitCenter()
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .fallback(R.drawable.ic_baseline_image_not_supported_24)
                .dontAnimate().into(holder.mcImageview);

        holder.mcTitle.setText(myChallenge.getMchallengeTitle());
        holder.mcEndDate.setText("End: "+myChallenge.getMchallengeEndDate());
        holder.mchallengeLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SentToMyChallengePage(mchallengeID);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myChallenges.size();
    }

    private void SentToMyChallengePage(String mchallengeID) {
        Intent mcIntent = new Intent(mContext, MyChallengeActivity.class);
        mcIntent.putExtra("mchallengeID", mchallengeID);
        mcIntent.putExtra("intentUserID", intentUserID);
        mContext.startActivity(mcIntent);
    }
}
