package com.example.Data;

import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Util.Record.Recorder;
import com.example.uidesign.data.model.ClassRecord;

public class MainViewModel extends ViewModel {

    public boolean isRecordBtnActive;
    public boolean isClassStarted;
    private String token;
    private String userName;
    private Recorder recorder;
    public String currentFile;
    private ClassRecord currentRecord;


    public void setToken(String t){
        token = t;
    }
    public String getToken(){
        return token;
    }
    public void setUserName(String t){
        userName = t;
    }
    public String getUserName(){
        return userName;
    }

    public void setCurrentRecord(ClassRecord classRecord) {
        currentRecord = classRecord;
    }

    public ClassRecord getCurrentRecord() {
        return currentRecord;
    }

}
