package com.fenqipai.fenqipaiandroid;

import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.fragment.ImageDetailFragment;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.fenqipai.fenqipaiandroid.view.photo.HackyViewPager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 图片浏览类
 * 
 * @name ImagePagerActivity
 * @author zhaoqingyang
 */
public class ImagePagerActivity extends BaseActivity {

	private static final String STATE_POSITION = "STATE_POSITION";
	public static final String EXTRA_IMAGE_INDEX = "image_index";
	public static final String EXTRA_IMAGE_URLS = "image_urls";
	public static final String EXTRA_IMAGE_ISEDIT = "isEdit";
	@ViewInject(R.id.photo_titleBar)
	private TitleBarView titleBar;

	@ViewInject(R.id.pager)
	private HackyViewPager mPager;

	@ViewInject(R.id.indicator)
	private TextView indicator;
    
	private int pagerPosition;
	private String[] urls;
	private boolean isEdit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_detail_pager);
		
		pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
		urls = getIntent().getStringArrayExtra(EXTRA_IMAGE_URLS);
		isEdit=getIntent().getBooleanExtra(EXTRA_IMAGE_ISEDIT, false);
		
		// 注入事件
		ViewUtils.inject(this);

		initView();

		initEvent();

		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

	}

	/**
	 * 初始化事件
	 * 
	 * @title initEvent
	 * @author zhaoqingyang
	 */
	private void initEvent() {

		// 更新下标
		mPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				
				String text = arg0 + 1 + "/" + urls.length;
				titleBar.setTitleText(text);
				indicator.setText(text);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		titleBar.setBtnLeftOnclickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		titleBar.setTextRightOnclickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setOnResult();
			}
		});
	}
	
	/**
	 * 
	 * 返回值
	 */
	private void setOnResult() {
		Intent in = new Intent();

		Bundle bundle = new Bundle();

		bundle.putInt("position", mPager.getCurrentItem());
		

		in.putExtras(bundle);

		setResult(5, in);

		finish();
	}
	
	/**
	 * 初始化视图
	 * 
	 * @title initView
	 * @author zhaoqingyang
	 */
	private void initView() {
		String text = (pagerPosition + 1) + "/" + urls.length;
		if(isEdit){
			titleBar.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE, View.VISIBLE);
			titleBar.setBtnLeft(R.drawable.btn_back);
			titleBar.setTitleText(text);
			titleBar.setTextRight(R.string.delete);
		}else{
			titleBar.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
			titleBar.setBtnLeft(R.drawable.btn_back);
			titleBar.setTitleText(R.string.img_list);
		}
		ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
		mPager.setAdapter(mAdapter);

		indicator.setText(text);

		mPager.setCurrentItem(pagerPosition);

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public String[] fileList;

		public ImagePagerAdapter(FragmentManager fm, String[] fileList) {
			super(fm);
			this.fileList = fileList;
		}

		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.length;
		}

		@Override
		public Fragment getItem(int position) {
			String url = fileList[position];
			if(!url.startsWith("http")){
				url="file://"+url;
			}
			return ImageDetailFragment.newInstance(url);
		}

	}
}