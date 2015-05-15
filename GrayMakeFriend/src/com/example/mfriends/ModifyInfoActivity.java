package com.example.mfriends;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.example.dialog.SelectPicture;
import com.example.dialog.SelectText;
import com.example.model.UserInfo;
import com.example.utils.Constant;
import com.example.utils.HttpEngine;
import com.example.utils.JSONUtils;
import com.example.utils.ProgressDialogUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.tencent.weibo.oauthv2.OAuthV2;


public class ModifyInfoActivity extends Activity {
	
	private UserInfo userinfo=new UserInfo();
	//表单显示元素
	private TextView TV_intro;
	private TextView TV_realname;
	private TextView TV_birthday;
	private TextView TV_age;

	private TextView TV_phone;
	private TextView TV_gender;
	private TextView TV_marital;
	private TextView TV_salary;
	private TextView TV_work;

	private TextView TV_school;
	private TextView TV_studypro;
	private TextView TV_education;
    //表单行定义
	private TableRow TableRowIntro;
	private TableRow TableRowRealname;
	private TableRow TableRowBirthday;
	private TableRow TableRowAge;

	private TableRow TableRowPhone;
	private TableRow TableRowGender;
	private TableRow TableRowMarital;
	private TableRow TableRowSalary;
	private TableRow TableRowWork;

	private TableRow TableRowSchool;
	private TableRow TableRowStudypro;
	private TableRow TableRowEducation;
	
	//菜单栏按钮
	private TextView back;
	private TextView save;
	
	private ImageView set;
	private ImageView add;

