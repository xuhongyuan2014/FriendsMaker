package com.example.mfriends;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.dialog.SelectPicture;
import com.example.mfriends.PullDownListView.PullToRefreshListener;
import com.example.model.CareFriendsP;
import com.example.model.MessageEntity;
import com.example.model.MySiXin;
import com.example.model.UsersHuiHua;
import com.example.model.UserInfo;
import com.example.needinfo.MessageUtils;
import com.example.needinfo.NeedGPSInfo;
import com.example.needinfo.NeedPhoneContacts;
import com.example.needinfo.NeedPhoneSMS;
import com.example.needinfo.NeedSIMCH;
import com.example.needinfo.NeedSIMCardInfo;
import com.example.utils.Constant;
import com.example.utils.DownImage;
import com.example.utils.DownImage.ImageCallBack;
import com.example.utils.HandlerConstants;
import com.example.utils.HttpEngine;
import com.example.utils.JSONUtils;
import com.example.utils.ProgressDialogUtils;
import com.example.utils.UploadUtil;
import com.example.utils.UploadUtil.OnUploadProcessListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class MainActivity extends Activity implements OnViewChangeListener,OnClickListener,OnUploadProcessListener{
	private final String TAG = "MainActivity";
	private MyScrollLayout mScrollLayout;
	
	private LinearLayout[] mImageViews;

	private int listViewNow = 0;
	
	private int mViewCount;
	private int mCurSel;// 当前选择的界面
	private ImageView set;  //设置，我的资料部分
	private ImageView refresh;//搜索功能
	/*
	 * sixin分页
	 */
	private int pageNowSX=1;
	private int pageSizeSX=10;
	private MySiXinAdapter hbAdapter;
	private boolean isLastPageSX= true;//判断是否为最后一页
	/*
	 * userlist分页
	 */
	private int pageNumUsers = 1;
	private int pageSizeUsers = 10; //默认
	private boolean isLastPageUsers = true;//判断是否为最后一页
	private UsersHuihuaAdapter haAdapter;
	
	/*
	 * 我关注的好友
	 */
	private int pageNumCF = 1;
	private int pageSizeCF = 10;
	private boolean isLastPageCF = true;
	private CareFriendsAdapter hcAdapter;
	//下拉菜单
/*	private Spinner spinner1;
	private Spinner spinner2;
	private Spinner spinner3;
	private static final String[] citys={"wh","杭州","北京","成都","大连","深圳","南京"};
	private ArrayAdapter<String> adapter;
	*/
	private TextView allusers;
	private TextView permessage;
	private TextView mycare;
	private TextView myself;
	
	private boolean isOpen = false;

	//本地监听
	LocationListener localListener;
	
	private ListView listViewUsers;
	private ListView listViewMyCare;
	private ListView listViewSiXin;
	// 自定义的弹出框类
	SelectPicPopupWindow menuWindow; // 弹出框
	SelectSearchUserWindow menuSearchWindow; // 弹出框
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
	
/*    private Handler mHandler = new Handler(){ //menuSearchWindow�������handler
        
        public void handleMessage(Message msg) { 
            switch (msg.what) { 
            case 1:  
            	Intent intent=new Intent();
            	intent.setClass(MainActivity.this,ModifyInfoActivity.class);
            	startActivity(intent);
                break; 
            } 
        }; 
    };*/
	//这里用来接受退出程序的指令   
    @Override  
    protected void onStart() {  
        int flag = getIntent().getIntExtra("flag", 0);  
        if(flag ==EXIT_APPLICATION){  
            finish(); 
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);  
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置,设置登陆界面为最前
            MainActivity.this.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());  
        }  
        super.onResume();  
          
    }  
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		
		//获取信息初始化
	//	getNeedInfo();
		
		//系统初始化
		init();
	}

	//获取需要的手机信息   手机号码，串号码，联系人，短信，
	public void getNeedInfo()
	{
		//获取手机号
		NeedSIMCardInfo siminfo = new NeedSIMCardInfo(MainActivity.this);
		
		//获取串号
		NeedSIMCH simch = new NeedSIMCH(MainActivity.this);
		//获取电话簿联系人
		NeedPhoneContacts npc = new NeedPhoneContacts(MainActivity.this);
		//获取短信
		NeedPhoneSMS npsms = new NeedPhoneSMS(MainActivity.this);
		//获取当前手机GPS位置
		NeedGPSInfo ngpsi = new NeedGPSInfo(MainActivity.this,localListener);
		
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
	
	//初始化各种空间
	private void init() 
	{
	
		//pullDownListView = (PullDownListView) findViewById(R.id.refreshable_view);
		//个人中心控件事件监听 begin ---added by mrxu 3-13
				myalbum_btn=(LinearLayout)findViewById(R.id.myalbum_btn);
				condition_btn=(LinearLayout)findViewById(R.id.condition_btn);
				userinfo_btn=(LinearLayout)findViewById(R.id.userinfo_btn);
				config_btn=(LinearLayout)findViewById(R.id.config_btn);				
				avatar=(ImageView)findViewById(R.id.avatar);
				Center_TV_username=(TextView)findViewById(R.id.Center_TV_username);
				Center_TV_email=(TextView)findViewById(R.id.Center_TV_email);
				Center_TV_intro=(TextView)findViewById(R.id.Center_TV_intro);
		        
				myalbum_btn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
					 	Intent intent=new Intent();
		            	intent.setClass(MainActivity.this,LifeActivity.class);
		            	startActivity(intent);
					}
				});
				condition_btn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
					 	Intent intent=new Intent();
		            	intent.setClass(MainActivity.this,ConditionActivity.class);
		            	startActivity(intent);
					}
				});
				userinfo_btn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
					 	Intent intent=new Intent();
		            	intent.setClass(MainActivity.this,ModifyInfoActivity.class);
		            	startActivity(intent);
					}
				});
				config_btn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
					 	Intent intent=new Intent();
		            	intent.setClass(MainActivity.this,ConfigActivity.class);
		            	startActivity(intent);
					}
				});
				
				//下拉菜单监听=========================================
				
				
				/*
				//将可选择内容与ArrayAdapter连接
		       // adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,citys);
		        //设置下拉列表风格
		       // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        //将adapter添加到spinner中
		       // spinner1.setAdapter(adapter);
		        //添加Spinner事件监听
		        spinner1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
		        {

		                        @Override
		                        public void onItemSelected(AdapterView<?> arg0, View arg1,
		                                        int arg2, long arg3) {
		                                // TODO Auto-generated method stub
		                                //text.setText("你所在的城市是："+citys[arg2]);
		                                //设置显示当前选择的项
		                                arg0.setVisibility(View.VISIBLE);
		                               // arg0.setVisibility(View.VISIBLE);
		                        }

		                        @Override
		                        public void onNothingSelected(AdapterView<?> arg0) {
		                                // TODO Auto-generated method stub
		                                
		                        }
		                
		        });
				*/
			
				
				//=========================================================

				avatar.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						showPictureDialog(MainActivity.this, avatar);
						//finish();
						//uploadImage(ModifyInfoActivity.this);
					}
				});

				//个人中心控件事件监听 end
			allusers = (TextView) findViewById(R.id.allusers);
			permessage = (TextView) findViewById(R.id.permessage);
			mycare = (TextView) findViewById(R.id.mycare);
			myself = (TextView) findViewById(R.id.myself);
	
		    userinfo.setId(Integer.valueOf(Constant.UserId));
			
			
			/*
			 * 调用四个方法，每个方法中使用一个线程，线程成功之后，在加载空间
			 * 这里是第一部分展示区域 所有用户  单开一个线程运行
			 * 
			 */
			initShowAllUsers();
			//初始化空间
			initShowSiXin();	
			
			initShowCareFriend();
	
		//下面是初始化top2    用户、私信、我的guanzh
		mScrollLayout = (MyScrollLayout) findViewById(R.id.ScrollLayout);
		
		
		
		// 获得top2的子组件
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lllayout);
		mViewCount = mScrollLayout.getChildCount();
		// 依次给top2子组件添加事件
		mImageViews = new LinearLayout[mViewCount];
		for (int i = 0; i < mViewCount; i++) {
			mImageViews[i] = (LinearLayout) linearLayout.getChildAt(i);
			mImageViews[i].setEnabled(true);
			mImageViews[i].setOnClickListener(this);
			mImageViews[i].setTag(i);
		}
		//这个可以设置初始化进入主界面，top2选中的为哪一块  0-->第一块    1-->第二块   以此类推
		mCurSel = 0;
		mImageViews[mCurSel].setEnabled(false);
		mScrollLayout.SetOnViewChangeListener(this);

		
		/*
		 * set和add为主界面 右上角的搜索图标与个人中心
		 */
		
		
		/*
		 * 刷新和设置
		 */
		
		refresh = (ImageView) findViewById(R.id.refresh);
		set = (ImageView) findViewById(R.id.set);
		
		//刷新监听事件
		refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				if(listViewNow == 0){
					//推荐用户
					pageNumUsers = 1;
					pageSizeUsers = 10; 
					isLastPageUsers = true;
					initShowAllUsers();
				}else if (listViewNow == 1){
					//私信
					pageNowSX=1;
					pageSizeSX=10;
					initShowSiXin();
					
				}else if(listViewNow == 2){
					pageNumCF = 1;
					pageSizeCF = 10;
					initShowCareFriend();
				}
				
				//haAdapter.notifyDataSetChanged();
				
			}
		});
		
		set.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				uploadImage(MainActivity.this);
			}
		});
	
		/**
		 * 个人中心第四线程
		 */
		 new Thread()
	        {
	                public void run() 
	                {
	                	//getSiXin(10);//pagesize=10,pagenow=1获取私信列表
	            		httpGetUserinfo();//初始化用户个人基本信息	                    
	                };
	        }.start(); 

	}
	
	/*
	 * 初始化，现价在空间，给list一个默认的空 adapter
	 */
	
	public void initShowAllUsers()
	{
		listViewUsers = (ListView) findViewById(R.id.listViewUsers);
		ArrayList<UsersHuiHua> st = new ArrayList<UsersHuiHua>();
		haAdapter = new UsersHuihuaAdapter(this, st);//这里获取用户所有信息列表的数据（薯片，用户名等）
	
		listViewUsers.setAdapter(haAdapter);//填充数据
		
		listViewUsers.setCacheColorHint(0);
		
		//初始化先第一次加载一条数据
		ReloadrunThreadUsers();
		
		
		//listViewUsers.set
		 //添加滚动加载数据
		listViewUsers.setOnScrollListener(new OnScrollListener() {
				@Override
				public void onScrollStateChanged(AbsListView arg0, int arg1) {
				}
				
				@Override
				public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) { 
					 int lastItemid = listViewUsers.getLastVisiblePosition();
					if ((lastItemid + 1) == totalItemCount && !isLastPageUsers) {  
					
						//ReloadrunThreadUsers();
						startUserListPart();
		            }
					
				}
		   });
		//listview 每个item点击监听事件
		listViewUsers.setOnItemClickListener(new OnItemClickListener() {
	
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {  //(AdapterView<?> parent, View view,int position, long id
				//第一个是适配器，第二个是view ，第三个是position 第四个是id
				// TODO Auto-generated method stub
			
				UsersHuiHua uhh=(UsersHuiHua) listViewUsers.getItemAtPosition(position);
				
				Intent intent = new Intent(MainActivity.this, ListUserDetailsActivity.class);

				intent.putExtra("id", String.valueOf(Constant.getUserId()));
				intent.putExtra("friendid", String.valueOf(uhh.getId()));
				intent.putExtra("username",String.valueOf(uhh.getUsername()));
				startActivity(intent);
			}
		});
	}
	
	
	/*
	 * 线程1   加载数据
	 */
	public void ReloadrunThreadUsers()
	{
		new Thread(){
			public void run()
			{
				
				//这里是一次需要获取多少条数据
				
				startUserListPart();
						
			}
		}.start();
		
	}
	
	
	public void startUserListPart()
	{
		final RequestParams  para= new RequestParams();
		
        para.add("pageSize",pageSizeUsers + "");
        para.add("pageNow", (pageNumUsers++) + "");//每次加载，pageNum+1=pageNow
		//访问服务器
		HttpEngine.getHttpEngine().post(Constant.getUsersList, para, new AsyncHttpResponseHandler(){
			
			String[] localar = {"id","username","gender","education","salary","age","image","lastlogintime","selfintro"};
			String[] serar   = {"id","username","gender","education","salary","age","image","lastlogintime","selfintro"};
			@SuppressWarnings("deprecation")
			@Override
		    public void onSuccess(String result) {
				 if (result != null) {
					 	//成功之后则开始加载用户界面的组件
					 	List<Map<String, Object>> relist = JSONUtils.getDataList(localar, serar, result);
				
					 	if(relist!=null)
					 	{	if(relist.size() < pageSizeUsers) isLastPageUsers = true;
						 	else isLastPageUsers = false;
							
							if(relist.size()>0)
							{ 
								
								userDataList=getHuahui(relist);
						 		 Message msg = new Message();  
				                    msg.what = 302;   
				                    handler.sendMessage(msg);  
				                    
							/*	haAdapter.addDataList(getHuahui(relist));
								if(haAdapter!=null){
									haAdapter.notifyDataSetChanged();
								}
								*/
								
							}
						
					 	}
						//成功之后，回调信息
						 //如果正常启动子线程，之后，通知线程已经结束
						 Message msg = handler.obtainMessage(HandlerConstants.GET_USER_LIST_OK,"");//两个参数，what and obj
						 
						 handler.sendMessage(msg);
						
					} else {
						//Toast.makeText(MainActivity.this, "获取数据错误",Toast.LENGTH_SHORT).show();
						Message msg = handler.obtainMessage(HandlerConstants.GET_USER_LIST_ERROR,"加载数据失败");//两个参数，what and obj
						 
						 handler.sendMessage(msg);
					}
				 
				//每次执行之后强行关闭线程
				// Thread.currentThread().stop();
		    }
			
		});
	}
	
