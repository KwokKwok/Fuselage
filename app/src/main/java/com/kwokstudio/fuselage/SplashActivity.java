package com.kwokstudio.fuselage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    private static final int MAIN_OPEN=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //添加一个动画
        ImageView img = (ImageView) findViewById(R.id.splash_img);
        ObjectAnimator animX=ObjectAnimator.ofFloat(img,"scaleX",1f,1.1f);
        ObjectAnimator animY=ObjectAnimator.ofFloat(img,"scaleY",1f,1.1f);
        AnimatorSet set=new AnimatorSet();
        set.play(animX).with(animY);
        set.setDuration(2000);
        set.start();
        //设置动画结束时开启MainActivity
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startActivityForResult(new Intent(SplashActivity.this,MainActivity.class),MAIN_OPEN);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

    }

    //开启MainActivity后finish(),可扩展
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==MAIN_OPEN){
            finish();
        }
    }

    @Override
    public void onBackPressed() {
    }
}