	// 自定义的弹出框类
	SelectPicPopupWindow menuWindow; // 弹出框
	SelectSearchUserWindow menuWindow2; // 弹出框
	//自定义的表单弹出框
	SelectBirthday birthdayDialog;
	SelectHeight heightDialog;
	SelectText textDialog;
	SelectArray arrayDialog;
	SelectCity cityDialog;
	SelectNumText numTextDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.user_info);
		userinfo.setId(Integer.valueOf(Constant.UserId));
		//初始化表单元素 begin
		TV_intro=(TextView)findViewById(R.id.TV_intro);
		TV_realname=(TextView)findViewById(R.id.TV_realname);
		TV_birthday=(TextView)findViewById(R.id.TV_birthday);
		TV_age=(TextView)findViewById(R.id.TV_age);

		TV_phone=(TextView)findViewById(R.id.TV_phone);
		TV_gender=(TextView)findViewById(R.id.TV_gender);
		TV_marital=(TextView)findViewById(R.id.TV_marital);
		TV_salary=(TextView)findViewById(R.id.TV_salary);
		TV_work=(TextView)findViewById(R.id.TV_work);

		TV_school=(TextView)findViewById(R.id.TV_school);
		TV_studypro=(TextView)findViewById(R.id.TV_studypro);
		TV_education=(TextView)findViewById(R.id.TV_education);
		
		TableRowIntro=(TableRow)findViewById(R.id.TableRowIntro);
		TableRowRealname=(TableRow)findViewById(R.id.TableRowRealname);
		TableRowBirthday=(TableRow)findViewById(R.id.TableRowBirthday);
		TableRowAge=(TableRow)findViewById(R.id.TableRowAge);

		TableRowPhone=(TableRow)findViewById(R.id.TableRowPhone);
		TableRowGender=(TableRow)findViewById(R.id.TableRowGender);
		TableRowMarital=(TableRow)findViewById(R.id.TableRowMarital);
		TableRowSalary=(TableRow)findViewById(R.id.TableRowSalary);
		TableRowWork=(TableRow)findViewById(R.id.TableRowWork);

		TableRowSchool=(TableRow)findViewById(R.id.TableRowSchool);
		TableRowStudypro=(TableRow)findViewById(R.id.TableRowStudypro);
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
				
				//ProgressDialogUtils.showProgressDialog(ModifyInfoActivity.this, "数据保存中...");
				RequestParams params= new RequestParams();
				params.add("id",String.valueOf(userinfo.getId()));
				params.add("intro", TV_intro.getText().toString());
				params.add("realname", TV_realname.getText().toString());
				params.add("birthday", String.valueOf(TV_birthday.getText()));
				params.add("userage", TV_age.getText().toString());
		
				params.add("phone", TV_phone.getText().toString());	
				if(TV_gender.getText().toString().equals("男性")||TV_gender.getText().toString().equals("man"))
					{params.add("gender","true");}
				else 
					{
					if(TV_gender.getText().toString().equals("女性")||TV_gender.getText().toString().equals("female"))
				params.add("gender","false");
					else 
						params.add("gender","");
					}
				//params.add("gender", TV_gender.getText().toString().equals("男")?"true":"false");	
				params.add("user_maritalstatus", TV_marital.getText().toString());
				params.add("user_salary", TV_salary.getText().toString());
				params.add("userwork", TV_work.getText().toString());
			
				params.add("graduatedschool", TV_school.getText().toString());
				params.add("studypro", TV_studypro.getText().toString());
				params.add("user_xueli", TV_education.getText().toString());
		        
				HttpEngine.getHttpEngine().post(Constant.updateUserInfo, params, new AsyncHttpResponseHandler(){
					@Override
				    public void onSuccess(String result) {
						// 关闭进度条
						//ProgressDialogUtils.dismissProgressDialog();
						 if (result != null) {
							 Toast.makeText(ModifyInfoActivity.this, getResources().getString(R.string.savesuccess),Toast.LENGTH_SHORT).show();
							 finish();
							 
							 
						 } else {
								Toast.makeText(ModifyInfoActivity.this,getResources().getString(R.string.registersuccess),Toast.LENGTH_SHORT).show();
							}
						 
					
				    }
					
				     @Override
		             public void onFailure(Throwable error) {
				    	// 关闭进度条
							//ProgressDialogUtils.dismissProgressDialog();
		                     error.printStackTrace();
		                     Toast.makeText(ModifyInfoActivity.this, getResources().getString(R.string.timeout),Toast.LENGTH_SHORT).show();
		             }
					
				});
				
				
				
				
				
				
				
			}
		});

		//表单元素设置监听 begin
		TableRowIntro.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showTextDialog(ModifyInfoActivity.this,TV_intro,getResources().getString(R.string.intro));
			}
		});
		TableRowRealname.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showTextDialog(ModifyInfoActivity.this,TV_realname,getResources().getString(R.string.realname));
			}
		});
		TableRowBirthday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showBirthDayDialog(ModifyInfoActivity.this,TV_birthday);
			}
		});

		TableRowAge.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showNumTextDialog(ModifyInfoActivity.this,TV_age,getResources().getString(R.string.age));
			}
		});
		TableRowMarital.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showArrayDialog(ModifyInfoActivity.this,TV_marital,getResources().getString(R.string.maritalStatus),R.array.marital);
			}
		});
		TableRowPhone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showNumTextDialog(ModifyInfoActivity.this,TV_phone,getResources().getString(R.string.phone));
			}
		});
		TableRowGender.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showArrayDialog(ModifyInfoActivity.this,TV_gender,getResources().getString(R.string.gender),R.array.gender);
			}
		});
		TableRowSalary.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showNumTextDialog(ModifyInfoActivity.this,TV_salary,getResources().getString(R.string.income));
			}
		});
		TableRowWork.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showTextDialog(ModifyInfoActivity.this,TV_work,getResources().getString(R.string.work));
			}
		});

		TableRowSchool.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showTextDialog(ModifyInfoActivity.this,TV_school,getResources().getString(R.string.graduateschool));
			}
		});
		TableRowStudypro.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showTextDialog(ModifyInfoActivity.this,TV_studypro,getResources().getString(R.string.specialty));
			}
		});
		TableRowEducation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showArrayDialog(ModifyInfoActivity.this,TV_education,getResources().getString(R.string.education),R.array.education);
			}
		});
		//表单元素设置监听 end
		
		//ProgressDialogUtils.showProgressDialog(this, "数据加载中...");
		RequestParams params= new RequestParams();
		params.add("id",String.valueOf(userinfo.getId()));   
		HttpEngine.getHttpEngine().post(Constant.getUserInfoById, params, new AsyncHttpResponseHandler(){
			@Override
		    public void onSuccess(String result) {
				// 关闭进度条
				//ProgressDialogUtils.dismissProgressDialog();
				 if (result != null) {
					/* List<Map<String, Object>> info = getDataMap(result);
						Myadapter myadapter=new Myadapter(TabNewsActivity.this);
						myadapter.setData(info);
					    infoList.setAdapter(myadapter);  
					    infoList.setDivider(new ColorDrawable(Color.GRAY));  
				        infoList.setDividerHeight(1);  */
					//将其转化成java bean
				       UserInfo re= JSONUtils.jsonToEntity(result,UserInfo.class);
				if(re.getIntro()!=null)TV_intro.setText(re.getIntro());
				if(re.getRealname()!=null)TV_realname.setText(re.getRealname());	
				 
				if(re.getBirthday()!=null){
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
					java.util.Date date=re.getBirthday();  
					String str=sdf.format(date); 
					TV_birthday.setText(str);
					
				}
			
				if(re.getUserage()!=null)TV_age.setText(String.valueOf(re.getUserage()));
				if(re.getPhone()!=null)TV_phone.setText(re.getPhone());
				if(re.getGender()!=null&&re.getGender()==true)
				{TV_gender.setText("男性");}
			else 
				{
				if(re.getGender()!=null&&re.getGender()==false)
					TV_gender.setText("女性");
				else 
					TV_gender.setText("保密");
				}
				
				if(re.getUser_maritalstatus()!=null)TV_marital.setText(re.getUser_maritalstatus());
				if(re.getUser_salary()!=null)TV_salary.setText(re.getUser_salary());
				if(re.getUserwork()!=null)TV_work.setText(re.getUserwork());
			
				if(re.getGraduatedschool()!=null)TV_school.setText(re.getGraduatedschool());
				if(re.getStudypro()!=null)TV_studypro.setText(re.getStudypro());
				if(re.getUser_xueli()!=null)TV_education.setText(re.getUser_xueli());
				 } else {
						Toast.makeText(ModifyInfoActivity.this, getResources().getString(R.string.dataerror),Toast.LENGTH_SHORT).show();
					}
		    }
			
			
		     @Override
             public void onFailure(Throwable error) {
		    	// 关闭进度条
					//ProgressDialogUtils.dismissProgressDialog();
                     error.printStackTrace();
                     Toast.makeText(ModifyInfoActivity.this,  getResources().getString(R.string.time_error),Toast.LENGTH_SHORT).show();
             }
		});
		
		
		
		
	}
