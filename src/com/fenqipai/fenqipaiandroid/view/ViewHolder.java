package com.fenqipai.fenqipaiandroid.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @ClassName: ViewHolder
 * @Description: 适配器使用holder
 * @author qianyuhang
 * @date 2016-6-24 下午1:28:07
 */
public class ViewHolder {

    private SparseArray<View> mViews;

    private int mPosition;

    private View mConvertView;
    
    private ViewGroup parent;
    
    private BaseApplication application;

    public ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
    	this.parent = parent;
        this.mPosition = position;
        this.application=(BaseApplication)context.getApplicationContext();
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

  /**
 * @Title:	单例
 * @Description: 获取ViewHolder
 * @param	
 * @return
 * @throws
 */
    public static ViewHolder getInstance(Context context, View convertView, ViewGroup parent,
                                         int layoutId, int position) {

        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.mPosition = position;//ViewHolder�����ˣ�����position��Ҫ����
            return holder;
        }
    }

    /**
     * ���ܼ���:ͨ��View id��ȡView����
     *
     * @param viewId [View id]
     * @return [View����]
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * ���ܼ���:����convertView����
     */
    public View getConvertView() {
        return mConvertView;
    }

    /**
     * ���ܼ���:ΪTextView�����ı�
     *
     * @param viewId [TextView id]
     * @param text   [TextView Ҫ��ʾ���ı�]
     * @return [VIewHolder ��ʽ���]
     */
    public ViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     * ���ܼ���:ΪImageView����ͼƬ
     *
     * @param viewId [ImageVIew��id]
     * @param resId  [ͼƬid]
     * @return [VIewHolder ��ʽ���]
     */
    public ViewHolder setImageResource(int viewId, int resId) {
        ImageView imgView = getView(viewId);
        imgView.setImageResource(resId);
        return this;
    }

    /**
     * ���ܼ���:ΪImageView����Bitmap
     *
     * @param viewId [ImageVIew��id]
     * @param bitmap [Bitmap]
     * @return [VIewHolder ��ʽ���]
     */
    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView imgView = getView(viewId);
        imgView.setImageBitmap(bitmap);
        return this;
    }

    /**
     * ���ܼ���:ΪImageView��������ͼƬ 
     *
     * @param viewId [ImageVIew��id]
     * @param uri    [����ͼƬ�ĵ�ַ]
     * @return [VIewHolder ��ʽ���]
     */
    public ViewHolder setImageURI(int viewId, String uri) {
        ImageView imgView = getView(viewId);
        application.imageLoader.displayImage(uri, imgView, application.options);
        return this;
    }

    /**
     * 功能简述: 设置View的可见性
      * @param viewId    [View 的id]
     * @param isVisible [View是否可见]
     * @return [返回类型说明]
     * @exception/throws [违例类型] [违例说明]
     */
    public ViewHolder setVisibility(int viewId, boolean isVisible) {
        View view = getView(viewId);
        if (isVisible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
        return this;
    }

    public int getPosition() {
        return mPosition;
    }

    public ViewHolder getHolder() {
        return this;
    }
    
    public ViewGroup getParent(){
    	return parent;
    }
}
