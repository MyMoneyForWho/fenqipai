package com.fenqipai.fenqipaiandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.adapter.GridImageViewAdapter;
import com.fenqipai.fenqipaiandroid.adapter.HorImgListAdapter;
import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.fragment.ImageShowFragment;
import com.fenqipai.fenqipaiandroid.net.NetClient;
import com.fenqipai.fenqipaiandroid.net.URL;
import com.fenqipai.fenqipaiandroid.util.NetUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.fenqipai.fenqipaiandroid.view.HorizontalListView;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.fenqipai.fenqipaiandroid.view.photo.HackyViewPager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;


/**
 * @Description:图片展示界面
 * @author qianyuhang
 * @date 2016-8-2 下午1:18:24
 */
public class ImageShowActivity extends BaseActivity {

	@ViewInject(R.id.photo_titleBar)
	private TitleBarView titleBar;

	@ViewInject(R.id.pager)
	private HackyViewPager mPager;

	@ViewInject(R.id.hor_list)
	private HorizontalListView hListView;

	@ViewInject(R.id.grid_view)
	private GridView gridView;

	@ViewInject(R.id.num)
	private TextView numTextView;

	private String imgMessage;

	private String auctionId;

	private List<Map<String, Object>> hotList = new ArrayList<Map<String, Object>>();

	private List<Map<String, String>> fileList = new ArrayList<Map<String, String>>();

	private HorImgListAdapter horImgListAdapter;

	private ImagePagerAdapter imagePagerAdapter;

	private GridImageViewAdapter gridImageViewAdapter;

	private View oldView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_show);

		// 注入事件
		ViewUtils.inject(this);

		initView();

		initEvent();

	}

	/**
	 * @Description:初始化视图
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	public void initView() {
		titleBar.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE,
				View.GONE, View.GONE);
		titleBar.setBtnLeft(R.drawable.btn_back_white);
		titleBar.setTitleText(R.string.img_list);
		
		Bundle bundle = getIntent().getExtras();
		auctionId = bundle.getString("auctionId", "71315863-fc1b-417c-a80a-99df25d2d821");
//		auctionId = "71315863-fc1b-417c-a80a-99df25d2d821";
		
		horImgListAdapter = new HorImgListAdapter(application, hotList);

		hListView.setAdapter(horImgListAdapter);
		
		imagePagerAdapter = new ImagePagerAdapter(
				getSupportFragmentManager());
		
		createLoadingDialog(ImageShowActivity.this);

		getAuctionImgList();

	}

	/**
	 * @Description:初始化事件
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	public void initEvent() {
		titleBar.setBtnLeftOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		hListView.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings({ "deprecation", "unchecked" })
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				if (oldView == null) {

					// 第一次点击
					oldView = hListView.getChildAt(hListView.getFirstVisiblePosition());

				}
				// 非第一次点击
				// 把上一次点击的 变化
				TextView tView = (TextView) oldView
						.findViewById(R.id.imgView_title);
				tView.setTextColor(application.getResources().getColor(
						R.color.car_detials_name_text));
				tView.setBackgroundDrawable(application.getResources()
						.getDrawable(R.color.onlookers_background_color));

				tView = (TextView) v.findViewById(R.id.imgView_title);
				tView.setTextColor(application.getResources().getColor(
						R.color.white));
				tView.setBackgroundDrawable(application.getResources()
						.getDrawable(R.color.car_text_orange));
				// 保存oldView
				oldView = v;
				
				
				//切换viewpager
				fileList = (List<Map<String, String>>) hotList.get(arg2).get(
						"mList");

				for (int i = 0; i < fileList.size(); i++) {
					fileList.get(i).put("flag", "0");
				}
				
				fileList.get(0).put("flag", "1");
				
				imagePagerAdapter = new ImagePagerAdapter(
						getSupportFragmentManager());

				mPager.setAdapter(imagePagerAdapter);

				String text = 1 + "/" + fileList.size();
				numTextView.setText(text);
				//切换gridview
				gridImageViewAdapter = new GridImageViewAdapter(
						application, fileList);

				gridView.setAdapter(gridImageViewAdapter);
				
			}
		});
		// 更新下标
		mPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {

				String text = arg0 + 1 + "/" + fileList.size();
				numTextView.setText(text);
//
				for (int i = 0; i < fileList.size(); i++) {
					fileList.get(i).put("flag", "0");
				}
				
				fileList.get(arg0).put("flag", "1");
				
				gridImageViewAdapter.notifyDataSetChanged();
				
				 
				gridView.smoothScrollToPositionFromTop (arg0,0);
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mPager.setCurrentItem(arg2);
			}
		});
		
		
		
	}

	/**
	 * @Description:获取图片列表接口
	 * @author hunaixin
	 * @parame auctionId
	 * @return imgMessage
	 */
	public void getAuctionImgList() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected void onPreExecute() {
				loadingDialogShow("加载中...");
				super.onPreExecute();
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				if (NetUtils.isConnected(getApplicationContext())) {
					imgMessage = NetClient.getAuctionImgList(application,
							auctionId);
					return true;
				}
				return false;
			}

			@SuppressWarnings({ "unchecked" })
			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					numTextView.setVisibility(View.VISIBLE);
					
					parsing(imgMessage);

					horImgListAdapter.setData(hotList);


					fileList = (List<Map<String, String>>) hotList.get(0).get(
							"mList");

					imagePagerAdapter = new ImagePagerAdapter(
							getSupportFragmentManager());

					mPager.setAdapter(imagePagerAdapter);

					String text = 1 + "/" + fileList.size();
					numTextView.setText(text);

					gridImageViewAdapter = new GridImageViewAdapter(
							application, fileList);

					gridView.setAdapter(gridImageViewAdapter);

				} else {
					ToastUtils.show(getApplicationContext(), R.string.no_net,
							ToastUtils.TOAST_SHORT);
				}
				loadingDialogDismiss();
				super.onPostExecute(result);
			}

		});

	}

	/**
	 * @Description:解析字符串
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	public void parsing(String str) {
		try {
			JSONObject jsonObject = new JSONObject(str);
			if (jsonObject.optString("code").equals("success")) {
				JSONArray array = jsonObject.optJSONArray("data");
				for (int i = 0; i < array.length(); i++) {
					JSONObject jObject = array.getJSONObject(i);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", jObject.optString("id"));
					map.put("subjectName", jObject.optString("subjectName"));
					if (i == 0) {
						map.put("flag", "1");
					} else {
						map.put("flag", "0");
					}
					JSONArray jaArray = jObject.optJSONArray("imgList");
					List<Map<String, String>> mList = new ArrayList<Map<String, String>>();
					if (jaArray != null && jaArray.length() > 0) {
						for (int j = 0; j < jaArray.length(); j++) {
							Map<String, String> childMap = new HashMap<String, String>();
							JSONObject mObject = jaArray.getJSONObject(j);
							childMap.put("id", jObject.optString("id"));
							childMap.put("imgId", mObject.optString("id"));
							childMap.put("imagePath",
									mObject.optString("imagePath"));
							if (j == 0) {
								childMap.put("flag", "1");
							} else {
								childMap.put("flag", "0");
							}
							mList.add(childMap);
						}
					}
					map.put("mList", mList);
					hotList.add(map);
				}

			} else {
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Description:ImagePagerAdapter
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public ImagePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.size();
		}

		@Override
		public Fragment getItem(int position) {
			String url = fileList.get(position).get("imagePath");
			url = URL.getURL( URL.FILE_UPLOAD) + url;
			return ImageShowFragment.newInstance(url);
		}

	}

}
