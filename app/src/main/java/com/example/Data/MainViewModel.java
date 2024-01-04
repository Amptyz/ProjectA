package com.example.Data;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.example.Util.Record.Recorder;

public class MainViewModel extends ViewModel {

    private String token;
    private String userName;

    private Recorder recorder;

    private String cachePath;

    public void setRecorder(Recorder rec){
        recorder=rec;
    }
    public void start(){

        Log.i("playStart","开始录音！");
        cachePath = recorder.Start();

    }
    public void stop(){


        Log.i("playStop","录音结束！");
        recorder.Stop();

        recorder.Store("TestWav.wav");

    }

    public void play(String file){
        MediaPlayer.OnCompletionListener listener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.i("playEnd","播放结束！");
            }
        };
        recorder.play(file,listener);
    }
















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
}