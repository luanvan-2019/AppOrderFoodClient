package com.hcmunre.apporderfoodclient.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.models.Entity.Order;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryCartAdatper extends RecyclerView.Adapter<HistoryCartAdatper.ViewHolder> {
    private Context context;
    private ArrayList<Order> listOrderHistory;
    SimpleDateFormat simpleDateFormat;

    public HistoryCartAdatper(Context context, ArrayList<Order> listOrderHistory) {
        this.context = context;
        this.listOrderHistory = listOrderHistory;
        simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
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
        holder.txt_orderId.setText(order.getId()+"");
        holder.txt_proccess.setText(Common.convertStatusToString(order.getOrderStatus()));
        holder.txt_phone.setText(order.getOrderPhone());
        holder.txt_address.setText(order.getOrderAddress());
        holder.txt_date.setText(new StringBuilder(simpleDateFormat.format(order.getOrderDate())));
        holder.txt_quantity.setText(order.getNumberOfItem()+"");
        holder.txt_total_price.setText(String.valueOf(holder.numberFormat.format(order.getTotalPrice())));
    }

    @Override
    public int getItemCount() {
        return listOrderHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_orderId)
        TextView txt_orderId;
        @BindView(R.id.txt_proccess)
        TextView txt_proccess;
        @BindView(R.id.txt_phone)
        TextView txt_phone;
        @BindView(R.id.txt_address)
        TextView txt_address;
        @BindView(R.id.txt_date)
        TextView txt_date;
        @BindView(R.id.txt_quantity)
        TextView txt_quantity;
        @BindView(R.id.txt_total_price)
        TextView txt_total_price;
        Locale locale=new Locale("vi","VN");
        NumberFormat numberFormat=NumberFormat.getCurrencyInstance(locale);
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
