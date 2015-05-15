package com.example.mfriends;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.dialog.SelectArray;
import com.example.dialog.SelectBirthday;
import com.example.dialog.SelectChangePassword;
import com.example.dialog.SelectCity;
import com.example.dialog.SelectHeight;
import com.example.dialog.SelectLanguage;
import com.example.dialog.SelectText;
import com.example.main.MainTabActivity;
import com.tencent.weibo.oauthv2.OAuthV2;


public class ConfigActivity extends Activity {
	//表单显示元素
	private TextView TV_password;
	private TextView TV_about;
	private TextView TV_language;
    //表单行定义
	private TableRow TableRowPassword;
	private TableRow TableRowAbout;
	private TableRow TableRowLanguage;

	//菜单栏按钮
	private TextView back;
	private TextView save;
	private TextView changeaccount_btn;



	//自定义的表单弹出框
	SelectBirthday birthdayDialog;
	SelectHeight heightDialog;
	SelectText textDialog;
	SelectChangePassword changePassDialog;
	SelectCity cityDialog;
	SelectArray arrayDialog;
	SelectLanguage languageDialog;
	 public static final int  EXIT_APPLICATION = 0x0001;  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.config_info);
		//初始化表单元素 begin
		TV_password=(TextView)findViewById(R.id.TV_password);
		TV_about=(TextView)findViewById(R.id.TV_about);
		TV_language=(TextView) findViewById(R.id.TV_language);

		TableRowPassword=(TableRow)findViewById(R.id.TableRowPassword);
		TableRowAbout=(TableRow)findViewById(R.id.TableRowAbout);
		TableRowLanguage=(TableRow) findViewById(R.id.TableRowLanguage);
		
		
		 SharedPreferences  share = getSharedPreferences("perference",MODE_PRIVATE);
		  Editor editor = share.edit();// 取得编辑器	 
		  String language = share.getString("language", "");//读取上次保存的语言设置
		  if(language.equals("en"))TV_language.setText("English");
		  else if(language.equals("ti"))TV_language.setText("Tibetan");
		  else TV_language.setText("简体中文");
		
		
		//初始化表单元素end
	back = (TextView) findViewById(R.id.back_btn);
		save = (TextView) findViewById(R.id.save_btn);
		changeaccount_btn = (TextView) findViewById(R.id.changeaccount_btn);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
				//uploadImage(ModifyInfoActivity.this);
			}
		});
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//uploadImage2(ModifyInfoActivity.this);
			}
		});
		changeaccount_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//uploadImage2(ModifyInfoActivity.this);
         	   //跳转到登陆界面  
				SharedPreferences sp;
		        sp = ConfigActivity.this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		        sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
/*                Intent intent = new Intent(ConfigActivity.this, LoginActivity.class);  
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置,设置登陆界面为最前
                ConfigActivity.this.startActivity(intent); */
		        Intent mIntent = new Intent();  
		        mIntent.setClass(ConfigActivity.this, MainTabActivity.class);  
		        //这里设置flag还是比较 重要的  
		        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
		        //发出退出程序指示  
		        mIntent.putExtra("flag", EXIT_APPLICATION);  
		        finish();
		        startActivity(mIntent);  
			}
		});

		//表单元素设置监听 begin
		TableRowPassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showChangePassDialog(ConfigActivity.this,TV_password,getResources().getString(R.string.accountpassword));
			}
		});
/*		TableRowAbout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showTextDialog(ConfigActivity.this,TV_about,getResources().getString(R.string.abountus));
			}
		});*/
		TableRowLanguage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showLanguageDialog(ConfigActivity.this,TV_language,getResources().getString(R.string.chooselanguage),R.array.language);
			}
		});

		//表单元素设置监听 end
		

	}

	
	public void showNickNameDialog(final Activity context,View view){
		
		
	};
   public void showBirthDayDialog(final Activity context,View view){
	   birthdayDialog=new SelectBirthday(context, view);
	   birthdayDialog.showAtLocation(ConfigActivity.this.findViewById(R.id.userInfo_mainlayout),
				Gravity.BOTTOM, 0, 0);
	};
	   public void showHeightDialog(final Activity context,View view){
		   heightDialog=new SelectHeight(context, view);
		   heightDialog.showAtLocation(ConfigActivity.this.findViewById(R.id.userInfo_mainlayout),
					Gravity.BOTTOM, 0, 0);
		};
		   public void showTextDialog(final Activity context,View view,String title){
			   textDialog=new SelectText(context, view,title);
			   textDialog.showAtLocation(view,
						Gravity.CENTER, 0, 0);
			};
			   public void showChangePassDialog(final Activity context,View view,String title){
				   changePassDialog=new SelectChangePassword(context, view,title);
				   changePassDialog.showAtLocation(view,
							Gravity.CENTER, 0, 0);
				};
				   public void showCityDialog(final Activity context,View view,String title){
					   cityDialog=new SelectCity(context, view,title);
					   cityDialog.showAtLocation(view,
								Gravity.CENTER, 0, 0);
					};
					   public void showLanguageDialog(final Activity context,View view,String title,int arrayType){
						   languageDialog=new SelectLanguage(context, view,title,arrayType);
						   languageDialog.showAtLocation(view,
									Gravity.CENTER, 0, 0);
						};
}
