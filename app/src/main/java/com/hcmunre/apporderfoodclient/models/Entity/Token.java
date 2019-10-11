package com.hcmunre.apporderfoodclient.models.Entity;

public class Token {
    private String FBID;
    private String toKen;

    public Token() {
    }

    public Token(String FBID, String toKen) {
        this.FBID = FBID;
        this.toKen = toKen;
    }

    public String getFBID() {
        return FBID;
    }

    public void setFBID(String FBID) {
        this.FBID = FBID;
    }

    public String getToKen() {
        return toKen;
    }

    public void setToKen(String toKen) {
        this.toKen = toKen;
    }
}
