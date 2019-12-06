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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.models.Entity.Menu;
import com.hcmunre.apporderfoodclient.models.eventbus.FoodListEvent;
import com.hcmunre.apporderfoodclient.views.activities.FoodActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Menu> listMenus;

    public MenuAdapter(Context mContext, ArrayList<Menu> menus) {
        this.mContext = mContext;
        this.listMenus = menus;
    }

    @NonNull
    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu, parent, false);
        return new MenuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.ViewHolder holder, int position) {
        final Menu menu = listMenus.get(position);
        if(menu.getmImage()!=null){
            holder.imageMenu.setImageBitmap(Common.getBitmap(menu.getmImage()));
        }
        holder.txtNameMenu.setText(menu.getmName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().postSticky(new FoodListEvent(true,menu));
                mContext.startActivity(new Intent(mContext, FoodActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMenus.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageMenu)
        ImageView imageMenu;
        @BindView(R.id.txtNameMenu)
        TextView txtNameMenu;

        public ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
