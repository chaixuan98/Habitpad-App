package com.example.habitpadapplication.Model;

public class Gift {

    private String giftID, giftimage, giftpoint, giftreward;

    public Gift(String giftID, String giftimage , String giftpoint, String giftreward){
        this.giftID = giftID;
        this.giftimage = giftimage;
        this.giftpoint = giftpoint;
        this.giftreward = giftreward;
    }

    public String getGiftID() {
        return giftID;
    }

    public String getGiftimage() {
        return giftimage;
    }

    public String getGiftpoint() {
        return giftpoint;
    }

    public String getGiftreward() {
        return giftreward;
    }

}
