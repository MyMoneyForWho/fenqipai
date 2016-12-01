package com.fenqipai.fenqipaiandroid.view;

import java.util.ArrayList;
import java.util.List;

import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.adapter.GridPricedadapter;
import com.fenqipai.fenqipaiandroid.model.SingleCarDetails;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


/**
 * @Description:从底部弹出或滑出选择菜单或窗口
 * @author:马思远   
 */
public class SelectPicPopupWindow extends PopupWindow {

	private View mMenuView;
	
    private HorizontalListView mListView;
    
    private LinearLayout priceLayout,confirmLayout;
    
    private ImageView backImg, closeImg;
    
    private TextView confirmPrice, bidPrice, nowPrice;
    
    private EditText freeBidEt;
    
    private Button freeBidBtn;
    
    
	@SuppressLint("InflateParams")
	public SelectPicPopupWindow(Context context, OnClickListener itemsOnClick, List<SingleCarDetails> carList) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.popupwindows_select, null);

		
		// 初始化组件
		mListView = (HorizontalListView) mMenuView.findViewById(R.id.price_gridView);
		priceLayout = (LinearLayout) mMenuView.findViewById(R.id.pop_layout);
		confirmLayout = (LinearLayout) mMenuView.findViewById(R.id.pop_confirm_layout);
		backImg = (ImageView) mMenuView.findViewById(R.id.back_to_price);
		closeImg = (ImageView) mMenuView.findViewById(R.id.price_pop_close);
		confirmPrice = (TextView) mMenuView.findViewById(R.id.pop_confirm_price);
		bidPrice = (TextView) mMenuView.findViewById(R.id.pop_bid_price);
		freeBidEt = (EditText) mMenuView.findViewById(R.id.pop_free_bid);
		freeBidBtn = (Button) mMenuView.findViewById(R.id.btn_price);
		nowPrice = (TextView) mMenuView.findViewById(R.id.pop_nowprice);
		
		// GridView
		String [] addPrices = carList.get(0).getAddPrice().split(",");
		
		List<String> priceList = new ArrayList<String>();
		
		for (int i = 0; i < addPrices.length; i++) {
			priceList.add(addPrices[i]);
		}
		
		mListView.setAdapter(new GridPricedadapter(context, priceList));

		// 设置按钮监听
		backImg.setOnClickListener(itemsOnClick);
		closeImg.setOnClickListener(itemsOnClick);
		


		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x80000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			@Override
			@SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

	}
	
	
	public void noticefiansetChange(){
		
		
		
	}
    
	

	public LinearLayout getPriceLayout() {
		return priceLayout;
	}


	public LinearLayout getConfirmLayout() {
		return confirmLayout;
	}


	public void setPriceLayoutVisibility(int visible){
		
		priceLayout.setVisibility(visible);
	}
	
	public void setConfirmLayoutVisibility(int visible){
		
		confirmLayout.setVisibility(visible);
	}



	public Button getFreeBidBtn() {
		return freeBidBtn;
	}



	public void setFreeBidBtn(Button freeBidBtn) {
		this.freeBidBtn = freeBidBtn;
	}



	public HorizontalListView getmListView() {
		return mListView;
	}



	public void setmListView(HorizontalListView mListView) {
		this.mListView = mListView;
	}



	public EditText getFreeBidEt() {
		return freeBidEt;
	}



	public void setFreeBidEt(EditText freeBidEt) {
		this.freeBidEt = freeBidEt;
	}



	public TextView getConfirmPrice() {
		return confirmPrice;
	}



	public void setConfirmPrice(TextView confirmPrice) {
		this.confirmPrice = confirmPrice;
	}



	public TextView getBidPrice() {
		return bidPrice;
	}



	public void setBidPrice(TextView bidPrice) {
		this.bidPrice = bidPrice;
	}



	public TextView getNowPrice() {
		return nowPrice;
	}



	public void setNowPrice(TextView nowPrice) {
		this.nowPrice = nowPrice;
	}

    




	
	

}
