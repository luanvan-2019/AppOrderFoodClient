package com.hcmunre.apporderfoodclient.models.Entity;

import java.io.Serializable;
import java.sql.Time;

public class Restaurant implements Serializable {
    private int mId;
    private String mName,mAddress,mPhone;
    private Double mLat,mLng;
    private String mImage;
    private Time Opening_Closing_Time;
    private String UserOwner;
//    Integer mImage;
//    String mName,mPrice;


    public Restaurant() {
    }

    public Restaurant(int mId, String mName, String mAddress, String mPhone, Double mLat, Double mLng,String mImage, Time opening_Closing_Time, String userOwner) {
        this.mId = mId;
        this.mName = mName;
        this.mAddress = mAddress;
        this.mPhone = mPhone;
        this.mLat = mLat;
        this.mLng = mLng;
        this.mImage = mImage;
        Opening_Closing_Time = opening_Closing_Time;
        UserOwner = userOwner;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public Double getmLat() {
        return mLat;
    }

    public void setmLat(Double mLat) {
        this.mLat = mLat;
    }

    public Double getmLng() {
        return mLng;
    }

    public void setmLng(Double mLng) {
        this.mLng = mLng;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public Time getOpening_Closing_Time() {
        return Opening_Closing_Time;
    }

    public void setOpening_Closing_Time(Time opening_Closing_Time) {
        Opening_Closing_Time = opening_Closing_Time;
    }

    public String getUserOwner() {
        return UserOwner;
    }

    public void setUserOwner(String userOwner) {
        UserOwner = userOwner;
    }
}
