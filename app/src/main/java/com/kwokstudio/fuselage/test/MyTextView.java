package com.kwokstudio.fuselage.test;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.kwokstudio.fuselage.R;

/**
 * kwokg
 * 2016/12/4
 */

public class MyTextView extends View {


    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta=context.obtainStyledAttributes(R.styleable.test);
        String text=ta.getString(R.styleable.test_text);
        int num=ta.getInteger(R.styleable.test_textInt,-1);

        Log.i("Tag",text+"----<-Text-----Int-->---"+num+this.getClass().getSimpleName());
        ta.recycle();
    }

}
