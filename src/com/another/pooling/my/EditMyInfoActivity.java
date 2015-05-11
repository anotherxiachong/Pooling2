package com.another.pooling.my;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import sun.geoffery.uploadpic.CircleImg;
import sun.geoffery.uploadpic.FileUtil;
import sun.geoffery.uploadpic.SelectPicPopupWindow;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.another.pooling.R;
import com.another.pooling.SettingActivity;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.example.testpic.PublishedActivity;

public class EditMyInfoActivity extends Activity {
	
	private CircleImg avatarImg;// 头像图片
	private SelectPicPopupWindow menuWindow; // 自定义的头像编辑弹出框
	private static final int REQUESTCODE_PICK = 0;		// 相册选图标记
	private static final int REQUESTCODE_TAKE = 1;		// 相机拍照标记
	private static final int REQUESTCODE_CUTTING = 2;	// 图片裁切标记
	private static final String IMAGE_FILE_NAME = "avatarImage.jpg";// 头像文件名称
	private String urlpath;			// 图片本地路径
	private String ObjectId;
	private EditText nickname;
	private EditText sex;
	private EditText age;
	private TextView save;
	private LinearLayout saveicon;
	private Bitmap bitmap;
	private String urlpre = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_my_info);
		
		avatarImg = (CircleImg) findViewById(R.id.avatarImg);
		avatarImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				menuWindow = new SelectPicPopupWindow(EditMyInfoActivity.this, itemsOnClick);  
				menuWindow.showAtLocation(findViewById(R.id.mainLayout), 
						Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); 
			}
		});
		
		initView();
	}

	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if (msg.what==0x9527) {
				//显示从网上下载的图片
				avatarImg.setImageBitmap(bitmap);
			}
		}         
	};
	
	private void initView() {
		// TODO Auto-generated method stub
		nickname = (EditText) findViewById(R.id.nickname_et_edit);
		sex = (EditText) findViewById(R.id.sex_et_edit);
		age = (EditText) findViewById(R.id.age_et_edit);
		save = (TextView) findViewById(R.id.save_tv_edit);
		saveicon = (LinearLayout) findViewById(R.id.save_icon_edit);
		save.setOnClickListener(SaveListener);
		saveicon.setOnClickListener(saveIconListener);
		BmobUser bmobUser = BmobUser.getCurrentUser(this);
		String username = bmobUser.getUsername();
		BmobQuery<UserInfo> bmobQuery = new BmobQuery<UserInfo>();
		bmobQuery.addWhereEqualTo("username", username);
		bmobQuery.findObjects(EditMyInfoActivity.this, new FindListener<UserInfo>() {
			
			@Override
			public void onSuccess(List<UserInfo> arg0) {
				// TODO Auto-generated method stub
				for(UserInfo userInfo : arg0) {
					nickname.setText(userInfo.getNickname());
					sex.setText(userInfo.getSex());
					age.setText(userInfo.getAge());
					urlpre = userInfo.getUsericon();
					ObjectId = userInfo.getObjectId();
				}
				
				new Thread(){
		            @Override
		            public void run() {
		            	try {
		            		if(urlpre == null || urlpre.equals("null")) {
		            			Toast.makeText(EditMyInfoActivity.this, "未设置头像", Toast.LENGTH_LONG).show();
		            		} else {
		            			//创建一个url对象
		            			URL url=new URL(urlpre);
		            			//打开URL对应的资源输入流
		            			InputStream is= url.openStream();
		            			//从InputStream流中解析出图片
		            			bitmap = BitmapFactory.decodeStream(is);
		            			//  imageview.setImageBitmap(bitmap); 
		            			//发送消息，通知UI组件显示图片
		            			handler.sendEmptyMessage(0x9527);
		            			//关闭输入流
		            			is.close();
		            		}
		            	} catch (Exception e) {
		            		e.printStackTrace();
		            	}
		            }          
				}.start();
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(EditMyInfoActivity.this, "请检查网络", Toast.LENGTH_LONG).show();
			}
		});
	}

	private OnClickListener itemsOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			// 拍照
			case R.id.takePhotoBtn:
				Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				//下面这句指定调用相机拍照后的照片存储的路径
				takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, 
						Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
				startActivityForResult(takeIntent, REQUESTCODE_TAKE);
				break;
				// 相册选择图片
			case R.id.pickPhotoBtn:
				Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
				// 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
				pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(pickIntent, REQUESTCODE_PICK);
				break;
			default:
				break;
			}
		}
	}; 
	
	private OnClickListener SaveListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			UserInfo userInfo = new UserInfo();
			userInfo.setNickname(nickname.getText().toString());
			userInfo.setSex(sex.getText().toString());
			userInfo.setAge(age.getText().toString());
			userInfo.setAlwaysaddres("暂未设置");
			userInfo.setSignature("这个人很懒，什么都没有留下");
			userInfo.update(EditMyInfoActivity.this, ObjectId, new UpdateListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					Toast.makeText(EditMyInfoActivity.this, "更新成功", Toast.LENGTH_LONG).show();
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					Toast.makeText(EditMyInfoActivity.this, "请检查网络", Toast.LENGTH_LONG).show();
				}
			});
		}
	}; 
	
	private OnClickListener saveIconListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(urlpath == null) {
				Toast.makeText(EditMyInfoActivity.this, "请设置头像", Toast.LENGTH_LONG).show();
			} else {
				BmobProFile.getInstance(EditMyInfoActivity.this).upload(urlpath, new UploadListener() {
					
					@Override
					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(EditMyInfoActivity.this, "请检查网络", Toast.LENGTH_LONG).show();
					}
					
					@Override
					public void onSuccess(String arg0, String arg1) {
						// TODO Auto-generated method stub
						updateIconInfo(arg0, arg1);
					}

					@Override
					public void onProgress(int arg0) {
						// TODO Auto-generated method stub
					}
				});
			}
		}
	};
	
	private void updateIconInfo(String arg0, String arg1) {
		// TODO Auto-generated method stub
		UserInfo userInfo = new UserInfo();
		userInfo.setUsericon(getURL(arg0, arg1));
		userInfo.update(EditMyInfoActivity.this, ObjectId, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				Toast.makeText(EditMyInfoActivity.this, "头像保存成功", Toast.LENGTH_LONG).show();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(EditMyInfoActivity.this, "请检查网络", Toast.LENGTH_LONG).show();
			}
		});
	}
	
	public String getURL(String arg0, String arg1) {
		String temp = BmobProFile.getInstance(EditMyInfoActivity.this).signURL(arg0, arg1,
						 "404fbf032078806733460a0b0cd4e6ab", 0, null);
		return temp;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case REQUESTCODE_PICK:// 直接从相册获取
			try {		
				startPhotoZoom(data.getData());
			} catch (NullPointerException e) {
				e.printStackTrace();// 用户点击取消操作
			}
			break;
		case REQUESTCODE_TAKE:// 调用相机拍照
			File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
			startPhotoZoom(Uri.fromFile(temp));
			break;
		case REQUESTCODE_CUTTING:// 取得裁剪后的图片
			if (data != null) {
				setPicToView(data);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 裁剪图片方法实现
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, REQUESTCODE_CUTTING);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			// 取得SDCard图片路径做显示
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(null, photo);
			urlpath = FileUtil.saveFile(EditMyInfoActivity.this, "temphead.jpg", photo);
			avatarImg.setImageDrawable(drawable);

			// 新线程后台上传服务端
			//			pd = ProgressDialog.show(mContext, null, "正在上传图片，请稍候...");
			//			new Thread(uploadImageRunnable).start();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_my_info, menu);
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
