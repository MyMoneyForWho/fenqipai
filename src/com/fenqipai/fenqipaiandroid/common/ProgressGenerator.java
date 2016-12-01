package com.fenqipai.fenqipaiandroid.common;

import java.util.Random;

import com.fenqipai.fenqipaiandroid.view.processbtn.ProcessButton;

import android.os.Handler;

/**
 * 等待按钮的构造器
 * 
 * @name ProgressGenerator
 * @author zhaoqingyang
 * @date 2015年9月15日
 * @modify
 * @modifyDate 2015年9月15日
 * @modifyContent
 */
public class ProgressGenerator {

	public interface OnCompleteListener {

		public void onComplete();
	}

	private OnCompleteListener mListener;
	private int mProgress;

	public ProgressGenerator(OnCompleteListener listener) {
		mListener = listener;
	}

	public void start(final ProcessButton button) {
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mProgress += 20;
				button.setProgress(mProgress);
				if (mProgress < 100) {
					handler.postDelayed(this, generateDelay());
				} else {
					mListener.onComplete();
				}
			}
		}, generateDelay());
	}

	private Random random = new Random();

	private int generateDelay() {
		return random.nextInt(1000);
	}
}
