package com.fenqipai.fenqipaiandroid.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 禁止滑动的listview
 * 
 * @name NoScrollListView
 * @author zhaoqingyang
 * @date 2015年10月30日
 * @modify
 * @modifyDate 2015年10月30日
 * @modifyContent
 */
public class NoScrollListView extends ListView {
	public NoScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, mExpandSpec);
	}
}