/*	public void uploadImage(final Activity context) {
		menuWindow = new SelectPicPopupWindow(ModifyInfoActivity.this, itemsOnClick);
		// 显示窗口
		View view = ModifyInfoActivity.this.findViewById(R.id.set);
		// 计算坐标的偏移量
		int xoffInPixels = menuWindow.getWidth() - view.getWidth() + 10;
		menuWindow.showAsDropDown(view, -xoffInPixels, 0);
	}*/

	public void uploadImage2(final Activity context) {
		menuWindow2 = new SelectSearchUserWindow(ModifyInfoActivity.this, itemsOnClick2);
		// 显示窗口
		View view = ModifyInfoActivity.this.findViewById(R.id.set);
		// 计算坐标的偏移量
		int xoffInPixels = menuWindow2.getWidth() - view.getWidth() + 10;
		menuWindow2.showAsDropDown(view, -xoffInPixels, 0);
		//menuWindow2.getContentView().setTag(mHandler);//传递hander的引用
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			menuWindow.dismiss();
		}
	};

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick2 = new OnClickListener() {

		public void onClick(View v) {
			menuWindow2.dismiss();
		}
	};
	
	   public void showNumTextDialog(final Activity context,View view,String title){
		   numTextDialog=new SelectNumText(context, view,title);
		   numTextDialog.showAtLocation(view,
					Gravity.CENTER, 0, 0);
		};
   public void showBirthDayDialog(final Activity context,View view){
	   birthdayDialog=new SelectBirthday(context, view);
	   birthdayDialog.showAtLocation(ModifyInfoActivity.this.findViewById(R.id.userInfo_mainlayout),
				Gravity.BOTTOM, 0, 0);
	};
	   public void showHeightDialog(final Activity context,View view){
		   heightDialog=new SelectHeight(context, view);
		   heightDialog.showAtLocation(ModifyInfoActivity.this.findViewById(R.id.userInfo_mainlayout),
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
				   public void showCityDialog(final Activity context,View view,String title){
					   cityDialog=new SelectCity(context, view,title);
					   cityDialog.showAtLocation(view,
								Gravity.CENTER, 0, 0);
					};
				

}
