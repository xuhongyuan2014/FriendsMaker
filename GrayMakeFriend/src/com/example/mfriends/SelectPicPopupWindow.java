package com.example.mfriends;


import com.example.main.DActivity;
import com.example.mfriends.R;
import com.example.model.UserInfo;
import com.example.utils.Constant;
import com.example.utils.DownImage;
import com.example.utils.HandlerConstants;
import com.example.utils.HttpEngine;
import com.example.utils.JSONUtils;
import com.example.utils.DownImage.ImageCallBack;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.weibo.oauthv2.OAuthV2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class SelectPicPopupWindow extends PopupWindow{

	private UserInfo userinfo=new UserInfo();
	private Button  btn_cancel;
	private View mMenuView;
	private LinearLayout my_photo,my_bank,my_info,userinfo_btn,condition_btn,config_btn,logout_btn;
	private SharedPreferences sp;
	private ImageView my_image;//top弹出菜单
	private TextView my_name;//top弹出菜单
	private TextView my_sign;//top弹出菜单
	private ImageView avatar;
	private Context mcontext;
	 public static final int  EXIT_APPLICATION = 0x0001;  
	public SelectPicPopupWindow(final Activity context,OnClickListener itemsOnClick,UserInfo userinfo) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.bottomdialog, null);
		mcontext=context;
		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
		my_image=(ImageView) mMenuView.findViewById(R.id.my_image);
		my_name=(TextView) mMenuView.findViewById(R.id.my_name);
		my_sign=(TextView) mMenuView.findViewById(R.id.my_sign);
		userinfo_btn=(LinearLayout) mMenuView.findViewById(R.id.userinfo_btn);
		condition_btn=(LinearLayout) mMenuView.findViewById(R.id.condition_btn);
		config_btn=(LinearLayout) mMenuView.findViewById(R.id.config_btn);
		logout_btn=(LinearLayout) mMenuView.findViewById(R.id.logout_btn);
		//my_name.setText(userinfo.getUsername());
		//my_sign.setText(userinfo.getIntro());
		//avatar=(ImageView) context.findViewById(R.id.avatar);
		//my_image.setImageDrawable(avatar.getDrawable());
/*		DownImage downImage = new DownImage(userinfo.getUserImg());
		downImage.loadImage(new ImageCallBack() {
			public void getDrawable(Drawable drawable) {

				// TODO Auto-generated method stub

				my_image.setImageDrawable(drawable);

			}
		});*/
		//取消按钮
		logout_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//销毁弹出框
				SaveDate.saveDate(context, new OAuthV2()); 
		        //获得实例对象  
		        sp = context.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		        sp.edit().putBoolean("AUTO_ISCHECK", false).commit();  
		        
		        context.finish(); 
	            Intent intent = new Intent(context, LoginActivity.class);  
	            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置,设置登陆界面为最前
	            context.startActivity(intent);
	            android.os.Process.killProcess(android.os.Process.myPid());  
		       // context.finish();
		        //Log.i("SelectPicPopupWindow", "结束");
			}
		});
		my_photo = (LinearLayout) mMenuView.findViewById(R.id.my_photo);
		my_photo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i("SelectPicPopupWindow", "我的相册");
				Intent intent=new Intent();
            	intent.setClass(context,LifeActivity.class);
            	context.startActivity(intent);
				dismiss();
			}
		});
		my_info = (LinearLayout) mMenuView.findViewById(R.id.my_info);
		my_info.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i("SelectPicPopupWindow", "个人信息");

			 	Intent intent=new Intent();
            	intent.setClass(context,ModifyInfoActivity.class);
            	context.startActivity(intent);
/*				Handler handler=(Handler) mMenuView.getTag();
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg); */
				dismiss();
			}
		});
		userinfo_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i("SelectPicPopupWindow", "个人资料");
			 	Intent intent=new Intent();
            	intent.setClass(context,ModifyInfoActivity.class);
            	context.startActivity(intent);
				dismiss();
			}
		});
		condition_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i("SelectPicPopupWindow", "征友条件");
			 	Intent intent=new Intent();
            	intent.setClass(context,ConditionActivity.class);
            	context.startActivity(intent);
				dismiss();
			}
		});
		config_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i("SelectPicPopupWindow", "设置");
			 	Intent intent=new Intent();
            	intent.setClass(context,ConfigActivity.class);
            	context.startActivity(intent);
				dismiss();
			}
		});
		httpGetUserinfo();
		//设置按钮监听
		//设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(w/2+50);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.mystyle);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}				
				return true;
			}
		});
	}
	
	

	/**
	 * 获取用户基本信息并显示
	 */
	public void httpGetUserinfo(){
		//ProgressDialogUtils.showProgressDialog(this, "数据加载中...");
		RequestParams params= new RequestParams();
		params.add("id",Constant.getUserId());   
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
               // msg.obj= "获取数据成功!"+userinfo.getUsername()+userinfo.getEmail()+userinfo.getIntro()+"页面|原始"+re.getUsername()+re.getEmail()+re.getIntro();
                msg.obj= "获取数据成功!"+userinfo.getUserImg();
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
	
	//线程执行结束后返回消息
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){//未使用
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HandlerConstants.GET_USER_INFO_OK:
				my_name.setText(userinfo.getUsername());
			    my_sign.setText(userinfo.getIntro());
				DownImage downImage = new DownImage(userinfo.getUserImg());
				downImage.loadImage(new ImageCallBack() {
					public void getDrawable(Drawable drawable) {

						// TODO Auto-generated method stub

						my_image.setImageDrawable(drawable);

					}
				});
				//Toast.makeText(DActivity.this, msg.obj.toString(),Toast.LENGTH_SHORT).show();
			break;
			case HandlerConstants.GET_USER_INFO_ERROR:
				//Toast.makeText(MainActivity.this, msg.obj.toString(),Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};
}
