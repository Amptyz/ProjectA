package com.example.Util.Record;

import android.media.MediaPlayer;

public class AudioPlayer {
    private MediaPlayer player;
    public AudioPlayer(){
        player = new MediaPlayer();

    }

    public void play(String FilePath,MediaPlayer.OnCompletionListener onCompletionListener){
        try {
            player.setDataSource(FilePath);
            player.setOnCompletionListener(onCompletionListener);
            player.prepare();
            player.start();

        }catch (Exception e){

        }
    }
}
