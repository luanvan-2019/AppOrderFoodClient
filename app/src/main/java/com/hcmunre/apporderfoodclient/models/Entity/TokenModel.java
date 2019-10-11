package com.hcmunre.apporderfoodclient.models.Entity;

import java.util.ArrayList;

public class TokenModel {
    private boolean success;
    private String message;
    private ArrayList<Token> tokens;

    public TokenModel() {
    }

    public TokenModel(boolean success, String message, ArrayList<Token> tokens) {
        this.success = success;
        this.message = message;
        this.tokens = tokens;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public void setTokens(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }
}
