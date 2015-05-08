package com.another.pooling.my;

import java.util.List;

import android.app.Activity;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.another.pooling.*;
import com.another.pooling.offline.PublishedActivityOffLine;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MyTelActivity extends Activity {
	private EditText mytelnum;
	private Button save;
	private String objectid;
	private TextView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_tel);
		
		mytelnum = (EditText) findViewById(R.id.mytel_et);
		save = (Button) findViewById(R.id.mytel_bt);
		
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String num = mytelnum.getText().toString();
				Tel tel = new Tel();
				tel.setTel(num);
				tel.update(MyTelActivity.this, objectid,  new UpdateListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						Toast.makeText(MyTelActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(MyTelActivity.this, "请检查网络"+arg1, Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
		
		back = (TextView) findViewById(R.id.back_tv_my_tel);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyTelActivity.this.finish();
			}
		});
		
		getData();
	}

	private void getData() {
		// TODO Auto-generated method stub
		BmobUser bmobUser = BmobUser.getCurrentUser(this);
		String username = bmobUser.getUsername();
		BmobQuery<Tel> bmobQuery = new BmobQuery<Tel>();
		bmobQuery.addWhereEqualTo("username", username);
		bmobQuery.findObjects(MyTelActivity.this, new FindListener<Tel>() {
			
			@Override
			public void onSuccess(List<Tel> arg0) {
				// TODO Auto-generated method stub
				String temp = arg0.get(0).getTel();
				String tempid = arg0.get(0).getObjectId();
				if(!temp.equals("")) {
					mytelnum.setText(temp);
				} else {
					mytelnum.setText("未设置");
				}
				
				if (!temp.equals("")) {
					objectid = tempid;
				} else {
					Toast.makeText(MyTelActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
					MyTelActivity.this.finish();
				}
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				mytelnum.setText("请检查网络");
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_tel, menu);
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
