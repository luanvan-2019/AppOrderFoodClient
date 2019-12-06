package com.hcmunre.apporderfoodclient.models.eventbus;

public class AddressEvent {
    private String addess;

    public AddressEvent(String addess) {
        this.addess = addess;
    }

    public String getAddess() {
        return addess;
    }

    public void setAddess(String addess) {
        this.addess = addess;
    }
}
