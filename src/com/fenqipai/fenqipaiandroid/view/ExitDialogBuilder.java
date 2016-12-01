package com.fenqipai.fenqipaiandroid.view;

import com.fenqipai.fenqipaiandroid.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by lee on 2014/7/30.
 */
public class ExitDialogBuilder extends Dialog implements DialogInterface {

	public static final int DEFAULT_DURATION = 500;

	private Effectstype type = null;

	private LinearLayout mLinearLayoutView;

	private RelativeLayout mRelativeLayoutView;

	private LinearLayout mLinearLayoutMsgView;

	private LinearLayout mLinearLayoutTopView;

	private FrameLayout mFrameLayoutCustomView;

	private View ExitDialogView;

	private View mDivider;

	private TextView mTitle;

	private TextView mMessage;

	private ImageView mIcon;

	private TextView mButton1;

	private TextView mButton2;

	private int mDuration = -1;

	private static int mOrientation = 1;

	private boolean isCancelable = true;

	private volatile static ExitDialogBuilder instance;

	public ExitDialogBuilder(Context context) {
		super(context);
		init(context);

	}

	public ExitDialogBuilder(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.height = ViewGroup.LayoutParams.MATCH_PARENT;
		params.width = ViewGroup.LayoutParams.MATCH_PARENT;
		getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

	}

	public static ExitDialogBuilder getInstance(Context context) {

		int ort = context.getResources().getConfiguration().orientation;
		if (mOrientation != ort) {
			mOrientation = ort;
			instance = null;
		}

		// if (instance == null || ((Activity) context).isFinishing()) {
		// synchronized (NiftyDialogBuilder.class) {
		// if (instance == null) {
		instance = new ExitDialogBuilder(context, R.style.dialog_untran);
		// }
		// }
		// }
		return instance;

	}

	private void init(Context context) {

		ExitDialogView = View.inflate(context, R.layout.exit_dialog_layout, null);

		mLinearLayoutView = (LinearLayout) ExitDialogView.findViewById(R.id.parentPanel);
		mRelativeLayoutView = (RelativeLayout) ExitDialogView.findViewById(R.id.main);
		mLinearLayoutTopView = (LinearLayout) ExitDialogView.findViewById(R.id.topPanel);
		mLinearLayoutMsgView = (LinearLayout) ExitDialogView.findViewById(R.id.contentPanel);
		mFrameLayoutCustomView = (FrameLayout) ExitDialogView.findViewById(R.id.customPanel);

		mTitle = (TextView) ExitDialogView.findViewById(R.id.alertTitle);
		mMessage = (TextView) ExitDialogView.findViewById(R.id.message);
		mIcon = (ImageView) ExitDialogView.findViewById(R.id.icon);
		mDivider = ExitDialogView.findViewById(R.id.titleDivider);
		mButton1 = (TextView) ExitDialogView.findViewById(R.id.button1);
		mButton2 = (TextView) ExitDialogView.findViewById(R.id.button2);

		setContentView(ExitDialogView);

		this.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface dialogInterface) {

				mLinearLayoutView.setVisibility(View.VISIBLE);
				if (type == null) {
					type = Effectstype.Fadein;
				}
				start(type);

			}
		});
		mRelativeLayoutView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isCancelable)
					dismiss();
			}
		});
	}

	public ExitDialogBuilder withDividerColor(String colorString) {
		mDivider.setBackgroundColor(Color.parseColor(colorString));
		return this;
	}

	public ExitDialogBuilder withTitle(CharSequence title) {
		toggleView(mLinearLayoutTopView, title);
		mTitle.setText(title);
		return this;
	}

	public ExitDialogBuilder withTitleColor(String colorString) {
		mTitle.setTextColor(Color.parseColor(colorString));
		return this;
	}

	public ExitDialogBuilder withMessage(int textResId) {
		toggleView(mLinearLayoutMsgView, textResId);
		mMessage.setText(textResId);
		return this;
	}

	public ExitDialogBuilder withMessage(CharSequence msg) {
		toggleView(mLinearLayoutMsgView, msg);
		mMessage.setText(msg);
		return this;
	}

	public ExitDialogBuilder withMessageColor(String colorString) {
		mMessage.setTextColor(Color.parseColor(colorString));
		return this;
	}

	public ExitDialogBuilder withIcon(int drawableResId) {
		mIcon.setImageResource(drawableResId);
		return this;
	}

	public ExitDialogBuilder withIcon(Drawable icon) {
		mIcon.setImageDrawable(icon);
		return this;
	}

	public ExitDialogBuilder withDuration(int duration) {
		this.mDuration = duration;
		return this;
	}

	public ExitDialogBuilder withEffect(Effectstype type) {
		this.type = type;
		return this;
	}

	public ExitDialogBuilder withButtonDrawable(int resid) {
		mButton1.setBackgroundResource(resid);
		mButton2.setBackgroundResource(resid);
		return this;
	}

	public ExitDialogBuilder withButton1Text(CharSequence text) {
		mButton1.setVisibility(View.VISIBLE);
		mButton1.setText(text);

		return this;
	}

	public ExitDialogBuilder withButton2Text(CharSequence text) {
		mButton2.setVisibility(View.VISIBLE);
		mButton2.setText(text);
		return this;
	}

	public ExitDialogBuilder setButton1Click(View.OnClickListener click) {
		mButton1.setOnClickListener(click);
		return this;
	}

	public ExitDialogBuilder setButton2Click(View.OnClickListener click) {
		mButton2.setOnClickListener(click);
		return this;
	}

	public ExitDialogBuilder setCustomView(int resId, Context context) {
		View customView = View.inflate(context, resId, null);
		if (mFrameLayoutCustomView.getChildCount() > 0) {
			mFrameLayoutCustomView.removeAllViews();
		}
		mFrameLayoutCustomView.addView(customView);
		return this;
	}

	public ExitDialogBuilder setCustomView(View view, Context context) {
		if (mFrameLayoutCustomView.getChildCount() > 0) {
			mFrameLayoutCustomView.removeAllViews();
		}
		mFrameLayoutCustomView.addView(view);

		return this;
	}

	public ExitDialogBuilder isCancelableOnTouchOutside(boolean cancelable) {
		this.isCancelable = cancelable;
		this.setCanceledOnTouchOutside(cancelable);
		return this;
	}

	public ExitDialogBuilder isCancelable(boolean cancelable) {
		this.isCancelable = cancelable;
		this.setCancelable(cancelable);
		return this;
	}

	private void toggleView(View view, Object obj) {
		if (obj == null) {
			view.setVisibility(View.GONE);
		} else {
			view.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void show() {

		super.show();
	}

	private void start(Effectstype type) {
		BaseEffects animator = type.getAnimator();
		if (mDuration != -1) {
			animator.setDuration(Math.abs(mDuration));
		}
		animator.start(mRelativeLayoutView);
	}

	@Override
	public void dismiss() {
		super.dismiss();
		mButton1.setVisibility(View.GONE);
		mButton2.setVisibility(View.GONE);
	}
}
