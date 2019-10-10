package com.hcmunre.apporderfoodclient.views.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.hcmunre.apporderfoodclient.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailOrderActivity extends AppCompatActivity {
    @BindView(R.id.txtfinish)
    TextView txtfinish;
    Dialog dialog;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        btnATM.setOnClickListener(view -> openDialogCardForm());
        txtupdate_phone.setOnClickListener(view -> openUpdatePhoneDialog());
        placeOrder();
    }
    private void placeOrder(){
        txtfinish.setOnClickListener(view -> {
            Intent infish = new Intent(DetailOrderActivity.this, MapsActivity.class);
            startActivity(infish);
        });
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
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,1000);
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
    }

}
