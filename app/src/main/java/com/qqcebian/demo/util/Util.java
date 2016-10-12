package com.qqcebian.demo.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.qqcebian.demo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 该类的作用是图片加载框架的使用
 */
public class Util {

    /*
     *初始化图片载入
     */
    public static void initImageLoader(Context context){

        //相关参数的配置
        DisplayImageOptions defaultOptions=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_face)  // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.default_face)    // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.default_face) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)   // default  设置下载的图片是否缓存在内存中
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300, true, true, true))  //设置图片的显示方式,第二个内部类表示 ：设置图片渐显的时间
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)    //设置图片的缩放方式  EXACTLY_STRETCHED表示：  图片会缩放到目标大小完全
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        //参数配置
        ImageLoaderConfiguration.Builder builder=new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache());

        ImageLoaderConfiguration config=builder.build();//开始构建

        ImageLoader.getInstance().init(config);//初始化操作

    }
    /*
       获取图片
     */
    @SuppressWarnings("deprecation")
    public  static ArrayList<String> getGalleryPhotos(Activity act){
        ArrayList<String> galleryList=new ArrayList<String>();
        try{
//            MediaStore这个类是android系统提供的一个多媒体数据库，android中多媒体信息都可以从这里提取。
            //MediaStore.Images.Media.DATA表示图片的绝对路径
            //MediaStore.Images.Media._ID 表示图片ID
            final String[] columns={MediaStore.Images.Media.DATA,MediaStore.Images.Media._ID};
            final String orderBy=MediaStore.Images.Media._ID;
            //通过内容提供器来查询 图片缩略图数据库

            Cursor imagecursor=act.managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    columns,null,null,orderBy);
            if(imagecursor!=null&&imagecursor.getCount()>0) {
                while (imagecursor.moveToNext()) {
                    String item = new String();
                    int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    item = imagecursor.getString(dataColumnIndex);
                    galleryList.add(item);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Collections.reverse(galleryList);//reverse()方法是反转，对结果从后往前输出

        return galleryList;
    }

    /*
       剪裁图像
     */
    public static Bitmap convertViewToBitmap(View view){
        Bitmap bitmap=null;
        try{
            int width=view.getWidth();
            int height=view.getHeight();
            if(width!=0&&height!=0){
                //压缩图片格式
                bitmap=Bitmap.createBitmap(width,height, Bitmap.Config.RGB_565);
                Canvas canvas=new Canvas(bitmap);
                view.layout(0,0,width,height);
                view.setBackgroundColor(Color.WHITE);
                view.draw(canvas);
            }
        }catch (Exception e){
            bitmap=null;
            e.printStackTrace();
        }
       return  bitmap;
    }

    /*
      保存图片
     */
    public static  boolean saveImageToGallery(Context context, Bitmap bmp, boolean isPng){
        if(bmp==null){
            return  false;
        }
        //从外部存储卡路径中获取图片
        File appDir=new File(Environment.getExternalStorageDirectory(),context.getString(R.string.app_name));
        //如果appDir这个文件和目录都不存在，则返回false
        if(!appDir.exists())
        {
            if(!appDir.mkdir()){
                return  false;
            }
        }
        String fileName;
        if(isPng){
            fileName=System.currentTimeMillis()+".png";
        }else{
            fileName=System.currentTimeMillis()+".jpg";
        }
        File file=new File(appDir,fileName);

        try{

            FileOutputStream fos=new FileOutputStream(file);
            if(isPng){
                //.compress()方法：把位图的压缩信息写入到一个指定的输出流中
                //第一个参数format为压缩的格式
                //第二个参数quality为图像压缩比的值,0-100.0 意味着小尺寸压缩,100意味着高质量压缩
                //第三个参数stream为输出流
                bmp.compress(Bitmap.CompressFormat.PNG,100,fos);
            }else{
                bmp.compress(Bitmap.CompressFormat.JPEG,100,fos);
            }
            bmp.recycle();
            fos.flush();
            fos.close();


        }catch (FileNotFoundException e){
            e.printStackTrace();
            return false;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }

        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        //发送广播
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(appDir)));
        return true;
    }

   public  static void t(Context context,String text){
       //弹出一个提示框
       Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
   }

}
