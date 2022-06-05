package com.example.habitpadapplication.Model;

public class Challenge {

    private String challengeID, challengeImage, challengeTitle;

    public Challenge(String challengeID, String challengeImage , String challengeTitle){
        this.challengeID = challengeID;
        this.challengeImage = challengeImage;
        this.challengeTitle = challengeTitle;
    }

    public String getChallengeID() {
        return challengeID;
    }

    public String getChallengeImage() {
        return challengeImage;
    }

    public String getChallengeTitle() {
        return challengeTitle;
    }

}
