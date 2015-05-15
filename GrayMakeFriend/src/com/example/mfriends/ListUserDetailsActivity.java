package com.example.mfriends;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.example.main.DActivity;
import com.example.model.CmsUserExt;
import com.example.utils.Constant;
import com.example.utils.DownImage;
import com.example.utils.HandlerConstants;
import com.example.utils.HttpEngine;
import com.example.utils.ImageLoader;
import com.example.utils.JSONUtils;
import com.example.utils.ProgressDialogUtils;
import com.example.utils.DownImage.ImageCallBack;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ListUserDetailsActivity extends Activity {
	
	private String userid;//查看的用户
	private String selfid;//当前登陆用户
	
	//头像
	private ImageView userimage;
	private String user_name;
	private static boolean isFriend=false;
	private TextView username;
	private TextView detailinfo;
	private TextView comefrom;
	private TextView intro;
	private TextView xingzuoinfo;
	private TextView xuexinginfo;
	private TextView height;
	private TextView weight;
	private TextView race;
	private TextView work;
	private TextView housecondition;
	private TextView school;
	private TextView carcondition;
	private TextView professional;
	private TextView marrstatus;
	private TextView childconditon;
	private TextView looks;
	private TextView lovestyle;
	
	private TextView phone;
	private TextView qq;
	
	private TextView place;
	private TextView selectheight;
	private TextView selectsalary;
	private TextView selectxueli;
	private TextView selectage;
	private ImageLoader imageLoader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_user_details);
		
		
		 imageLoader = new ImageLoader(ListUserDetailsActivity.this);
		
		//获取intent里面的类容
		Intent it = this.getIntent();
		userid = it.getStringExtra("friendid");  //默认为-1情况为id为空，报错
		selfid  = it.getStringExtra("id");
		user_name = it.getStringExtra("username");
		
		//初始化先查询数据，是否为已经为好友
		checkIsFriend();
		
		
		
		//查询数据
		initThread();
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_user_details, menu);
		return true;
	}
	
	
	public void checkIsFriend()
	{
		RequestParams rpsc = new RequestParams();
		rpsc.add("friend_id", userid);
		rpsc.add("id", selfid);
		HttpEngine.getHttpEngine().post(Constant.checkIsFriend, rpsc, new AsyncHttpResponseHandler(){
			
			@Override
			public void onSuccess(String result)
			{
				if(result!=null)
				{
					String check = JSON.parseObject(result).getString("info");
					if(check.equals("true")) {
						isFriend=true;
					}
					else {
						isFriend = false;
					}
				}
				
				//按钮监听
				initButton();
			}
		});
		
		//
		
		
	}
	
	
	public void initButton()
	{
		//关注好友按钮
		final Button btncare=(Button)findViewById(R.id.btncare);
		//发私信给好友
		Button btnsixin = (Button)findViewById(R.id.btnsixin);
		ImageView backtomenu = (ImageView) findViewById(R.id.backtomenu);
		
		
		
		if(isFriend) btncare.setText(getString(R.string.cancelcare));
		else btncare.setText(getString(R.string.carefriend));
		
		/*
		 * 关注好友
		 */
		btncare.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				//关注好友执行关注好友功能 需要userid 和friendid
				RequestParams rps = new RequestParams();
				rps.add("friend_id", userid);
				rps.add("id", selfid);

				//在当  isfriend=true 为朋友  isfriend=false  不为朋友
				if(!isFriend){
					
					//关注好友
					HttpEngine.getHttpEngine().post(Constant.addFriend, rps, new AsyncHttpResponseHandler(){
						
						@Override
						public void onSuccess(String result)
						{
							if(result!=null)
							{
								String info = JSON.parseObject(result).getString("info");
	
								if(info.equals("true"))
								{
									isFriend=true;
									btncare.setText(getString(R.string.cancelcare));
									Message mssad = handler.obtainMessage(HandlerConstants.ADDFRIENDOK,"关注成功");
									handler.handleMessage(mssad);
									
								}else{
									isFriend=false;
									Message mssad = handler.obtainMessage(HandlerConstants.ADDFRIENDERROR,"关注失败");
									handler.handleMessage(mssad);
								}
								
							}
							
							// 广播通知  
					         Intent intent = new Intent();  
					         intent.setAction("action.refreshFriends");  
					         sendBroadcast(intent); 
							
						}
				
					});
				}else{
					// 点击取消好友
					HttpEngine.getHttpEngine().post(Constant.removeFriend, rps, new AsyncHttpResponseHandler(){
						
						@Override
						public void onSuccess(String result)
						{
							if(result!=null)
							{
								String info = JSON.parseObject(result).getString("info");
	
								if(info.equals("true"))
								{
									isFriend=false;
									btncare.setText(getString(R.string.carefriend));
									Message mssad = handler.obtainMessage(HandlerConstants.REMOVEFRIENDOK,getString(R.string.success));
									handler.handleMessage(mssad);
								}else{
									isFriend=false;
									Message mssad = handler.obtainMessage(HandlerConstants.REMOVEFRIENDERROR,getString(R.string.failed));
									handler.handleMessage(mssad);
								}
								
							}
							
							
							// 广播通知  
					         Intent intent = new Intent();  
					         intent.setAction("action.refreshFriends");  
					         sendBroadcast(intent); 
						}
				
					});
				}
						
				
			}
		});
		
		/*
		 * 发私信
		 */
		btnsixin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(ListUserDetailsActivity.this,ReplyMessActivity.class);
				it.putExtra("userid", userid);
				it.putExtra("username", user_name);
				startActivity(it);
			}
		});
		
		
		
		
		backtomenu.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				
			}
		});
		
		
		
		
	}
	
	//调用数据库数据
	public void initThread()
	{
				final RequestParams param= new RequestParams(); //定义final ，子类不可继承
				
				param.add("id",userid);
				
				HttpEngine.getHttpEngine().post(Constant.getUserInfoDetails, param, new AsyncHttpResponseHandler(){

					
					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
					
						if(result!=null){
							
							CmsUserExt ext = JSONUtils.jsonToEntity(result, CmsUserExt.class);
							
							//初始化数据控件
							//用户头像
							userimage = (ImageView) findViewById(R.id.userDetaiImage);
							
							username = (TextView) findViewById(R.id.username);
							detailinfo = (TextView) findViewById(R.id.detailinfo);
							
							comefrom = (TextView) findViewById(R.id.comefrom);
							intro = (TextView) findViewById(R.id.intro);
							xingzuoinfo = (TextView) findViewById(R.id.xingzuoinfo);
							xuexinginfo = (TextView) findViewById(R.id.xuexinginfo);
							height = (TextView) findViewById(R.id.height);
							weight = (TextView) findViewById(R.id.weight);
							race = (TextView) findViewById(R.id.race);
							work = (TextView) findViewById(R.id.work);
							housecondition = (TextView) findViewById(R.id.housecondition);
							school = (TextView) findViewById(R.id.school);
							carcondition = (TextView) findViewById(R.id.carcondition);
							professional = (TextView) findViewById(R.id.professional);
							marrstatus = (TextView) findViewById(R.id.marrstatus);
							childconditon = (TextView) findViewById(R.id.childconditon);
							looks = (TextView) findViewById(R.id.looks);
							lovestyle = (TextView) findViewById(R.id.lovestyle);
							
							phone = (TextView) findViewById(R.id.phone);
							qq = (TextView) findViewById(R.id.qq);
							
							place = (TextView) findViewById(R.id.place);
							selectheight = (TextView) findViewById(R.id.selectheight);
							selectsalary = (TextView) findViewById(R.id.selectsalary);
							selectxueli = (TextView) findViewById(R.id.selectxueli);
							selectage = (TextView) findViewById(R.id.selectage);
							
							
							
							//set数据 进入textview里面
							if(ext.getUserImg()!=null&&ext.getUserImg()!="")
							{
								
								String url = ext.getUserImg();
								imageLoader.DisplayImage(url,userimage);
							/*	DownImage downImage = new DownImage(ext.getUserImg());
								downImage.loadImage(new ImageCallBack() {
									public void getDrawable(Drawable drawable) {

										userimage.setImageDrawable(drawable);
										
									}
								});*/
							}else{
								userimage.setImageResource(R.drawable.icon);
							}
							
							
							username.setText(checkNull(String.valueOf(ext.getUsername())));
							detailinfo.setText("");
							comefrom.setText(checkNull(String.valueOf(ext.getComefrom())));
							intro.setText(checkNull(String.valueOf(ext.getIntro())));
							xingzuoinfo.setText(checkNull(String.valueOf(ext.getXingzuo())));
							xuexinginfo.setText(checkNull(String.valueOf(ext.getBloodstyle())));
							height.setText(checkNull(String.valueOf(ext.getUser_height()))); //身高
							weight.setText(checkNull(String.valueOf(ext.getWeight())));
							race.setText(checkNull(String.valueOf(ext.getRace())));
							work.setText(checkNull(String.valueOf(ext.getUserwork())));
							housecondition.setText(checkNull(String.valueOf(ext.getUser_house())));
							school.setText(checkNull(String.valueOf(ext.getGraduatedschool())));
							carcondition.setText(checkNull(String.valueOf(ext.getBuycar())));
							professional.setText(checkNull(String.valueOf(ext.getStudypro())));
							marrstatus.setText(checkNull(String.valueOf(ext.getUser_maritalstatus())));
							childconditon.setText(checkNull(String.valueOf(ext.getHavebaby())));
							looks.setText(checkNull(String.valueOf(ext.getLooks())));
							lovestyle.setText(checkNull(String.valueOf(ext.getUser_lovestyle())));
							
							phone.setText(checkNull(String.valueOf(ext.getPhone())));
							qq.setText(checkNull(String.valueOf(ext.getQq())));
							
							place.setText(getString(R.string.juzhudi)+":"+checkNull(String.valueOf(ext.getSelect_place())));
							selectheight.setText(getString(R.string.height)+":"+checkNull(String.valueOf(ext.getSelect_height())));
							selectsalary.setText(getString(R.string.income)+":"+checkNull(String.valueOf(ext.getSelect_salary())));
							selectxueli.setText(getString(R.string.xueli)+":"+checkNull(String.valueOf(ext.getSelect_xueli())));
							 
							selectage.setText(getString(R.string.nianlingfanwei)+":"+checkNull(String.valueOf(ext.getSelect_age())));
						
						
						}
						else{
							
						}
						
					}
					
					
					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, responseBody, error);
					}
	
				});
				
		
		
	}
	
	
	//线程消息回调
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		
		public void handleMessage(Message messg)
		{
			switch(messg.what)  //messg what is integer   Object is data
			{
			case 1:
				System.out.println("1");
			    break;
			case 2:
				System.out.println("2");
			 	break;
			case HandlerConstants.ADDFRIENDOK:
				Toast.makeText(ListUserDetailsActivity.this, String.valueOf(messg.obj),Toast.LENGTH_SHORT).show();
			case HandlerConstants.ADDFRIENDERROR:
				Toast.makeText(ListUserDetailsActivity.this, String.valueOf(messg.obj),Toast.LENGTH_SHORT).show();
			case HandlerConstants.REMOVEFRIENDOK:
				Toast.makeText(ListUserDetailsActivity.this, String.valueOf(messg.obj),Toast.LENGTH_SHORT).show();
			case HandlerConstants.REMOVEFRIENDERROR:
				Toast.makeText(ListUserDetailsActivity.this, String.valueOf(messg.obj),Toast.LENGTH_SHORT).show();
			default:
				System.out.println("default");
				break;
			}
		}
	};
	public String checkNull(String str)
	{
		if(str!=null&&str!="null")
			return str;		
		else
			return "";
	}

}
