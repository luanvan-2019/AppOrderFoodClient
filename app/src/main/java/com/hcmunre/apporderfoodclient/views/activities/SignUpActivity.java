package com.hcmunre.apporderfoodclient.views.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.models.Common.CommonClass;
import com.hcmunre.apporderfoodclient.models.Database.UserData;
import com.hcmunre.apporderfoodclient.models.Entity.User;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {
    @BindView(R.id.btnSignUp)
    TextView btnSignUp;
    @BindView(R.id.editPhone)
    EditText editPhone;
    @BindView(R.id.editName)
    EditText editName;
    @BindView(R.id.editEmail)
    EditText editEmail;
    @BindView(R.id.editPassword)
    EditText editPassword;
    @BindView(R.id.edit_address)
    EditText edit_address;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    String editNhapSdt, editNhapTen, editNhapEmail, editNhapMatkhau;
    User user;
    UserData userData = new UserData();
    private static final int PLACE_PICKER_REQUEST = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        init();
        listenEvent();

    }

    private void init() {
        toolbar.setTitle("Tạo tài khoản mới");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (resultCode == RESULT_OK) {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    String address = place.getAddress();
                    edit_address.setText(address);
                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    Status status = Autocomplete.getStatusFromIntent(data);
                    Log.i("BBB", status.getStatusMessage());
                } else if (resultCode == RESULT_CANCELED) {
                }

            }
        }
    }

    public void searchAddress() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields).setCountry("VN")
                .build(this);
        startActivityForResult(intent, PLACE_PICKER_REQUEST);
    }

    private void listenEvent() {
        edit_address.setOnClickListener(v -> searchAddress());
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editNhapSdt = editPhone.getText().toString();
                editNhapTen = editName.getText().toString();
                editNhapEmail = editEmail.getText().toString();
                editNhapMatkhau = editPassword.getText().toString();
                user = new User();
                user.setmName(editNhapTen);
                user.setmPhone(editNhapSdt);
                user.setmAddress(edit_address.getText().toString());
                user.setmEmail(editNhapEmail);
                user.setmPassword(editNhapMatkhau);
                boolean success = userData.checkExistUser(editNhapEmail);
                if(checkInput()){
                    if (success == true) {
                        Toast.makeText(SignUpActivity.this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                    } else {
                        new signUp(user);
                    }
                }
            }
        });
    }

    public class signUp extends AsyncTask<String, String, Boolean> {
        private ProgressDialog progressDialog;
        private User user;

        public signUp(User user) {
            this.user = user;
            this.execute();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SignUpActivity.this);
            progressDialog.setMessage("Vui lòng đợi...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
                if (aBoolean == true) {
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(SignUpActivity.this, SignInActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Đăng ký thất bại", Toast.LENGTH_LONG).show();
                }
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean success = userData.SignUp(user);
            return success;
        }


    }

    private boolean checkInput() {
        if (editEmail.equals("")) {
            editEmail.setError("Vui lòng nhập email");
            return false;
        } else if (!CommonClass.EMAIL_PATTERN.matcher(editNhapEmail).matches()) {
            editEmail.setError("Vui lòng nhập đúng email");
            return false;
        } else if (editNhapMatkhau.length() <= 8) {
            editPassword.setError("Mật khẩu ít nhất 8 kí tự");
            return false;
        } else
            editEmail.setError(null);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
