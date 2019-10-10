package com.hcmunre.apporderfoodclient.views.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.views.activities.PreferenceUtils;
import com.hcmunre.apporderfoodclient.views.activities.SignInActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountFragment extends Fragment {


    @BindView(R.id.btnupdateuser) Button btnUpdateUser;
    @BindView(R.id.btnlogout) Button btnSignOut;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this,view);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder comfirmSignOut = new AlertDialog.Builder(view.getContext())
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
                                //xử lý Logout trong đây
                                PreferenceUtils.savePassword(null, getActivity());
                                PreferenceUtils.saveEmail(null, getActivity());
                                Intent intent = new Intent(getActivity(), SignInActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        });
                comfirmSignOut.show();
            }
        });

        return view;
    }
}
