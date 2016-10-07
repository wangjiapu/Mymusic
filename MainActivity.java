package com.example.musicplayer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends Activity implements View.OnClickListener {
    //自定义viewpager
    private MainActivity mmm;
    private List<View> mView = new ArrayList<View>();
    private List<Music> list=new ArrayList<Music>();
    private ViewPager mViewPager;
    private PagerAdapter mAdapter;

    private int songnum=0;
    //mainlayout2 三个标题栏的定义
    private TextView mymusic;
    private TextView myyinyue;
    private TextView myfind;
    //低栏的监听事件
    private ImageView imageView1;
    private TextView tv_songname;
    private TextView tv_singer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mmm=this;
        setContentView(R.layout.activity_main);
        initView();
        initEvents();
    }

    private void initEvents() {
        mymusic.setOnClickListener(this);
        myyinyue.setOnClickListener(this);
        myfind.setOnClickListener(this);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                int currentitem = mViewPager.getCurrentItem();
                setcolor();
                switch (currentitem) {
                    case 0:
                        mymusic.setTextColor(Color.parseColor("#2343e6"));
                        break;
                    case 1:
                        myyinyue.setTextColor(Color.parseColor("#2343e6"));
                        break;
                    case 2:
                        myfind.setTextColor(Color.parseColor("#2343e6"));
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View tab1 = mInflater.inflate(R.layout.mainlayout3, null);
        View tab2 = mInflater.inflate(R.layout.yinyuejia, null);
        View tab3 = mInflater.inflate(R.layout.findmusic, null);

        mView.add(tab1);
        mView.add(tab2);
        mView.add(tab3);
        godie();
        songnum = new Random().nextInt(list.size());  //生成一个随机数；

        mAdapter = new PagerAdapter() {
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mView.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // return super.instantiateItem(container, position);
                View view = mView.get(position);
                switch (position){
                    case 0:
                        view.findViewById(R.id.l_music1).setOnClickListener(mmm);
                        view.findViewById(R.id.l_music2).setOnClickListener(mmm);
                        view.findViewById(R.id.l_music3).setOnClickListener(mmm);

                        view.findViewById(R.id.l_music4).setOnClickListener(mmm);
                        view.findViewById(R.id.l_music5).setOnClickListener(mmm);
                        view.findViewById(R.id.l_music6).setOnClickListener(mmm);

                        break;
                    case 1:
                        //view.findViewById(R.id.l_music2).setOnClickListener(mmm);
                        break;
                    case 2:
                       // view.findViewById(R.id.l_music2).setOnClickListener(mmm);
                        break;
                }
                container.addView(view);
                return view;
            }

            @Override
            public int getCount() {
                return mView.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        };
        mViewPager.setAdapter(mAdapter);

        mymusic = (TextView) findViewById(R.id.tv1);
        myyinyue = (TextView) findViewById(R.id.tv2);
        myfind = (TextView) findViewById(R.id.tv3);

        imageView1=(ImageView)findViewById(R.id.imageButton2);
        imageView1.setOnClickListener(this);
        tv_songname=(TextView)findViewById(R.id.textview1);
        tv_songname.setOnClickListener(this);
        tv_singer=(TextView)findViewById(R.id.textView);
        tv_singer.setOnClickListener(this);

        mymusic.setTextColor(Color.parseColor("#2343e6"));
        //广播接收者
        IntentFilter filter=new IntentFilter(Queren.action);
        registerReceiver(broadcastReceiver,filter);

    }

    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            tv_songname.setText(intent.getStringExtra("songname"));
            tv_singer.setText(intent.getStringExtra("singer"));
            String str=intent.getStringExtra("zhuangtai");
            if (str!=null){
                if (str.equals("play")){
                    imageView1.setImageResource(R.drawable.startp);
                }
                if (str.equals("stop")) {
                    imageView1.setImageResource(R.drawable.stopp);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onClick(View v) {
        setcolor();
        switch (v.getId()) {
            case R.id.tv1:
                mymusic.setTextColor(Color.parseColor("#2343e6"));
                mViewPager.setCurrentItem(0);
                break;
            case R.id.tv2:
                myyinyue.setTextColor(Color.parseColor("#2343e6"));
                mViewPager.setCurrentItem(1);
                break;
            case R.id.tv3:
                myfind.setTextColor(Color.parseColor("#2343e6"));
                mViewPager.setCurrentItem(2);
                break;
            //跳转我的音乐界面
            case R.id.l_music1:
            case R.id.l_music2:
            case R.id.l_music3:
               // Toast.makeText(MainActivity.this,"我的音乐！",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity.this,MyMusicList.class);
                MainActivity.this.startActivity(intent);
                break;
            //跳转player界面
            case R.id.textView:
            case R.id.textview1:
            case R.id.imageview1:
                Intent intent3 = new Intent(this, Queren.class);
                intent3.putExtra("choose", songnum);
                startActivity(intent3);
                break;
            //跳转我的喜欢界面
            case R.id.l_music4:
            case R.id.l_music5:
            case R.id.l_music6:
                Intent intentlike=new Intent(MainActivity.this,MyLikeMusicList.class);
                MainActivity.this.startActivity(intentlike);
                break;

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

    private void setcolor() {
        myfind.setTextColor(Color.parseColor("#4d5053"));
        myyinyue.setTextColor(Color.parseColor("#4d5053"));
        mymusic.setTextColor(Color.parseColor("#4d5053"));
    }
}
