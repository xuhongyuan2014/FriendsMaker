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

import com.example.dialog.SelectArray;
import com.example.dialog.SelectBirthday;
import com.example.dialog.SelectCity;
import com.example.dialog.SelectHeight;
import com.example.dialog.SelectNumText;
import com.example.dialog.SelectText;
import com.example.model.UserInfo;
import com.example.utils.Constant;
import com.example.utils.HttpEngine;
import com.example.utils.JSONUtils;
import com.example.utils.ProgressDialogUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.tencent.weibo.oauthv2.OAuthV2;


public class ConditionActivity extends Activity {
	private UserInfo userinfo=new UserInfo();
	//表单显示元素
	private TextView TV_age;
	private TextView TV_height;
	private TextView TV_marital;
	private TextView TV_income;
	private TextView TV_education;

    //表单行定义
	private TableRow TableRowAge;
	private TableRow TableRowHeight;
	private TableRow TableRowMarital;
	private TableRow TableRowIncome;
	private TableRow TableRowEducation;

	//菜单栏按钮
	private TextView back;
	private TextView save;
	


	//自定义的表单弹出框
	SelectBirthday birthdayDialog;
	SelectHeight heightDialog;
	SelectText textDialog;
	SelectArray arrayDialog;
	SelectNumText numTextDialog;
	SelectCity cityDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
				
		setContentView(R.layout.condition_info);
		
