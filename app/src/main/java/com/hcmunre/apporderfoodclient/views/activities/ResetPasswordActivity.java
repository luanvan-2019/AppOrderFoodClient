package com.hcmunre.apporderfoodclient.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.models.Database.UserData;
import com.hcmunre.apporderfoodclient.models.Entity.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResetPasswordActivity extends AppCompatActivity {
    @BindView(R.id.edit_new_password)
    TextInputEditText edit_new_password;
    @BindView(R.id.edit_verify_password)
    TextInputEditText edit_verify_password;
    @BindView(R.id.btn_change)
    TextView btn_change;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    UserData userData=new UserData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        init();
        changePassword();
    }
    private void init(){
        String email= Common.currentUser.getmEmail();
        Log.d("BBB",email+"");
        toolbar.setTitle("Đổi mật khẩu");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void changePassword(){
        String email= Common.currentUser.getmEmail();
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_password=edit_new_password.getText().toString().trim();
                String verify_password=edit_verify_password.getText().toString().trim();
                if(verify_password.equals(new_password)){
                    User user=new User();
                    user.setmEmail(email);
                    user.setmPassword(new_password);
                    boolean success=userData.resetPassword(user);
                    if(success==true){
                        startActivity(new Intent(ResetPasswordActivity.this,SignInActivity.class));
                        finish();
                    }else {
                        Common.showToast(ResetPasswordActivity.this,"Không thể thay đổi");
                    }
                }else {
                    edit_verify_password.setError("Xác nhận mật khẩu không đúng");
                }
            }
        });
    }
}
