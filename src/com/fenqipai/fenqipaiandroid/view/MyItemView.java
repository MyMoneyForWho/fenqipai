package com.fenqipai.fenqipaiandroid.view;

import com.fenqipai.fenqipaiandroid.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * 我的页面Item view
 * 
 * @name MyItemView
 * @author 
 * @date 
 * @modify
 * @modifyDate 
 * @modifyContent
 */
public class MyItemView extends RelativeLayout {

	private Context mContext;

	private TextView myItemName;

	private TextView myItemCount;

	private ImageView myItemDetail;

	private ToggleButton myItemTb;

	// 0:文件名 数量 图标模式 1:文件名 图标模式 2:文件名 选择开关模式
	private int itemStatus = 0;

	public MyItemView(Context context) {
		super(context);
		mContext = context;

		initView();
	}

	public MyItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	@SuppressLint("Recycle")
	public MyItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mContext = context;

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyItemView, defStyle, 0);

		itemStatus = a.getInteger(R.styleable.MyItemView_item_status, 0);

		initView();
	}

	/**
	 * 初始化视图
	 * 
	 * @title initView
	 * @author zhaoQingyang
	 */
	private void initView() {
		LayoutInflater.from(mContext).inflate(R.layout.my_item_view, this);

		myItemName = (TextView) findViewById(R.id.my_item_names);

		myItemCount = (TextView) findViewById(R.id.my_item_count);

		myItemDetail = (ImageView) findViewById(R.id.my_item_detail);

		myItemTb = (ToggleButton) findViewById(R.id.my_item_tb);

		switch (itemStatus) {
		case 0:
			myItemTb.setVisibility(View.GONE);
			break;
		case 1:
			myItemTb.setVisibility(View.GONE);
			myItemCount.setVisibility(View.GONE);
			break;

		case 2:
			myItemCount.setVisibility(View.GONE);
			myItemDetail.setVisibility(View.GONE);
			break;
		}

	}

	/**
	 * 设置Item字体颜色
	 * 
	 * @title setItemColor
	 * @author liuchengbao
	 * @param resid
	 */
	public void setItemColor(int resid) {
		myItemName.setTextColor(resid);
	}

	/**
	 * 设置item名称
	 * 
	 * @title setItemName
	 * @author zhaoqingyang
	 * @param str
	 */
	public void setItemName(String str) {
		myItemName.setText(str);
	}

	/**
	 * 设置item名称
	 * 
	 * @title setItemName
	 * @author zhaoqingyang
	 * @param str
	 */
	public void setItemName(int resid) {
		myItemName.setText(resid);
	}
	
	/**
	 * 设置item图标
	 * 
	 * @title setItemImg
	 * @author zhaoqingyang
	 * @param str
	 */
	public void setItemImg(int resid) {
		myItemDetail.setBackgroundResource(resid);
	}

	/**
	 * 设置item count
	 * 
	 * @title setItemCount
	 * @author zhaoqingyang
	 * @param count
	 */
	public void setItemCount(String count) {
		myItemCount.setText(count);
	}

	/**
	 * 设置按钮开关
	 * 
	 * @title setItemTb
	 * @author zhaoQingyang
	 * @param checked
	 */
	public void setItemTb(boolean checked) {
		myItemTb.setChecked(checked);
	}

	public TextView getMyItemName() {
		return myItemName;
	}

	public TextView getMyItemCount() {
		return myItemCount;
	}

	public ImageView getMyItemDetail() {
		return myItemDetail;
	}

	public ToggleButton getMyItemTb() {
		return myItemTb;
	}

	public int getItemStatus() {
		return itemStatus;
	}

	public void setMyItemDetail(ImageView myItemDetail) {
		this.myItemDetail = myItemDetail;
	}
	
	

}
