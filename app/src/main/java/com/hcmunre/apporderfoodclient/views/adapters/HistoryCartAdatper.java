package com.hcmunre.apporderfoodclient.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import com.hcmunre.apporderfoodclient.interfaces.LocalStatusDataSource;
import com.hcmunre.apporderfoodclient.interfaces.StatusDataSource;
import com.hcmunre.apporderfoodclient.models.Database.ShipperData;
import com.hcmunre.apporderfoodclient.models.Entity.CartData;
import com.hcmunre.apporderfoodclient.models.Entity.Order;
import com.hcmunre.apporderfoodclient.models.Entity.Shipper;
import com.hcmunre.apporderfoodclient.views.activities.TrackingOrderActivity;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HistoryCartAdatper extends RecyclerView.Adapter<HistoryCartAdatper.ViewHolder> {
    private Context context;
    private ArrayList<Order> listOrderHistory;
    ShipperData shipperData=new ShipperData();
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
        if(order.getImage()!=null){
            holder.image_restaurant.setImageBitmap(Common.getBitmap(order.getImage()));
        }
        if(order.getOrderDate()!=null){
            holder.txt_order_date.setText(order.getOrderDate());
        }
        Locale locale=new Locale("vi","VN");
        NumberFormat numberFormat=NumberFormat.getInstance(locale);
        holder.txt_quantity.setText(new StringBuilder("").append(order.getNumberOfItem()).append(" phần x ")
        .append(numberFormat.format(order.getTotalPrice())).append("đ"));
        holder.txt_name_restaurant.setText(order.getNameRestaurant());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.curentOrder=listOrderHistory.get(position);
                context.startActivity(new Intent(context, TrackingOrderActivity.class));
            }
        });
        new getInforShipping(holder.image_complete,order.getId());
    }
    public void refresh(ArrayList<Order> orders) {
        this.listOrderHistory = orders;
        notifyDataSetChanged();
    }
    public class getInforShipping extends AsyncTask<String, String, Shipper> {
        ImageView image_complete;
        int orderId;

        public getInforShipping(ImageView image_complete, int orderId) {
            this.image_complete = image_complete;
            this.orderId = orderId;
            this.execute();
        }

        @Override
        protected void onPostExecute(Shipper shipper) {
            if(shipper!=null){
                if(shipper.getShippingStatus()==3){
                    image_complete.setVisibility(View.VISIBLE);
                }else {
                    image_complete.setVisibility(View.GONE);
                }
            }
        }

        @Override
        protected Shipper doInBackground(String... strings) {
            Shipper shipper;
            shipper = shipperData.getInforShipperOrder(orderId);
            return shipper;
        }
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
