package com.qqcebian.demo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.qqcebian.demo.R;
import com.qqcebian.demo.util.Callback;
import com.qqcebian.demo.util.Invoker;
import com.qqcebian.demo.util.Util;

/**
 * 图片页面
 * Created by Administrator on 2016/10/11.
 */
public class ImageActivity  extends Activity{

    private RelativeLayout rl;
    private ImageView iv;
    private TextView tv;
    private EditText et;
    private SeekBar sb1;//进度拉取条
    private SeekBar sb2;//进度条
    private String path;
    private ProgressDialog pd;//对话框


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        initViews();

        /*
         *该方法是用来获取View的高度或宽度，这是获得一个view的高度和宽度的方法之一
         * OnGloballLayoutListener是ViewTreeObserver的内部类，当一个视图的布局发生改变时，可以被
         * ViewTreeObserver监听到；
         */
        iv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                iv.getLayoutParams().height=iv.getHeight();
                iv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }
    /*
     *初始化控件方法
     */
    private void initViews() {

        path=getIntent().getStringExtra("path");

        rl=(RelativeLayout)findViewById(R.id.r1);
        iv=(ImageView)findViewById(R.id.iv);
        tv=(TextView)findViewById(R.id.tv);
        et=(EditText)findViewById(R.id.et);
        sb1=(SeekBar)findViewById(R.id.sb1);
        sb2=(SeekBar)findViewById(R.id.sb2);
        //图片加载开源框架方法
        ImageLoader.getInstance().displayImage("file://"+path,iv);

        /*
         *拖动进度条的监听方法
         */
        sb1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

//            该方法拖动进度条进度改变的时候调用
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int padding=200-progress;//内边距
                iv.setPadding(padding,padding,padding,padding);//设置图片内边距（上下左右）

                //invalidate()函数的主要作用是请求View树进行重绘，该函数可以由应用程序调用，或者由系统函数间接调用
                iv.invalidate();
            }

//            该方法拖动进度条开始拖动的时候调用。
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
//            该方法拖动进度条停止拖动的时候调用
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sb2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv.setTextSize(30+progress);//设置字体大小
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

            et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv.setText(s.toString());
            }
        });

    }


    private Bitmap bmp;
    /*
     * Button监听方法
     */
   public void onSave(View v){
       new Invoker(new Callback() {
           @Override
           public boolean onRun() {
               return Util.saveImageToGallery(getApplicationContext(), bmp, path.endsWith(".png"));
           }

           @Override
           public void onBefore() {
             pd=new ProgressDialog(ImageActivity.this);
             pd.setCancelable(false);
             pd.show();
             bmp= Util.convertViewToBitmap(rl);
           }

           @Override
           public void onAfter(boolean b) {
               if (b) {
                   Util.t(getApplicationContext(), "保存成功");
                   pd.dismiss();
                   finish();
               } else {
                   Util.t(getApplicationContext(), "保存失败");
                   pd.dismiss();
               }
           }
       }).start();
   }


}
