package com.example.service;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.uidesign.R;
import com.google.android.material.internal.StaticLayoutBuilderConfigurer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class RecordingService extends Service {

    private static final String LOG_TAG = "RecordingService";
    private String mBaseFilePath;
    private AudioRecord mRecorder;
    private String mCacheFilePath;
    private int mCacheSize;
    private long mStartTime;
    private File mCacheFile;
    private FileOutputStream mCacheFOS;

    public static final int SAMPLE_RATE = 16000;
    public static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    public static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    private boolean mIsRecording;
    private Thread mSavingThread;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!mIsRecording) {
            mIsRecording = true;
            mStartTime = System.currentTimeMillis();
            mBaseFilePath = intent.getStringExtra("baseFilePath");
            mCacheFilePath = mBaseFilePath + "/" + mStartTime + ".pcm";
            startRecording();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stopRecording();
    }

    private void startRecording() {
        try {
            int minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
            mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, minBufferSize);
            mRecorder.startRecording();
            mCacheSize = 0;
            mSavingThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (!createCacheFile()) return;
                    try {
                        byte[] data = new byte[minBufferSize];
                        while (mIsRecording && !mSavingThread.isInterrupted()) {
                            int state = mRecorder.read(data, 0, minBufferSize);
                            if (AudioRecord.ERROR_INVALID_OPERATION != state) {
                                mCacheFOS.write(data);
                                mCacheSize += state;
                            }
                            if (mCacheSize >= 5242880) { //5MB
                                mCacheSize = 0;
                                String recordFilePath = mBaseFilePath + "/record_" +  System.currentTimeMillis() + ".pcm";
                                if (!storeCacheFile(recordFilePath)) return;
                                createCacheFile();
                            }
                        }
                        String recordFilePath = mBaseFilePath + "/record_" +  System.currentTimeMillis() + ".pcm";
                        storeCacheFile(recordFilePath);
                    } catch (FileNotFoundException e) {
                        Log.w(TAG, "未找到缓冲文件！");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            mSavingThread.start();
        } catch(IllegalStateException | SecurityException e) {
            Log.w(TAG, "需要获取录音权限！");
        }
    }
    private void stopRecording() {
        if (mIsRecording) {
            mIsRecording = false;
            mRecorder.stop();
            stopForeground(STOP_FOREGROUND_REMOVE);
            mRecorder = null;
        }
    }
    private boolean createCacheFile() {
        mCacheFile = new File(mCacheFilePath);
        try {
            if (mCacheFile.exists()) {
                if (!mCacheFile.delete()) {
                    Log.w(TAG, "缓冲文件被占用！");
                    return false;
                }
            }
            if (!mCacheFile.createNewFile()) {
                Log.w(TAG, "创建缓冲文件失败！");
                return false;
            }
            mCacheFOS = new FileOutputStream(mCacheFile);
        } catch (SecurityException e) {
            Log.w(TAG, "需要获取文件读写权限！");
            return false;
        } catch (IOException e) {
            Log.w(TAG, "文件读写失败！");
            return false;
        }
        return true;
    }
    private boolean storeCacheFile(String recordFilePath) {
        try {
            File recordFile = new File(recordFilePath);
            mCacheFOS.close();
            if (!mCacheFile.renameTo(recordFile)) {
                Log.w(TAG, "写入记录文件失败！");
                return false;
            }
            Intent intent = new Intent();
            intent.putExtra("recordPath", recordFilePath);
            intent.setAction("com.example.receiver.RecordReceiver");
            //intent.setClass(this,Class.forName("com.example.receiver.RecordReceiver"));
            sendBroadcast(intent);
        } catch (IOException e) {
            Log.w(TAG, "写入记录文件失败！");
            return false;
        }
        return true;
    }

    private void initNotification() {
        String channelName = "即时录音";
        String channelID = "juanbao.record";
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("卷宝使用麦克风时使用的通知类别");
        notificationManager.createNotificationChannel(channel);
        Notification notification = new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("卷宝正在听课……")
                .setContentText("点击打开卷宝！")
                .setAutoCancel(false)
                .setOngoing(true)
                .build();
        startForeground(1, notification);
    }
}
