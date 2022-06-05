package com.example.habitpadapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitpadapplication.Model.Leaderboard;
import com.example.habitpadapplication.R;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.MyViewHolder> {

    private Context context;
    private List<Leaderboard> leaderboards = new ArrayList<>();

    public LeaderboardAdapter(Context context, List<Leaderboard> leaderboards) {
        this.leaderboards = leaderboards;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView leaderboardRank, leaderboardUsername, leaderboardStep;

        public MyViewHolder (View view){
            super(view);

            leaderboardRank = view.findViewById(R.id.leaderboard_rank);
            leaderboardUsername = view.findViewById(R.id.leaderboard_username);
            leaderboardStep = view.findViewById(R.id.leaderboard_total_step);

        }
    }

    @NonNull
    @Override
    public LeaderboardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.leaderboard_raw,parent,false);
        return new LeaderboardAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardAdapter.MyViewHolder holder, int position) {

        final Leaderboard leaderboard = leaderboards.get(position);

        holder.leaderboardRank.setText(leaderboard.getLeaderboardRank());
        holder.leaderboardUsername.setText(leaderboard.getLeaderboardUsername());
        holder.leaderboardStep.setText(leaderboard.getLeaderboardStep());

    }

    @Override
    public int getItemCount() {
        return leaderboards.size();
    }
}
