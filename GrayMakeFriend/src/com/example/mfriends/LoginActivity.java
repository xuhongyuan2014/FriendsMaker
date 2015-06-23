package com.example.mfriends;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.dialog.SelectBirthday;
import com.example.dialog.SelectHeight;
import com.example.dialog.SelectText;
import com.example.main.MainTabActivity;
import com.example.model.ResultForm;
import com.example.model.UserInfo;
import com.example.utils.Constant;
import com.example.utils.HttpEngine;
import com.example.utils.JSONUtils;
import com.example.utils.ProgressDialogUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.weibo.oauthv2.OAuthV2;


public class LoginActivity extends Activity {
	 private EditText username, password;  
	    private CheckBox cb_rem, cb_auto;  
	    private TextView login_btn,register_btn;  
	    private ImageView btnQuit;
	    private String userNameValue,passwordValue;  
	    private SharedPreferences sp;  
	    private Editor editor;
	    private SharedPreferences  share;
	    public void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	       share = getSharedPreferences("perference", MODE_PRIVATE);
			 editor = share.edit();// 取得编辑器	 
	        
	        
	        
	        
	        
	      //判断是否第一次使用app
		      Boolean user_first = share.getBoolean("FIRST",true);
		      if(user_first){//第一次
		    	  editor.putBoolean("FIRST", false).commit();
		        	//第一次使用，设置语言
		            new  AlertDialog.Builder(LoginActivity.this)  
		            .setTitle("请选择语言" )  
		            .setIcon(android.R.drawable.ic_dialog_info)                  
		            .setSingleChoiceItems(new  String[] {"中文", "English", "Z"  },  0 ,   
		              new  DialogInterface.OnClickListener() {  
		                 public   void  onClick(DialogInterface dialog,  int  which) {  
		                    
		                    if(which==0){
		                    	editor.putString("language", "cn");// 存储配置 语言选择中文	
		                    }
		                    else if(which==1){
		                    	editor.putString("language", "en");// 存储配置 语言选择English
		                    }
		                    else if(which==2){
		                    	editor.putString("language", "ti");// 存储配置 语言选择Z
		                    }
		                   
		                    editor.commit();// 将语言选择结果存入xml
		                    dialog.dismiss(); 
		                    setlanguage();
		                 }  
		              }  
		            ).show();   
		       }else{
		    	   setlanguage();  
		       }
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	       // setlanguage();
	        //去除标题  
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
	        setContentView(R.layout.login);  
	       
