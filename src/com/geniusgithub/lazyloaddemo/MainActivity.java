package com.geniusgithub.lazyloaddemo;


import java.util.HashMap;

import com.geniusgithub.lazyloaddemo.cache.ImageLoader;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.amap.api.location.c;
import com.another.pooling.DetailResultActivity;
import com.another.pooling.R;
import com.another.pooling.R.color;
import com.another.pooling.offline.DetailResultActivityOffLine;


/**
 * @author lance
 * @csdn  http://blog.csdn.net/geniuseoe2012
 * @github https://github.com/geniusgithub
 * @QQgroupID 298044305
 */
public class MainActivity extends Activity {


	/** Called when the activity is first created. */
	private ListView mListview;
	private LoaderAdapter adapter;
	private String[] URLS; 
	private String[] describe;
	private String[] no;
	private int itemsCount;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		URLS = this.getIntent().getExtras().getStringArray("urls");
		if(URLS == null) {
			this.finish();
		}
		itemsCount = this.getIntent().getExtras().getInt("count");
		describe = this.getIntent().getExtras().getStringArray("describe");
		no = this.getIntent().getExtras().getStringArray("no");
		setupViews(itemsCount, URLS, describe);
	}
	
	

	@Override
	protected void onDestroy() {
		
		
		ImageLoader imageLoader = adapter.getImageLoader();
		if (imageLoader != null){
			imageLoader.clearCache();
		}
		
		super.onDestroy();
	}



	public void setupViews(int itemsCount, String[] urls, String[] des) {
		mListview = (ListView) findViewById(R.id.datalist_sear_off);
		adapter = new LoaderAdapter(itemsCount, this, urls, des, 0);
		mListview.setAdapter(adapter);
		mListview.setOnScrollListener(mScrollListener);
		mListview.setOnItemClickListener(new OnItemClickListener(){  

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String objectid = no[position];
				//Toast.makeText(SQLiteCRUDActivity.this, userid +" , "+ name +" , "+ age ,Toast.LENGTH_LONG).show(); 
				Intent intent = new Intent(MainActivity.this, DetailResultActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("objectId", objectid);
				intent.putExtras(bundle);
				startActivity(intent);     
			}  
	});  
	}

	OnScrollListener mScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_FLING:
				adapter.setFlagBusy(true);
				break;
			case OnScrollListener.SCROLL_STATE_IDLE:
				adapter.setFlagBusy(false);
				break;
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				adapter.setFlagBusy(false);
				break;
			default:
				break;
			}
			adapter.notifyDataSetChanged();
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

		}
	};
    

	/*private static final String[] URLS = {
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG",
		"http://file.bmob.cn/M00/5B/32/oYYBAFU4dW6APD1RAABtlVrB0DE27.JPEG"
	};*/
}
