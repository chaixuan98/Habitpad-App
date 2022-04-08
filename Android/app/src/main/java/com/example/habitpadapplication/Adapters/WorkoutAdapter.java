package com.example.habitpadapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitpadapplication.AddWorkoutActivity;
import com.example.habitpadapplication.Model.Workout;
import com.example.habitpadapplication.R;


import java.util.ArrayList;
import java.util.List;


public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.MyViewHolder>{

    private Context mContext;
    private List<Workout> workouts = new ArrayList<>();

    private String intentUserID,intentUserGender,intentUserAge,intentUserLifestyle,intentUserWeight,intentUserHeight;


    public WorkoutAdapter (Context context,List<Workout> workouts){
        this.mContext = context;
        this.workouts = workouts;
    }


    // method for filtering our recyclerview items.
    public void filterList(List<Workout> filterllist) {
        // below line is to add our filtered list in our course array list.
        workouts = filterllist;
        // below line is to notify our adaptermas change in recycler view data.
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView workoutType,workoutMET;
        private LinearLayout workoutLayout;


        public MyViewHolder (View view){
            super(view);

            workoutType = view.findViewById(R.id.workout_name);
            workoutMET = view.findViewById(R.id.workout_MET);
            workoutLayout= view.findViewById(R.id.workout_layout);


        }
    }
    @NonNull
    @Override
    public WorkoutAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        Intent intent = ((Activity) mContext).getIntent();
        intentUserID = intent.getExtras().getString("intentUserID");
        intentUserGender = intent.getExtras().getString("intentUserGender");
        intentUserAge = intent.getExtras().getString("intentUserAge");
        intentUserLifestyle = intent.getExtras().getString("intentUserLifestyle");
        intentUserWeight = intent.getExtras().getString("intentUserWeight");
        intentUserHeight = intent.getExtras().getString("intentUserHeight");


        View view = LayoutInflater.from(mContext).inflate(R.layout.workout_raw,parent,false);
        return new WorkoutAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutAdapter.MyViewHolder holder, int position) {

        final Workout workout = workouts.get(position);

        final String workoutID = workout.getWorkoutID();
        final String tempWorkoutName = workout.getWorkoutType();
        final String tempWorkoutMET = workout.getworkoutMET();

        if(!(TextUtils.isEmpty(tempWorkoutName)))
        {
            holder.workoutType.setText(tempWorkoutName);
        }

        if(!(TextUtils.isEmpty(tempWorkoutMET)))
        {
            holder.workoutMET.setText("MET:"+tempWorkoutMET);
        }


        holder.workoutLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendUserToAddWorkoutPage(workoutID);
            }
        });

    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    private void SendUserToAddWorkoutPage(String workoutID)
    {
        Intent addWorkoutIntent = new Intent(mContext, AddWorkoutActivity.class);
        //addWorkoutIntent.putExtra("intentFrom", "Workout");
        addWorkoutIntent.putExtra("intentUserID", intentUserID);
        addWorkoutIntent.putExtra("workoutID", workoutID);
        addWorkoutIntent.putExtra("intentUserGender", intentUserGender);
        addWorkoutIntent.putExtra("intentUserAge", intentUserAge);
        addWorkoutIntent.putExtra("intentUserLifestyle", intentUserLifestyle);
        addWorkoutIntent.putExtra("intentUserWeight", intentUserWeight);
        addWorkoutIntent.putExtra("intentUserHeight", intentUserHeight);
        mContext.startActivity(addWorkoutIntent);

    }



}
