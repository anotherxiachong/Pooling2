package com.another.pooling;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.FindListener;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
//import com.another.pooling.MainActivity.AddPopupWindows;
import com.another.pooling.my.MyInfoActivity;
import com.another.pooling.offline.BillPoolingActivityOffLine;
import com.another.pooling.offline.PublishedActivityOffLine;
import com.example.testpic.PublishedActivity;
import com.geniusgithub.lazyloaddemo.LoaderAdapter;
//import com.geniusgithub.lazyloaddemo.cache.ImageLoader;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
//import android.view.TextureView;
import android.view.View;
import android.view.Window;
//import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
//import android.view.ext.SatelliteMenu;
//import android.view.ext.SatelliteMenuItem;
//import android.view.ext.SatelliteMenu.SateliteClickedListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
//import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;


public class BillPoolingActivity extends Activity implements AMapLocationListener{
	
	private SlidingMenu mLeftMenu ; 
	private TextView title;
	private CustomFAB customFAB;
	private RelativeLayout relativeLayout;

	boolean isExit;  
	
	private String username[] = null;
	private String image_uri[] = null;
	private List<String> uri;
	private String pre_desString[] = null;
	private String no[] = null;
	private String string_dec;
	private String string_username;
	private String string_no;
	private String string_file_name;
	private int length = 0;
	
	LocationManagerProxy mLocationManagerProxy;
	private double longitude;
	private double latitude;
	private BmobGeoPoint mPosition;
	
	private ListView mListview;
	private LoaderAdapter adapter;

	private TextView search;   //搜索按钮
	private TextView overflow;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_near);
//		 SatelliteMenu menu = (SatelliteMenu) findViewById(R.id.menu);
		 title = (TextView) findViewById(R.id.title_tv_near);
		 title.setText("线上拼单-附近");
		 
			mLeftMenu = (SlidingMenu) findViewById(R.id.id_menu_near);
		 
		 overflow = (TextView) findViewById(R.id.push_tv_near);
			overflow.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mLeftMenu.toggle();
				}
			});
			
			relativeLayout = (RelativeLayout) findViewById(R.id.near_rela_lay);
			customFAB = (CustomFAB) findViewById(R.id.near_add);
			customFAB.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new AddPopupWindows(BillPoolingActivity.this, relativeLayout);
				}
			});
