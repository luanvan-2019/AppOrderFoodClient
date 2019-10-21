package com.hcmunre.apporderfoodclient.models.Entity;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.hcmunre.apporderfoodclient.interfaces.CartDAO;

@Database(version = 2,entities = CartItem.class,exportSchema = false)
public abstract class CartData extends RoomDatabase {

    public abstract CartDAO cartDAO();
    private static CartData instance;
    public static CartData getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context,CartData.class,"MyRestaurant")
                    .fallbackToDestructiveMigration()
            .build();
        }
        return instance;
    }
}
