package com.hcmunre.apporderfoodclient.views.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.hcmunre.apporderfoodclient.models.Entity.Favorite;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Favorite> favorites;

    public FavoriteAdapter(Context context, ArrayList<Favorite> favorites) {
        this.context = context;
        this.favorites = favorites;
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite,null);
        return new FavoriteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, int position) {
        Favorite favorite=favorites.get(position);
        if(favorite.getFoodImage()!=null){
            holder.image_food.setImageBitmap(Common.getBitmap(favorite.getFoodImage()));
        }
        holder.txt_name_food.setText(favorite.getFoodName());
        holder.txt_name_restaurant.setText(favorite.getRestaurantName());
        holder.txt_price.setText(new StringBuilder(favorite.getPrice()+"").append("đ"));
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.image_food)
        RoundedImageView image_food;
        @BindView(R.id.txt_name_restaurant)
        TextView txt_name_restaurant;
        @BindView(R.id.txt_name_food)
        TextView txt_name_food;
        @BindView(R.id.txt_price)
        TextView txt_price;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
