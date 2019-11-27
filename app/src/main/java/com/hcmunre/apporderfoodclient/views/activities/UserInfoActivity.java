package com.hcmunre.apporderfoodclient.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
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
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.models.Database.UserData;
import com.hcmunre.apporderfoodclient.models.Entity.User;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class UserInfoActivity extends AppCompatActivity {
    @BindView(R.id.edit_email)
    EditText edit_email;
    @BindView(R.id.edit_name)
    EditText edit_name;
    @BindView(R.id.edit_address)
    EditText edit_address;
    @BindView(R.id.edit_phone)
    EditText edit_phone;
    @BindView(R.id.edit_password)
    EditText edit_password;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    User user;
    UserData userData=new UserData();
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    double mLat, mLng;
    private static final int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        init();
        eventClick();
    }

    private void init() {
        edit_phone.setText(PreferenceUtils.getPhone(this));
        edit_name.setText(PreferenceUtils.getName(this));
        edit_email.setText(PreferenceUtils.getEmail(this));
        edit_address.setText(PreferenceUtils.getAddress(this));
        edit_password.setText(PreferenceUtils.getPassword(this));
        toolbar.setTitle(PreferenceUtils.getName(this));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void eventClick() {
        edit_address.setOnClickListener(view -> {
            setAddress();
        });
    }

    private void setAddress() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields).setCountry("VN")
                .build(this);
        startActivityForResult(intent, PLACE_PICKER_REQUEST);
    }

    private void updateInfoUser() {
        user = new User();
        user.setId(PreferenceUtils.getUserId(this));
        user.setmName(edit_name.getText().toString());
        user.setmAddress(edit_address.getText().toString());
        user.setmPhone(edit_phone.getText().toString());
        Observable<Boolean> updateInfo = Observable.just(userData.updateUser(user));
        compositeDisposable.add(
                updateInfo
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aBoolean -> {
                            if (aBoolean == true) {
                                Toast.makeText(this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Không thể cập nhật", Toast.LENGTH_SHORT).show();
                            }
                        }, throwable -> {
                            Log.d(Common.TAG, "Lỗi" + throwable.getMessage());
                        })
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.toolbar_navigation,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                return true;
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
