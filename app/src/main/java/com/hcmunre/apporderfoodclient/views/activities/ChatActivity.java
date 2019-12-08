package com.hcmunre.apporderfoodclient.views.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.models.Database.RestaurantData;
import com.hcmunre.apporderfoodclient.models.Entity.ChatMessage;
import com.hcmunre.apporderfoodclient.views.adapters.ListMessageAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {
    @BindView(R.id.activity_chat)
    RelativeLayout activity_chat;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    String sender,receiver;
    @BindView(R.id.toolbar_name_receiver)
    TextView txtReceiverToolbar;
    ListMessageAdapter listMessageAdapter;
    List<ChatMessage> chatMessages;
    @BindView(R.id.recyclerview_chat)
    RecyclerView recyclerView;
    DatabaseReference reference;
    RestaurantData restaurantData=new RestaurantData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        init();
        eventClick();
        receivMessage();
    }
    private void init(){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
        private void eventClick(){
        String sender=PreferenceUtils.getEmail(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = findViewById(R.id.input);
                String msg = input.getText().toString();
                if (!msg.equals("")){
                    sendMessage(sender,receiver,msg);
                }else {
                    Toast.makeText(getApplicationContext(),"Không thể gửi tin nhắn trống!",Toast.LENGTH_LONG).show();
                }
                input.setText("");
            }
        });
        //        readMessage(sender,receiver);
        reference = FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                readMessage(sender,receiver);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        long time;
        time = new Date().getTime();
        hashMap.put("time",time);

        reference.child("chats").push().setValue(hashMap);
    }
    private void receivMessage(){
        ChatMessage chatMessage=restaurantData.getReceiver(PreferenceUtils.getEmail(this));
//        txtReceiverToolbar.setText(chatMessage.getReceiver());
    }
//    private void readMessage(final String sender, final String receiver) {
//
//        chatMessages = new ArrayList<>();
//        reference = FirebaseDatabase.getInstance().getReference("chats");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                chatMessages.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);
//                    Log.d("BBB",chatMessage.getSender()+""+chatMessage.getReceiver());
//                    if (chatMessage.getSender() != null ) {
//                        chatMessages.add(chatMessage);
//                    }
//                    listMessageAdapter = new ListMessageAdapter(ChatActivity.this, chatMessages);
//                    recyclerView.setAdapter(listMessageAdapter);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
}
