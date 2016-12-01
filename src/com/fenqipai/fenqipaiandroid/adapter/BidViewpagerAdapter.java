package com.fenqipai.fenqipaiandroid.adapter;

import java.util.List;
import com.fenqipai.fenqipaiandroid.R;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 出价viewpager的适配器
 * @author hunaixin
 */
public class BidViewpagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> list;
	private Context mContext;

	public BidViewpagerAdapter(FragmentManager fm, List<Fragment> list) {
		super(fm);
		this.list = list;
	}

	@Override
	public Fragment getItem(int position) {
		return list.get(position);
	}

	@Override
	public int getCount() {
		return list.size();
	}

}