package com.hcmunre.apporderfoodclient.models.Entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Food implements Parcelable {
    public int id;
    public String name;
    public Float price;
    public String nameMenu;
    public  int imageFood;
    private int quantity = 0;
    private String priceAsPerQuantity;
    public Food(){

    }

    public Food(String name, Float price, String nameMenu) {
        this.name = name;
        this.price = price;
        this.nameMenu = nameMenu;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPriceAsPerQuantity() {
        return priceAsPerQuantity;
    }

    public void setPriceAsPerQuantity(String priceAsPerQuantity) {
        this.priceAsPerQuantity = priceAsPerQuantity;
    }

    public String getNameMenu() {
        return nameMenu;
    }

    public void setNameMenu(String nameMenu) {
        this.nameMenu = nameMenu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public int getImageFood() {
        return imageFood;
    }

    public void setImageFood(int imageFood) {
        this.imageFood = imageFood;
    }

    public Food(int id, String name, Float price, int imageFood) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageFood = imageFood;
    }

    protected Food(Parcel in) {
        id = in.readInt();
        name = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readFloat();
        }
        imageFood = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(price);
        }
        dest.writeInt(imageFood);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };
}