//		WindowManager wm = this.getWindowManager();
//		@SuppressWarnings("deprecation")
//		int height = wm.getDefaultDisplay().getHeight();
//		
//		LinearLayout.LayoutParams params = (LayoutParams) menu.getLayoutParams();
//		params.topMargin = height - 1200;
//		menu.setLayoutParams(params);
//		
//		List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
//        items.add(new SatelliteMenuItem(6, R.drawable.ic_1));
//        items.add(new SatelliteMenuItem(5, R.drawable.ic_3));
//        items.add(new SatelliteMenuItem(4, R.drawable.ic_4));
//        items.add(new SatelliteMenuItem(3, R.drawable.ic_5));
//        items.add(new SatelliteMenuItem(2, R.drawable.ic_6));
//        items.add(new SatelliteMenuItem(1, R.drawable.ic_2));
////        items.add(new SatelliteMenuItem(5, R.drawable.sat_item));
//        menu.addItems(items);        
//        
//        menu.setOnItemClickedListener(new SateliteClickedListener() {
//			
//			public void eventOccured(int id) {
//				Log.i("sat", "Clicked on " + id);
//				switch (id) {
//				case 3:
//					Intent intent3 = new Intent(BillPoolingActivity.this, NearResultActivity.class);
//					startActivity(intent3);
//					break;
//					
//				case 5:
//					Intent intent5 = new Intent(BillPoolingActivity.this, PublishedActivity.class);
//					startActivity(intent5);
//					break;
//					
//				case 6:
//					Intent intent6  = new Intent(BillPoolingActivity.this, CitiesActivity.class);
//					Bundle classes = new Bundle();
//					classes.putString("classes", "search_online");
//					intent6.putExtras(classes);
//					startActivity(intent6);
//					break;
//
//				default:
//					break;
//				}
//			}
//		});
		 search = (TextView) findViewById(R.id.search_tv_near);
			search.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent  = new Intent(BillPoolingActivity.this, CitiesActivity.class);
					Bundle classes = new Bundle();
					classes.putString("classes", "search_online");
					intent.putExtras(classes);
					startActivity(intent);
				}
			});
		 Bmob.initialize(this, "dc417cd048f5197ba699440c13977f34");
			mPosition = new BmobGeoPoint();
			
			getPosition();
	}
	
	private void getPosition() {
		// TODO Auto-generated method stub
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
	    mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork,-1, 15, this);
		
	}
	
	private void getData(){
		// TODO Auto-generated method stub
		BmobQuery<BillInfo> bmobQuery = new BmobQuery<BillInfo>();
		bmobQuery.addWhereNear("position", mPosition);
		bmobQuery.addWhereEqualTo("classes", 0);
		bmobQuery.setLimit(100);    //获取最接近用户地点的10条数据
		bmobQuery.findObjects(BillPoolingActivity.this, new FindListener<BillInfo>() {
		    @Override
		    public void onSuccess(List<BillInfo> object) {
		        // TODO Auto-generated method stub
		        //Toast.makeText(NearResultActivity.this, "查询成功：共" + object.size() + "条数据。"/*  + mPosition.getLatitude() + " " + mPosition.getLongitude()*/, Toast.LENGTH_LONG).show();
		    	if(object.size() == 0) {
		    		Toast.makeText(BillPoolingActivity.this, "没有找到哦~快去发起拼单吧~", Toast.LENGTH_LONG).show();
		    		BillPoolingActivity.this.finish();
		    	}
		    	string_dec= "";
		    	string_username="";
		    	string_no="";
		    	string_file_name="";
		    	uri = new ArrayList<String>();
		    	//myApplication.configPath(NearResultActivity.this);
		    	for(BillInfo billInfo : object) {
		    		string_username = string_username + billInfo.getUsername() + " ";
		    		string_no = string_no + billInfo.getObjectId() + " ";
		    		uri.add(billInfo.getImgfilename()[0]);
		    		if(billInfo.getTabs() == null) {
		    			string_dec= string_dec + "暂无" + " ";
		    		} else {
		    			string_dec= string_dec + billInfo.getTabs() + " ";
		    		}
		    	}
		    	Log.e("url", string_file_name);
		    	username = string_username.trim().split(" ");
		    	no = string_no.trim().split(" ");
		    	pre_desString = string_dec.trim().split(" ");
		    	image_uri = uri.toArray(new String[uri.size()]);
		    	length = username.length;
		    	//Log.i("username", length+"");
//		    	try {
//					initView();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
		    	setupViews(length, image_uri, pre_desString);
		    }
		    @Override
		    public void onError(int code, String msg) {
		        // TODO Auto-generated method stub
		    	Toast.makeText(BillPoolingActivity.this, "查询失败。+" + msg, Toast.LENGTH_LONG).show();
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
		mListview = (ListView) findViewById(R.id.datalist_near);
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
				Intent intent = new Intent(BillPoolingActivity.this, DetailResultActivity.class);
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
		finish();
	}
	
	public void EnterBillPoolingOffLine(View view) {
		Intent intent = new Intent(this, BillPoolingActivityOffLine.class);
		startActivity(intent);
		finish();
	}
	
	public void EnterMyInfo(View view) {
		Intent intent = new Intent(this, MyInfoActivity.class);
		startActivity(intent);
		finish();
	}
	
	public void EnterMain(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {  
			exit();  
			return false;  
        } else {  
            return super.onKeyDown(keyCode, event);  
        } 
	}
	
	public void exit(){  
        if (!isExit) {  
            isExit = true;  
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();  
            mHandler.sendEmptyMessageDelayed(0, 2000);  
        } else {  
            finish(); 
            System.exit(0);  
        }  
    }  
	
	Handler mHandler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            // TODO Auto-generated method stub   
            super.handleMessage(msg);  
            isExit = false;  
        }  
    };  
	public void post(View view) {
		Intent intent = new Intent(this, PublishedActivity.class);
		//Bundle address = new Bundle();
		//address.putString("address", "");
		//intent.putExtras(address);
		startActivity(intent);
		//Intent intent = new Intent(this, PublishedActivity.class);
		//startActivityForResult(intent, RESULT_OK);
	}
	
	public void near(View view) {
		Intent intent = new Intent(this, NearResultActivity.class);
		startActivity(intent);
	}
	
	public void search(View view) {
		Intent intent  = new Intent(this, CitiesActivity.class);
		Bundle classes = new Bundle();
		classes.putString("classes", "search_online");
		intent.putExtras(classes);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bill_pooling, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(AMapLocation arg0) {
		// TODO Auto-generated method stub
		if(arg0 != null && arg0.getAMapException().getErrorCode() == 0) {
			latitude = arg0.getLatitude();
			longitude = arg0.getLongitude();
			mPosition.setLatitude(latitude);
			mPosition.setLongitude(longitude);
			//Toast.makeText(NearActivity.this, latitude + " " + longitude, Toast.LENGTH_LONG).show();
			getData();
			//Log.e("position", arg0.getLatitude() + " " + arg0.getLongitude());
		}
	}
	
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
					Intent intent = new Intent(BillPoolingActivity.this, PublishedActivity.class);
					startActivity(intent);
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					Intent intent = new Intent(BillPoolingActivity.this, PublishedActivityOffLine.class);
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
