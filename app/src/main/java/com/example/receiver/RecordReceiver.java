package com.example.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RecordReceiver extends BroadcastReceiver {
    private static final String TAG = "RecordReceiver";
    private String recordPath;
    @Override
    public void onReceive(Context context, Intent intent) {
        recordPath = intent.getStringExtra("recordPath");

    }

    public String getRecordPath(){
        return recordPath;
    }
}
