package com.hcmunre.apporderfoodclient.views.adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.models.Entity.Food;
import com.hcmunre.apporderfoodclient.views.activities.ListFoodActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private ArrayList<Food> arrayList;
    private Activity activity;
    int recentPos = -1;

    public FoodAdapter(ArrayList<Food> arrayList, Activity activity) {
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_restaurant, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Food food = arrayList.get(position);
        holder.itemView.setTag(position);
        holder.txtitem_name.setText(food.getName());
        holder.txtitem_price.setText(holder.currencyVN.format(food.getPrice()));
        holder.img_food.setImageResource(food.getImageFood());
        holder.quantityTxt.setText(food.getQuantity() + "");
        holder.quantity = food.getQuantity();
        if (food.getQuantity() > 0) {
            holder.quantityTxt.setVisibility(View.VISIBLE);
            holder.llMinus.setVisibility(View.VISIBLE);
        } else {
            holder.quantityTxt.setVisibility(View.GONE);
            holder.llMinus.setVisibility(View.GONE);
        }
        holder.llPlus.setOnClickListener(view -> {

            if (holder.quantity < 1000) {
                recentPos = position;
                holder.quantity = holder.quantity + 1;
                food.setQuantity(holder.quantity);
                ((ListFoodActivity) activity).txtCountFood_Order.setText(food.getQuantity() + "");
                food.setPriceAsPerQuantity("" + holder.quantity * food.getPrice());
                ((ListFoodActivity) activity).
                        txtTotalPrice.setText(String.valueOf(holder.currencyVN.format(
                        holder.quantity * food.getPrice())));
                holder.quantityTxt.setText("" + holder.quantity);
            }
            FoodAdapter.this.notifyDataSetChanged();

        });
        int count = Integer.parseInt(((ListFoodActivity) activity).txtCountFood_Order.getText().toString());
        if (count > 0) {
            ((ListFoodActivity) activity).txtorder.setVisibility(View.VISIBLE);
        } else {
            ((ListFoodActivity) activity).txtorder.setVisibility(View.GONE);
        }


        holder.llMinus.setOnClickListener(view -> {

            if (holder.quantity >= 0 && holder.quantity <= 1000) {
                recentPos = position;
                holder.quantity = holder.quantity - 1;
                food.setQuantity(holder.quantity);
                ((ListFoodActivity) activity).txtCountFood_Order.setText(food.getQuantity() + "");
                food.setPriceAsPerQuantity("" + holder.quantity * food.getPrice());
                ((ListFoodActivity) activity).
                        txtTotalPrice.setText(String.valueOf(
                        holder.currencyVN.format(
                                holder.quantity * food.getPrice())));
                holder.quantityTxt.setText("" + holder.quantity);
            }
            notifyDataSetChanged();

        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size() > 0 ? arrayList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtitem_name)
        TextView txtitem_name;
        @BindView(R.id.txtitem_price)
        TextView txtitem_price;
        @BindView(R.id.img_food)
        ImageView img_food;
        @BindView(R.id.quantityTxt)
        TextView quantityTxt;
        @BindView(R.id.llPlus)
        LinearLayout llPlus;
        @BindView(R.id.llMinus)
        LinearLayout llMinus;
        int quantity;
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void addFood(Food food) {
        arrayList.add(food);
        notifyDataSetChanged();
    }
}
