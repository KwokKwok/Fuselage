package com.kwokstudio.fuselage.ui.index;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郭垒 on 2016/11/17.
 */

public class IndexBar extends View {

    private Paint paint;
    List<String> indexArr = new ArrayList<>();
    //等会动态设置
    List<String> stringArr = new ArrayList<>();
    private int zone=0;
    private int lastIndex = -1;
    private float cellHeight = 0;
    private int width;
    private boolean onScreen = false;
    private int distance;

    public IndexBar(Context context) {
        super(context);
        init();
    }

    public IndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        indexArr.add("#");
        for (int i = 65; i <= 90; i++) {
            indexArr.add(String.valueOf((char) i));
        }

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setTextSize(35);
        paint.setTextAlign(Paint.Align.CENTER);

        setBackgroundColor(Color.TRANSPARENT);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int _zone=zone;
        for (int i = 0; i < indexArr.size(); i++) {
            float x;
            float y = cellHeight * (i+1) + cellHeight / 2;
            if (onScreen) {  //手指在屏幕上
                int abs = Math.abs(lastIndex - i);
                if (abs > 6) {
                    paint.setTextSize(35);
                    x = (float) (width * 0.9);
                    y+=(35/2);
                } else {
                    x = (float) (width * (1.1 - Math.cos(0.22 * abs)));
                    float textSize = ((cellHeight - (cellHeight - 35) * abs / 6)*6/5);
                    paint.setTextSize(textSize);
                    y+= textSize /2;
                }
            } else {
                paint.setTextSize(35);
                x = (float) (width * 0.9);
                y+=(35/2);
            }
            paint.setColor(lastIndex==i ? Color.BLUE : Color.DKGRAY);
            if (_zone>=0&&i>lastIndex){
                paint.setColor(lastIndex+zone>i ? Color.BLUE : Color.DKGRAY);
                _zone--;
            }
            canvas.drawText(indexArr.get(i), x, y, paint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cellHeight = getMeasuredHeight() * 1f / (indexArr.size() +2);
        width = getMeasuredWidth();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                onScreen = true;
                float y = event.getY();
                int index = (int) (y / cellHeight);
                if (index != lastIndex && index < 28&&index>0) {
                    lastIndex = index-1;
                    //按下时的背景色
                    if (listener != null&&index>-1) {  //有时index会小于0
                        listener.onTouchLetter(indexArr.get(index-1));
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                onScreen = false;
                lastIndex = -1;
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }

    //设置回调接口
    private OnTouchLetterListener listener;

    public void setOnTouchLetterListener(OnTouchLetterListener listener) {
        this.listener = listener;
    }

    public interface OnTouchLetterListener {
        void onTouchLetter(String letters);
    }

//    /**
//     * 设置显示区域
//     */
//    public void setZone(int start,int end) {
//        if (start>26||start<1){
//            lastIndex=0;
//        }else lastIndex=start;
//        this.zone=end-start+1;
//        invalidate();
//    }

}
