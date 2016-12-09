package com.kwokstudio.fuselage.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.kwokstudio.fuselage.MyApp;

/**
 * 项目杂项帮助类
 * Created by 郭垒 on 2016/10/21.
 */

public class FuselageHelper {

    private static Toast toast;

    //吐司设置
    public static void showToast(String string){
        if (toast==null){
            toast= Toast.makeText(MyApp.getContext(), string, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
        }else{
            toast.setText(string);
        }
        toast.show();
    }

}
