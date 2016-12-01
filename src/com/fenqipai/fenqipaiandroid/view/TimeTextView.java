package com.fenqipai.fenqipaiandroid.view;

import java.util.Timer;
import java.util.TimerTask;

import com.fenqipai.fenqipaiandroid.R;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 倒计时 文本
 * @author hunaixin
 */
public class TimeTextView extends LinearLayout {
    private long mday, mhour, mmin, msecond;//天，小时，分钟，秒
    private boolean run = false; //是否启动了
    Timer timer = new Timer();
    TextView Vdays, Vhour, Vmin, Vseconds,tvEnd,day,hour,min,sec;

    public TimeTextView(Context context) {
        super(context);
        iniUI(context);
    }

    public TimeTextView(Context context, AttributeSet attrs) {

        super(context, attrs);
        iniUI(context);
    }

    public void iniUI(Context context) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View myView = mInflater.inflate(R.layout.view_time_textviews, null);
        
        Vdays = (TextView) myView.findViewById(R.id.tv_days);
        Vhour = (TextView) myView.findViewById(R.id.tv_hours);
        Vmin = (TextView) myView.findViewById(R.id.tv_minutes);
        Vseconds = (TextView) myView.findViewById(R.id.tv_seconds);
        tvEnd = (TextView) myView.findViewById(R.id.tv_end);
        day = (TextView) myView.findViewById(R.id.day);
        hour = (TextView) myView.findViewById(R.id.hours);
        min = (TextView) myView.findViewById(R.id.min);
        sec = (TextView) myView.findViewById(R.id.sec);
        addView(myView);
    }

    public TimeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        iniUI(context);
    }

    private Handler mHandler = new Handler() {

    };

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    public void start() {
        if (!isRun()) {
            setRun(true);
            timer.schedule(task, 0, 1000);
        }
    }

    /**
     * 根据传进来的时间差 为textview 赋值
     *
     * @param duration
     */
    public void setTimes(long duration) {
    	
    	Long one_seconds = 1000L;
    	Long one_minutes = one_seconds * 60;
    	Long one_hours = one_minutes * 60;
    	Long oneday = one_hours * 24;
    	
    	mday = duration / oneday;
    	mhour = (duration - Integer.parseInt(String.valueOf(mday)) * oneday) / one_hours;
    	mmin = (duration - Integer.parseInt(String.valueOf(mday)) * oneday - Integer.parseInt(String.valueOf(mhour)) * one_hours) / one_minutes;
    	msecond = (duration - Integer.parseInt(String.valueOf(mday)) * oneday - Integer.parseInt(String.valueOf(mhour)) * one_hours - Integer.parseInt(String.valueOf(mmin)) * one_minutes) / one_seconds;
        
       String finalTime = mday +":" +mhour + ":" + mmin + ":" + msecond;
    }

    /**
     * 倒计时计算
     */
    private void ComputeTime() {
        msecond--;
        if (msecond < 0) {
            mmin--;
            msecond = 59;
            if (mmin < 0) {
                mmin = 59;
                mhour--;
                if (mhour < 0) {
                    // 倒计时结束
                    mhour = 24;
                    mday--;
                }
            }
        }
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {

            mHandler.post(new Runnable() {      // UI thread
                @Override
                public void run() {
                    run = true;
                    ComputeTime();
                    if (mday < 0) {
//                      setVisibility(View.GONE);
                        Vdays.setVisibility(View.GONE);
                        Vhour.setVisibility(View.GONE);
                        Vmin.setVisibility(View.GONE);
                        Vseconds.setVisibility(View.GONE);
                        day.setVisibility(View.GONE);
                        hour.setVisibility(View.GONE);
                        min.setVisibility(View.GONE);
                        sec.setVisibility(View.GONE);
                        tvEnd.setVisibility(View.VISIBLE);
                        setRun(false);
                    }
                    Vdays.setText(mday < 10 ? ("0" + mday) : mday + "");
                    Vhour.setText(mhour < 10 ? ("0" + mhour) : mhour + "");
                    Vseconds.setText(msecond < 10 ? ("0" + msecond) : msecond + "");
                    Vmin.setText(mmin < 10 ? ("0" + mmin) : mmin + "");
                }
            });
        }
    };
}