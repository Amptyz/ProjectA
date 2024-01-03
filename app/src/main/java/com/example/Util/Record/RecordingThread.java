package com.example.Util.Record;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class RecordingThread extends Thread {
    private AudioRecord audioRecord;

    @Override
    public void run() {
        // 设置录音参数
        int audioSource = MediaRecorder.AudioSource.MIC;  // 音频源为麦克风
        int sampleRate = 44100;  // 采样率为44100Hz
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;  // 声道数为单声道
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;  // 音频格式为16位PCM

        int bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);

        try {
            // 创建AudioRecord对象
            audioRecord = new AudioRecord(audioSource, sampleRate, channelConfig, audioFormat, bufferSize);
        }catch (SecurityException e){
            e.printStackTrace();
        }


        // 开始录音
        audioRecord.startRecording();

        // 录音逻辑...

        // 停止录音
        audioRecord.stop();

        // 释放录音资源
        audioRecord.release();
    }
}
