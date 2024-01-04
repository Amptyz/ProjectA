package com.example.Util.Record;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Recorder {

    private AudioRecord audioRecorder;
    private Thread recordingAudioThread;
    private boolean isRecording = false;//mark if is recording

    // 采样率，现在能够保证在所有设备上使用的采样率是44100Hz, 但是其他的采样率（22050, 16000, 11025）在一些设备上也可以使用。
    public static final int SAMPLE_RATE_INHZ = 16000;

    // 声道数。CHANNEL_IN_MONO and CHANNEL_IN_STEREO. 其中CHANNEL_IN_MONO是可以保证在所有设备能够使用的。
    public static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;

    // 返回的音频数据的格式。 ENCODING_PCM_8BIT, ENCODING_PCM_16BIT, and ENCODING_PCM_FLOAT.
    public static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;


    private Context context;
    private String audioCacheFilePath;

    private String audioStorePath;

    public Recorder(Context cont) {
        context=cont;
        player = new MediaPlayer();
        audioCacheFilePath = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + "jerboa_audio_cache.pcm";
        audioStorePath = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath();
    }

    public String Start(){

        try{
            // 获取最小录音缓存大小，
            int minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE_INHZ, CHANNEL_CONFIG, AUDIO_FORMAT);
            this.audioRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE_INHZ, CHANNEL_CONFIG, AUDIO_FORMAT, minBufferSize);


            // 开始录音
            this.isRecording = true;
            audioRecorder.startRecording();

            // 创建数据流，将缓存导入数据流
            this.recordingAudioThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    File file = new File(audioCacheFilePath);
                    Log.i(TAG, "audio cache pcm file path:" + audioCacheFilePath);

                    /*
                     *  以防万一，看一下这个文件是不是存在，如果存在的话，先删除掉
                     */
                    if (file.exists()) {
                        file.delete();
                    }

                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Log.e(TAG, "临时缓存文件未找到");
                    }
                    if (fos == null) {
                        return;
                    }

                    byte[] data = new byte[minBufferSize];
                    int read;
                    if (fos != null) {
                        while (isRecording && !recordingAudioThread.isInterrupted()) {
                            read = audioRecorder.read(data, 0, minBufferSize);
                            if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                                try {
                                    fos.write(data);
                                    Log.i("audioRecordTest", "写录音数据->" + read);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    try {
                        // 关闭数据流
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            this.recordingAudioThread.start();
        }
        catch(IllegalStateException e){
            Log.w(TAG,"需要获取录音权限！");

        }
        catch(SecurityException e){
            Log.w(TAG,"需要获取录音权限！");

        }

        return audioCacheFilePath;
    }
    public void Stop(){
        try {
            this.isRecording = false;
            if (this.audioRecorder != null) {
                this.audioRecorder.stop();
                this.audioRecorder.release();
                this.audioRecorder = null;
                this.recordingAudioThread.interrupt();
                this.recordingAudioThread = null;
            }
        }
        catch (Exception e){
            Log.w(TAG,e.getLocalizedMessage());
        }
    }

    public String Store(String wavFilePath){
        PcmToWavUtil ptwUtil = new PcmToWavUtil();
        wavFilePath = audioStorePath + "/" + wavFilePath;
        Log.i("wavFilePath",wavFilePath);
        ptwUtil.pcmToWav(audioCacheFilePath,wavFilePath,false);
        return wavFilePath;
    }

    private MediaPlayer player;


    public void play(String FilePath,MediaPlayer.OnCompletionListener onCompletionListener){
        FilePath = audioStorePath + "/" + FilePath;
        Log.i("playFilePath",FilePath);
        try {
            player.setDataSource(FilePath);
            player.setOnCompletionListener(onCompletionListener);
            player.prepare();
            player.start();

        }catch (Exception e){

        }
    }
    public void pause(){
        player.pause();
    }
    public void stopPlay(){
        player.stop();
    }

    public String getAudioStorePath(){
        return audioStorePath;
    }

}
