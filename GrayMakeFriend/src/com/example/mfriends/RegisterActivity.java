package com.example.mfriends;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.dialog.SelectArray;
import com.example.dialog.SelectBirthday;
import com.example.dialog.SelectCity;
import com.example.dialog.SelectHeight;
import com.example.dialog.SelectNumText;
import com.example.dialog.SelectPasswordText;
import com.example.dialog.SelectText;
import com.example.model.ResultForm;
import com.example.model.UserInfo;
import com.example.utils.Constant;
import com.example.utils.HttpEngine;
import com.example.utils.JSONUtils;
import com.example.utils.ProgressDialogUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.tencent.weibo.oauthv2.OAuthV2;


public class RegisterActivity extends Activity {

	//菜单栏按钮
	private TextView back;
	private TextView save;
	
	private TableRow TableRowUsername;
	private TableRow TableRowEmail;
	private TableRow TableRowPassword;
	private TableRow TableRowPasswordconfirm;
	private TableRow TableRowBirthday;
	private TableRow TableRowEducation;
	private TableRow TableRowGender;
	private TableRow TableRowSalary;

	
	private TextView TV_username;
	private TextView TV_email;
	private TextView TV_password;
	private TextView TV_passwordconfirm;
	private TextView TV_birthday;
	private TextView TV_education;
	private TextView TV_gender;
	private TextView TV_salary;

	
	


	//自定义的表单弹出框
	SelectBirthday birthdayDialog;
	SelectHeight heightDialog;
	SelectText textDialog;
	SelectArray arrayDialog;
	SelectNumText numTextDialog;
	SelectPasswordText passwordTextDialog;
	SelectCity cityDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.register);
		
		//初始化表单元素 begin
		TV_username=(TextView)findViewById(R.id.TV_username);
		TV_email=(TextView)findViewById(R.id.TV_email);
		TV_password=(TextView)findViewById(R.id.TV_password);
		TV_passwordconfirm=(TextView)findViewById(R.id.TV_passwordconfirm);
		TV_birthday=(TextView)findViewById(R.id.TV_birthday);
		TV_education=(TextView)findViewById(R.id.TV_education);
		TV_gender=(TextView)findViewById(R.id.TV_gender);
		TV_salary=(TextView)findViewById(R.id.TV_salary);


		
		
		TableRowUsername=(TableRow)findViewById(R.id.TableRowUsername);
		TableRowEmail=(TableRow)findViewById(R.id.TableRowEmail);
		TableRowPassword=(TableRow)findViewById(R.id.TableRowPassword);
		TableRowPasswordconfirm=(TableRow)findViewById(R.id.TableRowPasswordconfirm);
		TableRowBirthday=(TableRow)findViewById(R.id.TableRowBirthday);
		TableRowGender=(TableRow)findViewById(R.id.TableRowGender);
		TableRowEducation=(TableRow)findViewById(R.id.TableRowEducation);
		TableRowSalary=(TableRow)findViewById(R.id.TableRowSalary);

		
		//初始化表单元素end
	    back = (TextView) findViewById(R.id.back_btn);
		save = (TextView) findViewById(R.id.save_btn);

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
				if(validForm())registerUser();
			}
		});
		//表单元素设置监听 begin
		TableRowUsername.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showTextDialog(RegisterActivity.this,TV_username,getResources().getString(R.string.username));
			}
		});
		TableRowEmail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showTextDialog(RegisterActivity.this,TV_email,getResources().getString(R.string.email));
			}
		});
		TableRowPassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showPasswordTextDialog(RegisterActivity.this,TV_password,getResources().getString(R.string.password));
			}
		});
		TableRowPasswordconfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showPasswordTextDialog(RegisterActivity.this,TV_passwordconfirm,getResources().getString(R.string.passwordconfirm));
			}
		});
		TableRowBirthday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showBirthDayDialog(RegisterActivity.this,TV_birthday);
			}
		});
		TableRowGender.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showArrayDialog(RegisterActivity.this,TV_gender,getResources().getString(R.string.gender),R.array.gender);
			}
		});
		TableRowSalary.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showNumTextDialog(RegisterActivity.this,TV_salary,getResources().getString(R.string.income));
			}
		});

		TableRowEducation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showArrayDialog(RegisterActivity.this,TV_education,getResources().getString(R.string.education),R.array.education);
			}
		});
		//表单元素设置监听 end
		
		

	}

	  public void showBirthDayDialog(final Activity context,View view){
		   birthdayDialog=new SelectBirthday(context, view);
		   birthdayDialog.showAtLocation(context.findViewById(R.id.userInfo_mainlayout),
					Gravity.BOTTOM, 0, 0);
		};
		   public void showHeightDialog(final Activity context,View view){
			   heightDialog=new SelectHeight(context, view);
			   heightDialog.showAtLocation(context.findViewById(R.id.userInfo_mainlayout),
						Gravity.BOTTOM, 0, 0);
			};
			   public void showTextDialog(final Activity context,View view,String title){
				   textDialog=new SelectText(context, view,title);			  
				   textDialog.showAtLocation(view,
							Gravity.CENTER, 0, 0);
				};
				   public void showArrayDialog(final Activity context,View view,String title,int arrayType){
					   arrayDialog=new SelectArray(context, view,title,arrayType);
					   arrayDialog.showAtLocation(view,
								Gravity.CENTER, 0, 0);
					};
					   public void showPasswordTextDialog(final Activity context,View view,String title){
						   passwordTextDialog=new SelectPasswordText(context, view,title);
						   passwordTextDialog.showAtLocation(view,
									Gravity.CENTER, 0, 0);
						};
						   public void showNumTextDialog(final Activity context,View view,String title){
							   numTextDialog=new SelectNumText(context, view,title);
							   numTextDialog.showAtLocation(view,
										Gravity.CENTER, 0, 0);
							};
							   public void showCityDialog(final Activity context,View view,String title){
								   cityDialog=new SelectCity(context, view,title);
								   cityDialog.showAtLocation(view,
											Gravity.CENTER, 0, 0);
								};
				
