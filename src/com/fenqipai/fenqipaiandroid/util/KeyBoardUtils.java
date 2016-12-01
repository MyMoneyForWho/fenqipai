package com.fenqipai.fenqipaiandroid.util;

import com.fenqipai.fenqipaiandroid.view.ContainsEmojiEditText;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

/**
 * 
 * 打开或关闭键盘工具类
 * 
 * @name KeyBoardUtil
 * @author zhaoqingyang
 * @date 2015年9月11日
 * @modify
 * @modifyDate 2015年9月11日
 * @modifyContent
 */
public class KeyBoardUtils {

	private KeyBoardUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("KeyBoardUtils cannot be instantiated");
	}

	/**
	 * 打卡软键盘
	 * 
	 * @param mEditText
	 *            输入框
	 * @param mContext
	 *            上下文
	 */
	public static void openKeybord(ContainsEmojiEditText mEditText, Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	/**
	 * 关闭软键盘
	 * 
	 * @param mEditText
	 *            输入框
	 * @param mContext
	 *            上下文
	 */
	public static void closeKeybord(ContainsEmojiEditText mEditText, Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
	}

	/**
	 * 关闭软键盘
	 * 
	 * @param mTextView
	 * 
	 * @param mContext
	 *            上下文
	 */
	public static void closeKeybord(TextView mTextView, Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mTextView.getWindowToken(), 0);
	}
}
