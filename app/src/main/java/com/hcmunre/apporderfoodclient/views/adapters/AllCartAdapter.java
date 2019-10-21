package com.hcmunre.apporderfoodclient.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.interfaces.CartDataSource;
import com.hcmunre.apporderfoodclient.models.Entity.AllCartItem;
import com.hcmunre.apporderfoodclient.models.Entity.CartData;
import com.hcmunre.apporderfoodclient.models.Entity.LocalCartDataSource;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllCartAdapter extends RecyclerView.Adapter<AllCartAdapter.ViewHolder> {
    Context context;
    List<AllCartItem> allCartItems;
    CartDataSource cartDataSource;
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
        holder.txt_restaurant_name.setText(Common.currentRestaurant.getmName());
        holder.txtTotalPrice.setText(holder.currencyVN.format(cartItem.getTotalPrice()));
        holder.txt_count_item.setText(cartItem.getTotalItem()+"");

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
