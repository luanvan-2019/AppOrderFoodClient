package com.hcmunre.apporderfoodclient.views.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.models.Entity.ListMenu;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListMenuAdapter extends RecyclerView.Adapter<ListMenuAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ListMenu> listMenus=new ArrayList<>();
    private int selectPos=-1;
    public ListMenuAdapter(Context context, ArrayList<ListMenu> listMenus) {
        this.context = context;
        this.listMenus = listMenus;
    }

    @NonNull
    @Override
    public ListMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_menu,parent,false);
        return new ListMenuAdapter.ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ListMenuAdapter.ViewHolder holder, final int position) {
            final ListMenu listMenu=listMenus.get(position);
            holder.itemView.setTag(position);
            holder.txtListMenu.setText(listMenu.getmName());
            holder.imageListMenu.setImageResource(listMenu.getmImage());
            if(selectPos==position){
                holder.txtListMenu.setTextColor(Color.parseColor("#f05421"));
            }else{
                holder.txtListMenu.setTextColor(Color.parseColor("#000000"));
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectPos=position;
                    notifyDataSetChanged();
                }
            });
    }

    @Override
    public int getItemCount() {
        return listMenus.size()>0?listMenus.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageListMenu)
        ImageView imageListMenu;
        @BindView(R.id.txtListMenu)
        TextView txtListMenu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
