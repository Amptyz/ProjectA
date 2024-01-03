package com.example.Util.Record;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.security.Provider;

public class RecordingService extends Service {
    private AudioManager audioManager;
    private RecordingThread recordingThread;

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化音频管理器
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // 请求音频焦点
        int result = audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        // 如果请求焦点成功，则暂停其他正在播放的音频
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // ...
}
