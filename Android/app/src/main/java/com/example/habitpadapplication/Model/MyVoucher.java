package com.example.habitpadapplication.Model;

public class MyVoucher {

    private String myVoucherID, myVoucherImage,myVoucherTitle;

    public MyVoucher(String myVoucherID, String myVoucherImage, String myVoucherTitle){
        this.myVoucherID =myVoucherID;
        this.myVoucherImage = myVoucherImage;
        this.myVoucherTitle = myVoucherTitle;
    }

    public String getMyVoucherID() {
        return myVoucherID;
    }

    public String getMyVoucherImage() {
        return myVoucherImage;
    }


    public String getMyVoucherTitle() {
        return myVoucherTitle;
    }
}
