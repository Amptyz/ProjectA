package com.example;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.Data.MainViewModel;
import com.example.uidesign.R;
import com.example.uidesign.databinding.ActivityLoginBinding;

public class SummaryActivity extends AppCompatActivity {


    Toolbar toolbar;
    TextView summary;

    private MainViewModel mainViewModel;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        summary = findViewById(R.id.summary);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent=getIntent();
        getSupportActionBar().setTitle(intent.getStringExtra("className"));
        summary.setText("原文：" + intent.getStringExtra("originText") + "\n总结：" + intent.getStringExtra("summary"));

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
