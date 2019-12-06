package com.hcmunre.apporderfoodclient.views.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.util.DataUtils;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.snackbar.Snackbar;
import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.interfaces.CartDataSource;
import com.hcmunre.apporderfoodclient.models.Database.OrderData;
import com.hcmunre.apporderfoodclient.models.Database.UserData;
import com.hcmunre.apporderfoodclient.models.Entity.CartData;
import com.hcmunre.apporderfoodclient.models.Entity.CartItem;
import com.hcmunre.apporderfoodclient.models.Entity.LocalCartDataSource;
import com.hcmunre.apporderfoodclient.models.Entity.Order;
import com.hcmunre.apporderfoodclient.models.Entity.OrderDetail;
import com.hcmunre.apporderfoodclient.models.Entity.User;
import com.hcmunre.apporderfoodclient.models.eventbus.AddressEvent;
import com.hcmunre.apporderfoodclient.models.eventbus.SentTotalCashEvent;
import com.hcmunre.apporderfoodclient.notification.MySingleton;
import com.hcmunre.apporderfoodclient.views.adapters.OrderDetailFoodAdapter;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailOrderActivity extends AppCompatActivity {
    @BindView(R.id.btnOrder)
    TextView btnOrder;
    @BindView(R.id.txtupdate_phone)
    TextView txtupdate_phone;
    @BindView(R.id.txtTotalPrice)
    TextView txtTotalPrice;
    @BindView(R.id.txtAddress)
    TextView txtAddress;
    @BindView(R.id.txtPhone)
    TextView txtPhone;
    @BindView(R.id.btn_cod)
    RadioButton btn_cod;
    @BindView(R.id.btn_payment)
    RadioButton btn_payment;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txtupdate_address)
    TextView txtupdate_address;
    @BindView(R.id.txt_date)
    TextView txt_date;
    @BindView(R.id.recyc_order_detail)
    RecyclerView recyc_order_detail;
    CompositeDisposable compositeDisposable;
    CartDataSource cartDataSource;
    OrderData orderData;
    UserData userData = new UserData();
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int PAYPAL_REQUEST_CODE = 2;
    //
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAABxgzzk4:APA91bFOUq0T_vGnwemLQfJcU6akuV1gLQVJdL5mxyxV1m1bDeDbapGb8mWH0gKqSL2tSyuS_A7kTD3iWTfeFK0NhHNhcu8TY7Z7ClSu8LA2xJSJoDaYhbOge7MUF1J8V6FSRiUeDW8i";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    SimpleDateFormat simpleDateFormat;
    private PayPalConfiguration config=new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)//sử dụng sandbox để test
            .clientId(Common.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        init();
        eventClick();

    }

    private void init() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyc_order_detail.setLayoutManager(layoutManager);
        recyc_order_detail.setItemAnimator(new DefaultItemAnimator());
        //paypal
        Intent intent=new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);
        //paypal
        //toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chi tiết đơn hàng");
        txtupdate_phone.setOnClickListener(view -> openUpdatePhoneDialog());
        txtPhone.setText(Common.currentRestaurant.getmPhone());
        compositeDisposable = new CompositeDisposable();
        cartDataSource = new LocalCartDataSource(CartData.getInstance(this).cartDAO());
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.map_key));
        }
        placeOrder();
        getItemInCart();


    }

    private void eventClick() {
        txtupdate_address.setOnClickListener(view -> searchAddress());
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        String date=simpleDateFormat.format(calendar.getTime());
        txt_date.setText(date);
        txt_date.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(DetailOrderActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, (view, year, month, dayOfMonth) -> {
                txt_date.setText(new StringBuilder("")
                        .append(dayOfMonth)
                        .append("/")
                        .append(month + 1)
                        .append("/")
                        .append(year));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
    }

    private void placeOrder() {
        btnOrder.setOnClickListener(view -> {
            if (txt_date.getText().toString().equals("")) {
                Snackbar snackbar = Snackbar.make(view, "Vui lòng chọn ngày giao hàng", Snackbar.LENGTH_LONG)
                        .setAction("Action", null);
                snackbar.show();
            } else {
                String dataString = txt_date.getText().toString();
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date orderDate = df.parse(dataString);
                    Calendar calendar = Calendar.getInstance();
                    Date currentDate = df.parse(df.format(calendar.getTime()));
                    if (orderDate.before(currentDate)) {
                        Snackbar snackbar = Snackbar.make(view, "Vui lòng chọn ngày hiện tại hoặc ngày tương lai", Snackbar.LENGTH_LONG)
                                .setAction("Action", null);
                        snackbar.show();
                    }else {
                        if (btn_cod.isChecked()) {
                            if(Common.isConnectedToInternet(this)){
                                getOrderNumber(0);
                            }else {
                                Common.showToast(this,getString(R.string.check_internet));
                            }
                        } else if (btn_payment.isChecked()) {
                            if(Common.isConnectedToInternet(this)){
                                paypal();
                            }else {
                                Common.showToast(this,getString(R.string.check_internet));
                            }
                        }
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
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

    private void getItemInCart() {
        compositeDisposable.add(cartDataSource.getAllCart(PreferenceUtils.getEmail(this),
                Common.currentRestaurant.getmId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cartItems -> {
                    if (cartItems.size() > 0) {
                        OrderDetailFoodAdapter orderDetailFoodAdapter = new OrderDetailFoodAdapter(this, cartItems);
                        recyc_order_detail.setAdapter(orderDetailFoodAdapter);
                    } else {
                        Toast.makeText(this, "Không có món ăn để đặt", Toast.LENGTH_SHORT).show();
                    }

                }, throwable -> {
                    Log.d(Common.TAG, "Lỗi" + throwable.getMessage());
                })
        );
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (resultCode == RESULT_OK) {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    String address = place.getAddress();
                    txtAddress.setText(address);
                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    Status status = Autocomplete.getStatusFromIntent(data);
                    Log.i("BBB", status.getStatusMessage());
                } else if (resultCode == RESULT_CANCELED) {
                }

            }
        }else if(requestCode==PAYPAL_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                PaymentConfirmation confirmation=data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation!=null){
                    try {
                        String payment=confirmation.toJSONObject().toString(4);
                        getOrderNumber(1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void getOrderNumber(int payment) {
            orderData = new OrderData();
            compositeDisposable.add(cartDataSource.getAllCart(PreferenceUtils.getEmail(this),
                    Common.currentRestaurant.getmId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(cartItems -> {
                        if (cartItems.isEmpty()) {
                            Common.showToast(this,"Giỏ hàng trống");
                        } else {
                            Order order = new Order();
                            order.setUserId(PreferenceUtils.getUserId(this));
                            order.setRestaurantId(Common.currentRestaurant.getmId());
                            order.setOrderName(PreferenceUtils.getName(this));
                            order.setOrderPhone(txtPhone.getText().toString());
                            order.setOrderAddress(txtAddress.getText().toString());
                            order.setOrderDate(txt_date.getText().toString().trim());
                            order.setOrderStatus(0);
                            order.setTotalPrice(Common.curentOrder.getTotalPrice());
                            order.setNumberOfItem(cartItems.size());
                            order.setPayment(payment);
                            Observable<Boolean> insertOrderShip = Observable.just(orderData.insertOrder(order));
                            compositeDisposable.add(
                                    insertOrderShip
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(insertOrder -> {
                                                if (insertOrder == true) {
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

                                                                    new getOrderNumberBK(DetailOrderActivity.this, cartItems);
                                                                    Common.showToast(DetailOrderActivity.this,"Đặt hàng thành công");
                                                                    NOTIFICATION_TITLE = "App Food";
                                                                    NOTIFICATION_MESSAGE ="Bạn có đơn hàng mới "+Common.curentOrder.getId()+" ?";

                                                                    JSONObject notification = new JSONObject();
                                                                    JSONObject notifcationBody = new JSONObject();
                                                                    try {
                                                                        notifcationBody.put("title", NOTIFICATION_TITLE);
                                                                        notifcationBody.put("message", NOTIFICATION_MESSAGE);

                                                                        notification.put("to", Common.createTopicSender(Common.getTopicChannel(Common.currentRestaurant.getmId())));
                                                                        notification.put("data", notifcationBody);
                                                                    } catch (JSONException e) {
                                                                        Log.e(TAG, "onCreate: " + e.getMessage());
                                                                    }
                                                                    sendNotification(notification);
                                                                    startActivity(new Intent(DetailOrderActivity.this, TrackingOrderActivity.class));
                                                                    finish();
                                                                }

                                                                @Override
                                                                public void onError(Throwable e) {
                                                                    Common.showToast(DetailOrderActivity.this,"Xóa giỏ hàng");
                                                                }
                                                            });
                                                } else {
                                                    Common.showToast(this,"Không thêm được");
                                                }
                                            })
                            );
                        }
                    }, throwable -> {
                        Common.showToast(this,"Lỗi giỏ hàng");
                    })

            );
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
        EditText edit_phone = dialog.findViewById(R.id.edit_phone);
        //
        edit_phone.setText(txtPhone.getText().toString());
        Button btn_update_phone = dialog.findViewById(R.id.btn_update_phone);
        btn_update_phone.setOnClickListener(view -> {
            User user = new User();
            user.setmPhone(edit_phone.getText().toString());
            Observable<Boolean> update_phone = Observable.just(userData.updateUser(user));
            compositeDisposable.add(
                    update_phone
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aBoolean -> {
                                if (aBoolean == true) {
                                    Toast.makeText(this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(this, "Không thể cập nhật", Toast.LENGTH_SHORT).show();
                                }
                            }, throwable -> {
                                Toast.makeText(this, "Lỗi " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void sentToTalCash(SentTotalCashEvent event) {

        txtTotalPrice.setText(event.getCash());
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getAddress(AddressEvent event) {

        txtAddress.setText(event.getAddess());
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public class getOrderNumberBK extends AsyncTask<String, String, List<OrderDetail>> {
        private ProgressDialog mDialog;
        private Context mContext = null;
        private List<CartItem> cartItems;

        public getOrderNumberBK(Context mContext, List<CartItem> cartItems) {
            this.mContext = mContext;
            this.cartItems = cartItems;
            this.execute();
        }

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(mContext);
            mDialog.setMessage("Vui lòng chờ...");
            mDialog.show();
        }

        @Override
        protected void onPostExecute(List<OrderDetail> orderDetails) {
            if (orderDetails.size() > 0) {
                orderData.insertOrderDetail(orderDetails);
                //

            } else {
                Log.d("BBB", "Không thể thực hiện");
            }


        }

        @Override
        protected List<OrderDetail> doInBackground(String... strings) {
            List<OrderDetail> orderDetails = new ArrayList<>();
            OrderDetail orderDetail = new OrderDetail();
            for (int i = 0; i < cartItems.size(); i++) {
                orderDetail.setOrderId(Common.curentOrder.getId());
                orderDetail.setFoodId(cartItems.get(i).getFoodId());
                orderDetail.setPrice(cartItems.get(i).getFoodPrice());
                orderDetail.setQuantity(cartItems.get(i).getFoodQuantity());
                orderDetails.add(orderDetail);
            }
            return orderDetails;
        }
    }
    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailOrderActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
    private void paypal(){
        int length = txtTotalPrice.getText().toString().length();
        String txt_TotalPrice = txtTotalPrice.getText().toString();
        Float amount=Float.parseFloat(txt_TotalPrice.substring(0, length - 1));
        PayPalPayment payPalPayment=new PayPalPayment(new BigDecimal(amount),
                "USD","Thanh toán",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent=new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }
    // HTTP GET request

}
