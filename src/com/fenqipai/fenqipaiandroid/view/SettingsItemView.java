package com.fenqipai.fenqipaiandroid.view;

import com.fenqipai.fenqipaiandroid.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 设置页面菜单栏
 * 
 * @name SettingsBarView
 * @author wangzhonghan
 * @date 2015年9月16日
 * @modify
 * @modifyDate 2015年9月16日
 * @modifyContent
 */
public class SettingsItemView extends RelativeLayout {


	private Context mContext;
	
	private TextView textLeft;
	private ImageButton btnRight;
	private TextView textRight;
	private TextView textCenter;

	public SettingsItemView(Context context) {
		super(context);
		mContext = context;
		initView();
	}

	public SettingsItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}

		
	
	/**
	 * 初始化视图
	 * 
	 * @settings initView
	 * @author zhaoqingyang
	 */
	private void initView() {
		LayoutInflater.from(mContext).inflate(R.layout.activity_settings_item_view, this);
	
		textLeft = (TextView) findViewById(R.id.settings_txt_left);
		btnRight = (ImageButton) findViewById(R.id.settings_btn_right);
		textRight = (TextView) findViewById(R.id.settings_txt_right);
		textCenter = (TextView) findViewById(R.id.settings_txt);
	}

	/**
	 * 必须设置(设置控件隐藏还是显示)
	 * 
	 * @settings setCommonsettings
	 * @author zhaoqingyang
	 * @param leftBtnVisibility
	 *            左侧图标按钮
	 * @param leftTextVisibility
	 *            左侧文字按钮
	 * @param centerTextVisibility
	 *            中间文本框
	 * @param rightBtnVisibility
	 *            右侧图标按钮
	 * @param rightTextVisibility
	 *            右侧文字按钮
	 */
	public void setCommonsettings( int leftTextVisibility, int centerTextVisibility,
			int rightBtnVisibility, int rightTextVisibility) {
		
		textLeft.setVisibility(leftTextVisibility);
		textCenter.setVisibility(centerTextVisibility);
		btnRight.setVisibility(rightBtnVisibility);
		textRight.setVisibility(rightTextVisibility);

	}

	

	public void setTextLeft(int txtRes) {
		textLeft.setText(txtRes);
	}

	public void setBtnRight(int resid) {
		btnRight.setImageResource(resid);
	}

	public void setBtnRight(int resid, int textResId) {
		Drawable nav_up = getResources().getDrawable(resid);
		nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
		textRight.setCompoundDrawablePadding(10);
		textRight.setCompoundDrawables(nav_up, null, null, null);
		textRight.setText(textResId);
	}

	public void setTextRight(String txtRes) {
		textRight.setText(txtRes);
	}

	public void setsettingsText(int resid) {
		textCenter.setText(resid);
	}

	public void setsettingsText(String txt) {
		textCenter.setText(txt);
	}

	public void setsettingsDrawableLeft(int resid, int i) {
		textCenter.setCompoundDrawablePadding(i);
		textCenter.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(resid), null);
	}

	
	public void setTextLeftOnclickListener(OnClickListener listener) {
		textLeft.setOnClickListener(listener);
	}

	public void setBtnRightOnclickListener(OnClickListener listener) {
		btnRight.setOnClickListener(listener);
	}

	public void setTextRightOnclickListener(OnClickListener listener) {
		textRight.setOnClickListener(listener);
	}

	public void setTextsettingsOnclickListener(OnClickListener listener) {
		textCenter.setOnClickListener(listener);
	}

}




