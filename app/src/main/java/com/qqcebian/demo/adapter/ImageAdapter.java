package com.qqcebian.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.qqcebian.demo.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/11.
 */
public class ImageAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private ArrayList<String>   paths=new ArrayList<String>();

    public ImageAdapter(Context context){
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public Object getItem(int position) {
        return paths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addAll(ArrayList<String> paths){
        this.paths.clear();
        this.paths.addAll(paths);
    }


    /*
     *用于加载缓存布局
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;//该对象用于暂存布局文件

        if(convertView==null){ //如果当前布局文件为空，则重新加载
            convertView=inflater.inflate(R.layout.item_image,null);
            viewHolder=new ViewHolder();
            viewHolder.iv_item=(ImageView)convertView.findViewById(R.id.iv_item);

            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
//        viewHolder.iv_item.setImageResource(R.mipmap.default_face);
        //加载图片（包括路径和图片）
        ImageLoader.getInstance().displayImage("file://"+paths.get(position),viewHolder.iv_item);
        return convertView;
    }

    static class ViewHolder{
        ImageView iv_item;
    }
}
