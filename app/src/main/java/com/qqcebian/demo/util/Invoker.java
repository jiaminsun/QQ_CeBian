package com.qqcebian.demo.util;

import android.os.Handler;
import android.os.Looper;

/**
 * 定义一个线程Invoker
 */
public class Invoker extends  Thread{

    private Callback callback;

    public Invoker(Callback callback){
        this.callback=callback;
    }
    /*
     *定义一个同步的开启方法（带锁），表示同一时刻只有一个线程能调用
     */
     public synchronized void start(){
         callback.onBefore();
         super.start();
     }

    public void run() {
        final boolean b = callback.onRun();
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                callback.onAfter(b);
            }
        });
    }

}

