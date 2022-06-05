package com.example.habitpadapplication.Model;

public class Leaderboard {

    private String leaderboardRank,leaderboardUsername,leaderboardStep;

    public Leaderboard(String leaderboardRank,String leaderboardUsername, String leaderboardStep) {
        this.leaderboardRank = leaderboardRank;
        this.leaderboardUsername = leaderboardUsername;
        this.leaderboardStep = leaderboardStep;

    }

    public String getLeaderboardRank() {
        return leaderboardRank;
    }

    public String getLeaderboardUsername() {
        return leaderboardUsername;
    }

    public String getLeaderboardStep() {
        return leaderboardStep;
    }
}
