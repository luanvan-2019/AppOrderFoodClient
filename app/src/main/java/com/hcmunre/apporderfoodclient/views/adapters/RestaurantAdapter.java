package com.hcmunre.apporderfoodclient.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.models.Entity.Restaurant;
import com.hcmunre.apporderfoodclient.views.activities.DetailRestaurantActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Restaurant> restaurantArrayList;

    public RestaurantAdapter(Context context, ArrayList<Restaurant> restaurantArrayList) {
        mContext = context;
        this.restaurantArrayList = restaurantArrayList;
    }

//    public void setData(ArrayList<Restaurant> restaurants) {
//        restaurantArrayList.clear();
//        restaurantArrayList.addAll(restaurants);
//        notifyDataSetChanged();
//    }

    @NonNull
    @Override
    public RestaurantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantAdapter.ViewHolder holder, int position) {

        Restaurant restaurant = restaurantArrayList.get(position);
//        byte[] decodeString = Base64.decode(restaurant.getmImage(), Base64.DEFAULT);
//        Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        holder.txtName_Res.setText(restaurant.getmName());
        holder.txtAddress_Res.setText(restaurant.getmAddress());
//        holder.imageRes.setImageBitmap(decodebitmap);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, DetailRestaurantActivity.class);
            intent.putExtra("dataRestaurant", restaurant);
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

        //        byte[] decodeString = Base64.decode(, Base64.DEFAULT);
//        Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }


}



