package com.kwokstudio.fuselage.ui.bottomNav;


import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.kwokstudio.fuselage.R;
import com.kwokstudio.fuselage.utils.FuselageHelper;

import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChildCalendarFragment extends Fragment {


    private Chronometer chronometer;
    private CountDownTimer countDownTimer;

    public ChildCalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    private void initView(View v) {
        chronometer = (Chronometer) v.findViewById(R.id.time_chronometer);
        final Button data = (Button) v.findViewById(R.id.time_data);
        final Button time = (Button) v.findViewById(R.id.time_time);
        Button startChronometer = (Button) v.findViewById(R.id.start_chronometer);
        Button endChronometer = (Button) v.findViewById(R.id.end_chronometer);
        chronometer.setFormat("已过去：%s");

        v.findViewById(R.id.go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setAction(".singleInstance.test"));
            }
        });

        //开始计时按钮
        startChronometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
            }
        });

        //停止计时按钮
        endChronometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer.stop();
                chronometer.setBase(SystemClock.elapsedRealtime());
            }
        });

        //时间格式
//        final SimpleDateFormat formatDate=new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
//        final SimpleDateFormat formatTime=new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
        //获取一个布局填充器
        final LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //日期选择点击事件
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePicker datePicker = (DatePicker) layoutInflater.inflate(R.layout.date_picker, null);
                new AlertDialog.Builder(getActivity()).setCancelable(true)
                        .setView(datePicker).setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        data.setText(datePicker.getYear()+"-"+(datePicker.getMonth()+1)+"-"+datePicker.getDayOfMonth());
                    }
                }).show();

            }
        });




        //时间选择点击事件
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        if (countDownTimer!=null){
                            countDownTimer.cancel();
                        }
                        final Calendar t = Calendar.getInstance(Locale.CHINA);
                        final long countdown = (i - t.get(Calendar.HOUR_OF_DAY)) * 1000 * 60 * 60 + (i1 - t.get(Calendar.MINUTE)) * 1000 * 60+(60-t.get(Calendar.SECOND)*1000);
                        if (countdown>0){
                            countDownTimer = new CountDownTimer(countdown, 1000) {
                                @Override
                                public void onTick(long l) {
                                    time.setText(DateFormat.format("HH:mm:ss",l-8*1000*60*60));
                                    Log.i("Tag",l+"----------"+this.getClass().getSimpleName());
                                }
                                @Override
                                public void onFinish() {
                                    time.setText("计时结束");
                                }
                      }.start();
                        }else{
                            time.setText("倒计时设置");
                            FuselageHelper.showToast("时间错误，请重新设置");
                        }
                    }
                }, 24, 60, true).show();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i("Tag","2"+"----------"+this.getClass().getSimpleName());
        if (isHidden()){
            Log.i("Tag","1"+"----------"+this.getClass().getSimpleName());
        }
    }
}
