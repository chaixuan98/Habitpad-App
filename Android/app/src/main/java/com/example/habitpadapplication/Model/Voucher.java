package com.example.habitpadapplication.Model;

public class Voucher {

    private String voucherID, image,point,reward;

    public Voucher(String voucherID, String image , String point, String reward){
        this.voucherID = voucherID;
        this.image = image;
        this.point = point;
        this.reward = reward;
    }

    public String getVoucherID() {
        return voucherID;
    }

    public String getVoucherImage() {
        return image;
    }

    public String getPoint() {
        return point;
    }

    public String getReward() {
        return reward;
    }
}
