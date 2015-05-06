package com.another.pooling.offline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.FindListener;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.another.pooling.*;
import com.another.pooling.BillPoolingActivity.AddPopupWindows;
import com.another.pooling.my.MyInfoActivity;
import com.example.testpic.PublishedActivity;
import com.geniusgithub.lazyloaddemo.LoaderAdapter;
import com.geniusgithub.lazyloaddemo.cache.ImageLoader;

public class SearchResultActivityOffLine extends Activity {
//	private int user_icon[] = null;
	private String username[] = null;
//	private String image[] = null; 
	private String image_uri[] = null;
	private List<String> uri;
	private String pre_desString[] = null;
	private String no[] = null;
	private String string_dec;
	private String string_username;
	private String string_no;
	private int length = 0;

//	private ListView datalist = null; // 定义ListView组件
//	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); // 定义显示的内容包装
//	private SimpleAdapter simpleAdapter = null; // 进行数据的转换操作
	
	private ListView mListview;
	private LoaderAdapter adapter;
	private String address;
	private TextView title;
	
	private SlidingMenu mLeftMenu;
	private TextView overflow;
	private CustomFAB customFAB;
	private RelativeLayout relativeLayout;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_search_result_offline);
		
		Bmob.initialize(this, "dc417cd048f5197ba699440c13977f34");
		mLeftMenu = (SlidingMenu) findViewById(R.id.id_menu_search_result_offline);
		address = getIntent().getExtras().getString("address");
		 title = (TextView) findViewById(R.id.title_tv_search_result_offline);
		 title.setText("线下拼单-搜索");
		 
		 overflow = (TextView) findViewById(R.id.push_tv_search_result_offline);
			overflow.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mLeftMenu.toggle();
				}
			});
			relativeLayout = (RelativeLayout) findViewById(R.id.search_result_offline_rela_lay);
			customFAB = (CustomFAB) findViewById(R.id.search_result_offline_add);
			customFAB.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new AddPopupWindows(SearchResultActivityOffLine.this, relativeLayout);
				}
			});
		getData();
		
	}
	
	private void getData() {
		// TODO Auto-generated method stub
		BmobQuery<BillInfo> bmobQuery = new BmobQuery<BillInfo>();
		bmobQuery.addWhereEqualTo("address", address);
//		bmobQuery.addWhereEqualTo("address", "北京市东城区东华门街道");
		bmobQuery.addWhereEqualTo("classes", 1);
		bmobQuery.setLimit(100);    //获取最接近用户地点的10条数据
		bmobQuery.findObjects(SearchResultActivityOffLine.this, new FindListener<BillInfo>() {
		    @Override
		    public void onSuccess(List<BillInfo> object) {
		        // TODO Auto-generated method stub
		        //Toast.makeText(SearchResultActivityOffLine.this, "查询成功：共" + object.size() + "条数据。"/*  + mPosition.getLatitude() + " " + mPosition.getLongitude()*/, Toast.LENGTH_LONG).show();
		    	if(object.size() == 0) {
		    		Toast.makeText(SearchResultActivityOffLine.this, "没有找到哦~快去发起拼单吧~", Toast.LENGTH_LONG).show();
		    		Intent intent = new Intent(SearchResultActivityOffLine.this, BillPoolingActivityOffLine.class);
		    		startActivity(intent);
		    		SearchResultActivityOffLine.this.finish();
		    	} else {
		    		string_dec= "";
		    		string_username="";
		    		string_no="";
		    		uri = new ArrayList<String>();
		    		for(BillInfo billInfo : object) {
		    			string_username = string_username + billInfo.getUsername() + " ";
		    			string_no = string_no + billInfo.getObjectId() + " ";
		    			uri.add(billInfo.getImgfilename()[0]);
		    			if(billInfo.getDescribe().equals("")) {
		    				string_dec= string_dec + "暂无" + " ";
		    			} else {
		    				string_dec= string_dec + billInfo.getDescribe() + " ";
		    			}
		    		}
		    		username = string_username.trim().split(" ");
		    		no = string_no.trim().split(" ");
		    		pre_desString = string_dec.trim().split(" ");
		    		image_uri = uri.toArray(new String[uri.size()]);
		    		length = username.length;
		    		//Log.i("username", length+"");
		    		//		    	initView();
		    		//		    	Intent intent = new Intent(SearchResultActivityOffLine.this, com.geniusgithub.lazyloaddemo.MainActivity.class);
		    		//		    	Bundle bundle = new Bundle();
		    		//		    	bundle.putStringArray("urls", image_uri);
		    		//		    	bundle.putStringArray("describe", pre_desString);
		    		//		    	bundle.putStringArray("no", no);
		    		//		    	bundle.putInt("count", length);
		    		//		    	intent.putExtras(bundle);
		    		//		    	startActivity(intent);
		    		//		    	SearchResultActivityOffLine.this.finish();
		    		setupViews(length, image_uri, pre_desString);
		    	}
		    }
		    @Override
		    public void onError(int code, String msg) {
		        // TODO Auto-generated method stub
		    	Toast.makeText(SearchResultActivityOffLine.this, "查询失败。+" + msg, Toast.LENGTH_LONG).show();
		    }
		});
	}
	
