package com.hcmunre.apporderfoodclient.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.models.Common.CommonClass;
import com.hcmunre.apporderfoodclient.models.Database.SignUpData;
import com.hcmunre.apporderfoodclient.models.Entity.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {
    @BindView(R.id.btnSignUp) Button btnSignUp;
    @BindView(R.id.editPhone) EditText editPhone;
    @BindView(R.id.editName) EditText editName;
    @BindView(R.id.editEmail) EditText editEmail;
    @BindView(R.id.editPassword) EditText editPassword;
    String editNhapSdt, editNhapTen, editNhapEmail, editNhapMatkhau;
    User user;
    CommonClass commonClass = new CommonClass();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        listenEvent();

    }
    private void listenEvent(){
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editNhapSdt = editPhone.getText().toString();
                editNhapTen = editName.getText().toString();
                editNhapEmail = editEmail.getText().toString();
                editNhapMatkhau = editPassword.getText().toString();
                SignUpData signUpData = new SignUpData();
                user = new User();
                user.setmName(editNhapTen);
                user.setmPhone(editNhapSdt);
                user.setmEmail(editNhapEmail);
                user.setmPassword(editNhapMatkhau);
                int res = signUpData.SignUp(user);
                if (checkInput()) {
                    if (res > 0) {
                        Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(SignUpActivity.this, SplashScreenActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Đăng ký thất bại", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    private boolean checkInput() {
        if (editEmail.equals("")) {
            editEmail.setError("Vui lòng nhập email");
            return false;
        } else if (!commonClass.EMAIL_PATTERN.matcher(editNhapEmail).matches()) {
            editEmail.setError("Vui lòng nhập đúng email");
            return false;
        } else if(editNhapMatkhau.length()<=8) {
            editPassword.setError("Mật khẩu ít nhất 8 kí tự");
            return false;
        }else
            editEmail.setError(null);
            return true;
        }
}
