package com.fenqipai.fenqipaiandroid.fragment;

import com.fenqipai.fenqipaiandroid.LocationActivity;
import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.base.BaseFragment;
import com.fenqipai.fenqipaiandroid.model.MyLocation;
import com.lidroid.xutils.ViewUtils;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @Description:看车位置信息页面
 * @author hunaixin
 * @date 2016年11月25日 下午2:16:12
 */
public class LocationMessageFragment extends BaseFragment {

	@SuppressWarnings("unused")
	private LocationActivity activity;

	private MyLocation myLocation;

	private TextView title, tel, addr;

	public static LocationMessageFragment newInstance(Context mContext, MyLocation myLocation) {
		final LocationMessageFragment f = new LocationMessageFragment();

		final Bundle args = new Bundle();
		args.putSerializable("myLocation", myLocation);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = (LocationActivity) getActivity();

		myLocation = (MyLocation) (getArguments() != null ? getArguments().getSerializable("myLocation") : null);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.item_location_list, container, false);

		title = (TextView) v.findViewById(R.id.location_title);
		tel = (TextView) v.findViewById(R.id.location_name);
		addr = (TextView) v.findViewById(R.id.location_addr);
		// 注入view和事件
		ViewUtils.inject(this, v);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		title.setText(myLocation.getTitle());
		tel.setText(myLocation.getTel());
		addr.setText(myLocation.getAddress());

	}

}
