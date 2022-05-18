package com.example.habitpadapplication.Model;

public class MyGift {

    private String myGiftImage,myGiftReward;

    public MyGift(String myGiftImage, String myGiftReward){
        this.myGiftImage = myGiftImage;
        this.myGiftReward = myGiftReward;
    }

    public String getMyGiftImage() {
        return myGiftImage;
    }

    public String getMyGiftReward() {
        return myGiftReward;
    }
}
