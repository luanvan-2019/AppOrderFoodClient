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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.interfaces.CartDataSource;
import com.hcmunre.apporderfoodclient.models.Database.RestaurantData;
import com.hcmunre.apporderfoodclient.models.Entity.AllCartItem;
import com.hcmunre.apporderfoodclient.models.Entity.CartData;
import com.hcmunre.apporderfoodclient.models.Entity.LocalCartDataSource;
import com.hcmunre.apporderfoodclient.models.Entity.Restaurant;
import com.hcmunre.apporderfoodclient.models.eventbus.MenuItemEvent;
import com.hcmunre.apporderfoodclient.views.activities.MenuActivity;
import com.hcmunre.apporderfoodclient.views.activities.NearRestaurantActivity;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllCartAdapter extends RecyclerView.Adapter<AllCartAdapter.ViewHolder> {
    Context context;
    List<AllCartItem> allCartItems;
    CartDataSource cartDataSource;
    RestaurantData restaurantData=new RestaurantData();
    public AllCartAdapter(Context context, List<AllCartItem> allCartItems) {
        this.context = context;
        this.allCartItems = allCartItems;
        cartDataSource=new LocalCartDataSource(CartData.getInstance(context).cartDAO());
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_all_cart,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AllCartItem cartItem=allCartItems.get(position);
        holder.txt_restaurant_name.setText(cartItem.getRestaurantName());
        if(cartItem.getRestaurantImage()!=null){
            holder.image_restaurant.setImageBitmap(Common.getBitmap(cartItem.getRestaurantImage()));
        }

        holder.txtTotalPrice.setText(holder.currencyVN.format(cartItem.getTotalPrice()));
        holder.txt_count_item.setText(cartItem.getTotalItem()+"");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=String.valueOf(cartItem.getRestaurantId());
                new getRestaurantById(id);
            }
        });

    }
    public class getRestaurantById extends AsyncTask<String,String, ArrayList<Restaurant>> {
        private String id;

        public getRestaurantById(String id) {
            this.id = id;
            this.execute();
        }

        @Override
        protected void onPostExecute(ArrayList<Restaurant> restaurants) {
            Common.currentRestaurant = restaurants.get(0);
            EventBus.getDefault().postSticky(new MenuItemEvent(true, Common.currentRestaurant));
            context.startActivity(new Intent(context, MenuActivity.class));

        }

        @Override
        protected ArrayList<Restaurant> doInBackground(String... strings) {
            ArrayList<Restaurant> restaurants;
            restaurants=restaurantData.getRestaurantById(id);
            return restaurants;
        }
    }

    @Override
    public int getItemCount() {
        return allCartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.txt_restaurant_name)
        TextView txt_restaurant_name;
        @BindView(R.id.txtTotalPrice)
        TextView txtTotalPrice;
        @BindView(R.id.txt_count_item)
        TextView txt_count_item;
        @BindView(R.id.image_restaurant)
        RoundedImageView image_restaurant;
        Locale locale=new Locale("vi","VN");
        NumberFormat currencyVN=NumberFormat.getCurrencyInstance(locale);
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
