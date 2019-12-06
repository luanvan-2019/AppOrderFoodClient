package com.hcmunre.apporderfoodclient.views.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.models.Database.JavaMailAPI;
import com.hcmunre.apporderfoodclient.models.Database.UserData;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgetPassActivity extends AppCompatActivity {
    @BindView(R.id.editEmail)
    EditText editEmail;
    @BindView(R.id.btn_send)
    TextView btn_send;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    UserData userData=new UserData();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass);
        ButterKnife.bind(this);
        init();
        sendEmail();
    }

    private void init() {
        toolbar.setTitle("Quên mật khẩu");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }
    private void sendEmail(){
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=editEmail.getText().toString().trim();
                int otp=new Random().nextInt(999999);
                boolean success=userData.updateOTP(email,otp);
                if(success==true){
                    JavaMailAPI javaMailAPI = new JavaMailAPI(ForgetPassActivity.this,email,"OTP","OTP xác thực: "+otp,false);
                    javaMailAPI.execute();
                }else {
                    Toast.makeText(ForgetPassActivity.this, "Không thể gửi", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
