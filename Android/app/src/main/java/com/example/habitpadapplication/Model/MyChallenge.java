package com.example.habitpadapplication.Model;

public class MyChallenge {

    private String mchallengeID, mchallengeImage, mchallengeTitle, mchallengeEndDate;

    public MyChallenge(String mchallengeID, String mchallengeImage , String mchallengeTitle, String mchallengeEndDate){
        this.mchallengeID = mchallengeID;
        this.mchallengeImage = mchallengeImage;
        this.mchallengeTitle = mchallengeTitle;
        this.mchallengeEndDate = mchallengeEndDate;
    }

    public String getMchallengeID() {
        return mchallengeID;
    }

    public String getMchallengeImage() {
        return mchallengeImage;
    }

    public String getMchallengeTitle() {
        return mchallengeTitle;
    }

    public String getMchallengeEndDate() {
        return mchallengeEndDate;
    }
}
