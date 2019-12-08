package com.hcmunre.apporderfoodclient.views.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.models.Entity.ChatMessage;
import com.hcmunre.apporderfoodclient.views.activities.PreferenceUtils;
import com.hcmunre.apporderfoodclient.views.adapters.ListMessageAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ReviewFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.recyclerview_chat)
    RecyclerView recyc_chat;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.input)
    TextInputEditText input;
    @BindView(R.id.linear_reviews)
    LinearLayout linear_reviews;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipe_layout;
    DatabaseReference reference;
    String email,restaurantId;
    List<ChatMessage> chatMessageList;
    ListMessageAdapter listMessageAdapter;
    public ReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_review, container, false);
        unbinder= ButterKnife.bind(this,view);
        init();
        eventClick();
        return view;
    }
    private void init(){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        recyc_chat.setLayoutManager(linearLayoutManager);
        recyc_chat.setItemAnimator(new DefaultItemAnimator());
        linear_reviews.setVisibility(View.GONE);
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_layout.setRefreshing(false);
            }
        });
        swipe_layout.setRefreshing(false);

    }
    private void eventClick(){

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=input.getText().toString().trim();
                email= PreferenceUtils.getEmail(getActivity());
                restaurantId=String.valueOf(Common.currentRestaurant.getmId());
                if(!msg.equals("")){
                    sendMessage(email,restaurantId,msg);
                }else {
                    Common.showToast(getActivity(),"Vui lòng nhập tin nhắn");
                }
                input.setText("");

            }
        });
        reference=FirebaseDatabase.getInstance().getReference("Reviews");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                email= PreferenceUtils.getEmail(getActivity());
                Log.d("BBB","Email Hệ thống"+email);
                restaurantId=String.valueOf(Common.currentRestaurant.getmId());
                readMessage(email,restaurantId);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void sendMessage(final String userId,final String restaurantId, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId",userId);
        hashMap.put("restaurantId", restaurantId);
        hashMap.put("message",message);
        long time;
        time = new Date().getTime();
        hashMap.put("time",time);
        reference.child("Reviews").push().setValue(hashMap);
    }
    private void readMessage(final String email,final String restaurantId){
        chatMessageList=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Reviews");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatMessageList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    ChatMessage chatMessage=snapshot.getValue(ChatMessage.class);
                    if(chatMessage.getRestaurantId()!=null&&chatMessage.getUserId()!=null){
                        linear_reviews.setVisibility(View.GONE);
                        if(chatMessage.getRestaurantId().equals(restaurantId)
                                || chatMessage.getRestaurantId().equals(email)){
                            chatMessageList.add(chatMessage);
                        }
                    }else {
                    }
                    listMessageAdapter=new ListMessageAdapter(getActivity(),chatMessageList);
                    recyc_chat.setAdapter(listMessageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
