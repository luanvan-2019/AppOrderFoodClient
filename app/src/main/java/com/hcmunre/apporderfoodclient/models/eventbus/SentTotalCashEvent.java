package com.hcmunre.apporderfoodclient.models.eventbus;

public class SentTotalCashEvent {
    private String cash;

    public SentTotalCashEvent(String cash) {
        this.cash = cash;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }
}
