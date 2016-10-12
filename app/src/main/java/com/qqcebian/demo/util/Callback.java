package com.qqcebian.demo.util;

/**
 * 定义Callback接口
 */
public interface Callback {

    public boolean onRun();

    public void onBefore();

    public void onAfter(boolean b);
}
