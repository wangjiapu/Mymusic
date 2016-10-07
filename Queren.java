package com.example.musicplayer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;


/**
 * Created by 蒲家旺 on 2016/10/5.
 */
public class Queren extends Activity implements View.OnClickListener {

    public static final String action = "xu_dong_sb";

    private List<Music> list = new ArrayList<Music>();
    private int index = 0;//用来记录音乐
    private boolean flag = true;
    private String zhuangtai = "play";//记录播放状态

    private SeekBar sb;
    private TextView music_progress;//音乐播放当前时间
    private TextView music_duration;//音乐时长
    private ImageView play_pasue_music;//播放暂停按钮
    private ImageView pre;//上一首
    private ImageView next;//下一首
    private ImageView moshi;//播放模式
    private TextView singer;
    private MusicService musicService;//音乐服务端
    private MediaPlayer mediaPlayer;//媒体
    private TextView musicName;//音乐名
    private MyServiceConnection conn;

    private android.os.Handler handler = new android.os.Handler();  //暂时看不懂
    //private Handler handler=new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sb.setProgress(mediaPlayer.getCurrentPosition());//当前音乐刻度
            music_progress.setText(scendtoString(mediaPlayer.getCurrentPosition()));
            handler.postDelayed(runnable, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);

        Bundle bu = getIntent().getExtras();
        index = bu.getInt("choose");

        initView();
        godie();
        Intent intentser = new Intent(this, MusicService.class);
        this.startService(intentser);
        //绑定服务
        conn = new MyServiceConnection();
        this.bindService(intentser, conn, Context.BIND_AUTO_CREATE);
    }

    private void initView() {
        sb = (SeekBar) findViewById(R.id.seekBar);
        pre = (ImageView) findViewById(R.id.imageView2);
        next = (ImageView) findViewById(R.id.imageView4);
        music_progress = (TextView) findViewById(R.id.textView2);
        music_duration = (TextView) findViewById(R.id.textView3);
        play_pasue_music = (ImageView) findViewById(R.id.imageView);
        musicName = (TextView) findViewById(R.id.textView7);
        moshi = (ImageView) findViewById(R.id.imageView3);
        moshi.setOnClickListener(this);
        singer = (TextView) findViewById(R.id.singger);
        singer.setOnClickListener(this);

        findViewById(R.id.seekBar).setOnClickListener(this);
        findViewById(R.id.imageView2).setOnClickListener(this);
        findViewById(R.id.imageView4).setOnClickListener(this);
        findViewById(R.id.textView2).setOnClickListener(this);
        findViewById(R.id.textView3).setOnClickListener(this);
        findViewById(R.id.imageView).setOnClickListener(this);
        findViewById(R.id.textView7).setOnClickListener(this);

    }


    public class MyServiceConnection implements ServiceConnection {

        //绑定成功
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
            musicService = myBinder.getMusicService();
            //开始播放
            mediaPlayer = musicService.player;
            if (!mediaPlayer.isPlaying()) {
                playMusicByStatu(0);
                play_pasue_music.setImageResource(R.drawable.play2);//改变图标
                sendmessege();
               // zhuangtai = "play";
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        playMusicByStatu(1);
                        sendmessege();
                       // zhuangtai = "play";
                    }
                });
            } else {
                play_pasue_music.setImageResource(R.drawable.play2);//改变图标
               // zhuangtai = "play";
                sendmessege();
            }
            //播放音乐
            //sendmessege();
            sb.setMax(mediaPlayer.getDuration());
            handler.removeCallbacks(runnable);
            handler.post(runnable);
        }

        //绑定失败
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    public void playMusicByStatu(int statu) {
        switch (statu) {
            case 0:
                //直接播放
                break;
            case 1:
                //下一首
                index++;
                if (index == list.size()) {
                    index = 0;
                }
                break;
            case 2:
                //上一首
                index--;
                if (index == -1) {
                    index = list.size() - 1;
                }
                break;
        }
        playMusic(index);
    }

    private void playMusic(int index) {
        if (list.size() > 0) {
            musicService.play(list.get(index).songpath);
            musicName.setText(list.get(index).songname);
            music_duration.setText(scendtoString(list.get(index).duration));
            singer.setText(list.get(index).songer);

            //设置的控件的同时发送广播.改变前面控件的状态
            sendmessege();
        }
        sb.setMax(mediaPlayer.getDuration());   //设置最大值
    }

    private void sendmessege() {
        Intent intent = new Intent(action);
        intent.putExtra("songname", musicName.getText());
        sendBroadcast(intent);
        intent.putExtra("singer", singer.getText());
        sendBroadcast(intent);
        if (!mediaPlayer.isPlaying()) {
            intent.putExtra("zhuangtai", "play");
        }else{
            intent.putExtra("zhuangtai", "stop");
        }
        sendBroadcast(intent);
    }

    //时间转化
    private String scendtoString(int duration) {
        int seconds = duration / 1000;
        int scond = seconds % 60;
        int mu = (seconds - scond) / 60;
        DecimalFormat de = new DecimalFormat("00");
        return de.format(mu) + ":" + de.format(scond);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView2:
                playMusicByStatu(2);
                break;
            case R.id.imageView4:
                playMusicByStatu(1);
                break;
            case R.id.imageView:
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        musicService.pause();
                        //改变图标
                        play_pasue_music.setImageResource(R.drawable.stop2);
                        sendmessege();
                        //zhuangtai = "stop";
                    } else {
                        musicService.countineplay();
                        //改变图标
                        play_pasue_music.setImageResource(R.drawable.play2);
                        sendmessege();
                        //zhuangtai = "play";
                    }
                }
                break;
            case R.id.imageView3:
                if (flag) {
                    moshi.setImageResource(R.drawable.shuiji2);
                    //方法未实现  -_-
                    flag = false;
                } else {
                    moshi.setImageResource(R.drawable.shun2);
                    flag = true;
                }
                break;

        }
    }

    //解绑服务
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null) {
            this.unbindService(conn);
        }
    }

    private void godie() {
        Cursor cursor = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        int isMusic;
        while (cursor != null && cursor.moveToNext()) {
            Music song = new Music();
            song.tupian = R.drawable.m1;
            song.songname = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            song.songer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            song.duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
            song.songpath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            song.size = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
            isMusic = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC));
            if (isMusic != 0) {
                list.add(song);
            }
        }
    }
}
