package com.another.pooling.my;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.another.pooling.BillInfo;
import com.another.pooling.R;
import com.aps.p;
public class FollowManActivity extends Activity {

	private ListView listview;
	private String no;
	private String[] followman = null;
	private List<String> followmantel = null;
	private static int pos = 0;
	private static boolean flag;
	private String[] temp = null;

	private TextView back;
	private TextView about;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_follow_man);

		back = (TextView) findViewById(R.id.back_tv_follow_man);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FollowManActivity.this.finish();
			}
		});
		
		about = (TextView) findViewById(R.id.about_tv_follow_man);
		about.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(FollowManActivity.this)  
				                .setTitle("提示")
				                .setMessage("点击用户名获取联系方式")
				                .setPositiveButton("确定", null)
				                .show();
			}
		});
		flag = false;
		no = this.getIntent().getExtras().getString("objectId");
		//		Log.e("length", no + "");
		//		f = new String[]{"1", "2", "3"};
		//		f = getData();
		listview = (ListView) findViewById(R.id.datalist_follow_man);
		//		listview  = new ListView(this);
		getData();
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				setListView();
			}
		}, 3000);
	}

	private void getData() {
		// TODO Auto-generated method stub
		BmobQuery<BillInfo> bmobQuery = new BmobQuery<BillInfo>();
		bmobQuery.addWhereEqualTo("objectId", no);
		bmobQuery.findObjects(FollowManActivity.this, new FindListener<BillInfo>() {

			@Override
			public void onSuccess(List<BillInfo> arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(FollowManActivity.this, "查询成功", Toast.LENGTH_LONG).show();
				for(BillInfo billInfo : arg0) {
					if(billInfo.getFollowman().length != 1) {
						//					Toast.makeText(FollowManActivity.this, billInfo.getFollowman()[1], Toast.LENGTH_LONG).show();
						followman = billInfo.getFollowman();
//						FollowManActivity.this.setContentView(listview);
					} else {
						Toast.makeText(FollowManActivity.this, "无人跟单", Toast.LENGTH_LONG).show();
						FollowManActivity.this.finish();
					}
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(FollowManActivity.this, arg1, Toast.LENGTH_LONG).show();
			}
		});
	}
	
	private void setListView() {
		temp = new String[followman.length-1];
		for(int i = 1; i < followman.length; i++) {
			temp[i-1] = followman[i];
		}
		listview.setAdapter(new ArrayAdapter<String>(FollowManActivity.this, android.R.layout.simple_expandable_list_item_1, temp));
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				pos = position;
				BmobQuery<Tel> bmobQuery = new BmobQuery<Tel>();
				bmobQuery.addWhereEqualTo("username", temp[position]);
				bmobQuery.findObjects(FollowManActivity.this, new FindListener<Tel>() {

					@Override
					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(List<Tel> arg0) {
						// TODO Auto-generated method stub
						for(Tel tel : arg0) {
							temp[pos] = temp[pos] + "\n电话：" + tel.getTel();
							listview.setAdapter(new ArrayAdapter<String>(FollowManActivity.this, android.R.layout.simple_expandable_list_item_1, temp));
						}
					}
				});
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.follow_man, menu);
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
}
