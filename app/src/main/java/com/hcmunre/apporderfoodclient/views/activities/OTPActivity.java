package com.hcmunre.apporderfoodclient.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.models.Database.JavaMailAPI;
import com.hcmunre.apporderfoodclient.models.Database.UserData;
import com.hcmunre.apporderfoodclient.models.Entity.User;
import com.hololo.library.otpview.OTPListener;
import com.hololo.library.otpview.OTPView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OTPActivity extends AppCompatActivity {
    @BindView(R.id.otp_view)
    OTPView otp_view;
    @BindView(R.id.txt_email)
    TextView txt_email;
    @BindView(R.id.txt_resend)
    TextView txt_resend;
    UserData userData=new UserData();
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);
        init();
        resendCode();
    }
    private void init(){
        email= Common.currentUser.getmEmail();
        txt_email.setText(email);
        otp_view.setListener(new OTPListener() {
            @Override
            public void otpFinished(String otp) {
                User user=userData.getOTP(email);
                int get_otp=Integer.parseInt(otp);
                Log.d("BBB",user.getOtp()+"");
                if(get_otp==user.getOtp()){
                    startActivity(new Intent(OTPActivity.this,ResetPasswordActivity.class));
                    finish();
                }else {
                    Toast.makeText(OTPActivity.this, "Mã xác nhận không đúng", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void resendCode(){
        txt_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int otp=new Random().nextInt(999999);
                boolean success=userData.updateOTP(email,otp);
                if(success==true){
                    JavaMailAPI javaMailAPI = new JavaMailAPI(OTPActivity.this,email,"OTP","OTP xác thực: "+otp,true);
                    javaMailAPI.execute();
                }else {
                    Toast.makeText(OTPActivity.this, "Không thể gửi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
