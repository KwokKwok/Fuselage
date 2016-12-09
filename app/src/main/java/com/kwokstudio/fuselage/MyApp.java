package com.kwokstudio.fuselage;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.kwokstudio.fuselage.widget.SQLHelper;

/**
 * Created by 郭垒 on 2016/11/15.
 */

public class MyApp extends Application {
    private static Context context;
    private static MyApp application;

    private SQLHelper sqlHelper;//别人的
    @Override
    public void onCreate() {
        super.onCreate();
        //获取一个全局Context;
        context=getApplicationContext();
        application=this;
        initFresco();
    }


    public static Context getContext(){
        return context;
    }

    public static MyApp getApp(){
        return application;
    }

    private void initFresco() {
        Fresco.initialize(this);
    }



    /** 获取数据库Helper */
    public SQLHelper getSQLHelper() {
        if (sqlHelper == null)
            sqlHelper = new SQLHelper(application);
        return sqlHelper;
    }

    /** 摧毁应用进程时候调用 */
    public void onTerminate() {
        if (sqlHelper != null)
            sqlHelper.close();
        super.onTerminate();
    }

    public void clearAppCache() {
    }
}
