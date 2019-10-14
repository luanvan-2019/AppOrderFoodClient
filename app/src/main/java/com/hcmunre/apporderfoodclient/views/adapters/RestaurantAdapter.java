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
import com.hcmunre.apporderfoodclient.models.Entity.Restaurant;
import com.hcmunre.apporderfoodclient.views.activities.MenuRestaurantActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    private Context mContext;
    private  ArrayList<Restaurant> restaurantArrayList;

    public RestaurantAdapter(Context context, ArrayList<Restaurant> restaurantArrayList) {
        mContext = context;
        this.restaurantArrayList = restaurantArrayList;
    }

    @NonNull
    @Override
    public RestaurantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantAdapter.ViewHolder holder, int position) {
        Common.currentRestaurant = restaurantArrayList.get(position);
        byte[] decodeString = Base64.decode(Common.currentRestaurant.getmImage(), Base64.DEFAULT);
        Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        holder.txtName_Res.setText(Common.currentRestaurant.getmName());
        holder.txtAddress_Res.setText(Common.currentRestaurant.getmAddress());
        holder.imageRes.setImageBitmap(decodebitmap);
        holder.itemView.setOnClickListener(view -> {
            Common.currentRestaurant = restaurantArrayList.get(position);
            Intent intent = new Intent(mContext, MenuRestaurantActivity.class);
            intent.putExtra(Common.KEY_RESTAURANT, Common.currentRestaurant);
            mContext.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {

        return restaurantArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageRes)
        ImageView imageRes;
        @BindView(R.id.txtName_Res)
        TextView txtName_Res;
        @BindView(R.id.txtAddress_Res)
        TextView txtAddress_Res;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }


}



