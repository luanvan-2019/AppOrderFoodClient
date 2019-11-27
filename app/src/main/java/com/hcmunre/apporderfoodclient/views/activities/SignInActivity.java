package com.hcmunre.apporderfoodclient.views.activities;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.models.Database.UserData;
import com.hcmunre.apporderfoodclient.models.Entity.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;
import io.reactivex.disposables.CompositeDisposable;

public class SignInActivity extends AppCompatActivity {
    private static final int API_REQUEST_CODE = 1234;
    String usernam, passwordd;
    User user;
    @BindView(R.id.btnSignIn)
    TextView btnSigin;
    @BindView(R.id.btnLoginPhone)
    TextView btnLoginPhone;
    @BindView(R.id.editEmail)
    EditText editEmail;
    @BindView(R.id.editPassword)
    EditText editPassword;
    @BindView(R.id.txtSignUp)
    TextView txtSignup;
    @BindView(R.id.txtForgetPass)
    TextView txtForgetPass;
    @BindView(R.id.btn_login_facebook)
    LoginButton btn_login_facebook;
    private CallbackManager mCallbackManager;
    private List<AuthUI.IdpConfig> providers;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        init();
//        setBtn_login_facebook();


    }

    private void init() {
        Paper.init(this);
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
            finish();
        } else {

        }
        providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = firebaseAuth1 -> {
            FirebaseUser user = firebaseAuth1.getCurrentUser();
            if (user != null) { //user đã login
                startActivity(new Intent(SignInActivity.this, CreateUserInfoActivity.class)
                        .putExtra("phone", user.getPhoneNumber())
                );
                finish();
            } else {
                loginWithPhone();

            }
        };
    }
//    private void setBtn_login_facebook(){
//        btn_login_facebook.setReadPermissions("user_friends");
//        mCallbackManager = CallbackManager.Factory.create();
//        btn_login_facebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Profile profile=Profile.getCurrentProfile();
//                userInfor(profile);
//            }
//
//            @Override
//            public void onCancel() {
//                Log.d("BBB","Login canceled.");
//            }
//
//            @Override
//            public void onError(FacebookException e) {
//
//            }
//        });
//    }

//    private void userInfor(Profile profile){
//        if(profile!=null){
//            Intent intent=new Intent(this,HomeActivity.class);
//            PreferenceUtils.saveFBId(profile.getId(),this);
//            PreferenceUtils.saveName(profile.getName(),this);
//            intent.putExtra("name",profile.getFirstName());
//            intent.putExtra("surname",profile.getLastName());
//            intent.putExtra("imageUrl",profile.getProfilePictureUri(200,200).toString());
//            startActivity(intent);
//
//        }
//    }

    private void loginWithPhone() {
        btnLoginPhone.setOnClickListener(view ->
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers).build(), API_REQUEST_CODE));


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

    public class CheckLogin extends AsyncTask<String, String, User>//<params:giá trị truyền vào phần xử lý logic,progress,result>
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
        protected void onPostExecute(User user) {
            mDialog.dismiss();
            if (usernam.trim().equals("") || passwordd.trim().equals("")) {
                Toast.makeText(mContext, "Vui lòng nhập tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            } else if (user != null) {
                Common.currentUser = user;
                PreferenceUtils.saveEmail(usernam, SignInActivity.this);
                PreferenceUtils.savePassword(passwordd, SignInActivity.this);
                PreferenceUtils.saveUserId(Common.currentUser.getId(), SignInActivity.this);
                PreferenceUtils.saveName(Common.currentUser.getmName(), SignInActivity.this);
                PreferenceUtils.savePhone(Common.currentUser.getmPhone(), SignInActivity.this);
                PreferenceUtils.saveAddress(Common.currentUser.getmAddress(), SignInActivity.this);
                Intent i = new Intent(SignInActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(mContext, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            }
        }

        @SuppressLint("WrongThread")
        @Override
        protected User doInBackground(String... params)//...mảng dạng array
        {
            User user1 = null;
            if (usernam.trim().equals("") || passwordd.trim().equals("")) {
                z = "Vui lòng nhập tên đăng nhập hoặc mật khẩu";
            } else {
                UserData userModel = new UserData();
                user = new User();
                user.setmEmail(usernam);
                user.setmPassword(passwordd);
                user1 = new User();
                user1 = userModel.getInforUser(user);

            }
            return user1;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == API_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            firebaseAuth = FirebaseAuth.getInstance();
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            } else {
                Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authStateListener != null && firebaseAuth != null) {
            firebaseAuth.addAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onStop() {
        if (authStateListener != null && firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignInActivity.this,HomeActivity.class));
        finish();
        super.onBackPressed();
    }
}

