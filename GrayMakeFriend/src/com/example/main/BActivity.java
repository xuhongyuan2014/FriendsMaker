package com.example.main;

import java.util.ArrayList;
import java.util.List;

import com.example.mfriends.MainActivity;
import com.example.mfriends.MySiXinAdapter;
import com.example.mfriends.MySiXinDetails;
import com.example.mfriends.R;
import com.example.model.MessageEntity;
import com.example.utils.Constant;
import com.example.utils.HandlerConstants;
import com.example.utils.HttpEngine;
import com.example.utils.JSONUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class BActivity extends Activity{

	private ListView listview1;
	/*
	 * sixin分页
	 */
	private int pageNowSX=1;
	private int pageSizeSX=10;
	private MySiXinAdapter hbAdapter;
	private boolean isLastPageSX= true;//判断是否为最后一页
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	/*	TextView tv = new TextView(this);
		tv.setText("This is B Activity!");
		tv.setGravity(Gravity.CENTER);*/
		setContentView(R.layout.menu_list);
		
		 IntentFilter intentFilter = new IntentFilter();  
		 intentFilter.addAction("action.refresh");  
	      intentFilter.addAction("action.refreshMessage");  
	      registerReceiver(mRefreshBroadcastReceiver, intentFilter); 
		
		listview1=(ListView) findViewById(R.id.listView1);
		
		
		
		List<MessageEntity> messlist=new ArrayList<MessageEntity>();//私信列表总数据,初始化为空
		hbAdapter = new MySiXinAdapter(this, messlist);
		listview1.setAdapter(hbAdapter);
		listview1.setCacheColorHint(0);
		
		//第一次先调用
		//reloadRunThreadSiXin();
		startSiXinPart(pageSizeSX);
		
		
		 //添加滚动加载数据
		listview1.setOnScrollListener(new OnScrollListener() {
				@Override
				public void onScrollStateChanged(AbsListView arg0, int arg1) {
				}
				
				@Override
				public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) { 
					 int lastItemid = listview1.getLastVisiblePosition();
					 //pageNowValue=pageNowValue+1;
					if ((lastItemid + 1) == totalItemCount && !isLastPageSX) {  							
						startSiXinPart(pageSizeSX);
		            }
					
				}
		   });
		listview1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				//跳转到另外一个界面（个人用户的详细信息界面）
				Intent intent = new Intent(BActivity.this, MySiXinDetails.class);			
				Bundle bundle = new Bundle();
				MessageEntity msen = (MessageEntity) listview1.getItemAtPosition(position);
				bundle.putSerializable("sixin.message", msen);
				intent.putExtras(bundle);
				startActivity(intent);
				
				
			}
		});
		
		
		
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
							
						
/*
							sxDataList=getSiXinList(re);
					 		 Message msg = new Message();  
			                    msg.what = 303;   
			                    handler.sendMessage(msg);  */
			                    
						    	hbAdapter.addDataList(getSiXinList(re));
							if(hbAdapter!=null){
								hbAdapter.notifyDataSetChanged();
							}
							
							
						/*	Message msg=new Message();
			                msg.what=HandlerConstants.GET_MESSAGES_OK;
			                msg.obj= "获取数据成功!";
			                handler.sendMessage(msg);*/
						}
				  
				      }
				
				 } else {
	             /*      Message msg=new Message();
	                     msg.what=HandlerConstants.GET_MESSAGES_ERROR;
	                     msg.obj= "获取数据错误!";
	                     handler.sendMessage(msg);*/
					 
						//Toast.makeText(MainActivity.this, "获取数据错误",Toast.LENGTH_SHORT).show();
					}
		    }
			
		     @Override
             public void onFailure(Throwable error) {
		    	// 关闭进度条
					//ProgressDialogUtils.dismissProgressDialog();
         /*            error.printStackTrace();
                     Message msg=new Message();
                     msg.what=HandlerConstants.GET_MESSAGES_ERROR;
                     msg.obj= "请求超时,请检查网络!";
                     handler.sendMessage(msg);*/
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

	 // broadcast receiver  
	  private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {  
	  
	      @Override  
	      public void onReceive(Context context, Intent intent) {  
	          String action = intent.getAction();  
	          if (action.equals("action.refreshMessage")||action.equals("action.refresh"))  
	          {  
	              reFreshList();  
	          }  
	      }  
	  };
	  public void reFreshList(){
			pageNowSX=1;
			pageSizeSX=10;
			isLastPageSX= true;//判断是否为最后一页
			hbAdapter.clearList();
			startSiXinPart(pageSizeSX);
	  }
	    @Override  
	       protected void onDestroy() {  
	           super.onDestroy();  
	           unregisterReceiver(mRefreshBroadcastReceiver);  
	       }  
	
}
