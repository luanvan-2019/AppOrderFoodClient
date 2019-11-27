package com.hcmunre.apporderfoodclient.models.Entity;

import com.hcmunre.apporderfoodclient.interfaces.CartDAO;
import com.hcmunre.apporderfoodclient.interfaces.CartDataSource;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class LocalCartDataSource implements CartDataSource {
    private CartDAO cartDAO;

    public LocalCartDataSource(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    @Override
    public Flowable<List<CartItem>> getAllCart(String email, int restaurantId) {
        return cartDAO.getAllCart(email, restaurantId);
    }

    @Override
    public Flowable<List<AllCartItem>> getAllCartItem(String email) {
        return cartDAO.getAllCartItem(email);
    }

    @Override
    public Single<Long> sumPriceByRestaurant(String email) {
        return cartDAO.sumPriceByRestaurant(email);
    }

    @Override
    public Single<Integer> countItemInCart(String email, int restaurantId) {
        return cartDAO.countItemInCart(email, restaurantId);
    }

    @Override
    public Single<Long> sumPrice(String email, int restaurantId) {
        return cartDAO.sumPrice(email, restaurantId);
    }

    @Override
    public Single<CartItem> getItemInCart(String foodId, String email, int restaurantId) {
        return cartDAO.getItemInCart(foodId, email, restaurantId);
    }

    @Override
    public Completable insertOrReplaceAll(CartItem... cartItems) {
        return cartDAO.insertOrReplaceAll(cartItems);
    }

    @Override
    public Single<Integer> updateCart(CartItem cartItem) {
        return cartDAO.updateCart(cartItem);
    }

    @Override
    public Single<Integer> deleteCart(CartItem cartItem) {
        return cartDAO.deleteCart(cartItem);
    }

    @Override
    public Single<Integer> cleanCart(String email, int restaurantId) {
        return cartDAO.cleanCart(email, restaurantId);
    }

}
