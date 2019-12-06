package com.hcmunre.apporderfoodclient.views.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.contentcapture.ContentCaptureCondition;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.interfaces.CartDataSource;
import com.hcmunre.apporderfoodclient.interfaces.ImageOnClickListener;
import com.hcmunre.apporderfoodclient.models.Entity.CartData;
import com.hcmunre.apporderfoodclient.models.Entity.CartItem;
import com.hcmunre.apporderfoodclient.models.Entity.LocalCartDataSource;
import com.hcmunre.apporderfoodclient.models.eventbus.CalculatePriceEvent;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Context context;
    List<CartItem> cartItems;
    CartDataSource cartDataSource;
    public CartAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
        cartDataSource=new LocalCartDataSource(CartData.getInstance(context).cartDAO());
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart,null);
        return new CartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        CartItem cartItem=cartItems.get(position);
       if(cartItem.getFoodImage()!=null){
           holder.img_food.setImageBitmap(Common.getBitmap(cartItem.getFoodImage()));
       }
        holder.txt_food_name.setText(cartItem.getFoodName());
        holder.txt_food_price.setText(new StringBuilder(holder.currencyVN.format(cartItem.getFoodPrice())).append("đ"));
        holder.txtQuantity.setText(String.valueOf(cartItem.getFoodQuantity()));
        Float result=cartItem.getFoodPrice()*cartItem.getFoodQuantity();
        holder.txtTotalPrice.setText(new StringBuilder(holder.currencyVN.format(result)).append("đ"));
        holder.setCartOnClickListener((view, position1, isMinus, isDelete) -> {
            if(!isDelete){
                //NẾU không có button delete
                if(isMinus){//nếu giảm số lượng
                    if(cartItem.getFoodQuantity()>1){
                        cartItem.setFoodQuantity(cartItem.getFoodQuantity()-1);
                    }
                }else {//nếu tăng số lương
                    if(cartItem.getFoodQuantity()<99){
                        cartItem.setFoodQuantity(cartItem.getFoodQuantity()+1);
                    }
                }
                //cập nhật cart
                cartDataSource.updateCart(cartItem)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<Integer>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(Integer integer) {
                                holder.txtQuantity.setText(String.valueOf(cartItem.getFoodQuantity()));
                                EventBus.getDefault().postSticky(new CalculatePriceEvent());
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(context, "[UPDATE CART]"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else {
                //xóa item
                cartDataSource.deleteCart(cartItems.get(position))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<Integer>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(Integer integer) {
                                notifyItemRemoved(position);
                                EventBus.getDefault().postSticky(new CalculatePriceEvent());
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(context, "[DELETE ITEM]"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
        );
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.txtTotalPrice)
        TextView txtTotalPrice;
        @BindView(R.id.txtQuantity)
        TextView txtQuantity;
        @BindView(R.id.txt_food_name)
        TextView txt_food_name;
        @BindView(R.id.txt_food_price)
        TextView txt_food_price;
        @BindView(R.id.img_Minus)
        LinearLayout img_Minus;
        @BindView(R.id.img_Add)
        LinearLayout img_Add;
        @BindView(R.id.img_delete_food)
        ImageView img_delete_food;
        @BindView(R.id.img_food)
        ImageView img_food;
        ImageOnClickListener cartOnClickListener;
        Locale locale=new Locale("vi","VN");
        NumberFormat currencyVN=NumberFormat.getInstance(locale);
        public void setCartOnClickListener(ImageOnClickListener cartOnClickListener) {
            this.cartOnClickListener = cartOnClickListener;
        }

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this,itemView);
            img_Minus.setOnClickListener(this);
            img_Add.setOnClickListener(this);
            img_delete_food.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view==img_Minus){
                cartOnClickListener.onCalculatePriceListener(view,getAdapterPosition(),true,false);
            }else if(view==img_Add){
                cartOnClickListener.onCalculatePriceListener(view,getAdapterPosition(),false,false);
            }
            else if(view==img_delete_food){
                cartOnClickListener.onCalculatePriceListener(view,getAdapterPosition(),true,true);
            }
        }
    }
}
