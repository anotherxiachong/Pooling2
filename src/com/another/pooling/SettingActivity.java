package com.another.pooling;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.another.pooling.my.MyInfoActivity;
import com.another.pooling.offline.BillPoolingActivityOffLine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

@SuppressLint("HandlerLeak")
public class SettingActivity  extends Activity{

//	String json = "";
//	String from = "";
//	TextView tv_info;
//
//	Button btn_relation_qq, btn_relation_weibo, btn_logout,
//			btn_remove_qq, btn_remove_weibo;
//
//	ImageView iv_back;
	
	private TextView overflow;
	private SlidingMenu mLeftMenu;

	boolean isExit;  

	private LinearLayout feedback;
	private LinearLayout about;
	private LinearLayout clean;
	private LinearLayout exit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		mLeftMenu = (SlidingMenu) findViewById(R.id.id_menu_setting);
		overflow = (TextView) findViewById(R.id.push_tv_setting);
		overflow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mLeftMenu.toggle();
			}
		});
		
		feedback = (LinearLayout) findViewById(R.id.feedback_setting);
		feedback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BmobUser bmobUser = BmobUser.getCurrentUser(SettingActivity.this);
				String username = bmobUser.getUsername();
				Intent data=new Intent(Intent.ACTION_SENDTO); 
				data.setData(Uri.parse("mailto:manholee@foxmail.com")); 
				data.putExtra(Intent.EXTRA_SUBJECT, "来自" + username + "用户的反馈"); 
				data.putExtra(Intent.EXTRA_TEXT, "想对我们说些什么呢？"); 
				startActivity(data); 
			}
		});
		
		about = (LinearLayout) findViewById(R.id.about_setting);
		about.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SettingActivity.this, AboutActivity.class);
				startActivity(intent);
			}
		});
		
		clean = (LinearLayout) findViewById(R.id.clear_cache_setting);
		clean.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DataCleanManager.cleanInternalCache(SettingActivity.this);
				DataCleanManager.cleanExternalCache(SettingActivity.this);
				Toast.makeText(SettingActivity.this, "清理成功", Toast.LENGTH_LONG).show();
			}
		});
		
		exit = (LinearLayout) findViewById(R.id.account_exit_setting);
		exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BmobUser.logOut(SettingActivity.this);
				BmobUser currentUser = BmobUser.getCurrentUser(SettingActivity.this);
				if(currentUser == null) { 
					Toast.makeText(SettingActivity.this, "退出成功", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
					startActivity(intent);
					SettingActivity.this.finish();
				} else {
					Toast.makeText(SettingActivity.this, "未知错误，请重试", Toast.LENGTH_LONG).show();
				}
			}
		});
