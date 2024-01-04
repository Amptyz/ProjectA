package com.example;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uidesign.R;
import com.example.uidesign.databinding.ActivityLoginBinding;

public class SummaryActivity extends AppCompatActivity {


    Toolbar toolbar;
    TextView summary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        summary = findViewById(R.id.summary);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        Intent intent=getIntent();
        String sum = intent.getStringExtra("summary");
        Log.d("Summary--",sum);
        summary.setText(sum);


    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