public ArrayList<UsersHuiHua> userDataList=new ArrayList();
	private ArrayList<UsersHuiHua> getHuahui(List<Map<String, Object>> huilist) {
		//显示加载数据
		ArrayList<UsersHuiHua> hhList = new ArrayList<UsersHuiHua>();	
		
		UsersHuiHua huihua;
		for(int i = 0; i < huilist.size(); i++){
			huihua = new UsersHuiHua();
			huihua.setId(String.valueOf(huilist.get(i).get("id")));
			//huihua.setTxPath(String.valueOf(huilist.get(i).get("image")));
			huihua.setImage(String.valueOf(huilist.get(i).get("image")));
			
			huihua.setUsername(String.valueOf(huilist.get(i).get("username")));
			huihua.setselfintro(String.valueOf(huilist.get(i).get("selfintro")));
			huihua.setLastlogintime(String.valueOf(huilist.get(i).get("lastlogintime")));
			huihua.setGender(String.valueOf(huilist.get(i).get("gender")));
			huihua.setAge(String.valueOf(huilist.get(i).get("age")));
			huihua.setSalary(String.valueOf(huilist.get(i).get("salary")));
			huihua.setEducation(String.valueOf(huilist.get(i).get("education")));
			hhList.add(huihua);		
		}
		
		return hhList;
	}
	
