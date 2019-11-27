package com.hcmunre.apporderfoodclient.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.models.Entity.CartItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailFoodAdapter extends RecyclerView.Adapter<OrderDetailFoodAdapter.ViewHolder> {
    private Context context;
    private List<CartItem> cartItems;

    public OrderDetailFoodAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_detail_food,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem cartItem=cartItems.get(position);
        holder.txt_name_food.setText(cartItem.getFoodName());
        holder.txt_quantity.setText(String.valueOf(cartItem.getFoodQuantity()));
        holder.txt_price.setText(new StringBuffer(holder.currency.format(cartItem.getFoodPrice())).append("đ"));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_quantity)
        TextView txt_quantity;
        @BindView(R.id.txt_name_food)
        TextView txt_name_food;
        @BindView(R.id.txt_price)
        TextView txt_price;
        Locale locale=new Locale("vi","VN");
        NumberFormat currency=NumberFormat.getInstance(locale);
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
