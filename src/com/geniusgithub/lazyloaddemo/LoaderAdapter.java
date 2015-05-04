package com.geniusgithub.lazyloaddemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.geniusgithub.lazyloaddemo.cache.ImageLoader;
import com.another.pooling.R;





public class LoaderAdapter extends BaseAdapter{

	private static final String TAG = "LoaderAdapter";
	private boolean mBusy = false;

	public void setFlagBusy(boolean busy) {
		this.mBusy = busy;
	}

	
	private ImageLoader mImageLoader;
	private int mCount;
	private Context mContext;
	private String[] urlArrays;
	private String[] describe;
	
	
	public LoaderAdapter(int count, Context context, String []url, String []des) {
		this.mCount = count;
		this.mContext = context;
		urlArrays = url;
		describe = des;
		mImageLoader = new ImageLoader(context);
	}
	
	public ImageLoader getImageLoader(){
		return mImageLoader;
	}

	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.bill_info_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.mTextView = (TextView) convertView
					.findViewById(R.id.pre_describe);
			viewHolder.mImageView = (ImageView) convertView
					.findViewById(R.id.image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String url = "";
		url = urlArrays[position % urlArrays.length];
		
		viewHolder.mImageView.setImageResource(R.drawable.ic_launcher);
		

		if (!mBusy) {
			mImageLoader.DisplayImage(url, viewHolder.mImageView, false);
			viewHolder.mTextView.setText(describe[position]);
		} else {
			mImageLoader.DisplayImage(url, viewHolder.mImageView, false);		
			viewHolder.mTextView.setText("加载中");
		}
		return convertView;
	}

	static class ViewHolder {
		TextView mTextView;
		ImageView mImageView;
	}
}
