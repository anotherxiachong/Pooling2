package com.another.pooling;

import java.util.List;

import com.another.pooling.offline.DetailResultActivityOffLine;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DetailResultActivity extends Activity {

	private String no;
	private TextView r_username;
	private EditText r_describe;
	private EditText r_deadline;
	private EditText r_online_address;
	private EditText r_offline_address;
	private EditText r_link;
	private BillInfo mBillInfo;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_detail_result);
		
		Bmob.initialize(this, "dc417cd048f5197ba699440c13977f34");
		
		no = getIntent().getExtras().getString("objectId");

		r_username = (TextView) findViewById(R.id.r_username);
		r_describe = (EditText) findViewById(R.id.r_describe);
		r_deadline = (EditText) findViewById(R.id.r_deadline);
		r_online_address = (EditText) findViewById(R.id.r_online_address);
		r_offline_address = (EditText) findViewById(R.id.r_offline_address);
		r_link = (EditText) findViewById(R.id.r_link);
		
		getData();
	}

	private void getData() {
		// TODO Auto-generated method stub
		BmobQuery<BillInfo> bmobQuery = new BmobQuery<BillInfo>();
		bmobQuery.addWhereEqualTo("objectId", no);
		bmobQuery.setLimit(1);   
		bmobQuery.findObjects(DetailResultActivity.this, new FindListener<BillInfo>() {
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
		    	}
		    }
		    @Override
		    public void onError(int code, String msg) {
		        // TODO Auto-generated method stub
		    	Toast.makeText(DetailResultActivity.this, "查询失败。+" + msg, Toast.LENGTH_LONG).show();
		    }
		});
	}
	
	private BillInfo getObject() {
		// TODO Auto-generated method stub
		BmobQuery<BillInfo> bmobQuery = new BmobQuery<BillInfo>();
		bmobQuery.addWhereEqualTo("objectId", no);
		bmobQuery.setLimit(1);   
		bmobQuery.findObjects(DetailResultActivity.this, new FindListener<BillInfo>() {
		    @Override
		    public void onSuccess(List<BillInfo> object) {
		        // TODO Auto-generated method stub
		        //Toast.makeText(NearActivity.this, "查询成功：共" + object.size() + "条数据。"/*  + mPosition.getLatitude() + " " + mPosition.getLongitude()*/, Toast.LENGTH_LONG).show();
		    	mBillInfo = object.get(0);
		    }
		    @Override
		    public void onError(int code, String msg) {
		        // TODO Auto-generated method stub
		    	Toast.makeText(DetailResultActivity.this, "查询失败。+" + msg, Toast.LENGTH_LONG).show();
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
		billInfo.update(DetailResultActivity.this, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				Toast.makeText(DetailResultActivity.this, "跟单成功", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(DetailResultActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
			}
		});
	}
}