//	@Override
//	protected void onDestroy() {
//		
//		
//		ImageLoader imageLoader = adapter.getImageLoader();
//		if (imageLoader != null){
//			imageLoader.clearCache();
//		}
//		
//		super.onDestroy();
//	}



	public void setupViews(int itemsCount, String[] urls, String[] des) {
		mListview = (ListView) findViewById(R.id.datalist_search_result_offline);
		adapter = new LoaderAdapter(itemsCount, this, urls, des);
		mListview.setAdapter(adapter);
		mListview.setOnScrollListener(mScrollListener);
		mListview.setOnItemClickListener(new OnItemClickListener(){  

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String objectid = no[position];
				//Toast.makeText(SQLiteCRUDActivity.this, userid +" , "+ name +" , "+ age ,Toast.LENGTH_LONG).show(); 
				Intent intent = new Intent(SearchResultActivityOffLine.this, DetailResultActivityOffLine.class);
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

	public void toggleMenu(View view)
	{
		mLeftMenu.toggle();
	}
	
	public void EnterBillPoolingOnLine(View view) {
		Intent intent = new Intent(this, BillPoolingActivity.class);
		startActivity(intent);
	}
	
	public void EnterBillPoolingOffLine(View view) {
		Intent intent = new Intent(this, BillPoolingActivityOffLine.class);
		startActivity(intent);
	}
	
	public void EnterMyInfo(View view) {
		Intent intent = new Intent(this, MyInfoActivity.class);
		startActivity(intent);
	}
	
	
	
//	private void initView() {
//		this.datalist = (ListView) super.findViewById(R.id.datalist); // 取得组件
//		for (int x = 0; x < length; x++) {
//			Map<String, Object> map = new HashMap<String, Object>(); // 定义Map集合，保存每一行数据
//			map.put("user_icon", String.valueOf(R.drawable.pooling_ic)); // 与data_list.xml中的TextView组加匹配
//			map.put("username", this.username[x]); // 与data_list.xml中的TextView组加匹配
//			map.put("no", this.no[x]);
//			map.put("image", String.valueOf(R.drawable.guide_image1)); // 与data_list.xml中的TextView组加匹配
//			map.put("pre_describe", this.pre_desString[x]);
//			this.list.add(map); // 保存了所有的数据行
//		} 
//		this.simpleAdapter = new SimpleAdapter(this, this.list,
//				R.layout.bill_info_layout, new String[] { "user_icon", "username", "no", "image",
//						"pre_describe"} // Map中的key的名称
//				, new int[] { R.id.user_icon, R.id.username, R.id.no, R.id.image, R.id.pre_describe }); // 是data_list.xml中定义的组件的资源ID
//		this.datalist.setAdapter(this.simpleAdapter);
//		
//		datalist.setOnItemClickListener(new OnItemClickListener(){  
//
//			@SuppressWarnings("unchecked")  
//			@Override  
//
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {  
//				ListView listView = (ListView)parent;  
//				HashMap<String, String> map = (HashMap<String, String>) listView.getItemAtPosition(position);  
//				String no = map.get("no");
//				//Toast.makeText(SQLiteCRUDActivity.this, userid +" , "+ name +" , "+ age ,Toast.LENGTH_LONG).show(); 
//				Intent intent = new Intent(SearchResultActivityOffLine.this, DetailResultActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putString("objectId", no);
//				intent.putExtras(bundle);
//				startActivity(intent);
//			}  
//		});  
//	}
	
	public class AddPopupWindows extends PopupWindow
	{

		public AddPopupWindows(Context mContext, View parent)
		{
			
			 super(mContext);

			View view = View
					.inflate(mContext, R.layout.add_popupwindows, null);  //选择拍照相册..
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins)); //设置渐变出现动画
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));  //按钮按下的动画效果

			setWidth(LayoutParams.MATCH_PARENT);
			setHeight(LayoutParams.MATCH_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.add_popupwindows_online);
			Button bt2 = (Button) view
					.findViewById(R.id.add_popupwindows_offline);
			Button bt3 = (Button) view
					.findViewById(R.id.add_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					Intent intent = new Intent(SearchResultActivityOffLine.this, PublishedActivity.class);
					startActivity(intent);
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					Intent intent = new Intent(SearchResultActivityOffLine.this, PublishedActivityOffLine.class);
					startActivity(intent);
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					dismiss();
				}
			});

		}
	}
}


	