	        //获得实例对象  
	        sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);  
	        username = (EditText) findViewById(R.id.ET_username);  
	        password = (EditText) findViewById(R.id.ET_password);  
	        cb_rem = (CheckBox) findViewById(R.id.CB_rememberme);  
	        cb_auto = (CheckBox) findViewById(R.id.CB_autologin);  
	        login_btn = (TextView) findViewById(R.id.login_btn);  
	        register_btn = (TextView)findViewById(R.id.register_btn);  
	        btnQuit = (ImageView) findViewById(R.id.btnQuit);  
	          
	        //判断记住密码多选框的状态  
	      if(sp.getBoolean("ISCHECK", false))  
	        {  
	          //设置默认是记录密码状态  
	    	  cb_rem.setChecked(true);  
	    	  username.setText(sp.getString("USER_NAME", ""));  
	    	  userNameValue=sp.getString("USER_NAME", "");
	          password.setText(sp.getString("PASSWORD", ""));  
	          passwordValue=sp.getString("PASSWORD", "");
	          //判断自动登陆多选框状态  
	          if(sp.getBoolean("AUTO_ISCHECK", false))  
	          {  
	                 //设置默认是自动登录状态  
	        	  cb_auto.setChecked(true);  
	              /*  //跳转界面  
	                Intent intent = new Intent(LoginActivity.this,MainActivity.class);  
	                startActivity(intent); */ 
	        	  checkAccount(userNameValue, passwordValue);
	                  
	          }  
	        }  
	          
	        // 登录监听事件   
	      login_btn.setOnClickListener(new OnClickListener() {  
	  
	            public void onClick(View v) {  
	                userNameValue = username.getText().toString();  
	                passwordValue = password.getText().toString();  
	                checkAccount(userNameValue, passwordValue);
/*                if(checkAccount(userNameValue, passwordValue))  
	                {  
	                    Toast.makeText(LoginActivity.this,"登录成功", Toast.LENGTH_SHORT).show();  
	                    //登录成功和记住密码框为选中状态才保存用户信息  
	                    if(cb_rem.isChecked())  
	                    {  
	                     //记住用户名、密码、  
	                      Editor editor = sp.edit();  
	                      editor.putString("USER_NAME", userNameValue);  
	                      editor.putString("PASSWORD",passwordValue);  
	                      editor.commit();  
	                    }  
	                    //跳转界面  
	                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);  
	                    LoginActivity.this.startActivity(intent);  
	                    finish();  
	                      
	                }else{  
	                      
	                    Toast.makeText(LoginActivity.this,"用户名或密码错误，请重新登录", Toast.LENGTH_LONG).show();  
	                } */ 
	                  
	            }  
	        });  
	  
	        //监听记住密码多选框按钮事件  
	      cb_rem.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
	            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {  
	                if (cb_rem.isChecked()) {  
	                      
	                    System.out.println("记住密码已选中");  
	                    sp.edit().putBoolean("ISCHECK", true).commit();  
	                      
	                }else {  
	                      
	                    System.out.println("记住密码没有选中");  
	                    sp.edit().putBoolean("ISCHECK", false).commit();  
	                      
	                }  
	  
	            }  
	        });  
	          
	        //监听自动登录多选框事件  
	      cb_auto.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
	            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {  
	                if (cb_auto.isChecked()) {  
	                    System.out.println("自动登录已选中");  
	                    sp.edit().putBoolean("AUTO_ISCHECK", true).commit();  
	  
	                } else {  
	                    System.out.println("自动登录没有选中");  
	                    sp.edit().putBoolean("AUTO_ISCHECK", false).commit();  
	                }  
	            }  
	        });  
	        register_btn.setOnClickListener(new OnClickListener() {  
	              
	            @Override  
	            public void onClick(View v) {  
	            	   //跳转界面  
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);  
                    LoginActivity.this.startActivity(intent); 
	            }  
	        });  
	        btnQuit.setOnClickListener(new OnClickListener() {  
	              
	            @Override  
	            public void onClick(View v) {  
	                finish();  
	            }  
	        });  
	  
	    }  
		public void checkAccount(String username,String password){
			
			RequestParams params= new RequestParams();
			params.add("username",username); 
			params.add("password",password);  
			String returncode="";
			
			HttpEngine.getHttpEngine().get(Constant.login, params, new AsyncHttpResponseHandler(){
				@Override
			    public void onSuccess(String result) {

					 if (result != null) {					
						 //UserInfo user= JSONUtils.jsonToEntity(result,UserInfo.class);	//取ResultForm其中的data对应的对象					 
						 String info = JSON.parseObject(result).getString("info");
						 String infoText = JSON.parseObject(result).getString("infoText");
						 String tokenId = JSON.parseObject(result).getString("tokenId");
						 String data = JSON.parseObject(result).getString("data");
						  if(info.equals("-1")){
                        	 Toast.makeText(LoginActivity.this, infoText,Toast.LENGTH_SHORT).show();                   	 
                         }
                         else {                   
     	                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.loginsuccess), Toast.LENGTH_SHORT).show();  
    	                    //登录成功和记住密码框为选中状态才保存用户信息  
    	                    if(cb_rem.isChecked())  
    	                    {  
    	                     //记住用户名、密码、  
    	                      Editor editor = sp.edit();  
    	                      editor.putString("USER_NAME", userNameValue);  
    	                      editor.putString("PASSWORD",passwordValue);  
    	                      editor.commit();  
    	                    }  
    	                    
    	                    Constant.setToken(tokenId);
    	                    Constant.setUserId(data);
    	                    
    	                    
    	                if(info.equals("1")){//已开通交友直接登录
    	                    //跳转界面  
    	                    Intent intent = new Intent(LoginActivity.this,MainTabActivity.class);  
    	                    finish();
    	                    LoginActivity.this.startActivity(intent);  
    	                    //finish();  
    	                	
    	                }
    	                if(info.equals("2")){//未开通交友需要完善信息
    	                    //跳转界面  
    	                    Intent intent = new Intent(LoginActivity.this,ModifyInfoActivity.class);  
    	                    finish();
    	                    LoginActivity.this.startActivity(intent);  
    	                    
    	                   // finish();  
    	                	
    	                }

    	                    
                         }
						// Toast.makeText(MainActivity.this, "上传成功",Toast.LENGTH_SHORT).show();
					 } else {

							Toast.makeText(LoginActivity.this, getResources().getString(R.string.dataerror),Toast.LENGTH_SHORT).show();
						}
			    }						
			     @Override
	             public void onFailure(Throwable error) {
			    	 if(Constant.Debug){
			    		 Constant.UserId="007";
			    		 //跳转界面  
 	                    Intent intent = new Intent(LoginActivity.this,MainTabActivity.class);  
 	                    finish();
 	                    LoginActivity.this.startActivity(intent);  
			    	 }
	                     error.printStackTrace();
	                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.timeout),Toast.LENGTH_SHORT).show();
	             }
			});	
			
			
			
/*			
			if(username.equals(password))
				return true;
			else return false;*/
			
		}

		
		
		public void setlanguage(){
			
			  String language = share.getString("language", "");//读取上次保存的语言设置

				Resources resources = getResources();//获得res资源对象

		        Configuration config = resources.getConfiguration();//获得设置对象

		        DisplayMetrics dm = resources .getDisplayMetrics();//获得屏幕参数：主要是分辨率，像素等。

		        
		      if(language.equals("en"))  
		    	  {
		    	
		    	  config.locale = Locale.ENGLISH; //英文
		    	 
		    	  }
		      else if(language.equals("ti"))
		      {

		    	  config.locale = new Locale("bo"); //藏文
		    	  resources.updateConfiguration(config, dm);
		    	  }
		      else {

		    	  config.locale = Locale.SIMPLIFIED_CHINESE; //简体中文
		    	
		    	  }
		      resources.updateConfiguration(config, dm);
              
		     /*   if("ti".equals(language))config.locale = new Locale("bo"); //藏文
		        else if("en".equals(language))config.locale = Locale.ENGLISH; //英文
		        else{
		        	config.locale = Locale.SIMPLIFIED_CHINESE; //简体中文
		        	
		        }*/
		 
		      
			 
			 
		}
		
		
	}



