package com.hcmunre.apporderfoodclient.interfaces;

import com.hcmunre.apporderfoodclient.models.Entity.AllCartItem;
import com.hcmunre.apporderfoodclient.models.Entity.CartItem;
import com.hcmunre.apporderfoodclient.models.Entity.Status;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface CartDataSource {
    Flowable<List<CartItem>> getAllCart(String email, int restaurantId);

    Flowable<List<AllCartItem>> getAllCartItem(String email);

    Single<Long> sumPriceByRestaurant(String email);

    Single<Integer> countItemInCart(String email, int restaurantId);

    Single<Long> sumPrice(String email,int restaurantId);

    Single<CartItem> getItemInCart(String foodId,String email,int restaurantId);

    Completable insertOrReplaceAll(CartItem... cartItems);

    Single<Integer> updateCart(CartItem cartItem);

    Single<Integer> deleteCart(CartItem cartItem);

    Single<Integer> cleanCart(String email,int restaurantId);
}
