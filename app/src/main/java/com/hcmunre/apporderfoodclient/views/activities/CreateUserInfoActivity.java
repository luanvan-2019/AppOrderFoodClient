package com.hcmunre.apporderfoodclient.views.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.models.Common.CommonClass;
import com.hcmunre.apporderfoodclient.models.Database.UserData;
import com.hcmunre.apporderfoodclient.models.Entity.User;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateUserInfoActivity extends AppCompatActivity {
    @BindView(R.id.edit_email)
    EditText edit_email;
    @BindView(R.id.edit_name)
    EditText edit_name;
    @BindView(R.id.edit_address)
    EditText edit_address;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_create)
    TextView btn_create;
    UserData userData=new UserData();
    private static final  int PLACE_PICKER_REQUEST=1;
    double mLat,mLng;
    String name,address,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_info);
        ButterKnife.bind(this);
        init();
        eventClick();

    }
    private void init(){
        toolbar.setTitle("Tạo thông tin người dùng");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void eventClick(){
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=edit_name.getText().toString();
                address=edit_address.getText().toString();
                email=edit_email.getText().toString();
                if(name.isEmpty()){
                    edit_name.setError("Tên trống");

                }else if(address.isEmpty()){
                    edit_address.setError("Địa chỉ trống");
                }else if (!CommonClass.EMAIL_PATTERN.matcher(email).matches()){
                    edit_email.setError("Email sai");
                }
                else{
                    boolean success = userData.checkExistUser(email);
                        if (success == true) {
                            Toast.makeText(CreateUserInfoActivity.this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                        } else {
                            new  createNewUser().execute();
                        }
                }
            }
        });
        edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddress();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (resultCode == RESULT_OK) {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    String address = place.getAddress();
                    mLat = place.getLatLng().latitude;
                    mLng = place.getLatLng().longitude;
                    edit_address.setText(address);
                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    Status status = Autocomplete.getStatusFromIntent(data);
                    Log.i("BBB", status.getStatusMessage());
                } else if (resultCode == RESULT_CANCELED) {
                }

            }
        }
    }
    public class createNewUser extends AsyncTask<String,String,User>{

        @Override
        protected void onPostExecute(User user) {

            if(user!=null){
                PreferenceUtils.saveUserId(user.getId(),CreateUserInfoActivity.this);
                PreferenceUtils.saveName(user.getmName(),CreateUserInfoActivity.this);
                PreferenceUtils.saveAddress(user.getmAddress(),CreateUserInfoActivity.this);
                PreferenceUtils.savePhone(user.getmPhone(),CreateUserInfoActivity.this);
                PreferenceUtils.saveEmail(user.getmEmail(),CreateUserInfoActivity.this);
                startActivity(new Intent(CreateUserInfoActivity.this,HomeActivity.class));
                finish();
            }
        }

        @SuppressLint("WrongThread")
        @Override
        protected User doInBackground(String... strings) {
            Intent intent=getIntent();
            String phone=intent.getStringExtra("phone");
            User user=new User();
            User userInfor=new User();
            user.setmName(name);
            user.setmPhone(phone);
            user.setmEmail(email);
            user.setmAddress(address);
            userInfor=userData.createNewUserInfo(user);
            return userInfor;
        }
    }
    private void setAddress() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields).setCountry("VN")
                .build(this);
        startActivityForResult(intent, PLACE_PICKER_REQUEST);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
