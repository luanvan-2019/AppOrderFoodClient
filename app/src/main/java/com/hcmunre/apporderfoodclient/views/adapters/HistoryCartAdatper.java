package com.hcmunre.apporderfoodclient.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
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
import com.hcmunre.apporderfoodclient.models.Entity.Order;
import com.hcmunre.apporderfoodclient.views.activities.TrackingOrderActivity;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryCartAdatper extends RecyclerView.Adapter<HistoryCartAdatper.ViewHolder> {
    private Context context;
    private ArrayList<Order> listOrderHistory;

    public HistoryCartAdatper(Context context, ArrayList<Order> listOrderHistory) {
        this.context = context;
        this.listOrderHistory = listOrderHistory;
    }

    @NonNull
    @Override
    public HistoryCartAdatper.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_history,null);
        return new HistoryCartAdatper.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryCartAdatper.ViewHolder holder, int position) {
        Order order=listOrderHistory.get(position);
        if(Common.currentRestaurant.getmImage()!=null){
            byte[] decodeString = Base64.decode(order.getImage(), Base64.DEFAULT);
            Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
            holder.image_restaurant.setImageBitmap(decodebitmap);
        }
        if(order.getOrderDate()!=null){
            holder.txt_order_date.setText(order.getOrderDate());
        }
        holder.txt_quantity.setText(new StringBuilder("").append(order.getNumberOfItem()).append(" phần x "));
        holder.txt_total_price.setText(new StringBuilder(holder.numberFormat.format(order.getTotalPrice())).append("đ"));
        holder.txt_name_restaurant.setText(order.getNameRestaurant());
//        if(Common.curentOrder.getId()==3){
//            holder.image_complete.setVisibility(View.VISIBLE);
//        }else {
//            holder.image_complete.setVisibility(View.GONE);
//        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.curentOrder=listOrderHistory.get(position);
                context.startActivity(new Intent(context, TrackingOrderActivity.class));
            }
        });
    }
    public void refresh(ArrayList<Order> orders) {
        this.listOrderHistory = orders;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return listOrderHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_name_restaurant)
        TextView txt_name_restaurant;
        @BindView(R.id.txt_order_date)
        TextView txt_order_date;
        @BindView(R.id.txt_quantity)
        TextView txt_quantity;
        @BindView(R.id.txt_total_price)
        TextView txt_total_price;
        @BindView(R.id.image_restaurant)
        RoundedImageView image_restaurant;
        @BindView(R.id.image_complete)
        ImageView image_complete;
        Locale locale=new Locale("vi","VN");
        NumberFormat numberFormat=NumberFormat.getInstance(locale);
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
