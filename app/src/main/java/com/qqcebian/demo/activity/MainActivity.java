package com.qqcebian.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qqcebian.demo.R;
import com.qqcebian.demo.adapter.ImageAdapter;
import com.qqcebian.demo.util.Callback;
import com.qqcebian.demo.util.Invoker;
import com.qqcebian.demo.util.Util;
import com.qqcebian.demo.view.DragLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private DragLayout dl;
    private GridView gv_img;//网格视图控件
    private ImageAdapter adapter;
    private ListView lv;
    private TextView tv_noimg;
    private ImageView iv_icon, iv_bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Util.initImageLoader(this);

        initDragLayout();
        initViews();
    }


    public void initDragLayout(){
        dl=(DragLayout)findViewById(R.id.dl);
        dl.setDragListener(new DragLayout.DragListener() {
            @Override
            public void onOpen() {
                lv.smoothScrollToPosition(new Random().nextInt(30));//产生随机数
            }

            @Override
            public void onClose() {
                shake();
            }

            @Override
            public void onDrag(float percent) {
                ViewHelper.setAlpha(iv_icon,1-percent);
            }
        });
    }

    /*
     *初始化控件方法
     */
    private void initViews() {
        iv_icon=(ImageView)findViewById(R.id.iv_icon);
        iv_bottom=(ImageView)findViewById(R.id.iv_bottom);
        gv_img=(GridView)findViewById(R.id.gv_img);
        tv_noimg=(TextView)findViewById(R.id.iv_noimg);

        gv_img.setFastScrollEnabled(true);
        adapter=new ImageAdapter(this);
        gv_img.setAdapter(adapter);
        /*
         *网格视图GridView的监听事件
         */
        gv_img.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,ImageActivity.class);
                intent.putExtra("path", (Bundle) adapter.getItem(position));
                startActivity(intent);
            }
        });

        lv=(ListView)findViewById(R.id.lv);
        lv.setAdapter(new ArrayAdapter<String>(MainActivity.this,R.layout.item_text,
                new String[]{"NewBee", "ViCi Gaming",
                        "Evil Geniuses", "Team DK", "Invictus Gaming", "LGD",
                        "Natus Vincere", "Team Empire", "Alliance", "Cloud9",
                        "Titan", "Mousesports", "Fnatic", "Team Liquid",
                        "MVP Phoenix", "NewBee", "ViCi Gaming",
                        "Evil Geniuses", "Team DK", "Invictus Gaming", "LGD",
                        "Natus Vincere", "Team Empire", "Alliance", "Cloud9",
                        "Titan", "Mousesports", "Fnatic", "Team Liquid",
                        "MVP Phoenix" }));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Util.t(getApplicationContext(),"click"+position);
            }
        });

        iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.open();
            }
        });

    }

    //准备和用户进行交互，Activity生命周期方法
    @Override
    protected void onResume() {
        super.onResume();
        loadImage();
    }
    /*
     *载入图片
     */
    private void loadImage() {
        new Invoker(new Callback() {
            @Override
            public boolean onRun() {
                adapter.addAll(Util.getGalleryPhotos(MainActivity.this));
                return adapter.isEmpty();
            }

            @Override
            public void onBefore() {
                // 转菊花
            }

            @Override
            public void onAfter(boolean b) {
                adapter.notifyDataSetChanged();
                if(b){
                    tv_noimg.setVisibility(View.VISIBLE);
                }else{
                    tv_noimg.setVisibility(View.GONE);
                    String s="file://"+adapter.getItem(0);
                    ImageLoader.getInstance().displayImage(s,iv_icon);
                    ImageLoader.getInstance().displayImage(s,iv_bottom);
                }
                shake();
            }
        }).start();
    }

    private void shake() {
        //开启动画效果
        iv_icon.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
    }

}
