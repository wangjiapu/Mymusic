package com.example.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Created by 蒲家旺 on 2016/10/4.
 * 本类主要内容:音乐播放的后台
 */
public class MusicService extends Service {
    private MyBinder binder=new MyBinder();
    public MediaPlayer player=new MediaPlayer();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class MyBinder extends Binder {
        public MusicService getMusicService(){
            return MusicService.this;
        }
    }
    //音乐播放
    public void play(String path){
        player.reset();
        try {
            player.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //监听是否已经准备好
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                player.start();//开始播放音乐
            }
        });
    }
    //音乐暂停
    public void pause(){
        if (player!=null){
            player.pause();
        }
    }

    public  void stop(){
        if (player!=null){
            player.stop();
            player.release();//释放
        }
    }
    public void countineplay(){
        player.start();
    }
    //下一首

    //上一首

}
