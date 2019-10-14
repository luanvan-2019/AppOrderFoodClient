package com.hcmunre.apporderfoodclient.views.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.models.Common.CommonClass;
import com.hcmunre.apporderfoodclient.models.Database.DataConnetion;
import com.hcmunre.apporderfoodclient.models.Database.SignInData;
import com.hcmunre.apporderfoodclient.models.Entity.User;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends AppCompatActivity {
    private static final int API_REQUEST_CODE = 1234;
    String usernam, passwordd;
    User user;
    CommonClass commonClass = new CommonClass();
    Connection con;
    DataConnetion dataCon = new DataConnetion();
    @BindView(R.id.btnSignIn)
    Button btnSigin;
//    @BindView(R.id.btnLoginPhone)
    Button btnLoginPhone;
    @BindView(R.id.editEmail)
    EditText editEmail;
    @BindView(R.id.editPassword)
    EditText editPassword;
    @BindView(R.id.txtSignUp)
    TextView txtSignup;
    @BindView(R.id.txtForgetPass)
    TextView txtForgetPass;
    @BindView(R.id.progress_login)
    ProgressBar progress_login;
    SharedPreferences sharedPreferences;
    AlertDialog dialog;
    private List<AuthUI.IdpConfig> providers;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        btnLoginPhone=findViewById(R.id.btnLoginPhone);
        init();


    }

    private void init() {
        progress_login.setVisibility(View.GONE);
        btnSigin.setOnClickListener(v -> {
            usernam = editEmail.getText().toString();
            passwordd = editPassword.getText().toString();
            new CheckLogin(SignInActivity.this).execute();
        });
        listenClickSignup();
        listenClickForgetPass();
        loginWithPhone();
        PreferenceUtils utils = new PreferenceUtils();

        if (utils.getEmail(this) != null) {
            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
            startActivity(intent);
        } else {

        }
    }
    private void loginWithPhone(){
        btnLoginPhone.setOnClickListener(view -> {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.PhoneBuilder().build());
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                finish();
            } else {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        API_REQUEST_CODE);
            }
        });
    }
    private void listenClickSignup() {
        txtSignup.setOnClickListener(view -> {
            Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(i);
            finish();

        });
    }

    private void listenClickForgetPass() {
        txtForgetPass.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, ForgetPassActivity.class);
            startActivity(intent);
        });
    }
    public class CheckLogin extends AsyncTask<String, String, String>//<params:giá trị truyền vào phần xử lý logic,progress,result>
    {
        //progress:giá trị mà xử lý logic nó bắn ra cho onProgressUpdate
        //resul: khi thực thi xong luong tra ve gì thì truyền qua cho onPostExecute
        private ProgressDialog mDialog;
        private Context mContext = null;

        public CheckLogin(Context context) {
            mContext = context;
        }

        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(mContext);
            mDialog.setMessage("Đang đăng nhập ...");
            mDialog.show();
        }

        @Override
        protected void onPostExecute(String r) {
            mDialog.dismiss();
            if (z != "success") {
                Toast.makeText(SignInActivity.this, r, Toast.LENGTH_SHORT).show();
            } else {
                PreferenceUtils.saveEmail(usernam, SignInActivity.this);
                PreferenceUtils.savePassword(passwordd, SignInActivity.this);
                Intent i = new Intent(SignInActivity.this, HomeActivity.class);
                i.putExtra(Common.KEY_USER,usernam);
                startActivity(i);
                finish();

            }
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params)//...mảng dạng array
        {

            if (usernam.trim().equals("") || passwordd.trim().equals("")) {
                z = "Vui lòng nhập tên đăng nhập hoặc mật khẩu";
            } else {
                SignInData userModel = new SignInData();
                user = new User();
                user.setmEmail(usernam);
                user.setmPassword(passwordd);
                z = userModel.loginUser(user);

            }
            return z;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == API_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                if(!FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().isEmpty()){
                    Intent i = new Intent(SignInActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                }else{
                    Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

