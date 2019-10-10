package com.hcmunre.apporderfoodclient.models.Entity;

public class User {
    private String mName;
    private String mAddress;
    private String mPhone;
    private byte[] mImage;
    private String mEmail;
    private String mPassword;
    public User(){
    }

    public User(String mName, String mAddress, String mPhone, byte[] mImage, String mEmail, String mPassword) {
        this.mName = mName;
        this.mAddress = mAddress;
        this.mPhone = mPhone;
        this.mImage = mImage;
        this.mEmail = mEmail;
        this.mPassword = mPassword;
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

    public byte[] getmImage() {
        return mImage;
    }

    public void setmImage(byte[] mImage) {
        this.mImage = mImage;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }
}
