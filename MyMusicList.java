package com.example.musicplayer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by 蒲家旺 on 2016/10/4.
 * 版本:1.0
 * 内容：我的音乐的列表
 */

public class MyMusicList extends Activity {

    private ListView mListView;
    private List<Music> list = new ArrayList<Music>();
    private MyAdapter myadapter;
    //private int index = 0;//用来记录音乐
    private MyMusicList mymusiclist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        mymusiclist = this;
        initMyMusiclist();
        // initView();

    }


    private void initMyMusiclist() {
        mListView = (ListView) findViewById(R.id.listView);
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
        // cursor.close();
        myadapter = new MyAdapter(this, list);
        mListView.setAdapter(myadapter);
        mListView.setOnItemClickListener(new ItemClickEvent());

    }

    //继承OnItemClickListener，当子项目被点击的时候触发
    private final class ItemClickEvent implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            //通过单击事件，获得单击选项的内容
            Intent intent3 = new Intent(mymusiclist, Queren.class);
            intent3.putExtra("choose", position);
            startActivity(intent3);


        }
    }
}