public void registerUser(){
	ProgressDialogUtils.showProgressDialog(RegisterActivity.this, "注册中...");
	RequestParams params= new RequestParams();
	params.add("username", TV_username.getText().toString());
	params.add("password", String.valueOf(TV_password.getText()));
	params.add("email", TV_email.getText().toString());
	params.add("birthday", TV_birthday.getText().toString());
	if(TV_gender.getText().toString().equals("男性")||TV_gender.getText().toString().equals("man"))
		{params.add("gender","true");}
	else 
		{
		if(TV_gender.getText().toString().equals("女性")||TV_gender.getText().toString().equals("female"))
	params.add("gender","false");
		else 
			params.add("gender","");
		}

	params.add("user_salary", TV_salary.getText().toString());

	params.add("user_xueli", TV_education.getText().toString()); 
	HttpEngine.getHttpEngine().post(Constant.register, params, new AsyncHttpResponseHandler(){
		@Override
	    public void onSuccess(String result) {
			// 关闭进度条
			ProgressDialogUtils.dismissProgressDialog();
			 if (result != null) {
				 ResultForm re= JSONUtils.jsonToEntity(result,ResultForm.class);
				 
				 String info = JSON.parseObject(result).getString("info");
				 String infoText = JSON.parseObject(result).getString("infoText");
				 if(info.equals("1")){
				 Toast.makeText(RegisterActivity.this, getResources().getString(R.string.registersuccess),Toast.LENGTH_SHORT).show();
			     //跳转界面  
	                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);  
	                startActivity(intent);  
				 }
				 else {
					 Toast.makeText(RegisterActivity.this,infoText,Toast.LENGTH_SHORT).show();
					 
				 }
			 
			 } else {
					Toast.makeText(RegisterActivity.this, getResources().getString(R.string.dataerror),Toast.LENGTH_SHORT).show();
				}
	    }
		
		
	     @Override
         public void onFailure(Throwable error) {
	    	// 关闭进度条
				ProgressDialogUtils.dismissProgressDialog();
                 error.printStackTrace();
                 Toast.makeText(RegisterActivity.this, getResources().getString(R.string.timeout),Toast.LENGTH_SHORT).show();
         }
	});
	
	
	
	
}

	public boolean validForm(){
		if(TV_username.getText().equals("")||TV_password.getText().equals("")
				||TV_passwordconfirm.getText().equals("")||TV_email.getText().equals("")
				||TV_birthday.getText().equals("")||TV_gender.getText().equals("")
				) 
			  
		{
			Toast.makeText(RegisterActivity.this, "请填写完整注册信息!",Toast.LENGTH_SHORT).show();;
		    return false;
			
		}
		else if(TV_password.getText().equals(TV_passwordconfirm.getText()))
				{
			
			Toast.makeText(RegisterActivity.this, "两次密码输入不一致!",Toast.LENGTH_SHORT).show();;
		    return false;
				}
		else {
			//Toast.makeText(RegisterActivity.this, "两次密码输入不一致!",Toast.LENGTH_SHORT).show();;
		    return true;
			
		}
	}

}
