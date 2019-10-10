package com.hcmunre.apporderfoodclient.models.Entity;

import java.util.ArrayList;

public class FoodModel {
    private String name;
    public ArrayList<Food> productsArrayList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Food> getProductsArrayList() {
        return productsArrayList;
    }

    public void setProductsArrayList(ArrayList<Food> productsArrayList) {
        this.productsArrayList = productsArrayList;
    }
}
