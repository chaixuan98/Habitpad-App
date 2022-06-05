package com.example.habitpadapplication.Model;

public class MyGift {

    private String giftID,myGiftImage,myGiftReward;

    public MyGift(String giftID, String myGiftImage, String myGiftReward){
        this.giftID = giftID;
        this.myGiftImage = myGiftImage;
        this.myGiftReward = myGiftReward;
    }

    public String getGiftID() {
        return giftID;
    }
    public String getMyGiftImage() {
        return myGiftImage;
    }

    public String getMyGiftReward() {
        return myGiftReward;
    }
}
