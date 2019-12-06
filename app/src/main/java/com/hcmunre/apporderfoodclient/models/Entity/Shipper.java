package com.hcmunre.apporderfoodclient.models.Entity;

public class Shipper {
    private String name,address,phone;
    private Double lat,lng;
    private String image;
    private int shippingStatus;

    public Shipper() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(int shippingStatus) {
        this.shippingStatus = shippingStatus;
    }
}
