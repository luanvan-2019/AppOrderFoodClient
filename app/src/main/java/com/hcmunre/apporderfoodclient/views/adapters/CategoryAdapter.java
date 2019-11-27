package com.hcmunre.apporderfoodclient.views.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import com.hcmunre.apporderfoodclient.models.Entity.Category;
import com.hcmunre.apporderfoodclient.models.eventbus.CategoryItemEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Category> listMenus;
    private int selectPos = 0;

    public CategoryAdapter(Context context, ArrayList<Category> listMenus) {
        this.context = context;
        this.listMenus = listMenus;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, final int position) {
        final Category listMenu = listMenus.get(position);
        if(listMenu.getImage()!=null){
            byte[] decodeString = Base64.decode(listMenu.getImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
            holder.imageListMenu.setImageBitmap(bitmap);
        }
        holder.itemView.setTag(position);
        holder.txtListMenu.setText(listMenu.getName());
//            holder.imageListMenu.setImageResource(listMenu.getImage());
        if (selectPos == position) {
            holder.txtListMenu.setTextColor(Color.parseColor("#f05421"));
            holder.itemView.setSelected(true);
        } else {
            holder.txtListMenu.setTextColor(Color.parseColor("#000000"));
        }
        holder.itemView.setOnClickListener(view -> {
            selectPos = position;
            Common.currentCategory = listMenus.get(position);
            EventBus.getDefault().postSticky(new CategoryItemEvent(true, Common.currentCategory));
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return listMenus.size() > 0 ? listMenus.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageListMenu)
        ImageView imageListMenu;
        @BindView(R.id.txtListMenu)
        TextView txtListMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
