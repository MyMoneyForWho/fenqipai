package com.fenqipai.fenqipaiandroid.view;

import com.fenqipai.fenqipaiandroid.util.ToastUtils;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * 查看EditText是否包含emoji表情
 * 
 * @name ContainsEmojiEditText
 * @author zhaoqingyang
 * @date 2015年12月14日
 * @modify
 * @modifyDate 2015年12月14日
 * @modifyContent
 */
public class ContainsEmojiEditText extends EditText {
	private String inputAfterText; // 是否重置了EditText的内容
	private boolean resetText = false;
	private Context mContext;

	public ContainsEmojiEditText(Context context) {
		super(context);
		this.mContext = context;
		initEditText();
	}

	public ContainsEmojiEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initEditText();
	}

	public ContainsEmojiEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
		initEditText();
	} // 初始化edittext 控件

	private void initEditText() {
		addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int before, int count) {
				if (!resetText) {
					// 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
					// inputAfterText也就改变了，那么表情过滤就失败了
					inputAfterText = s.toString();
				}

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!resetText) {
					if (containsEmoji(s.toString())) {
						resetText = true;
						ToastUtils.show(mContext, "不支持输入Emoji表情符号", ToastUtils.TOAST_SHORT);
						setText(inputAfterText);
						CharSequence text = getText();
						if (text instanceof Spannable) {
							Spannable spanText = (Spannable) text;
							Selection.setSelection(spanText, text.length());
						}
					}
				} else {
					resetText = false;
				}
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});
	}

	/**
	 * 检测是否有emoji表情
	 *
	 * @param source
	 * @return
	 */
	public static boolean containsEmoji(String source) {
		int len = source.length();
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (!isEmojiCharacter(codePoint)) { // 如果不能匹配,则该字符是Emoji表情
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否是Emoji
	 *
	 * @param codePoint
	 *            比较的单个字符
	 * @return
	 */
	private static boolean isEmojiCharacter(char codePoint) {
		return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
				|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
				|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}

}
