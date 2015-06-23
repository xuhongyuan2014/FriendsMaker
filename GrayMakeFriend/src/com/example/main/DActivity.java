package com.example.main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.example.dialog.SelectPicture;
import com.example.mfriends.ConditionActivity;
import com.example.mfriends.ConfigActivity;
import com.example.mfriends.LifeActivity;
import com.example.mfriends.MainActivity;
import com.example.mfriends.ModifyInfoActivity;
import com.example.mfriends.OnViewChangeListener;
import com.example.mfriends.R;
import com.example.model.UserInfo;
import com.example.utils.Constant;
import com.example.utils.DownImage;
import com.example.utils.HandlerConstants;
import com.example.utils.HttpEngine;
import com.example.utils.ImageLoader;
import com.example.utils.JSONUtils;
import com.example.utils.ProgressDialogUtils;
import com.example.utils.UploadUtil;
import com.example.utils.DownImage.ImageCallBack;
import com.example.utils.UploadUtil.OnUploadProcessListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DActivity extends Activity implements OnUploadProcessListener{
	//个人中心控件等信息定义 begin --------------------------------------added by mrxu 3-13	
		private UserInfo userinfo=new UserInfo();
		LinearLayout myalbum_btn;
		LinearLayout condition_btn;
		LinearLayout userinfo_btn;
		LinearLayout config_btn;
		SelectPicture selectPicture;
		private TextView Center_TV_username;
		private TextView Center_TV_email;
		private TextView Center_TV_intro;
		private ImageView avatar;
		private ImageLoader imageLoader;
		
		/***
		 * 使用照相机拍照获取图片
		 */
		public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
		/***
		 * 使用相册中的图片
		 */
		public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
		
		/***
		 * 从Intent获取图片路径的KEY
		 */
		public static final String KEY_PHOTO_PATH = "photo_path";

		/**获取到的图片路径*/
		private String picPath;
			
		private Uri photoUri;
		
		/**
		 * 获取相片路径
		 */
		private static final int PHOTO_URL=103;
		 public static final int  EXIT_APPLICATION = 0x0001;  
		//个人中心控件等信息定义 end  --------------------------------------added by mrxu 3-13
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	/*	TextView tv = new TextView(this);
		tv.setText("This is D Activity!");
		tv.setGravity(Gravity.CENTER);*/
		setContentView(R.layout.individualcenter);
		//个人中心控件事件监听 begin ---added by mrxu 3-13
		myalbum_btn=(LinearLayout)findViewById(R.id.myalbum_btn);
		condition_btn=(LinearLayout)findViewById(R.id.condition_btn);
		userinfo_btn=(LinearLayout)findViewById(R.id.userinfo_btn);
		config_btn=(LinearLayout)findViewById(R.id.config_btn);				
		avatar=(ImageView)findViewById(R.id.avatar);
		Center_TV_username=(TextView)findViewById(R.id.Center_TV_username);
		Center_TV_email=(TextView)findViewById(R.id.Center_TV_email);
		Center_TV_intro=(TextView)findViewById(R.id.Center_TV_intro);
        
		
		 imageLoader = new ImageLoader(DActivity.this);
		
		
		myalbum_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
			 	Intent intent=new Intent();
            	intent.setClass(DActivity.this,LifeActivity.class);
            	startActivity(intent);
			}
		});
		condition_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
			 	Intent intent=new Intent();
            	intent.setClass(DActivity.this,ConditionActivity.class);
            	startActivity(intent);
			}
		});
		userinfo_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
			 	Intent intent=new Intent();
            	intent.setClass(DActivity.this,ModifyInfoActivity.class);
            	startActivity(intent);
			}
		});
		config_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
			 	Intent intent=new Intent();
            	intent.setClass(DActivity.this,ConfigActivity.class);
            	startActivity(intent);
			}
		});


		avatar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showPictureDialog(DActivity.this, avatar);
				//finish();
				//uploadImage(ModifyInfoActivity.this);
			}
		});

		//个人中心控件事件监听 end
		
		
		httpGetUserinfo();//初始化用户个人基本信息	       
		
		
	}
	
	
	/*------------------------------------------------------------------------------------------------------------------个人中心所用方法 begin             added by mrxu*/
	/**显示头像修改菜单
	 * @param context
	 * @param view
	 */
	public void showPictureDialog(final Activity context,View view){
		selectPicture = new SelectPicture(context,handler);
		// 显示窗口
		View showview = view;
		// 计算坐标的偏移量
		int xoffInPixels = selectPicture.getWidth() - showview.getWidth() + 10;
		selectPicture.showAsDropDown(showview, xoffInPixels, 0);
		
	};
	
	
	/**选择图片后返回处理
	 * @param context
	 * @param view
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK)
		{
			doPhoto(requestCode,data);
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 选择图片后，获取图片的路径
	 * @param requestCode
	 * @param data
	 */
	private void doPhoto(int requestCode,Intent data)
	{
		if(requestCode == SELECT_PIC_BY_PICK_PHOTO)  //从相册取图片，有些手机有异常情况，请注意
		{
			if(data == null)
			{
				Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
			photoUri = data.getData();
			if(photoUri == null )
			{
				Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
		}
		//photoUri =Uri.parse(data.getStringExtra(android.provider.MediaStore.EXTRA_OUTPUT));//added by mrxu 3-19
		String[] pojo = {MediaStore.Images.Media.DATA};
		Cursor cursor = managedQuery(photoUri, pojo, null, null,null);   
		if(cursor != null )
		{
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToFirst();
			picPath = cursor.getString(columnIndex);
			
			
			try  
            {  
                //4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)  
                if(Integer.parseInt(Build.VERSION.SDK) < 14)  
                {  
                    cursor.close();  
                }  
            }catch(Exception e)  
            {  
                Log.i("DActivity", "error:" + e);  
            }  
			
			//cursor.close();
		}
		Log.i("AlbumActivity", "imagePath = "+picPath);
		if(picPath != null && ( picPath.endsWith(".png") || picPath.endsWith(".PNG") ||picPath.endsWith(".jpg") ||picPath.endsWith(".JPG")  ))
		{
			Log.i("AlbumActivity", "最终选择的图片="+picPath);
			Bitmap bm = BitmapFactory.decodeFile(picPath);
			/*添加处理图片代码 begin*/
			
			avatar.setImageBitmap(bm);
			if(picPath!=null)
			{
				//handler.sendEmptyMessage(TO_UPLOAD_FILE);
				toUploadFile();
			}else{
				Toast.makeText(this, "上传的文件路径出错", Toast.LENGTH_LONG).show();
			}
			
			/*添加处理图片代码 end*/
/*			lastIntent.putExtra(KEY_PHOTO_PATH, picPath);
			setResult(Activity.RESULT_OK, lastIntent);
			finish();*/
		}else{
			Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 获取用户基本信息并显示
	 */
	public void httpGetUserinfo(){
		//ProgressDialogUtils.showProgressDialog(this, "数据加载中...");
		RequestParams params= new RequestParams();
		params.add("id",Constant.UserId);   
		HttpEngine.getHttpEngine().get(Constant.getBaseInfoById, params, new AsyncHttpResponseHandler(){
			@Override
		    public void onSuccess(String result) {
				// 关闭进度条
			//	ProgressDialogUtils.dismissProgressDialog();
				 if (result != null) {
					 //String info = JSON.parseObject(result).get("")
					//将其转化成java bean
				       UserInfo re= JSONUtils.jsonToEntity(result,UserInfo.class);
				if(re.getUsername()!=null){/*Center_TV_username.setText(re.getUsername());*/userinfo.setUsername(re.getUsername());	}
				if(re.getEmail()!=null){/*Center_TV_email.setText(re.getEmail());*/userinfo.setEmail(re.getEmail());}
				if(re.getIntro()!=null){/*Center_TV_intro.setText(re.getIntro());*/userinfo.setIntro(re.getIntro());}
				//Toast.makeText(MainActivity.this, re.getUsername()+re.getEmail()+re.getIntro(),Toast.LENGTH_SHORT).show();
				// 接口回调的方法，完成图片的读取;
				if(re.getUserImg()!=null){userinfo.setUserImg(re.getUserImg());}
	/*			DownImage downImage = new DownImage(re.getUserImg());
				downImage.loadImage(new ImageCallBack() {
					public void getDrawable(Drawable drawable) {

						// TODO Auto-generated method stub

						avatar.setImageDrawable(drawable);

					}

				});*/
                Message msg=new Message();
                msg.what=HandlerConstants.GET_USER_INFO_OK;
                msg.obj= "获取数据成功!"+userinfo.getUsername()+userinfo.getEmail()+userinfo.getIntro()+"页面|原始"+re.getUsername()+re.getEmail()+re.getIntro();
               // msg.obj= "获取数据成功!"+userinfo.getUserImg();
                handler.sendMessage(msg);
				
				 } else {
	                   Message msg=new Message();
	                     msg.what=HandlerConstants.GET_USER_INFO_ERROR;
	                     msg.obj= "获取数据错误!";
	                     handler.sendMessage(msg);
					 
						//Toast.makeText(MainActivity.this, "获取数据错误",Toast.LENGTH_SHORT).show();
					}
		    }
			
		     @Override
             public void onFailure(Throwable error) {
		    	 if(Constant.Debug){
						userinfo.setUsername("mrxu");	
						userinfo.setEmail("mrxu@163.com");
						userinfo.setIntro("个人简介");
						userinfo.setUserImg("");
		    	 }
		    	// 关闭进度条
					//ProgressDialogUtils.dismissProgressDialog();
                     error.printStackTrace();
                     Message msg=new Message();
                     msg.what=HandlerConstants.GET_USER_INFO_ERROR;
                     msg.obj= "请求超时,请检查网络!";
                     handler.sendMessage(msg);
                    // Toast.makeText(MainActivity.this, "请求超时,请检查网络!",Toast.LENGTH_SHORT).show();
             }
		});
		
	}
	/**
	 * 上传服务器响应回调
	 */
	@Override
	public void onUploadDone(int responseCode, String message) {
		//progressDialog.dismiss();
		Message msg = Message.obtain();
		//msg.what = UPLOAD_FILE_DONE;
		msg.arg1 = responseCode;
		msg.obj = message;
		
		String imageUrl=message;
		updateUserImage(imageUrl);
		//handler.sendMessage(msg);
	}
	
	private void toUploadFile()
	{
		//uploadImageResult.setText("正在上传中...");
		//progressDialog.setMessage("正在上传文件...");
		//progressDialog.show();
		String fileKey = "img";
		UploadUtil uploadUtil = UploadUtil.getInstance();;
		uploadUtil.setOnUploadProcessListener(this);  //设置监听器监听上传状态
		
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("orderId", "11111");
		params.put("uploadNum", "1");
		File file = new File(picPath);
		params.put("filename",uploadUtil.generateFileName()+file.getName() );
		uploadUtil.uploadFile( picPath,fileKey, Constant.uploadImage,params);
	}
	
	
	//线程执行结束后返回消息
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){//未使用
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HandlerConstants.TO_UPLOAD_FILE:
				toUploadFile();
				break;
			
			case HandlerConstants.UPLOAD_INIT_PROCESS:
				//progressBar.setMax(msg.arg1);
				break;
			case HandlerConstants.UPLOAD_IN_PROCESS:
				//progressBar.setProgress(msg.arg1);
				break;
			case HandlerConstants.UPLOAD_FILE_DONE:
				String result=(String) msg.obj;
				Log.i("*********", result);
				break;
			case 10:
				Toast.makeText(DActivity.this, "上传成功",Toast.LENGTH_SHORT).show();
 				// 广播通知  
		         Intent intent = new Intent();  
		         intent.setAction("action.refresh");  
		         sendBroadcast(intent); 
				break;
			case 11:
				Toast.makeText(DActivity.this, "上传失败",Toast.LENGTH_SHORT).show();
				break;
			case 12:
				Toast.makeText(DActivity.this, "请求超时,请检查网络!",Toast.LENGTH_SHORT).show();
				break;
			case HandlerConstants.GET_USER_INFO_OK:
				Center_TV_username.setText(userinfo.getUsername());
			    Center_TV_email.setText(userinfo.getEmail());
				Center_TV_intro.setText(userinfo.getIntro());
				
				/*String url = userinfo.getUserImg();
				imageLoader.DisplayImage(url,avatar);*/
				DownImage downImage = new DownImage(userinfo.getUserImg());
				downImage.loadImage(new ImageCallBack() {
					public void getDrawable(Drawable drawable) {

						// TODO Auto-generated method stub

						avatar.setImageDrawable(drawable);

					}
				});
			//	Toast.makeText(DActivity.this, msg.obj.toString(),Toast.LENGTH_SHORT).show();
			break;
			case HandlerConstants.GET_USER_INFO_ERROR:
				Center_TV_username.setText(userinfo.getUsername());
			    Center_TV_email.setText(userinfo.getEmail());
				Center_TV_intro.setText(userinfo.getIntro());
				//Toast.makeText(MainActivity.this, msg.obj.toString(),Toast.LENGTH_SHORT).show();
				break;

			case PHOTO_URL:
				photoUri=(Uri) msg.obj;
				Toast.makeText(DActivity.this, photoUri.toString(),Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};

	@Override
	public void onUploadProcess(int uploadSize) {
		Message msg = Message.obtain();
		msg.what = HandlerConstants.UPLOAD_IN_PROCESS;
		msg.arg1 = uploadSize;
		//handler.sendMessage(msg );
	}

	@Override
	public void initUpload(int fileSize) {
		Message msg = Message.obtain();
		msg.what = HandlerConstants.UPLOAD_INIT_PROCESS;
		msg.arg1 = fileSize;
		//handler.sendMessage(msg );
	}
	public void updateUserImage(String imageUrl){//更新用户头像
		/*UserInfo user=new UserInfo();
		user.setId(userinfo.getId());
		user.setUserImg(imageUrl);*/		
		//ProgressDialogUtils.showProgressDialog(this, "正在上传头像...");
		RequestParams params= new RequestParams();
		params.add("id",Constant.getUserId()); 
		params.add("userImg",imageUrl);  
		HttpEngine.getHttpEngine().get(Constant.updateUserImage, params, new AsyncHttpResponseHandler(){
			@Override
		    public void onSuccess(String result) {
				// 关闭进度条
				ProgressDialogUtils.dismissProgressDialog();
				 if (result != null) {					
					//将其转化成java bean
					 Message msg = Message.obtain();
						msg.what = 10;
					 handler.sendMessage(msg);
					// Toast.makeText(MainActivity.this, "上传成功",Toast.LENGTH_SHORT).show();
				 } else {
					 Message msg = Message.obtain();
						msg.what = 11;
					 handler.sendMessage(msg);
						//Toast.makeText(MainActivity.this, "获取数据错误",Toast.LENGTH_SHORT).show();
					}
		    }						
		     @Override
             public void onFailure(Throwable error) {
		    	// 关闭进度条
					//ProgressDialogUtils.dismissProgressDialog();
                     error.printStackTrace();
                     Message msg = Message.obtain();
						msg.what = 12;
					 handler.sendMessage(msg);
                   //  Toast.makeText(MainActivity.this, "请求超时,请检查网络!",Toast.LENGTH_SHORT).show();
             }
		});	
	}


	
	
	
	/*-------------------------------------------------------------------------------------------------------------------------------个人中心所用方法 end*/
}
