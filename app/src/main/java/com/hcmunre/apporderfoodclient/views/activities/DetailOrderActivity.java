package com.hcmunre.apporderfoodclient.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.interfaces.CartDataSource;
import com.hcmunre.apporderfoodclient.models.Database.OrderData;
import com.hcmunre.apporderfoodclient.models.Database.SignUpData;
import com.hcmunre.apporderfoodclient.models.Entity.CartData;
import com.hcmunre.apporderfoodclient.models.Entity.LocalCartDataSource;
import com.hcmunre.apporderfoodclient.models.Entity.Order;
import com.hcmunre.apporderfoodclient.models.Entity.OrderDetail;
import com.hcmunre.apporderfoodclient.models.Entity.User;
import com.hcmunre.apporderfoodclient.models.eventbus.SentTotalCashEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailOrderActivity extends AppCompatActivity {
    @BindView(R.id.txtfinish)
    TextView txtfinish;
    @BindView(R.id.txtupdate_phone)
    TextView txtupdate_phone;
    @BindView(R.id.txtTotalPrice)
    TextView txtTotalPrice;
    @BindView(R.id.txtAddress)
    TextView txtAddress;
    @BindView(R.id.txtPhone)
    TextView txtPhone;
    @BindView(R.id.btnDirect)
    RadioButton btnDirect;
    @BindView(R.id.btnATM)
    RadioButton btnATM;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txtupdate_address)
    TextView txtupdate_address;
    CompositeDisposable compositeDisposable;
    CartDataSource cartDataSource;
    OrderData orderData;
    SignUpData signUpData=new SignUpData();
    int PLACE_PICKER_REQUEST=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        String apiKey = "AIzaSyDb8vrfFWCuSNigtxvfM-zC-4MvNQlGIFQ";
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }
        init();
        txtupdate_address.setOnClickListener(view -> searchAddress());

    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chi tiết đơn hàng");
        btnATM.setOnClickListener(view -> openDialogCardForm());
        txtupdate_phone.setOnClickListener(view -> openUpdatePhoneDialog());
        txtAddress.setText(Common.currentRestaurant.getmAddress());
        txtPhone.setText(Common.currentRestaurant.getmPhone());
        compositeDisposable=new CompositeDisposable();
        cartDataSource=new LocalCartDataSource(CartData.getInstance(this).cartDAO());
        placeOrder();

    }

    private void placeOrder() {
        txtfinish.setOnClickListener(view -> {
            if (btnDirect.isChecked()) {
                getOrderNumber(false);
            } else if (btnATM.isChecked()) {
                //process online payment
            }

        });
    }
    public void searchAddress() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields).setCountry("VN")
                .build(this);
        startActivityForResult(intent, PLACE_PICKER_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PLACE_PICKER_REQUEST){
            if (resultCode==RESULT_OK){
                if (resultCode == RESULT_OK) {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    String address = place.getAddress();
                    txtAddress.setText(address);
                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    Status status = Autocomplete.getStatusFromIntent(data);
                    Log.i("BBB", status.getStatusMessage());
                } else if (resultCode == RESULT_CANCELED){
                }

            }
        }
    }

    private void getOrderNumber(boolean isOnlinePayment) {
        if(!isOnlinePayment){
            orderData = new OrderData();
            compositeDisposable.add(cartDataSource.getAllCart(PreferenceUtils.getEmail(this),
                    Common.currentRestaurant.getmId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(cartItems -> {
                        if(cartItems.isEmpty()){
                            Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
                        }else{
                            Order order=new Order();
//                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//                            Date DmyDate = (Date) sdf.parse("15/2/2019");
                            order.setUserId(Common.currentUser.getId());
                            order.setRestaurantId(Common.currentRestaurant.getmId());
                            order.setOrderName(Common.currentUser.getmName());
                            order.setOrderPhone(Common.currentUser.getmPhone());
                            order.setOrderAddress(txtAddress.getText().toString());
                            order.setTotalPrice(Float.parseFloat(txtTotalPrice.getText().toString()));
                            order.setNumberOfItem(cartItems.size());
                            Observable<Boolean> insertOrderShip = Observable.just(orderData.insertOrder(order));
                            compositeDisposable.add(
                                    insertOrderShip
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(insertOrder->{
                                                if(insertOrder==true){
                                                    //chọn cart items
                                                    cartDataSource.cleanCart(PreferenceUtils.getEmail(this),
                                                            Common.currentRestaurant.getmId())
                                                            .subscribeOn(Schedulers.io())
                                                            .observeOn(AndroidSchedulers.mainThread())
                                                            .subscribe(new SingleObserver<Integer>() {
                                                                @Override
                                                                public void onSubscribe(Disposable d) {

                                                                }

                                                                @Override
                                                                public void onSuccess(Integer integer) {
                                                                    ArrayList<OrderDetail> orderDetails=new ArrayList<>();
                                                                    OrderDetail orderDetail=new OrderDetail();
                                                                    for (int i=0;i<cartItems.size();i++){
                                                                        orderDetail.setOrderId(Common.curentOrder.getId());
                                                                        orderDetail.setFoodId(cartItems.get(i).getFoodId());
                                                                        orderDetail.setPrice(cartItems.get(i).getFoodPrice());
                                                                        orderDetail.setQuantity(cartItems.get(i).getFoodQuantity());
                                                                        orderDetails.add(orderDetail);
                                                                        orderData.insertOrderDetail(orderDetails);
                                                                    }
                                                                    Toast.makeText(DetailOrderActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                                                                    startActivity(new Intent(DetailOrderActivity.this, TrackingOrderActivity.class));
                                                                    finish();
                                                                }

                                                                @Override
                                                                public void onError(Throwable e) {
                                                                    Toast.makeText(DetailOrderActivity.this, "CLEAR CART"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }else {
                                                    Toast.makeText(this, "Không thêm được", Toast.LENGTH_SHORT).show();
                                                }

                                            })
                            );
                        }
                    }, throwable -> {
                        Toast.makeText(this, "[GET ALL CART] " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    })

            );
        }


    }

    private void openDialogCardForm() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.CustomDialogAnimation);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialog_payment = layoutInflater.inflate(R.layout.dialog_payment, null);
        alertDialog.setView(dialog_payment);
        final AlertDialog dialog = alertDialog.create();
        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, 1000);
    }

    private void openUpdatePhoneDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.CustomDialogAnimation);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialog_update_phone = layoutInflater.inflate(R.layout.dialog_update_phone, null);
        alertDialog.setView(dialog_update_phone);
        final AlertDialog dialog = alertDialog.create();
        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, 700);
        EditText edit_phone=dialog.findViewById(R.id.edit_phone);
        //
        edit_phone.setText(txtPhone.getText().toString());
        Button btn_update_phone=dialog.findViewById(R.id.btn_update_phone);
        btn_update_phone.setOnClickListener(view -> {
            User user=new User();
            user.setmPhone(edit_phone.getText().toString());
            Observable<Boolean> update_phone=Observable.just(signUpData.updatePhone(user));
            compositeDisposable.add(
                    update_phone
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aBoolean -> {
                        if(aBoolean==true){
                            Toast.makeText(this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(this, "Không thể cập nhật", Toast.LENGTH_SHORT).show();
                        }
                    }, throwable -> {
                        Toast.makeText(this, "Lỗi "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    })
            );
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void sentToTalCash(SentTotalCashEvent event){
        txtTotalPrice.setText(event.getCash());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
