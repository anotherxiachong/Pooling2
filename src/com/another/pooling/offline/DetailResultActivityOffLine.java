package com.another.pooling.offline;

import java.util.ArrayList;
import java.util.List;

import com.another.pooling.*;
import com.geniusgithub.lazyloaddemo.LoaderAdapter;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class DetailResultActivityOffLine extends Activity {

	private String no;
	private TextView r_username;
	private EditText r_describe;
	private EditText r_deadline;
	private EditText r_online_address;
	private EditText r_offline_address;
	private EditText r_link;
	private TextView back;
	
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
		super.setContentView(R.layout.activity_detail_result_offline);
		
		Bmob.initialize(this, "dc417cd048f5197ba699440c13977f34");
		
		no = getIntent().getExtras().getString("objectId");

		r_username = (TextView) findViewById(R.id.r_username_offline);
		r_describe = (EditText) findViewById(R.id.r_describe_offline);
		r_deadline = (EditText) findViewById(R.id.r_deadline_offline);
		r_online_address = (EditText) findViewById(R.id.r_online_address_offline);
		r_offline_address = (EditText) findViewById(R.id.r_offline_address_offline);
		r_link = (EditText) findViewById(R.id.r_link_offline);
		r_link.setVisibility(View.GONE);
		
		back = (TextView) findViewById(R.id.back_tv_detail_offline);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DetailResultActivityOffLine.this.finish();
			}
		});
		
		getData();
	}

	private void getData() {
		// TODO Auto-generated method stub
		BmobQuery<BillInfo> bmobQuery = new BmobQuery<BillInfo>();
		bmobQuery.addWhereEqualTo("objectId", no);
		bmobQuery.setLimit(1);   
		bmobQuery.findObjects(DetailResultActivityOffLine.this, new FindListener<BillInfo>() {
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
		    	Toast.makeText(DetailResultActivityOffLine.this, "查询失败。+" + msg, Toast.LENGTH_LONG).show();
		    }
		});
	}
	
	public void setupViews(int itemsCount, String[] urls, String[] des) {
		mListview = (ListView) findViewById(R.id.datalist_detail_offline);
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

	public void follow(View view) {
		BillInfo billInfo = new BillInfo();
		BmobUser bmobUser = BmobUser.getCurrentUser(this);
		String username = bmobUser.getUsername();
		billInfo.setClasses(1);
		billInfo.update(DetailResultActivityOffLine.this, no, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				//Toast.makeText(DetailResultActivityOffLine.this, "跟单成功", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				//Toast.makeText(DetailResultActivityOffLine.this, "请检查网络", Toast.LENGTH_SHORT).show();
			}
		});
		
		billInfo.setObjectId(no);
		billInfo.addUnique("followman", username);
		billInfo.update(DetailResultActivityOffLine.this, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				Toast.makeText(DetailResultActivityOffLine.this, "跟单成功", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(DetailResultActivityOffLine.this, "请检查网络", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public void cancel(View view) {
		finish();
	}
}