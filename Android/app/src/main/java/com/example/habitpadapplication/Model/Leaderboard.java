package com.example.habitpadapplication.Model;

public class Leaderboard {

    private String leaderboardRank,leaderboardUsername,leaderboardPoint;

    public Leaderboard(String leaderboardRank,String leaderboardUsername, String leaderboardPoint) {
        this.leaderboardRank = leaderboardRank;
        this.leaderboardUsername = leaderboardUsername;
        this.leaderboardPoint = leaderboardPoint;

    }

    public String getLeaderboardRank() {
        return leaderboardRank;
    }

    public String getLeaderboardUsername() {
        return leaderboardUsername;
    }

    public String getLeaderboardPoint() {
        return leaderboardPoint;
    }
}