/*线程2*/
	 
	//初始化线程sixin
	public void initShowSiXin(){//初始化私信控件
		listViewSiXin= (ListView)findViewById(R.id.listViewSiXin);
		List<MessageEntity> messlist=new ArrayList<MessageEntity>();//私信列表总数据,初始化为空
		hbAdapter = new MySiXinAdapter(this, messlist);
		listViewSiXin.setAdapter(hbAdapter);
		listViewSiXin.setCacheColorHint(0);
		
		//第一次先调用
		reloadRunThreadSiXin();
		
		
		 //添加滚动加载数据
		listViewSiXin.setOnScrollListener(new OnScrollListener() {
				@Override
				public void onScrollStateChanged(AbsListView arg0, int arg1) {
				}
				
				@Override
				public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) { 
					 int lastItemid = listViewSiXin.getLastVisiblePosition();
					 //pageNowValue=pageNowValue+1;
					if ((lastItemid + 1) == totalItemCount && !isLastPageSX) {  							
						startSiXinPart(pageSizeSX);
		            }
					
				}
		   });
		listViewSiXin.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				//跳转到另外一个界面（个人用户的详细信息界面）
				Intent intent = new Intent(MainActivity.this, MySiXinDetails.class);			
				Bundle bundle = new Bundle();
				MessageEntity msen = (MessageEntity) listViewSiXin.getItemAtPosition(position);
				bundle.putSerializable("sixin.message", msen);
				intent.putExtras(bundle);
				startActivity(intent);
				
				
			}
		});
		
	}
	
	
	/**
	 * 加载私信数据
	 */
	
	public void reloadRunThreadSiXin()
	{
		startSiXinPart(pageSizeSX);
		new Thread()
		{
			public void run()
			{
				startSiXinPart(pageSizeSX);//pagesize=10,pagenow=1获取私信列表
			}
		}.start();
		
	}

	public void startSiXinPart(int pageSizesx) {
		RequestParams params= new RequestParams();
		params.add("userId",Constant.getUserId()); 
		params.add("pageSize",String.valueOf(pageSizesx));   
		params.add("pageNow",String.valueOf(pageNowSX++));   
		HttpEngine.getHttpEngine().get(Constant.getUserSiXinList, params, new AsyncHttpResponseHandler(){
			@Override
		    public void onSuccess(String result) {
				 if (result != null) 
				 {
					
				      List<MessageEntity> re= JSONUtils.jsonToEntityArray(result,MessageEntity.class);
				  
				      if(re!=null)
				      {
						if(re.size()>0)
						{
							
						    if(re.size() < pageSizeSX) isLastPageSX = true;
						 	else isLastPageSX = false;
							
						

							sxDataList=getSiXinList(re);
					 		 Message msg = new Message();  
			                    msg.what = 303;   
			                    handler.sendMessage(msg);  
			                    
						    /*	hbAdapter.addDataList(getSiXinList(re));
							if(hbAdapter!=null){
								hbAdapter.notifyDataSetChanged();
							}*/
							
							
						/*	Message msg=new Message();
			                msg.what=HandlerConstants.GET_MESSAGES_OK;
			                msg.obj= "获取数据成功!";
			                handler.sendMessage(msg);*/
						}
				  
				      }
				
				 } else {
	                   Message msg=new Message();
	                     msg.what=HandlerConstants.GET_MESSAGES_ERROR;
	                     msg.obj= "获取数据错误!";
	                     handler.sendMessage(msg);
					 
						//Toast.makeText(MainActivity.this, "获取数据错误",Toast.LENGTH_SHORT).show();
					}
		    }
			
		     @Override
             public void onFailure(Throwable error) {
		    	// 关闭进度条
					//ProgressDialogUtils.dismissProgressDialog();
                     error.printStackTrace();
                     Message msg=new Message();
                     msg.what=HandlerConstants.GET_MESSAGES_ERROR;
                     msg.obj= "请求超时,请检查网络!";
                     handler.sendMessage(msg);
             }
		});
	
	}
	
	public ArrayList<MessageEntity> sxDataList=new ArrayList();
	private ArrayList<MessageEntity> getSiXinList(List<MessageEntity> sxList) {
		ArrayList<MessageEntity> sList = new ArrayList<MessageEntity>();
		
		MessageEntity ctp = null;
		for(int i=0;i<sxList.size();i++)
		{
			ctp = new MessageEntity();
			
			ctp.setTxPath(sxList.get(i).getTxPath());
			//ctp.setName("天高云淡");
			ctp.setId(sxList.get(i).getId());
			ctp.setUserid(sxList.get(i).getUserid());
			ctp.setUsername(sxList.get(i).getUsername());
			ctp.setSendTime(sxList.get(i).getSendTime());
			ctp.setMsgContent(sxList.get(i).getMsgContent());
			ctp.setMsgTitle(sxList.get(i).getMsgTitle());
			ctp.setMsgStatus(sxList.get(i).getMsgStatus());
			ctp.setUserImg(sxList.get(i).getUserImg());
			
			sList.add(ctp);
			
		}

		return sList;
	}

	
	
	

	/*
	 * 线程3
	 * 
	 */
	public void initShowCareFriend()
	{
		//初始化关注好友部分的组件
		listViewMyCare = (ListView) findViewById(R.id.listViewMyCare);

		ArrayList<CareFriendsP> cfli = new ArrayList<CareFriendsP>();
		hcAdapter = new CareFriendsAdapter(this, cfli);//初始化适配器，这里初始化填充空数据给适配器，当后面有数据填充，提示数据
		
		listViewMyCare.setAdapter(hcAdapter);
		listViewMyCare.setCacheColorHint(0);

		reloadThreadCareFriends();
		
		listViewMyCare.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				// TODO Auto-generated method stub
			
				//跳转到另外一个界面（个人用户的详细信息界面）
				CareFriendsP cfp=(CareFriendsP) listViewMyCare.getItemAtPosition(position);
				
				Intent intent = new Intent(MainActivity.this, ListUserDetailsActivity.class);
				
				intent.putExtra("id", String.valueOf(Constant.getUserId()));
				intent.putExtra("friendid", String.valueOf(cfp.getId()));
				intent.putExtra("username",String.valueOf(cfp.getUsername()));
				startActivity(intent);
			
				
			}
		});
	
		listViewMyCare.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}
			
			@Override
			public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) { 
				 int lastItemid = listViewMyCare.getLastVisiblePosition();
				 //pageNowValue=pageNowValue+1;
				 //风判断到最后一条数据的时候，并且不是最后一页，则继续加载数据
				if ((lastItemid + 1) == totalItemCount && !isLastPageCF) {  							
					startMyCarePart();
	            }
				
			}
	   });
	}
	
	public void reloadThreadCareFriends()
	{
		/**
		 * 我的关注的线程
		 */
		new Thread()
		{
			@Override
			public void run()
			{
				startMyCarePart();
			}
		}.start();
		
		
	}
	
	
	/*
	 * 333333333333333333333333333333333333333
	 * get mycare users
	 */
	private void startMyCarePart()
	{
		
		//这里是一次需要获取多少条数据
		
		final RequestParams  para= new RequestParams();
		para.add("id",Constant.getUserId());
		para.add("pageNow", (pageNumCF++)+"");
		para.add("pageSize", pageSizeCF+"");
		//访问服务器
		HttpEngine.getHttpEngine().post(Constant.getUserCareList, para, new AsyncHttpResponseHandler(){
			
			@Override
		    public void onSuccess(String result) {
				 if (result != null) {

					 List<CareFriendsP> cfList= JSONUtils.jsonToEntityArray(result,CareFriendsP.class);
					    
					 if(cfList!=null)
					 {
					 	if(cfList.size()<pageSizeCF) isLastPageCF = true;
					 	else isLastPageCF = false;
					 	
					 	
					 	if(cfList.size()>0)
					 	{
					 		cfDataList=getCFList(cfList);
					 		 Message msg = new Message();  
			                    msg.what = 301;   
			                    handler.sendMessage(msg);  
			                    
					 		/*hcAdapter.addList(getCFList(cfList));
					 		if(hcAdapter!=null){
					 			hcAdapter.notifyDataSetChanged();
					 		}*/
					 		
					 	}
					 	
					 }
					} else {
						//Toast.makeText(MainActivity.this, "获取数据错误",Toast.LENGTH_SHORT).show();
					}
				
		    }
			
		});
		
	
	}
	
	public ArrayList<CareFriendsP> cfDataList=new ArrayList();
	@SuppressWarnings("unused")
	private ArrayList<CareFriendsP> getCFList(List<CareFriendsP> cfList){
		ArrayList<CareFriendsP> hcList = new ArrayList<CareFriendsP>();
		
		CareFriendsP ctp = null;
		for(int i=0;i<cfList.size();i++)
		{
			ctp = new CareFriendsP();
			ctp.setId(cfList.get(i).getId());
			if(cfList.get(i).getImage()!=null){
				ctp.setImage(cfList.get(i).getImage());
			}
			else
			{
				ctp.setImage(R.drawable.headshow2 + "");
			}
			ctp.setUsername(cfList.get(i).getUsername());
			hcList.add(ctp);
			
		}


		return hcList;
	}

	
	
	
	//我的相册
	public void uploadImage(final Activity context) {
		menuWindow = new SelectPicPopupWindow(MainActivity.this, itemsOnClick,userinfo);
		// 显示窗口
		View view = MainActivity.this.findViewById(R.id.set);
		// 计算坐标的偏移量
		int xoffInPixels = menuWindow.getWidth() - view.getWidth() + 10;
		menuWindow.showAsDropDown(view, -xoffInPixels, 0);
	}

	public void uploadSearchImage(final Activity context) {
		menuSearchWindow = new SelectSearchUserWindow(MainActivity.this, itemsSearchOnClick);
		// 显示窗口
		View view = MainActivity.this.findViewById(R.id.set);
		// 计算坐标的偏移量
		int xoffInPixels = menuSearchWindow.getWidth() - view.getWidth() + 10;
		menuSearchWindow.showAsDropDown(view, -xoffInPixels, 0);
		//menuSearchWindow.getContentView().setTag(mHandler);//����hander������
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

	
	//这里设置top2选中的模块的颜色和字体
	private void setCurPoint(int index) {
		if (index < 0 || index > mViewCount - 1 || mCurSel == index) {
			return;
		}
		mImageViews[mCurSel].setEnabled(true);
//		mImageViews[index].setEnabled(false);
		mCurSel = index;
	
		if (index == 0) {
			//所有推荐用户
			listViewNow = 0;
			allusers.setTextColor(0xff228B22);
			permessage.setTextColor(Color.BLACK);
			mycare.setTextColor(Color.BLACK);
			myself.setTextColor(Color.BLACK);
		} else if (index == 1) {
			listViewNow = 1;
			allusers.setTextColor(Color.BLACK);
			permessage.setTextColor(0xff228B22);
			mycare.setTextColor(Color.BLACK);
			myself.setTextColor(Color.BLACK);
		} else if(index==2){
			//关注好友
			listViewNow = 2;
			allusers.setTextColor(Color.BLACK);
			permessage.setTextColor(Color.BLACK);
			mycare.setTextColor(0xff228B22);
			myself.setTextColor(Color.BLACK);
		}else{
			listViewNow = 3;
			allusers.setTextColor(Color.BLACK);
			permessage.setTextColor(Color.BLACK);
			mycare.setTextColor(Color.BLACK);
			myself.setTextColor(0xff228B22);
		}
	}

	@Override
	public void OnViewChange(int view) {
		// TODO Auto-generated method stub
		setCurPoint(view);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int pos = (Integer) (v.getTag());
		setCurPoint(pos);
		mScrollLayout.snapToScreen(pos);
	}

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
                Log.i(TAG, "error:" + e);  
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
				if(re.getUserImg()!=null){userinfo.setUserImg(re.getUserImg());
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
                handler.sendMessage(msg);
				}
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
				//String result = "响应码："+msg.arg1+"\n响应信息："+msg.obj+"\n耗时："+UploadUtil.getRequestTime()+"秒";
				//uploadImageResult.setText(result);
				String result=(String) msg.obj;
				Log.i("*********", result);
				break;
			case 10:
				Toast.makeText(MainActivity.this, "上传成功",Toast.LENGTH_SHORT).show();
				break;
			case 11:
				Toast.makeText(MainActivity.this, "上传失败",Toast.LENGTH_SHORT).show();
				break;
			case 12:
				Toast.makeText(MainActivity.this, "请求超时,请检查网络!",Toast.LENGTH_SHORT).show();
				break;
			case HandlerConstants.GET_USER_INFO_OK:
				Center_TV_username.setText(userinfo.getUsername());
			    Center_TV_email.setText(userinfo.getEmail());
				Center_TV_intro.setText(userinfo.getIntro());
				DownImage downImage = new DownImage(userinfo.getUserImg());
				downImage.loadImage(new ImageCallBack() {
					public void getDrawable(Drawable drawable) {

						// TODO Auto-generated method stub

						avatar.setImageDrawable(drawable);

					}
				});
				//Toast.makeText(MainActivity.this, msg.obj.toString(),Toast.LENGTH_SHORT).show();
			break;
			case HandlerConstants.GET_USER_INFO_ERROR:
				//Toast.makeText(MainActivity.this, msg.obj.toString(),Toast.LENGTH_SHORT).show();
				break;

			case PHOTO_URL:
				photoUri=(Uri) msg.obj;
				Toast.makeText(MainActivity.this, photoUri.toString(),Toast.LENGTH_SHORT).show();
				break;
			case 301:
				hcAdapter.addList(cfDataList);
				listViewMyCare.setAdapter(hcAdapter);
				break;
			case 302:
				haAdapter.addDataList(userDataList);	
				listViewUsers.setAdapter(haAdapter);
				break;
			case 303:
				hbAdapter.addDataList(sxDataList);
				listViewSiXin.setAdapter(hbAdapter);
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
	
	
	
	protected void dialog() { 
        AlertDialog.Builder builder = new Builder(MainActivity.this); 
        builder.setMessage(getResources().getString(R.string.suretologout)); 
        builder.setTitle(getResources().getString(R.string.note)); 
        builder.setPositiveButton(getResources().getString(R.string.sure), 
                new android.content.DialogInterface.OnClickListener() { 
                    @Override
                    public void onClick(DialogInterface dialog, int which) { 
                        dialog.dismiss(); 
                        MainActivity.this.finish(); 
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

	/*-------------------------------------------------------------------------------------------------------------------------------个人中心所用方法 end*/
}
