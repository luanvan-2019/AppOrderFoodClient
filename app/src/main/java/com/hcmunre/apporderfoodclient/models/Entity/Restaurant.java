package com.hcmunre.apporderfoodclient.models.Entity;

import java.io.Serializable;
import java.sql.Time;

public class Restaurant implements Serializable {
    private int mId;
    private String mName,mAddress,mPhone;
    private Double mLat,mLng;
    private String mImage;
    private String UserOwner;
    private Time opening,closing;


    public Restaurant() {
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

    public String getUserOwner() {
        return UserOwner;
    }

    public void setUserOwner(String userOwner) {
        UserOwner = userOwner;
    }

    public Time getOpening() {
        return opening;
    }

    public void setOpening(Time opening) {
        this.opening = opening;
    }

    public Time getClosing() {
        return closing;
    }

    public void setClosing(Time closing) {
        this.closing = closing;
    }
}
