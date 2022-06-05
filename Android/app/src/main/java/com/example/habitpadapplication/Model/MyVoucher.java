package com.example.habitpadapplication.Model;

public class MyVoucher {

    private String voucherID, myVoucherImage,myVoucherReward;

    public MyVoucher(String voucherID, String myimage, String myreward){
        this.voucherID =voucherID;
        this.myVoucherImage = myVoucherImage;
        this.myVoucherReward = myVoucherReward;
    }

    public String getVoucherID() {
        return voucherID;
    }

    public String getMyVoucherImage() {
        return myVoucherImage;
    }


    public String getMyVoucherReward() {
        return myVoucherReward;
    }
}
