package com.qqcebian.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2016/10/11.
 */
  public class MyRelativeLayout extends RelativeLayout{

    private DragLayout dl;

    public MyRelativeLayout(Context context) {
        super(context);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDragLayout(DragLayout dl){
        this.dl = dl;
    }


    /*
     *用于拦截手势 监听事件
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(dl.getStatus()!= DragLayout.Status.Close){
            return  true; //该方法如果返回false,则手势事件会向子控件传递；返回true，则调用onTouchEvent()方法
        }

        return super.onInterceptTouchEvent(ev);
    }
    /*
     *触摸监听事件 ，用于处理View中的手势事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(dl.getStatus()!= DragLayout.Status.Close){
            if(event.getAction()==MotionEvent.ACTION_UP){
                dl.close();
            }
            return true; //onTouchEvent()事件返回true，表示当前事件结束了，并不会向下传递给子控件
        }
        return super.onTouchEvent(event);
    }

    //    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }
}
