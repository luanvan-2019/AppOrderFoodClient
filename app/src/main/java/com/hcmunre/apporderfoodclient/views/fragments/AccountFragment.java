package com.hcmunre.apporderfoodclient.views.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.google.firebase.auth.FirebaseAuth;
import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.views.activities.PreferenceUtils;
import com.hcmunre.apporderfoodclient.views.activities.SignInActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountFragment extends Fragment {


    @BindView(R.id.btnupdateuser)
    Button btnUpdateUser;
    @BindView(R.id.btnlogout)
    Button btnSignOut;
    @BindView(R.id.txtPhone)
    TextView txtPhone;
    @BindView(R.id.edit_email)
    EditText edit_email;
    @BindView(R.id.edit_name)
    EditText edit_name;
    @BindView(R.id.edit_address)
    EditText edit_address;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this,view);
        btnSignOut.setOnClickListener(view1 -> {
            AlertDialog.Builder comfirmSignOut = new AlertDialog.Builder(view1.getContext())
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có muốn đăng xuất không ?")
                    .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Common.currentUser=null;
                            Common.currentRestaurant=null;
                            PreferenceUtils.savePassword(null, getActivity());
                            PreferenceUtils.saveEmail(null, getActivity());
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(getActivity(), SignInActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            getActivity().finish();
                            //logout phone
                        }
                    });
            comfirmSignOut.show();
        });
        init();
        return view;
    }
    private void init(){
        if(getActivity().getIntent()!=null){
//            txtPhone.setText(getActivity().getIntent().getStringExtra("phone"));
//            edit_email.setText(Common.currentUser.getmEmail());

        }
    }
}
