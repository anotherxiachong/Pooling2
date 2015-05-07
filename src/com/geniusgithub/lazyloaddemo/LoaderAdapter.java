package com.geniusgithub.lazyloaddemo;

import android.R.integer;
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
	private static int flag = 0;
	
	public LoaderAdapter(int count, Context context, String []url, String []des, int flag) {
		this.mCount = count;
		this.mContext = context;
		urlArrays = url;
		describe = des;
		LoaderAdapter.flag = flag;
		mImageLoader = new ImageLoader(context);
	}
	
	public ImageLoader getImageLoader(){
		if(mImageLoader != null) {
			return mImageLoader;
		} else {
			return null;
		}
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
			if(LoaderAdapter.flag == 0) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.bill_info_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.mTextView = (TextView) convertView
						.findViewById(R.id.pre_describe);
				viewHolder.mImageView = (ImageView) convertView
						.findViewById(R.id.image);
			} else {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.image_info_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.mTextView = (TextView) convertView
						.findViewById(R.id.pre_describe_image);
				viewHolder.mImageView = (ImageView) convertView
						.findViewById(R.id.image_image);
			}
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String url = "";
		if(urlArrays.length != 0) {
			url = urlArrays[position % urlArrays.length];
		} else {
			return null;
		}
		
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
