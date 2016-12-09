package com.kwokstudio.fuselage.ui.bottomNav;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 郭垒 on 2016/11/19.
 */

public class MyViewPager extends ViewPager {

    private boolean isSwipePageEnable=true;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isSwipePageEnable && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return isSwipePageEnable&&super.onInterceptHoverEvent(event);
    }

    public void setSwipePageEnable(boolean b){
        this.isSwipePageEnable=b;
    }
}
