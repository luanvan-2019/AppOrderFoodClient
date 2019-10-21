package com.hcmunre.apporderfoodclient.views.adapters;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.interfaces.CartDataSource;
import com.hcmunre.apporderfoodclient.interfaces.cartClickListerner;
import com.hcmunre.apporderfoodclient.models.Entity.CartData;
import com.hcmunre.apporderfoodclient.models.Entity.CartItem;
import com.hcmunre.apporderfoodclient.models.Entity.Food;
import com.hcmunre.apporderfoodclient.models.Entity.LocalCartDataSource;
import com.hcmunre.apporderfoodclient.models.Entity.Restaurant;
import com.hcmunre.apporderfoodclient.models.Entity.User;
import com.hcmunre.apporderfoodclient.views.activities.PreferenceUtils;

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
        holder.txtitem_price.setText(holder.currencyVN.format(food.getPrice()));
        byte[] decodeString= Base64.decode(food.getImage(),Base64.DEFAULT);
        Bitmap decodeImage= BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
        holder.img_food.setImageBitmap(decodeImage);
        holder.setListerner((view, position1) -> {
            //tạo cart
            CartItem cartItem=new CartItem();
            cartItem.setFoodId(food.getId());
            cartItem.setFoodName(food.getName());
            cartItem.setFoodImage(food.getImage());
            cartItem.setFoodPrice(food.getPrice());
            cartItem.setFoodQuantity(1);
            cartItem.setEmail(PreferenceUtils.getEmail(activity));
            cartItem.setRestaurantId(Common.currentRestaurant.getmId());
            compositeDisposable.add(
                    cartDataSource.insertOrReplaceAll(cartItem)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(()->{
                                        Toast.makeText(activity, "Đã thêm vào giỏ hàng "+food.getName(), Toast.LENGTH_SHORT).show();
                                    },
                                    throwable -> {
                                        Toast.makeText(activity, "[THÊM VÀO GIỎ]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    })
            );
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
        @BindView(R.id.quantityTxt)
        TextView quantityTxt;
        @BindView(R.id.btnAddCart)
        LinearLayout btnAddCart;
        @BindView(R.id.llMinus)
        LinearLayout llMinus;
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
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
