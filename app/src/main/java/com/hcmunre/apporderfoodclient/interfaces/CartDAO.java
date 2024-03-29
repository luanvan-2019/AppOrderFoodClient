package com.hcmunre.apporderfoodclient.interfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.hcmunre.apporderfoodclient.models.Entity.AllCartItem;
import com.hcmunre.apporderfoodclient.models.Entity.CartItem;
import com.hcmunre.apporderfoodclient.models.Entity.Status;

import java.time.Instant;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
@Dao
public interface CartDAO {
    @Query("SELECT * FROM Cart WHERE email=:email AND restaurantId=:restaurantId")
    Flowable<List<CartItem>> getAllCart(String email, int restaurantId);

    @Query("SELECT SUM(foodPrice*foodQuantity) FROM Cart " +
            "WHERE email=:email " +
            "GROUP BY restaurantId")
    Single<Long> sumPriceByRestaurant(String email);
    //
    @Query("SELECT restaurantId,restaurantName,restaurantImage,SUM(foodPrice*foodQuantity) AS totalPrice,COUNT(foodId) AS totalItem  FROM Cart WHERE email=:email GROUP BY restaurantId")
    Flowable<List<AllCartItem>> getAllCartItem(String email);
    //
    @Query("SELECT COUNT(*) FROM Cart WHERE email=:email AND restaurantId=:restaurantId")
    Single<Integer> countItemInCart(String email,int restaurantId);

    @Query("SELECT SUM(foodPrice*foodQuantity) FROM Cart WHERE email=:email AND restaurantId=:restaurantId")
    Single<Long> sumPrice(String email,int restaurantId);

    @Query("SELECT * FROM Cart WHERE foodId=:foodId AND email=:email AND restaurantId=:restaurantId")
    Single<CartItem> getItemInCart(String foodId,String email,int restaurantId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)//nếu confict foodId,sẽ cập nhật thông tin
    Completable insertOrReplaceAll(CartItem... cartItems);

    @Update(onConflict=OnConflictStrategy.REPLACE)
    Single<Integer> updateCart(CartItem cartItem);

    @Delete
    Single<Integer> deleteCart(CartItem cartItem);

    @Query("DELETE FROM Cart WHERE email=:email AND restaurantId=:restaurantId ")
    Single<Integer> cleanCart(String email,int restaurantId);

}
