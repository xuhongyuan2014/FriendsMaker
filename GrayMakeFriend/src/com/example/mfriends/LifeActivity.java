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


public class LifeActivity extends Activity {
	private UserInfo userinfo=new UserInfo();
	//表单显示元素
	private TextView TV_drink;
	private TextView TV_smoke;
	private TextView TV_house;
	private TextView TV_cook;

    //表单行定义
	private TableRow TableRowDrink;
	private TableRow TableRowSmoke;
	private TableRow TableRowHouse;
	private TableRow TableRowCook;

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
				
		setContentView(R.layout.life_info);
		
		//初始化表单元素 begin
		TV_drink=(TextView)findViewById(R.id.TV_drink);
		TV_smoke=(TextView)findViewById(R.id.TV_smoke);
		TV_house=(TextView)findViewById(R.id.TV_house);
		TV_cook=(TextView)findViewById(R.id.TV_cook);

		TableRowDrink=(TableRow)findViewById(R.id.TableRowDrink);
		TableRowSmoke=(TableRow)findViewById(R.id.TableRowSmoke);
		TableRowHouse=(TableRow)findViewById(R.id.TableRowHouse);
		TableRowCook=(TableRow)findViewById(R.id.TableRowCook);

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
		TableRowDrink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showArrayDialog(LifeActivity.this,TV_drink,getResources().getString(R.string.drinkorno),R.array.drinkorno);
			}
		});
		TableRowSmoke.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showArrayDialog(LifeActivity.this,TV_smoke,getResources().getString(R.string.smokeorno),R.array.smokeorno);
			}
		});

		TableRowHouse.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showTextDialog(LifeActivity.this,TV_house,getResources().getString(R.string.housework));
			}
		});
		TableRowCook.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showTextDialog(LifeActivity.this,TV_cook,getResources().getString(R.string.cooking));
			}
		});

		//表单元素设置监听 end
		
		userinfo.setId(Integer.valueOf(Constant.UserId));
		getData();
	}

	
	
	public void getData(){
		
		ProgressDialogUtils.showProgressDialog(this, "数据加载中...");
		RequestParams params= new RequestParams();
		params.add("id",Constant.getUserId());   
		HttpEngine.getHttpEngine().get(Constant.getLifeInfo, params, new AsyncHttpResponseHandler(){
			@Override
		    public void onSuccess(String result) {
				// 关闭进度条
				ProgressDialogUtils.dismissProgressDialog();
				 if (result != null) {
					//将其转化成java bean
				       UserInfo re= JSONUtils.jsonToEntity(result,UserInfo.class);
				if(re.getDrinking()!=null)TV_drink.setText(re.getDrinking());	
				if(re.getSmoking()!=null)TV_smoke.setText(re.getSmoking());
				if(re.getHousework()!=null)TV_house.setText(re.getHousework());
				if(re.getCooking()!=null)TV_cook.setText(re.getCooking());


				 } else {
						Toast.makeText(LifeActivity.this, "获取数据错误",Toast.LENGTH_SHORT).show();
					}
		    }
			
			
		     @Override
             public void onFailure(Throwable error) {
		    	// 关闭进度条
					ProgressDialogUtils.dismissProgressDialog();
                     error.printStackTrace();
                     Toast.makeText(LifeActivity.this, "请求超时,请检查网络!",Toast.LENGTH_SHORT).show();
             }
		});
		
		
	}
	
	public void saveData(){
		ProgressDialogUtils.showProgressDialog(LifeActivity.this, "数据保存中...");
		RequestParams params= new RequestParams();
		params.add("id",String.valueOf(userinfo.getId()));
		params.add("drinking", TV_drink.getText().toString());
		params.add("smoking", TV_smoke.getText().toString());
		params.add("housework", TV_house.getText().toString());
		params.add("cooking", TV_cook.getText().toString());

        
		HttpEngine.getHttpEngine().post(Constant.updateLifeInfo, params, new AsyncHttpResponseHandler(){
			@Override
		    public void onSuccess(String result) {
				// 关闭进度条
				ProgressDialogUtils.dismissProgressDialog();
				 if (result != null) {
					 Toast.makeText(LifeActivity.this, "保存成功",Toast.LENGTH_SHORT).show();
					 finish();
				 } else {
						Toast.makeText(LifeActivity.this, "获取数据错误",Toast.LENGTH_SHORT).show();
					}
		    }
			
		     @Override
             public void onFailure(Throwable error) {
		    	// 关闭进度条
					ProgressDialogUtils.dismissProgressDialog();
                     error.printStackTrace();
                     Toast.makeText(LifeActivity.this, "请求超时,请检查网络!",Toast.LENGTH_SHORT).show();
             }
			
		});
		
		
		
		
		
		
		
	}


		

	
	
	
	
	public void showNickNameDialog(final Activity context,View view){
		
		
	};
   public void showBirthDayDialog(final Activity context,View view){
	   birthdayDialog=new SelectBirthday(context, view);
	   birthdayDialog.showAtLocation(LifeActivity.this.findViewById(R.id.userInfo_mainlayout),
				Gravity.BOTTOM, 0, 0);
	};
	   public void showHeightDialog(final Activity context,View view){
		   heightDialog=new SelectHeight(context, view);
		   heightDialog.showAtLocation(LifeActivity.this.findViewById(R.id.userInfo_mainlayout),
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
