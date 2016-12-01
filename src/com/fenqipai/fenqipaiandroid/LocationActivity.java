package com.fenqipai.fenqipaiandroid;

import java.util.ArrayList;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.fragment.LocationMessageFragment;
import com.fenqipai.fenqipaiandroid.model.MyLocation;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * @Description:定位位置界面
 * @author hunaixin
 * @date 2016年11月25日 上午11:48:28
 */
public class LocationActivity extends BaseActivity {
	// 定位必备监听
	// public LocationClient mLocationClient;
	// public BDLocationListener myListener;
	//
	// 标题栏
	@ViewInject(R.id.location_titleBar)
	private TitleBarView titleBarView;

	// 地图
	@ViewInject(R.id.map_view)
	private MapView mMapView;

	private BaiduMap mBaiduMap;

	// 横滑位置信息
	@ViewInject(R.id.location_viewpager)
	private ViewPager viewpager;

	// 位置指引
	@ViewInject(R.id.location_garden)
	private LinearLayout garden;

	// 定位相关
	LocationClient mLocClient;

	private ImageView[] indicators;

	boolean isFirstLoc = true; // 是否首次定位
	public MyLocationListenner myListener = new MyLocationListenner();

	// private CommonAdapter<MyLocation> adapter;

	private List<MyLocation> list = new ArrayList<MyLocation>();

	BitmapDescriptor mCurrentMarker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);

		// 注入view和事件
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
		titleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
		titleBarView.setBtnLeft(R.drawable.btn_back_red);
		titleBarView.setTitleText("我的位置");

		mMapView = (MapView) findViewById(R.id.map_view);

		// myListener = new MyLocationListener();
		// mLocationClient = new LocationClient(getApplicationContext()); //
		// 声明LocationClient类
		// mLocationClient.registerLocationListener(myListener); // 注册监听函数
		// initLocation();
		// mLocationClient.start();
		// mMapView = new MapView(this, new BaiduMapOptions());

		// 地图初始化
		mBaiduMap = mMapView.getMap();

		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();

		getLocation();
		
	

	}

	/**
	 * @Description:初始化事件
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public void initEvent() {
		titleBarView.setBtnLeftOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int pos) {
				setIndicator(pos);
				
				getMyLocation(pos);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	// 定位初始化设置
	// private void initLocation() {
	//
	// LocationClientOption option = new LocationClientOption();
	// option.setLocationMode(LocationMode.Hight_Accuracy);//
	// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
	// // option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
	// option.setCoorType("gcj02");// 可选，默认gcj02，设置返回的定位结果坐标系
	// int span = 1;
	// option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
	// option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
	// option.setOpenGps(true);// 可选，默认false,设置是否使用gps
	// option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
	// option.setIsNeedLocationDescribe(true);//
	// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
	// option.setIsNeedLocationPoiList(true);//
	// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
	// option.setIgnoreKillProcess(false);//
	// 可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
	// option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
	// option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
	// mLocationClient.setLocOption(option);
	// }

	// 定位获取信息
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nerror code : ");
			sb.append(location.getAltitude());
			sb.append("\nerror code : ");
			sb.append(location.getLatitude());
			sb.append("\nerror code : ");
			sb.append(location.getLongitude());
			sb.append("\nerror code : ");
			sb.append(location.getCity());
			sb.append("\nerror code : ");
			sb.append(location.getCountry());
			sb.append("\nerror code : ");
			sb.append(location.getDerect());
			sb.append("\nerror code : ");
			sb.append(location.getProvince());
			sb.append("\nerror code : ");
			sb.append(location.getLocationDescribe());

			// 初始化定位坐标，不再改变的坐标
			@SuppressWarnings("unused")
			String xlatitude = String.valueOf(location.getLatitude());
			@SuppressWarnings("unused")
			String ylontitude = String.valueOf(location.getLongitude());

		}
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
				MapStatus.Builder builder = new MapStatus.Builder();
				builder.target(ll).zoom(18.0f);
				mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
			
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	/**
	 * @Description:获取位置接口
	 * @author hunaixin
	 * @parame 
	 * @return list
	 */
	public void getLocation() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				list = application.getAllSeeCarPoint();

				return true;
			}

			@SuppressLint("InflateParams")
			@Override
			protected void onPostExecute(Boolean result) {

				if (result) {
					ImagePagerAdapter adapter = new ImagePagerAdapter(getSupportFragmentManager());

					viewpager.setAdapter(adapter);
					indicators = new ImageView[list.size()];
					for (int i = 0; i < indicators.length; i++) {
						View view = LayoutInflater.from(application).inflate(R.layout.view_cycle_viewpager_indicator,
								null);
						indicators[i] = (ImageView) view.findViewById(R.id.image_indicator);
						garden.addView(view);
					}
					setIndicator(0);
					getMyLocation(0);
				}

				super.onPostExecute(result);
			}

		});
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public ImagePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {

			return list == null ? 0 : list.size();
		}

		@Override
		public Fragment getItem(int position) {
			return LocationMessageFragment.newInstance(application, list.get(position));
		}

	}

	/**
	 * 设置指示器
	 * @param selectedPosition
	 *          
	 */
	private void setIndicator(int selectedPosition) {
		for (int i = 0; i < indicators.length; i++) {
			indicators[i].setBackgroundResource(R.drawable.grden_kong);
		}
		if (indicators.length > selectedPosition)
			indicators[selectedPosition].setBackgroundResource(R.drawable.greng_man);
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}
	
	private double mlatitude ,mlongitude;
	private LatLng point;
	
	/**
	 * 获取公司坐标并显示
	 * @param pos
	 */
	public void getMyLocation(int pos){
		mlatitude = Double.parseDouble(list.get(pos).getLatitude());
		mlongitude = Double.parseDouble(list.get(pos).getLongitude());
		point = new LatLng(mlatitude,mlongitude);
		BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_marker);
		OverlayOptions options = new MarkerOptions().icon(icon).position(point);
		mBaiduMap.addOverlay(options);
		//GeoPoint geoPoint = new GeoPoint((int)(x * 1E6), (int)(y*1E6));
		
		//设定中心点坐标 
		//LatLng cenpt = new LatLng(30.663791,104.07281); 
		//定义地图状态
		MapStatus mMapStatus = new MapStatus.Builder()
		.target(point)
		.zoom(12)
		.build();
		//定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		//改变地图状态
		mBaiduMap.setMapStatus(mMapStatusUpdate);
	}


}
