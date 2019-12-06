package com.hcmunre.apporderfoodclient.models.Entity;

import java.io.Serializable;

public class Menu implements Serializable {
    private int mId;
    private String mName;
    private String mImage;
    public Menu() {
    }

    public Menu(int mId, String mName, String mImage) {
        this.mId = mId;
        this.mName = mName;
        this.mImage = mImage;
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

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }
}
