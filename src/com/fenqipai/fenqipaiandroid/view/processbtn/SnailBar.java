package com.fenqipai.fenqipaiandroid.view.processbtn;

import com.fenqipai.fenqipaiandroid.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.SeekBar;

/**
 * Created by cjj on 2015/9/14.
 */
public class SnailBar extends SeekBar {

    public SnailBar(Context context) {
        super(context);
        init();
    }


    public SnailBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SnailBar(Context context, AttributeSet attrs,  int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setMax(100);
        this.setThumbOffset(dip2px(getContext(), 20));
        this.setBackgroundResource(R.drawable.sbg);
        int padding = dip2px(getContext(),(float)20);
        this.setPadding(padding, padding, padding, padding);
        this.setProgressDrawable(getResources().getDrawable(R.drawable.snailbar_define_style));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        AnimationDrawable drawable = (AnimationDrawable)this.getThumb();
        drawable.start();
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
/**
 * 
 *  使用声明文件
 */
//
//    seekBar.setMax(100);
//
//    seekBar.setOnSeekBarChangeListener(seekbarChangeListener);
//
//    handler.postDelayed(new Runnable() {
//        @Override
//        public void run() {
//          handler.sendEmptyMessage(1);
//        }
//    },2000);

    /**
     * 监听
     */

//	  private SeekBar.OnSeekBarChangeListener seekbarChangeListener = new SeekBar.OnSeekBarChangeListener() {
//
//	        @Override
//	        public void onStopTrackingTouch(SeekBar seekBar) {
//	            // TODO Auto-generated method stub
////	            seekbar_status.setText("snailbar stop");
//
//	        }
//
//	        @Override
//	        public void onStartTrackingTouch(SeekBar seekBar) {
//	            // TODO Auto-generated method stub
////	            seekbar_status.setText("snailbar changle");
//	        }
//
//	        @Override
//	        public void onProgressChanged(SeekBar seekBar, int progress,
//	                                      boolean fromUser) {
////	            seekbar_status.setText("snailbar working");
//
//	            Message message = new Message();
//
//	            Bundle bundle = new Bundle();
//
//	            float pro = seekBar.getProgress();
//
//	            float num = seekBar.getMax();
//
//	            float result = (pro / num) * 100;
//
//	            bundle.putFloat("per", result);
//
//	            message.setData(bundle);
//
//	            message.what = 0;
//
//	            handlers.sendMessage(message);
//
//	        }
//	    };

    /**
     * 处理监听
     */
//	  private Handler handlers = new Handler() {
//	        @Override
//	        public void handleMessage(Message msg) {
//	            switch (msg.what)
//	            {
//	                case 0:
////	                    seekbar_percent.setText("snailbar percent :       "
////	                            + msg.getData().getFloat("per") + "%");
//
//	                    break;
//	                case 1:
//
//	                    if (seekBar.getProgress()<100)
//	                    {
//
//	                        if(seekBar.getProgress()<20)
//	                        {
//	                            len += 2;
//	                            handler.sendEmptyMessageDelayed(1,500);
//	                        }else if(seekBar.getProgress()>21&&seekBar.getProgress()<26)
//	                        {
//	                            len += 1;
//	                            handler.sendEmptyMessageDelayed(1,1000);
//	                        }
//	                        else
//	                        {
//	                            len += 2;
//	                            handler.sendEmptyMessageDelayed(1,50);
//	                        }
//	                        seekBar.setProgress(len);
//
//	                    }
//	                    break;
//	            }
//
//	        }
//
//	    };
    

}
