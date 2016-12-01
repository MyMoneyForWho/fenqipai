package com.fenqipai.fenqipaiandroid.base;

import com.fenqipai.fenqipaiandroid.R;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.widget.TextView;

/**
 * 所有fragment的基类
 * 
 * @name BaseFragment
 * @author zhaoqingyang
 * @Description TODO
 * @date 2015年9月21日
 * @modify
 * @modifyDate 2015年9月21日
 * @modifyContent
 */
public class BaseFragment extends Fragment {

	public BaseApplication application;

	// false 是刷新操作 true 是加载操作
	protected boolean isLoad = true;

	// 加载索引
	protected int loadIndex = 0;

	public TextView emptyView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		application = (BaseApplication) getActivity().getApplication();
		createEmptyView();
	}

	private void createEmptyView() {
		emptyView = new TextView(getActivity());

		emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		emptyView.setText("暂无数据");

		emptyView.setGravity(Gravity.CENTER_HORIZONTAL);

		emptyView.setTextColor(getResources().getColor(R.color.hint_color));
	}

	/**
	 * 通过Class跳转界面
	 */
	protected void startActivity(Class<?> cls) {

		startActivity(cls, null);
	}

	/**
	 * 含有Bundle通过Class跳转界面
	 */
	protected void startActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();

		intent.setClass(getActivity(), cls);

		if (bundle != null) {
			intent.putExtras(bundle);
		}

		startActivity(intent);
	}

	/**
	 * 搜索
	 * 
	 */
	public void search(String text) {

	}
}
