package com.hcmunre.apporderfoodclient.views.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.models.Entity.ChatMessage;
import com.hcmunre.apporderfoodclient.views.activities.PreferenceUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListMessageAdapter extends RecyclerView.Adapter<ListMessageAdapter.ListMessageHolder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    Context mContext;
    View v;
    List<ChatMessage> mChatMessage;
    private String sender;

    public ListMessageAdapter(Context mContext,List<ChatMessage> mChatMessage) {
        this.mChatMessage = mChatMessage;
        this.mContext = mContext;
        sender= PreferenceUtils.getEmail(mContext);
    }

    @NonNull
    @Override
    public ListMessageAdapter.ListMessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_sender,null);
            return new ListMessageAdapter.ListMessageHolder(view);
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_receiver,null);
            return new ListMessageAdapter.ListMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ListMessageAdapter.ListMessageHolder holder, int position) {
        ChatMessage chatMessage = mChatMessage.get(position);
        holder.show_message.setText(chatMessage.getMessage());
        holder.time.setText(DateFormat.format("HH:mm dd-MM-yyyy",chatMessage.getTime()));
    }

    @Override
    public int getItemCount() {
        return mChatMessage.size() > 0 ? mChatMessage.size() : 0;
    }

    class ListMessageHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.show_message)
        TextView show_message;
        @BindView(R.id.time_send_message)
        TextView time;
        @BindView(R.id.txt_username)
        TextView txt_username;
        public ListMessageHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mChatMessage.get(position).getUserId().equals(sender)){
            return MSG_TYPE_RIGHT;
        }else return MSG_TYPE_LEFT;
    }
}