		//初始化表单元素 begin
		TV_age=(TextView)findViewById(R.id.TV_age);
		TV_height=(TextView)findViewById(R.id.TV_height);
		TV_marital=(TextView)findViewById(R.id.TV_marital);
		TV_income=(TextView)findViewById(R.id.TV_income);
		TV_education=(TextView)findViewById(R.id.TV_education);
		TableRowAge=(TableRow)findViewById(R.id.TableRowAge);
		TableRowHeight=(TableRow)findViewById(R.id.TableRowHeight);
		TableRowMarital=(TableRow)findViewById(R.id.TableRowMarital);
		TableRowIncome=(TableRow)findViewById(R.id.TableRowIncome);
		TableRowEducation=(TableRow)findViewById(R.id.TableRowEducation);
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
				saveData();
				//finish();
			}
		});
		//表单元素设置监听 begin
		TableRowAge.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showNumTextDialog(ConditionActivity.this,TV_age,getResources().getString(R.string.age));
			}
		});
		TableRowHeight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showHeightDialog(ConditionActivity.this,TV_height);
			}
		});

		TableRowMarital.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showArrayDialog(ConditionActivity.this,TV_marital,getResources().getString(R.string.maritalStatus),R.array.marital);
			}
		});
		TableRowIncome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showNumTextDialog(ConditionActivity.this,TV_income,getResources().getString(R.string.income));
			}
		});
		TableRowEducation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showArrayDialog(ConditionActivity.this,TV_education,getResources().getString(R.string.education),R.array.education);
			}
		});

		//表单元素设置监听 end
		
		userinfo.setId(Integer.valueOf(Constant.UserId));
		getData();
	}

	
	
	public void getData(){
		
		//ProgressDialogUtils.showProgressDialog(this, "数据加载中...");
		RequestParams params= new RequestParams();
		params.add("id",String.valueOf(userinfo.getId()));   
		HttpEngine.getHttpEngine().get(Constant.getConditionsById, params, new AsyncHttpResponseHandler(){
			@Override
		    public void onSuccess(String result) {
				// 关闭进度条
				//ProgressDialogUtils.dismissProgressDialog();
				 if (result != null) {
					//将其转化成java bean
				       UserInfo re= JSONUtils.jsonToEntity(result,UserInfo.class);
				if(re.getSelect_age()!=null)TV_age.setText(re.getSelect_age());	
				if(re.getSelect_height()!=null)TV_height.setText(re.getSelect_height());
				if(re.getSelect_marritalstatus()!=null)TV_marital.setText(re.getSelect_marritalstatus());
				if(re.getSelect_salary()!=null)TV_income.setText(re.getSelect_salary());
				if(re.getSelect_xueli()!=null)TV_education.setText(re.getSelect_xueli());
		

				 } else {
						Toast.makeText(ConditionActivity.this,  getResources().getString(R.string.dataerror),Toast.LENGTH_SHORT).show();
					}
		    }
			
			
		     @Override
             public void onFailure(Throwable error) {
		    	// 关闭进度条
					//ProgressDialogUtils.dismissProgressDialog();
                     error.printStackTrace();
                     Toast.makeText(ConditionActivity.this,  getResources().getString(R.string.time_error),Toast.LENGTH_SHORT).show();
             }
		});
		
		
	}
	
	public void saveData(){
		//ProgressDialogUtils.showProgressDialog(ConditionActivity.this, "数据保存中...");
		RequestParams params= new RequestParams();
		params.add("id",String.valueOf(userinfo.getId()));
		params.add("select_age", TV_age.getText().toString());
		params.add("select_height", TV_height.getText().toString());
		params.add("select_marritalstatus", TV_marital.getText().toString());
		params.add("select_salary", TV_income.getText().toString());
		params.add("select_xueli", TV_education.getText().toString());	
			

        
		HttpEngine.getHttpEngine().post(Constant.updateConditions, params, new AsyncHttpResponseHandler(){
			@Override
		    public void onSuccess(String result) {
				// 关闭进度条
				//ProgressDialogUtils.dismissProgressDialog();
				 if (result != null) {
					 Toast.makeText(ConditionActivity.this,  getResources().getString(R.string.savesuccess),Toast.LENGTH_SHORT).show();
					 finish();
				 } else {
						Toast.makeText(ConditionActivity.this, getResources().getString(R.string.dataerror),Toast.LENGTH_SHORT).show();
					}
		    }
			
		     @Override
             public void onFailure(Throwable error) {
		    	// 关闭进度条
				//	ProgressDialogUtils.dismissProgressDialog();
                     error.printStackTrace();
                     Toast.makeText(ConditionActivity.this,  getResources().getString(R.string.time_error),Toast.LENGTH_SHORT).show();
             }
			
		});
		
		
		
		
		
		
		
	}


		

	
	
	
	
	public void showNickNameDialog(final Activity context,View view){
		
		
	};
   public void showBirthDayDialog(final Activity context,View view){
	   birthdayDialog=new SelectBirthday(context, view);
	   birthdayDialog.showAtLocation(ConditionActivity.this.findViewById(R.id.userInfo_mainlayout),
				Gravity.BOTTOM, 0, 0);
	};
	   public void showHeightDialog(final Activity context,View view){
		   heightDialog=new SelectHeight(context, view);
		   heightDialog.showAtLocation(ConditionActivity.this.findViewById(R.id.userInfo_mainlayout),
					Gravity.BOTTOM, 0, 0);
		};
		   public void showTextDialog(final Activity context,View view,String title){
			   textDialog=new SelectText(context, view,title);
			   textDialog.showAtLocation(view,
						Gravity.CENTER, 0, 0);
			};
				   public void showNumTextDialog(final Activity context,View view,String title){
					   numTextDialog=new SelectNumText(context, view,title);
					   numTextDialog.showAtLocation(view,
								Gravity.CENTER, 0, 0);
					};
			   public void showArrayDialog(final Activity context,View view,String title,int arrayType){
				   arrayDialog=new SelectArray(context, view,title,arrayType);
				   arrayDialog.showAtLocation(view,
							Gravity.CENTER, 0, 0);
				};
				   public void showCityDialog(final Activity context,View view,String title){
					   cityDialog=new SelectCity(context, view,title);
					   cityDialog.showAtLocation(view,
								Gravity.CENTER, 0, 0);
					};
}
