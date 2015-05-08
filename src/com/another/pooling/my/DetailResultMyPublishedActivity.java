package com.another.pooling.my;

import java.util.ArrayList;

import com.another.pooling.R;

import java.util.List;

import com.another.pooling.BillInfo;
import com.another.pooling.MainActivity.AddPopupWindows;
import com.another.pooling.Utility;
import com.another.pooling.offline.DetailResultActivityOffLine;
import com.another.pooling.offline.PublishedActivityOffLine;
import com.example.testpic.PublishedActivity;
import com.geniusgithub.lazyloaddemo.LoaderAdapter;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class DetailResultMyPublishedActivity extends Activity {

	private String no;
	private TextView r_username;
	private EditText r_describe;
	private EditText r_deadline;
	private EditText r_online_address;
	private EditText r_offline_address;
	private EditText r_link;
	private BillInfo mBillInfo;
	private TextView back;

	private TextView overflow;
	private RelativeLayout relativeLayout;
	
//	private String username[] = null;
	private String image_uri[] = null;
	private List<String> uri;
	private String pre_desString[] = null;
	private String string_dec;
//	private String string_username;
//	private String string_no;
//	private String string_file_name;
	private int length = 0;
	
	private ListView mListview;
	private LoaderAdapter adapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_detail_result_my_published);
		
		Bmob.initialize(this, "dc417cd048f5197ba699440c13977f34");
		
		no = getIntent().getExtras().getString("objectId");

		r_username = (TextView) findViewById(R.id.r_username_my_puhlish);
		r_describe = (EditText) findViewById(R.id.r_describe_my_puhlish);
		r_deadline = (EditText) findViewById(R.id.r_deadline_my_puhlish);
		r_online_address = (EditText) findViewById(R.id.r_online_address_my_puhlish);
		r_offline_address = (EditText) findViewById(R.id.r_offline_address_my_puhlish);
		r_link = (EditText) findViewById(R.id.r_link_my_puhlish);
		
		
		relativeLayout = (RelativeLayout) findViewById(R.id.my_publish_detail_re_lay);
		overflow = (TextView) findViewById(R.id.op_tv_my_publish);
		overflow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new OpPopupWindows(DetailResultMyPublishedActivity.this, relativeLayout);
			}
		});
		
		back = (TextView) findViewById(R.id.back_tv_detail_my_publish);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DetailResultMyPublishedActivity.this.finish();
			}
		});
		
		getData();
	}

	private void getData() {
		// TODO Auto-generated method stub
		BmobQuery<BillInfo> bmobQuery = new BmobQuery<BillInfo>();
		bmobQuery.addWhereEqualTo("objectId", no);
		bmobQuery.setLimit(1);   
		bmobQuery.findObjects(DetailResultMyPublishedActivity.this, new FindListener<BillInfo>() {
		    @Override
		    public void onSuccess(List<BillInfo> object) {
		        // TODO Auto-generated method stub
		        //Toast.makeText(NearActivity.this, "查询成功：共" + object.size() + "条数据。"/*  + mPosition.getLatitude() + " " + mPosition.getLongitude()*/, Toast.LENGTH_LONG).show();
		    	for(BillInfo billInfo : object) {
		    		r_username.setText(billInfo.getUsername());
		    		r_describe.setText(billInfo.getDescribe());
		    		r_deadline.setText(billInfo.getDeadline());
		    		r_online_address.setText(billInfo.getAddress());
		    		r_offline_address.setText(billInfo.getDetailaddress());
		    		r_link.setText(billInfo.getLink());
		    		
		    		string_dec= "";
//			    	string_username="";
//			    	string_no="";
//			    	string_file_name="";
			    	uri = new ArrayList<String>();
			    	//myApplication.configPath(NearResultActivity.this);
			    	
			    	for(int i = 0; i < billInfo.getImgfilename().length; i++) {
			    		uri.add(billInfo.getImgfilename()[i]);
			    		if(billInfo.getTabs() == null) {
			    			string_dec= string_dec + "暂无" + " ";
			    		} else {
			    			string_dec= string_dec + billInfo.getTabs() + " ";
			    		}
			    	}
			    	
//			    	Log.e("url", string_file_name);
//			    	username = string_username.trim().split(" ");
			    	pre_desString = string_dec.trim().split(" ");
			    	image_uri = uri.toArray(new String[uri.size()]);
			    	length = image_uri.length;
			    	//Log.i("username", length+"");
//			    	try {
//						initView();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
			    	setupViews(length, image_uri, pre_desString);
			   
		    	}
		    }
		    @Override
		    public void onError(int code, String msg) {
		        // TODO Auto-generated method stub
		    	Toast.makeText(DetailResultMyPublishedActivity.this, "查询失败。+" + msg, Toast.LENGTH_LONG).show();
		    }
		});
	}
	
	public void setupViews(int itemsCount, String[] urls, String[] des) {
		mListview = (ListView) findViewById(R.id.datalist_detail_my_puhlish);
		adapter = new LoaderAdapter(itemsCount, this, urls, des, 1);
		mListview.setAdapter(adapter);
		Utility.setListViewHeightBasedOnChildren(mListview);
		mListview.setOnScrollListener(mScrollListener);
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
	
	private BillInfo getObject() {
		// TODO Auto-generated method stub
		BmobQuery<BillInfo> bmobQuery = new BmobQuery<BillInfo>();
		bmobQuery.addWhereEqualTo("objectId", no);
		bmobQuery.setLimit(1);   
		bmobQuery.findObjects(DetailResultMyPublishedActivity.this, new FindListener<BillInfo>() {
		    @Override
		    public void onSuccess(List<BillInfo> object) {
		        // TODO Auto-generated method stub
		        //Toast.makeText(NearActivity.this, "查询成功：共" + object.size() + "条数据。"/*  + mPosition.getLatitude() + " " + mPosition.getLongitude()*/, Toast.LENGTH_LONG).show();
		    	mBillInfo = object.get(0);
		    }
		    @Override
		    public void onError(int code, String msg) {
		        // TODO Auto-generated method stub
		    	Toast.makeText(DetailResultMyPublishedActivity.this, "查询失败。+" + msg, Toast.LENGTH_LONG).show();
		    }
		});
		return mBillInfo;
	}

	public void follow(View view) {
		BillInfo billInfo = new BillInfo();
		BmobUser bmobUser = BmobUser.getCurrentUser(this);
		String username = bmobUser.getUsername();
		billInfo.setObjectId(no);
		billInfo.addUnique("followman", username);
		billInfo.update(DetailResultMyPublishedActivity.this, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				Toast.makeText(DetailResultMyPublishedActivity.this, "跟单成功", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(DetailResultMyPublishedActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public void cancel(View view) {
		finish();
	}
	
	public class OpPopupWindows extends PopupWindow
	{

		public OpPopupWindows(Context mContext, View parent)
		{
			
			 super(mContext);

			View view = View
					.inflate(mContext, R.layout.op_popupwindows, null);  //选择拍照相册..
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins)); //设置渐变出现动画
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup_op);
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
					.findViewById(R.id.op_popupwindows_query_followman);
			Button bt2 = (Button) view
					.findViewById(R.id.op_popupwindows_offline_undo);
			Button bt3 = (Button) view
					.findViewById(R.id.op_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					Intent intent = new Intent(DetailResultMyPublishedActivity.this, FollowManActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("objectId", no);
					intent.putExtras(bundle);
					startActivity(intent);
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					Intent intent = new Intent(DetailResultMyPublishedActivity.this, PublishedActivityOffLine.class);
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