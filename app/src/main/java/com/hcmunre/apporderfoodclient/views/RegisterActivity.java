package com.hcmunre.apporderfoodclient.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import com.hcmunre.apporderfoodclient.R;
public class RegisterActivity extends AppCompatActivity {

    private Spinner spinner;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phone);

        spinner = findViewById(R.id.spinnerCountries);
        editText = findViewById(R.id.editTextPhone);


    }

}

