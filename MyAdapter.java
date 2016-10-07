package com.example.musicplayer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 蒲家旺 on 2016/10/4.
 * 内容：打造音乐列表适配器
 */
public class MyAdapter extends BaseAdapter {
    private Context context;
    private List<Music> list;

    public MyAdapter(Context context, List<Music> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            holder = new ViewHolder();
            //引入布局
            convertView = View.inflate(context, R.layout.musicitem, null);
            //实例化对象
            holder.tupiam=(ImageView)convertView.findViewById(R.id.item_mymusic_postion);
            holder.song = (TextView) convertView.findViewById(R.id.item_mymusic_song);
            holder.songer = (TextView) convertView.findViewById(R.id.item_mymusic_singer);
           // holder.duration = (TextView) convertView.findViewById(R.id.item_mymusic_duration);
           // holder.position = (TextView) convertView.findViewById(R.id.item_mymusic_postion);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //给控件赋值
      //  holder.tupiam.setBackgroundResource(list.get(position).tupian);
        holder.song.setText(String.valueOf(list.get(position).songname));
        holder.songer.setText(String.valueOf(list.get(position).songer));
        //时间需要转换一下
       // int duration = list.get(position).duration;
        //String time = MusicUtils.formatTime(duration);
       // holder.duration.setText(time);
        //holder.position.setText(i+1+"");

        return convertView;
    }

    class ViewHolder {
        ImageView tupiam;
        TextView song;//歌曲名
        TextView songer;//歌手
        //TextView duration;//时长
        //TextView position;//序号
    }
}
