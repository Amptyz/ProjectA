package com.example;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uidesign.R;

public class  OrderPlace extends AppCompatActivity {

    private EditText editTextSenderAddress;
    private EditText editTextReceiverAddress;
    private EditText editTextItemType;
    private EditText editTextDeliveryTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_place);

        // 初始化EditText
        editTextSenderAddress = findViewById(R.id.editTextSenderAddress);
        editTextReceiverAddress = findViewById(R.id.editTextReceiverAddress);
        editTextItemType = findViewById(R.id.editTextItemType);
        editTextDeliveryTime = findViewById(R.id.editTextDeliveryTime);

        // 设置Toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        // 设置"Add Order Item"按钮的点击事件
        Button btnAddOrderItem = findViewById(R.id.btnAddOrderItem);
        btnAddOrderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
