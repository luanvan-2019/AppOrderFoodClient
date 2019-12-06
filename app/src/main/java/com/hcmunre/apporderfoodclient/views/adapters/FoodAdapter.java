package com.hcmunre.apporderfoodclient.views.adapters;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.interfaces.CartDataSource;
import com.hcmunre.apporderfoodclient.interfaces.cartClickListerner;
import com.hcmunre.apporderfoodclient.models.Database.FoodData;
import com.hcmunre.apporderfoodclient.models.Entity.CartData;
import com.hcmunre.apporderfoodclient.models.Entity.CartItem;
import com.hcmunre.apporderfoodclient.models.Entity.Favorite;
import com.hcmunre.apporderfoodclient.models.Entity.Food;
import com.hcmunre.apporderfoodclient.models.Entity.LocalCartDataSource;
import com.hcmunre.apporderfoodclient.models.Entity.FavoriteOnlyId;
import com.hcmunre.apporderfoodclient.models.eventbus.CalculatePriceEvent;
import com.hcmunre.apporderfoodclient.views.activities.PreferenceUtils;
import com.hcmunre.apporderfoodclient.views.activities.SignInActivity;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private ArrayList<Food> arrayList;
    private Activity activity;
    CompositeDisposable compositeDisposable;
    CartDataSource cartDataSource;
    FoodData foodData=new FoodData();
    public void onStop(){
        compositeDisposable.clear();
    }
    public FoodAdapter(ArrayList<Food> arrayList, Activity activity) {
        this.arrayList = arrayList;
        this.activity = activity;
        compositeDisposable=new CompositeDisposable();
        cartDataSource=new LocalCartDataSource(CartData.getInstance(activity).cartDAO());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_restaurant, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Food food = arrayList.get(position);
        holder.itemView.setTag(position);
        holder.txtitem_name.setText(food.getName());
        holder.txtitem_price.setText(new StringBuilder(holder.currencyVN.format(food.getPrice())).append("đ"));
        holder.txtdescription.setText(food.getDescription());
        if(food.getImage()!=null){
            holder.img_food.setImageBitmap(Common.getBitmap(food.getImage()));
        }
        holder.txt_status_food.setText(Common.convertStatusFoodToString(food.getStatus()));
        if(holder.txt_status_food.getText().toString()=="Hết món"){
            holder.txt_status_food.setVisibility(View.VISIBLE);
            holder.btnAddCart.setVisibility(View.GONE);
            holder.btn_fav.setVisibility(View.GONE);
        }else {
            holder.txt_status_food.setVisibility(View.GONE);
            holder.btnAddCart.setVisibility(View.VISIBLE);
            holder.setListerner((view, position1) -> {
                //tạo cart
                if(PreferenceUtils.getEmail(activity)!=null){
                    CartItem cartItem=new CartItem();
                    cartItem.setFoodId(food.getId());
                    cartItem.setFoodName(food.getName());
                    cartItem.setFoodImage(food.getImage());
                    cartItem.setFoodPrice(food.getPrice());
                    cartItem.setFoodQuantity(1);
                    cartItem.setEmail(PreferenceUtils.getEmail(activity));
                    cartItem.setRestaurantId(Common.currentRestaurant.getmId());
                    cartItem.setRestaurantName(Common.currentRestaurant.getmName());
                    cartItem.setRestaurantImage(Common.currentRestaurant.getmImage());
                    compositeDisposable.add(
                            cartDataSource.insertOrReplaceAll(cartItem)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(()->{
                                                EventBus.getDefault().postSticky(new CalculatePriceEvent());
                                            },
                                            throwable -> {
                                                Toast.makeText(activity, "[THÊM VÀO GIỎ]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                            })
                    );
                }else {
                    activity.startActivity(new Intent(activity, SignInActivity.class));
                    activity.finish();
                }

            });
        }
        if(Common.currentFavorite!=null&&Common.currentFavorite.size()>0){
            if(Common.checkFavorite(food.getId())){
                holder.btn_fav.setImageResource(R.drawable.ic_favorite_orange);
                holder.btn_fav.setTag(true);
            }else {
                holder.btn_fav.setImageResource(R.drawable.ic_favorite_border);
                holder.btn_fav.setTag(false);
            }
        }else {
            holder.btn_fav.setTag(false);
        }
        holder.btn_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView=(ImageView)v;
                if(PreferenceUtils.getEmail(activity)!=null){
                    if((Boolean)v.getTag()){
                        Favorite favorite=new Favorite();
                        favorite.setUserId(PreferenceUtils.getUserId(activity));
                        favorite.setRestaurantId(Common.currentRestaurant.getmId());
                        favorite.setFoodId(food.getId());
                        boolean success=foodData.deleteFavorite(favorite);
                        Log.d("BBB",success+"XÓA");
                        if(success==false){
                            Common.showToast(activity,"Đã xóa món ăn yêu thích");
                            imageView.setImageResource(R.drawable.ic_favorite_border);
                            imageView.setTag(false);
                            if(Common.currentFavorite!=null){
                                Common.removeFavorite(food.getId());
                            }
                        }else {
                            Common.showToast(activity,"Không thể xóa");
                        }
                    }else {
                        Favorite favorite=new Favorite();
                        favorite.setUserId(PreferenceUtils.getUserId(activity));
                        favorite.setRestaurantId(Common.currentRestaurant.getmId());
                        favorite.setFoodId(food.getId());
                        favorite.setRestaurantName(Common.currentRestaurant.getmName());
                        favorite.setFoodName(food.getName());
                        favorite.setFoodImage(food.getImage());
                        favorite.setPrice(food.getPrice());
                        boolean success=foodData.insertFavorite(favorite);
                        if(success==false){
                            Common.showToast(activity,"Đã thêm món ăn yêu thích");
                            imageView.setImageResource(R.drawable.ic_favorite_orange);
                            imageView.setTag(true);
                            if(Common.currentFavorite!=null){
                                Common.currentFavorite.add(new FavoriteOnlyId(food.getId()));
                            }
                        }else {
                            Common.showToast(activity,"Không thể thêm");
                        }
                    }
                }else {
                    activity.startActivity(new Intent(activity,SignInActivity.class));
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size() > 0 ? arrayList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txtitem_name)
        TextView txtitem_name;
        @BindView(R.id.txtitem_price)
        TextView txtitem_price;
        @BindView(R.id.img_food)
        ImageView img_food;
        @BindView(R.id.btn_fav)
        ImageView btn_fav;
        @BindView(R.id.btnAddCart)
        ImageView btnAddCart;
        @BindView(R.id.txtdescription)
        TextView txtdescription;
        @BindView(R.id.txt_status_food)
        TextView txt_status_food;
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getInstance(localeVN);
        cartClickListerner listerner;
        public void setListerner(cartClickListerner listerner) {
            this.listerner = listerner;
        }
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            btnAddCart.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
                listerner.onFoodItemClickLister(view,getAdapterPosition());
        }
    }

}