//		json = getIntent().getStringExtra("json");
//		from = getIntent().getStringExtra("from");
//		iv_back = (ImageView) findViewById(R.id.iv_back);
//		tv_info = (TextView) findViewById(R.id.tv_info);
//
//		btn_relation_qq = (Button) findViewById(R.id.btn_relation_qq);
//		btn_relation_qq.setOnClickListener(this);
//		btn_relation_weibo = (Button) findViewById(R.id.btn_relation_weibo);
//		btn_relation_weibo.setOnClickListener(this);
//
//		btn_logout = (Button) findViewById(R.id.btn_logout);
//		btn_logout.setOnClickListener(this);
//		// 取消关联
//		btn_remove_weibo = (Button) findViewById(R.id.btn_remove_weibo);
//		btn_remove_weibo.setOnClickListener(this);
//		btn_remove_qq = (Button) findViewById(R.id.btn_remove_qq);
//		btn_remove_qq.setOnClickListener(this);
//
//		iv_back.setOnClickListener(this);
//		if (json != null && !json.equals("")) {
//			btn_relation_qq.setVisibility(View.GONE);
//			btn_relation_weibo.setVisibility(View.GONE);
//			tv_info.setVisibility(View.VISIBLE);
//		} else {
//			btn_relation_qq.setVisibility(View.VISIBLE);
//			btn_relation_weibo.setVisibility(View.VISIBLE);
//			tv_info.setVisibility(View.GONE);
//		}
//		// 请求个人信息
//		if (from != null) {
//			if (from.equals("weibo")) {
//				btn_remove_qq.setVisibility(View.GONE);
//				getWeiboInfo();
//			} else if (from.equals("qq")) {
//				btn_remove_weibo.setVisibility(View.GONE);
//				getQQInfo();
//			}
//		}
	}
	
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
	
	public void EnterSetting(View view) {
		Intent intent = new Intent(this, SettingActivity.class);
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

//	// 依次类推，想要获取QQ或者新浪微博其他的信息，开发者可自行根据官方提供的API文档，传入对应的参数即可
//	// QQ的API文档地址：http://wiki.connect.qq.com/api%E5%88%97%E8%A1%A8
//	// 微博的API文档地址：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
//	JSONObject obj;
//
//	/**
//	 * 获取微博的资料
//	 * 
//	 * @Title: getWeiboInfo
//	 * @Description: TODO
//	 * @param
//	 * @return void
//	 * @throws
//	 */
//	public void getWeiboInfo() {
//		// 根据http://open.weibo.com/wiki/2/users/show提供的API文档
//		new Thread() {
//			@Override
//			public void run() {
//				try {
//					obj = new JSONObject(json);
//					Map<String, String> params = new HashMap<String, String>();
//					if (obj != null) {
//						params.put("access_token", obj.getJSONObject("weibo")
//								.getString("access_token"));// 此为微博登陆成功之后返回的access_token
//						params.put("uid",
//								obj.getJSONObject("weibo").getString("uid"));// 此为微博登陆成功之后返回的uid
//					}
//					String result = NetUtils.getRequest(
//							"https://api.weibo.com/2/users/show.json", params);
//					Log.d("login", "微博的个人信息：" + result);
//					Message msg = new Message();
//					msg.obj = result;
//					handler.sendMessage(msg);
//
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//			}
//
//		}.start();
//	}
//
//	/**
//	 * 获取QQ的信息
//	 * 
//	 * @Title: getQQInfo
//	 * @Description: TODO
//	 * @param
//	 * @return void
//	 * @throws
//	 */
//	public void getQQInfo() {
//		// 若更换为自己的APPID后，仍然获取不到自己的用户信息，则需要
//		// 根据http://wiki.connect.qq.com/get_user_info提供的API文档，想要获取QQ用户的信息，则需要自己调用接口，传入对应的参数
//		new Thread() {
//			@Override
//			public void run() {
//				Map<String, String> params = new HashMap<String, String>();
//				params.put("access_token", "05636ED97BAB7F173CB237BA143AF7C9");// 此为QQ登陆成功之后返回access_token
//				params.put("openid", "B4F5ABAD717CCC93ABF3BF28D4BCB03A");
//				params.put("oauth_consumer_key", "222222");// oauth_consumer_key为申请QQ登录成功后，分配给应用的appid
//				params.put("format", "json");// 格式--非必填项
//				String result = NetUtils.getRequest(
//						"https://graph.qq.com/user/get_user_info", params);
//				Log.d("login", "QQ的个人信息：" + result);
//				Message msg = new Message();
//				msg.obj = result;
//				handler.sendMessage(msg);
//			}
//
//		}.start();
//	}
//
//	Handler handler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			String result = (String) msg.obj;
//			if (result != null) {
//				tv_info.setText((String) msg.obj);
//			} else {
//				tv_info.setText("暂无个人信息");
//			}
//		};
//	};
//
//	/**
//	 * 关联到当前用户用户
//	 * 
//	 * @Title: associateUser
//	 * @Description: TODO
//	 * @param
//	 * @return void
//	 * @throws
//	 */
//	private void associateUser(JSONObject totalJson) {
//		BmobUser.associateWithAuthDate(this,BmobUser.getCurrentUser(getApplicationContext()), totalJson,
//				new UpdateListener() {
//
//					@Override
//					public void onSuccess() {
//						// TODO Auto-generated method stub
//						toast("关联成功");
//					}
//
//					@Override
//					public void onFailure(int code, String msg) {
//						// TODO Auto-generated method stub
//						toast("关联失败：code =" + code + ",msg = " + msg);
//					}
//
//				});
//	}
//
//	/**
//	 * 取消QQ关联
//	 * 
//	 * @Title: dissociateQQAuthData
//	 * @Description: TODO
//	 * @param
//	 * @return void
//	 * @throws
//	 */
//	private void dissociateQQAuthData() {
//		BmobUser.dissociateQQAuthData(this,
//				BmobUser.getCurrentUser(getApplicationContext()),
//				new UpdateListener() {
//
//					@Override
//					public void onSuccess() {
//						// TODO Auto-generated method stub
//						toast("取消QQ关联成功");
//					}
//
//					@Override
//					public void onFailure(int code, String msg) {
//						// TODO Auto-generated method stub
//						if (code == 208) {// 208错误指的是没有绑定相应账户的授权信息
//							toast("你没有关联该QQ账号");
//						} else {
//							toast("取消QQ关联失败：code =" + code + ",msg = " + msg);
//						}
//					}
//				});
//	}
//
//	/**
//	 * 取消Weibo关联
//	 * 
//	 * @Title: dissociateWeiboAuthData
//	 * @Description: TODO
//	 * @param
//	 * @return void
//	 * @throws
//	 */
//	private void dissociateWeiboAuthData() {
//		BmobUser.dissociateWeiboAuthData(this,
//				BmobUser.getCurrentUser(getApplicationContext()),
//				new UpdateListener() {
//
//					@Override
//					public void onSuccess() {
//						// TODO Auto-generated method stub
//						toast("取消Weibo关联成功");
//					}
//
//					@Override
//					public void onFailure(int code, String msg) {
//						// TODO Auto-generated method stub
//						toast("取消Weibo关联失败：code =" + code + ",msg = " + msg);
//					}
//				});
//	}
//
//	// 这个是你所要关联的第三方账号返回的授权信息，这个方法用在：用户先使用bmob的用户系统进行登陆，之后，在使用分享功能之后得到的授权信息再和当前的bmob用户进行绑定
//	String weibo = "{\"weibo\":{\"uid\":\"2696876973\",\"expires_in\":1410548398554,\"access_token\":\"2.00htoVwCV9DWcB02e14b7fa50vUwjg\"}}";
//
//	String qq = "{\"qq\":{\"openid\":\"2F848CC297DCD3C0494E99DC71CECB16\",\"access_token\":\"C22C36515783D4DB80095D4E7AC72CB0\",\"expires_in\":7776000}}";
//
//	@Override
//	public void onClick(View arg0) {
//		// TODO Auto-generated method stub
//		if (arg0 == btn_relation_weibo) {
//			JSONObject obj;
//			try {
//				obj = new JSONObject(weibo);
//				associateUser(obj);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} else if (arg0 == btn_relation_qq) {
//			JSONObject obj;
//			try {
//				obj = new JSONObject(qq);
//				associateUser(obj);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} else if (arg0 == btn_remove_qq) {
//			dissociateQQAuthData();
//		} else if (arg0 == btn_remove_weibo) {
//			dissociateWeiboAuthData();
//		} else if (arg0 == iv_back) {
//			finish();
//		} else if (arg0 == btn_logout) {
//			BmobUser.logOut(this);
//			finish();
//		}
//	}
//
//	private void toast(String msg) {
//		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//	}

}
